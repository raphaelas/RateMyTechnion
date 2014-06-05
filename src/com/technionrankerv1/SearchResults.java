package com.technionrankerv1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CursorAdapter;

import com.serverapi.TechnionRankerAPI;

public abstract class SearchResults extends ActionBarActivity {
	TechnionRankerAPI db = new TechnionRankerAPI();
	public String[] professorsAndCourses = null;
	android.support.v4.widget.CursorAdapter cursorAdapter;
	HashMap<String, String> hebrewTranslations = new HashMap<String, String>();
	HashMap<String, String> facultyMap = new HashMap<String, String>();
	public ViewPager viewPager;
		
	public void onCreate(Bundle savedInstance){

		super.onCreate(savedInstance);
		professorsAndCourses = concat(capsFix(parseCourses()), parseHebrewProfessors());
	}
	
	
	/**
	 * This populates the professorSet (returned) and hebrewTranslations
	 * HashMap (class variable).
	 * @return
	 */
	public String[] parseHebrewProfessors() {
		HashSet<String> professorSet = new HashSet<String>();
		String inputLine;
		BufferedReader infile;
		int start;
		int end;
		String englishName = null;
		String hebrewName;
		boolean arrivedAtProfessors = false;
		try {
			String[] hebrewProfessorFiles = getAssets().list("HebrewProfessorListingsEncoded");
			for (int i = 0; i < hebrewProfessorFiles.length; i++) {
				arrivedAtProfessors = false;
				infile = new BufferedReader(new InputStreamReader(
						getAssets().open("HebrewProfessorListingsEncoded/" + hebrewProfessorFiles[i])));
				while (infile.ready()) {// while more info exists
					inputLine = infile.readLine();
					if (inputLine.contains("mailto:")  && arrivedAtProfessors == true) {
						start = inputLine.indexOf("[") + 1;
						end = inputLine.indexOf("]");
						englishName = inputLine.substring(start, end);
						//Make name first name last name.
						String[] splittedOnSpace = englishName.split(" ");
						if (splittedOnSpace.length == 2) {
							englishName = "" + splittedOnSpace[1] + " " + splittedOnSpace[0];
						}
						else if (splittedOnSpace.length == 1){
							englishName = "" + splittedOnSpace[0];
						}
						else if (splittedOnSpace.length == 3) {
							if (splittedOnSpace[0].indexOf("-") == splittedOnSpace[0].length() - 1) {
								englishName = splittedOnSpace[2] + " " + splittedOnSpace[0] + splittedOnSpace[1];  
							}
							else {
								englishName = splittedOnSpace[1] + " " + splittedOnSpace[2] + " " + splittedOnSpace[0];
							}						
						}
						else {
							throw new IOException("Unfortunately, there is a quadruple name.");
						}
					}
					else if (inputLine.contains("GetEmployeeDetails") && arrivedAtProfessors == true) {
						start = inputLine.indexOf(">") + 1;
						end = inputLine.length();
						hebrewName = inputLine.substring(start, end);
						String[] splittedOnSpace = hebrewName.split(" ");
						if (splittedOnSpace.length == 2) {
							hebrewName = "" + splittedOnSpace[1] + " " + splittedOnSpace[0];
						}
						else if (splittedOnSpace.length == 1){
							hebrewName = "" + splittedOnSpace[0];
						}
						else if (splittedOnSpace.length == 3) {
							if (splittedOnSpace[0].indexOf("-") == splittedOnSpace[0].length() - 1) {
								hebrewName = splittedOnSpace[2] + " " + splittedOnSpace[0] + splittedOnSpace[1];  
							}
							else {
								hebrewName = splittedOnSpace[1] + " " + splittedOnSpace[2] + " " + splittedOnSpace[0];
							}
						}
						else {
							throw new IOException("Unfortunately, there is a quadruple hebrew name.");
						}
						if (englishName == null || hebrewName == null) {
							//There's a null name, so throw an exception 
							//(albeit IOException is not proper type but it works)
							throw new IOException("There's a null name."); 
						}
						hebrewTranslations.put(englishName, hebrewName);
						String faculty = hebrewProfessorFiles[i].substring(0, hebrewProfessorFiles[i].indexOf(".html"));
						facultyMap.put(englishName, faculty);
						//Professor p = new Professor(null, englishName, faculty, hebrewName, true);
						String hebrewNameToUse = StringEscapeUtils.unescapeHtml4(hebrewName);		
						//This will make the hebrew professor name in a new line after the english name.
						professorSet.add(englishName + "\n" + hebrewNameToUse);
					}
					else if (inputLine.contains("searchtable")) {
						//We've arrived at the professor listing section.
						arrivedAtProfessors = true;
					}
				}
				infile.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Object[] professorArrayObjects = professorSet.toArray();
		String[] professorArrayStrings = Arrays.copyOf(professorArrayObjects, professorArrayObjects.length, String[].class);
		return professorArrayStrings;
	}

	public String[] parseCourses() {
		// create Hashmap, where the numbers are the keys and the Titles are the
		// values

		HashMap<String, String> map = new HashMap<String, String>();
		HashSet<String> numberAndName = new HashSet<String>();
		String inputLine = "";
		String[] temp;

		String[] courseFiles;
		try {
			courseFiles = getAssets().list("CourseListings");
			for (int i = 0; i < courseFiles.length; i++) {
				int lineNumber = 0;
				BufferedReader infile = new BufferedReader(new InputStreamReader(
						getAssets().open("CourseListings/" + courseFiles[i])));
				while (infile.ready()) {// while more info exists
					if (lineNumber >= 13 && lineNumber % 3 == 1) { // only take
																	// numbers 13
																	// and up for
																	// every 3
						temp = inputLine.split(" - ");
						for (int t = 0; t < temp.length; t++) {
							String number = temp[0].trim();// number;
							String name = temp[1].replaceAll("</A>", "").trim();
							String faculty = courseFiles[i].substring(0, courseFiles[i].indexOf(".html"));
							//Course c = new Course(null, name, number, null, null, faculty, true);
							map.put(number, name); // trim and place only the number
													// and name in
							facultyMap.put(number, faculty);
							numberAndName.add("" + number + " - " + capsFix2(name));
						} // for temp
					} // if
					inputLine = infile.readLine(); // read the next line of the text
					lineNumber++;
				} // while infile
				infile.close();
			} // for courseFiles
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object[] allNumbersAndNames = numberAndName.toArray();
		String[] numbersAndNamesToReturn = Arrays.copyOf(allNumbersAndNames,
				allNumbersAndNames.length, String[].class);
		//Code to populate database:
		ClientAsync as = new ClientAsync();
		as.execute(numbersAndNamesToReturn);
		return numbersAndNamesToReturn;
	} // parse()

	String[] concat(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c = new String[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
	
	public String[] capsFix(String[] s) {
		for (int z = 0; z < s.length; z++) {
			String[] temp = s[z].split(" ");

			for (int t = 0; t < temp.length; t++) {
				if (t == 0 && temp[t].equals("A")) {
					t++;
				}
				if (temp[t].equals("A") || temp[t].equals("FOR")
						|| temp[t].equals("THE") || temp[t].equals("OF")
						|| temp[t].equals("AND") || temp[t].equals("IN")
						|| temp[t].equals("AT") || temp[t].equals("AN")) {
					temp[t] = temp[t].toLowerCase(Locale.ENGLISH);
				}
				String firstLetter = temp[t].substring(0, 1); // take the first
																// letter
				temp[t] = temp[t].toLowerCase(Locale.ENGLISH); // make the word
																// lowercase
				String end = temp[t].substring(1, temp[t].length()); // get ride
																		// of
																		// the
																		// first
																		// letter
				temp[t] = firstLetter + end; // add firstletter and the rest of
												// the word
			}
			s[z] = "";
			for (int q = 0; q < temp.length; q++) {
				s[z] = s[z] + temp[q] + " ";
			}
		}
		return s;
	}
	
	public String capsFix2(String s) {
			String[] temp = s.split(" ");

			for (int t = 0; t < temp.length; t++) {
				if (t == 0 && temp[t].equals("A")) {
					t++;
				}
				if (temp[t].equals("A") || temp[t].equals("FOR")
						|| temp[t].equals("THE") || temp[t].equals("OF")
						|| temp[t].equals("AND") || temp[t].equals("IN")
						|| temp[t].equals("AT") || temp[t].equals("AN")) {
					temp[t] = temp[t].toLowerCase(Locale.ENGLISH);
				}
				String firstLetter = temp[t].substring(0, 1); // take the first
																// letter
				temp[t] = temp[t].toLowerCase(Locale.ENGLISH); // make the word
																// lowercase
				String end = temp[t].substring(1, temp[t].length()); // get ride
																		// of
																		// the
																		// first
																		// letter
				temp[t] = firstLetter + end; // add firstletter and the rest of
												// the word
			}
			s = "";
			for (int q = 0; q < temp.length; q++) {
				s = s + temp[q] + " ";
			}
		return s;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		// Get the SearchView and set the searchable configuration
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		 // Do not iconify the widget; expand it by default
		searchView.setIconifiedByDefault(false);
		//This onclicklistener widens the search text:
		searchView.setOnSearchClickListener(new OnClickListener() {
		    private boolean extended = false;
		    @Override
		    public void onClick(View v) {
		        if (!extended) {
		            extended = true;
		            LayoutParams lp2 = ((View) v.getParent()).getLayoutParams();
		            lp2.width = LayoutParams.MATCH_PARENT;
		        }
		    }
		});
	    String[] columnNames = {"_id","coursesAndProfessors", "hebrewProfessorName"};
	    MatrixCursor cursor = new MatrixCursor(columnNames);
	    String[] from = {"coursesAndProfessors", "hebrewProfessorName"}; 
	    int[] to = {R.id.lblListItem, R.id.hebrewListItem};
	    cursorAdapter = 
	    		new android.support.v4.widget.SimpleCursorAdapter(getApplicationContext(), R.layout.list_item, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );
        searchView.setOnQueryTextListener(searchQueryListener);
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setOnSuggestionListener(new OnSuggestionListener() {

           @Override
           public boolean onSuggestionClick(int position) {
        	   Cursor c = (MatrixCursor) cursorAdapter.getItem(position);
        	   String value =  c.getString(c.getColumnIndexOrThrow("coursesAndProfessors"));
        	   if (Character.isDigit(value.charAt(0))) {
					String[] splitted = value.split(" - ");
					String courseNumber = splitted[0];
					String courseName = splitted[1];
					Intent i = new Intent(SearchResults.this, CourseView.class);
					i.putExtra("courseNumber", courseNumber);
					i.putExtra("courseName", courseName);
					i.putExtra("faculty", facultyMap.get(courseNumber));
					startActivity(i);
				}
				else {
					Intent i = new Intent(SearchResults.this, ProfessorView.class);
					i.putExtra("professorName", value);
					i.putExtra("faculty", facultyMap.get(value));
					startActivity(i);
				}
               return true;
           }

           @Override
           public boolean onSuggestionSelect(int position) {
               return false;
           }
        });
        return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Log.d(getLocalClassName(), "In OptionsItemSelected");
		switch (item.getItemId()) {
		case R.id.action_logout:
			//TODO: openLoginPage(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private OnQueryTextListener searchQueryListener = new OnQueryTextListener() {
	    @Override
	    public boolean onQueryTextSubmit(String query) {
	        search(query);
	        return true;
	    }

	    @Override
	    public boolean onQueryTextChange(String newText) {
	    	final int CHARACTER_THRESHOLD = 0;
	        if (!TextUtils.isEmpty(newText) && newText.length() > CHARACTER_THRESHOLD) { //searchView.isExpanded() && 
		        search(newText);
	        }
	        else {
	        	//Swap cursor with blank cursor to remove all suggestions.
			    String[] columnNames = {"_id","coursesAndProfessors", "hebrewProfessorName"};
			    MatrixCursor cursor = new MatrixCursor(columnNames);
		        //TODO: deal with the internal IllegalStateException problem.
			    cursorAdapter.swapCursor(cursor);
	        }
	        return true;
	    }

	    public void search(String query) {
	        // reset loader, swap cursor, etc.
	    	query = query.toLowerCase(Locale.ENGLISH);
		    String[] columnNames = {"_id","coursesAndProfessors", "hebrewProfessorName"};
		    MatrixCursor cursor = new MatrixCursor(columnNames);
		    String[] temp = new String[3];
		    int id = 0;
		    for(String item : professorsAndCourses){
		    	String toCheck = item.toLowerCase(Locale.ENGLISH);
				//TODO: make the english and hebrew names appear in the same line together.
		    	if (toCheck.contains(query)) {
			        temp[0] = Integer.toString(id++);
			        if (!item.contains("\n")) {
				        temp[1] = item;
				        temp[2] = null;
			        }
			        else { //It's a professor.
			        	String[] splitted = item.split("\n");
			        	temp[1] = splitted[0]; //english name
			        	temp[2] = splitted[1]; //hebrew name
			        }
			        cursor.addRow(temp);
		    	}
		    }
		    cursorAdapter.swapCursor(cursor);
	    }

	};
	
	
	/**
	 * This is used whenever we need to populate the courses
	 * or professors database tables.
	 */
	private class ClientAsync extends AsyncTask<String, Void, List<Professor>> {

		public ClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * This is the method that does the database call.  Comment
		 * everything in this method to ignore the database.
		 */
		@Override
		protected List<Professor> doInBackground(String... params) {
			List<Professor> result = null;
			//Professor p = new Professor(null, "Cool Professor", null, null, false);
			//result = db.insertProfessor(p).toString();
			//result = new TechnionRankerAPI().getProfessorByProfessorName(p);
			/* This would populate the courses database:
			String result = null;
			for (int i = 0; i < params.length; i++) {
				String[] splitted = params[i].split(" - ");
				String number = splitted[0];
				String name = splitted[1];
				Log.d(number, name);
				Course c = new Course(null, name, number, null, null, true);
				result = db.insertCourse(c).toString();
			}
			return result;
			*/ 
			//This would get an example course:
			//Course c = new Course(null, "Project in Software", "236504", null, null, "Computer Science", true);
			//result = db.getCourse(c);
			//
			//result = new TechnionRankerAPI().insertCourse(c).toString();
			return result;
		}

		@Override
		protected void onPostExecute(List<Professor> res) {
			if (res == null)
				Log.d(getLocalClassName(), "SearchResults async unsuccessful");
			else {
				Log.d(getLocalClassName(), res.get(0).getId() + res.get(0).getName());
			}
		}
	}
}


