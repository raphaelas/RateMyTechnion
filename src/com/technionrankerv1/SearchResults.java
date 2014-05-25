package com.technionrankerv1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
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
	String[] professorsAndCourses = null;
	android.support.v4.widget.CursorAdapter cursorAdapter;
	//@override
	public void onCreate(Bundle savedInstance){

		super.onCreate(savedInstance);
		professorsAndCourses = concat(capsFix(parseCourses()), parseProfessors());
		//Uncomment this if you want a hashmap translating professor names hebrew/english:
		//HashMap<String, String> hebrewTranslations = parseHebrewProfessors();
	}
	
	public HashMap<String, String> parseHebrewProfessors() {
		HashMap<String, String> hebrewTranslations = new HashMap<String, String>();
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
						else {
							englishName = "" + splittedOnSpace[0];
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
						else {
							hebrewName = "" + splittedOnSpace[0];
						}
						if (englishName == null || hebrewName == null) {
							//There's a null name, so throw an exception 
							//(albeit IOException is not proper type but it works)
							throw new IOException(); 
						}
						hebrewTranslations.put(englishName, hebrewName);
					}
					else if (inputLine.contains("searchtable")) {
						arrivedAtProfessors = true;
					}
				}
				infile.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return hebrewTranslations;
	}
	
	
	public String[] parseProfessors() {
		ArrayList<String> profList = new ArrayList<String>();
		String inputLine = "";
		String[] temp;
		String[] profListArray = null;
		String[] professorFiles;
		try {
			professorFiles = getAssets().list("ProfessorListings");
			for (int i = 0; i < professorFiles.length; i++) {
				BufferedReader infile = new BufferedReader(new InputStreamReader(
						getAssets().open("ProfessorListings/" + professorFiles[i])));
				while (infile.ready()) {// while more info exists
					inputLine = infile.readLine();
					if (inputLine.startsWith("<td><a href=")) {
						/*
						 * This would parse the professor's ID #. int start =
						 * inputLine.indexOf("code=") + 5; int end =
						 * inputLine.indexOf(" rel") - 1; String id =
						 * inputLine.substring(start, end); Log.d(professorFiles[i],
						 * id);
						 */
						inputLine = inputLine.substring(1, inputLine.length() - 9);
						temp = inputLine.split(">");
						String[] splittedOnSpace = temp[2].split(" ");
						String firstNameLastName;
						if (splittedOnSpace.length == 2) {
							firstNameLastName = "" + splittedOnSpace[1] + " " + splittedOnSpace[0];
						}
						else {
							firstNameLastName = "" + splittedOnSpace[0];
						}
						profList.add(firstNameLastName);
					}
				}
				profListArray = profList.toArray(new String[profList.size()]);
				infile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return profListArray;
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
							String name = temp[1].replaceAll("</A>", "").trim();// name
							map.put(number, name); // trim and place only the number
													// and name in
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
		            LayoutParams lp = v.getLayoutParams();
		            LayoutParams lp2 = ((View) v.getParent()).getLayoutParams();
		            lp2.width = LayoutParams.MATCH_PARENT;
		            lp.width = LayoutParams.MATCH_PARENT;
		        }
		    }
		});
	    String[] columnNames = {"_id","coursesAndProfessors"};
	    MatrixCursor cursor = new MatrixCursor(columnNames);
	    String[] from = {"coursesAndProfessors"}; 
	    int[] to = {R.id.lblListItem};
	    cursorAdapter = 
	    		new android.support.v4.widget.SimpleCursorAdapter(getApplicationContext(), R.layout.list_item, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );
        searchView.setOnQueryTextListener(searchQueryListener);
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setOnSuggestionListener(new OnSuggestionListener() {

           @Override
           public boolean onSuggestionClick(int position) {
        	   Cursor c = (MatrixCursor) cursorAdapter.getItem(position);
        	   String value =  c.getString(c.getColumnIndexOrThrow("coursesAndProfessors"));
               Log.d(getLocalClassName(), value);
        	   if (Character.isDigit(value.charAt(0))) {
					String[] splitted = value.split(" - ");
					String courseNumber = splitted[0];
					String courseName = splitted[1];
					Intent i = new Intent(SearchResults.this, CourseView.class);
					i.putExtra("courseNumber", courseNumber);
					i.putExtra("courseName", courseName);
					startActivity(i);
				}
				else {
					Intent i = new Intent(SearchResults.this, ProfessorView.class);
					i.putExtra("professorName", value);
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
		switch (item.getItemId()) {
		case R.id.action_logout:
			// openLoginPage(item);
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
	        if (!TextUtils.isEmpty(newText) && newText.length() > 1) { //searchView.isExpanded() && 
		        search(newText);
	        }
	        else {
	        	//Swap cursor with blank cursor to remove all suggestions.
			    String[] columnNames = {"_id","coursesAndProfessors"};
			    MatrixCursor cursor = new MatrixCursor(columnNames);
	        	cursorAdapter.swapCursor(cursor);
	        }
	        return true;
	    }

	    public void search(String query) {
	        // reset loader, swap cursor, etc.
	    	query = query.toLowerCase(Locale.ENGLISH);
		    String[] columnNames = {"_id","coursesAndProfessors"};
		    MatrixCursor cursor = new MatrixCursor(columnNames);
		    String[] temp = new String[2];
		    int id = 0;
		    for(String item : professorsAndCourses){
		    	String toCheck = item.toLowerCase(Locale.ENGLISH);
		    	if (toCheck.contains(query)) {
			        temp[0] = Integer.toString(id++);
			        temp[1] = item;
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
	private class ClientAsync extends AsyncTask<String, Void, Course> {

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
		protected Course doInBackground(String... params) {
			Course result = null;
			/* This would populate the courses database:
			Log.d(getLocalClassName(), String.valueOf(params.length));
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
			/* This would get an example course:
			Course c = new Course(Long.valueOf(3), null, null, null, null, true);
			result = db.getCourse(c);
			*/
			return result;
		}

		@Override
		protected void onPostExecute(Course res) {
			if (res == null)
				Log.d(getLocalClassName(), "unsuccessful");
			else {
				Log.d(getLocalClassName(), res.getName());
			}
		}
	}
	
}


