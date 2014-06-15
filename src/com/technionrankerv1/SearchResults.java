 package com.technionrankerv1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.serverapi.TechnionRankerAPI;

public abstract class SearchResults extends ActionBarActivity {
	private TechnionRankerAPI db = new TechnionRankerAPI();
	public String[] professorsAndCourses = null;
	private CursorAdapter cursorAdapter;
	public HashMap<String, String> hebrewTranslations = new HashMap<String, String>();
	public HashMap<String, String> facultyMap = new HashMap<String, String>();
	public ViewPager viewPager;
	public LinkedHashSet<String> courseNumbers = new LinkedHashSet<String>();
	public HashMap<String, Course> courseNumbersToCourses = new HashMap<String, Course>();
	public List<Course> coursesToInsert = new ArrayList<Course>();

	public void onCreate(Bundle savedInstance){

		super.onCreate(savedInstance);
		if (!getLocalClassName().equals("MainActivity") && !getLocalClassName().equals("FragmentMainActivity") && !getLocalClassName().equals("SplashActivity")) {
	        getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		// Detect if connected to Internet
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
		if (!isInternetPresent) {
			Log.d(getLocalClassName(), "Warning: there is no Internet connection.");
			Toast.makeText(getApplicationContext(), "Please check your"
					+ "Internet connection.", Toast.LENGTH_LONG).show();
		}
		String[] globalProfessorsAndCourses = ((ApplicationWithGlobalVariables)
				getApplication()).professorsAndCourses;
		if (globalProfessorsAndCourses == null) {
			concatMethod concatMethod1 = new concatMethod();
			concatMethod1.execute();
			((ApplicationWithGlobalVariables)
					getApplication()).professorsAndCourses = professorsAndCourses;
		}
		else {
			professorsAndCourses = globalProfessorsAndCourses;
		}
//
//		ClientAsync t = new ClientAsync();
//		t.execute();
		
	}
	
	private class concatMethod extends AsyncTask<String[], Void, Void> {
	
		public concatMethod(){	
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(String[]... params) {
			((ApplicationWithGlobalVariables)
					getApplication()).professorsAndCourses = concat(parseCourses(), parseHebrewProfessors());
			return null;
		}
		
		@Override
		protected void onPostExecute(Void res) {
			Log.d(getLocalClassName(), "done!");
		}
		}

	/**
	 * This populates the professorSet (returned) and hebrewTranslations
	 * HashMap (class variable).
	 * @return
	 */
	public String[] parseHebrewProfessors() {
		HashSet<String> professorSet = new HashSet<String>();
		try {
			BufferedReader infile = new BufferedReader(new InputStreamReader(
					getAssets().open("professors.txt")));
			while (infile.ready()) {
				String inputLine = infile.readLine().trim();
				String[] splitted = inputLine.split("\t");
				String hebName = StringEscapeUtils.unescapeJava(splitted[3]).trim();
				String engName = splitted[4].trim();
				hebrewTranslations.put(hebName, engName);
				String faculty = splitted[2].trim();
				String hebrewNameToUse = hebName;
				facultyMap.put(hebrewNameToUse, faculty);
				//This will make the hebrew professor name in a new line after the english name.
				if (!engName.equals("<null>")) {
					professorSet.add(engName + "\n" + hebrewNameToUse);
				}
				else {
					professorSet.add("\n" + hebrewNameToUse);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object[] professorArrayObjects = professorSet.toArray();
		String[] professorArrayStrings = Arrays.copyOf(professorArrayObjects, professorArrayObjects.length, String[].class);
		return professorArrayStrings;
	}

	public String[] parseCourses() {
		HashSet<String> numberAndName = new HashSet<String>();
		try {
			BufferedReader infile = new BufferedReader(new InputStreamReader(
					getAssets().open("courses.txt")));
			while (infile.ready()) {
				String inputLine = infile.readLine();
				String[] splitted = inputLine.split("\t");
				String number = splitted[4].trim();
				String name = splitted[3].trim();
				String faculty = splitted[2].trim();
//				map.put(number, name);
				facultyMap.put(number, faculty);
				courseNumbers.add(number);
				Course c = new Course(null, name, number, null, null, faculty, true);
				coursesToInsert.add(c);
				courseNumbersToCourses.put(number, c);
				numberAndName.add("" + number + " - " + name);

			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Object[] allNumbersAndNames = numberAndName.toArray();
		String[] numbersAndNamesToReturn = Arrays.copyOf(allNumbersAndNames,
				allNumbersAndNames.length, String[].class);
		return numbersAndNamesToReturn;
	}

	
//	/**
//	 * This populates the professorSet (returned) and hebrewTranslations
//	 * HashMap (class variable).
//	 * @return
//	 */
//	public String[] parseHebrewProfessors() {
//		List<Professor> professorsToInsert = new ArrayList<Professor>();
//		HashSet<String> professorSet = new HashSet<String>();
//		String inputLine;
//		BufferedReader infile;
//		int start;
//		int end;
//		String englishName = null;
//		String hebrewName;
//		boolean arrivedAtProfessors = false;
//		try {
//			String[] hebrewProfessorFiles = getAssets().list("HebrewProfessorListingsEncoded");
//			for (int i = 0; i < hebrewProfessorFiles.length; i++) {
//				arrivedAtProfessors = false;
//				infile = new BufferedReader(new InputStreamReader(
//						getAssets().open("HebrewProfessorListingsEncoded/" + hebrewProfessorFiles[i])));
//				while (infile.ready()) {// while more info exists
//					inputLine = infile.readLine();
//					if (inputLine.contains("mailto:")  && arrivedAtProfessors == true) {
//						start = inputLine.indexOf("[") + 1;
//						end = inputLine.indexOf("]");
//						englishName = inputLine.substring(start, end);
//						//Make name first name last name.
//						String[] splittedOnSpace = englishName.split(" ");
//						if (splittedOnSpace.length == 2) {
//							englishName = "" + splittedOnSpace[1] + " " + splittedOnSpace[0];
//						}
//						else if (splittedOnSpace.length == 1){
//							englishName = "" + splittedOnSpace[0];
//						}
//						else if (splittedOnSpace.length == 3) {
//							if (splittedOnSpace[0].indexOf("-") == splittedOnSpace[0].length() - 1) {
//								englishName = splittedOnSpace[2] + " " + splittedOnSpace[0] + splittedOnSpace[1];  
//							}
//							else {
//								englishName = splittedOnSpace[1] + " " + splittedOnSpace[2] + " " + splittedOnSpace[0];
//							}						
//						}
//						else {
//							throw new IOException("Unfortunately, there is a quadruple name.");
//						}
//					}
//					else if (inputLine.contains("GetEmployeeDetails") && arrivedAtProfessors == true) {
//						start = inputLine.indexOf(">") + 1;
//						end = inputLine.length();
//						hebrewName = inputLine.substring(start, end);
//						String[] splittedOnSpace = hebrewName.split(" ");
//						if (splittedOnSpace.length == 2) {
//							hebrewName = "" + splittedOnSpace[1] + " " + splittedOnSpace[0];
//						}
//						else if (splittedOnSpace.length == 1){
//							hebrewName = "" + splittedOnSpace[0];
//						}
//						else if (splittedOnSpace.length == 3) {
//							if (splittedOnSpace[0].indexOf("-") == splittedOnSpace[0].length() - 1) {
//								hebrewName = splittedOnSpace[2] + " " + splittedOnSpace[0] + splittedOnSpace[1];  
//							}
//							else {
//								hebrewName = splittedOnSpace[1] + " " + splittedOnSpace[2] + " " + splittedOnSpace[0];
//							}
//						}
//						else {
//							throw new IOException("Unfortunately, there is a quadruple hebrew name.");
//						}
//						if (englishName == null || hebrewName == null) {
//							//There's a null name, so throw an exception 
//							//(albeit IOException is not proper type but it works)
//							throw new IOException("There's a null name."); 
//						}
//						hebrewTranslations.put(StringEscapeUtils.unescapeHtml4(hebrewName), englishName);
//						String faculty = hebrewProfessorFiles[i].substring(0, hebrewProfessorFiles[i].indexOf(".html"));
//						String hebrewNameToUse = StringEscapeUtils.unescapeHtml4(hebrewName);
//						String hebrewNameEscapedJava = StringEscapeUtils.escapeJava(hebrewNameToUse);
//						Professor p = new Professor(null, englishName, faculty, hebrewNameEscapedJava, true);
//						professorsToInsert.add(p);
//						facultyMap.put(hebrewNameToUse, faculty);
//						//This will make the hebrew professor name in a new line after the english name.
//						professorSet.add(englishName + "\n" + hebrewNameToUse);
//					}
//					else if (inputLine.contains("searchtable")) {
//						//We've arrived at the professor listing section.
//						arrivedAtProfessors = true;
//					}
//				}
//				infile.close();
//			} //for loop
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		Object[] professorArrayObjects = professorSet.toArray();
//		String[] professorArrayStrings = Arrays.copyOf(professorArrayObjects, professorArrayObjects.length, String[].class);
//		return professorArrayStrings;
//	}

//	public String[] parseCourses() {
//		// create Hashmap, where the numbers are the keys and the Titles are the
//		// values
//
//		HashMap<String, String> map = new HashMap<String, String>();
//		HashSet<String> numberAndName = new HashSet<String>();
//		String inputLine = "";
//		String[] temp;
//
//		String[] courseFiles;
//		try {
//			courseFiles = getAssets().list("CourseListings");
//			for (int i = 0; i < courseFiles.length; i++) {
//				int lineNumber = 0;
//				BufferedReader infile = new BufferedReader(new InputStreamReader(
//						getAssets().open("CourseListings/" + courseFiles[i])));
//				while (infile.ready()) {// while more info exists
//					if (lineNumber >= 13 && lineNumber % 3 == 1) { // only take
//																	// numbers 13
//																	// and up for
//																	// every 3
//						temp = inputLine.split(" - ");
//						for (int t = 0; t < temp.length; t++) {
//							String number = temp[0].trim();// number;
//							String name = temp[1].replaceAll("</A>", "").trim();
//							name = capsFix2(name);
//							String faculty = courseFiles[i].substring(0, courseFiles[i].indexOf(".html"));
//							//Course c = new Course(null, name, number, null, null, faculty, true);
//							map.put(number, name); // trim and place only the number
//													// and name in
//							facultyMap.put(number, faculty);
//							courseNumbers.add(number);
//							
//							Course c = new Course(null, name, number, null, null, faculty, true);
//							coursesToInsert.add(c);
//							courseNumbersToCourses.put(number, c);
//							
//							numberAndName.add("" + number + " - " + name);
//						} // for temp
//					} // if
//					inputLine = infile.readLine(); // read the next line of the text
//					lineNumber++;
//				} // while infile
//				infile.close();
//			} // for courseFiles
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Object[] allNumbersAndNames = numberAndName.toArray();
//		String[] numbersAndNamesToReturn = Arrays.copyOf(allNumbersAndNames,
//				allNumbersAndNames.length, String[].class);
//		return numbersAndNamesToReturn;
//	} // parse()

	String[] concat(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c = new String[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
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
		MenuItem logoutItem = menu.findItem(R.id.action_logout);
		MenuItem loginItem = menu.findItem(R.id.action_login);
		MenuItem myAccountItem = menu.findItem(R.id.action_my_account);
    	boolean loggedIn = ((ApplicationWithGlobalVariables) this.getApplication()).isLoggedIn();
		if (!loggedIn) {
			logoutItem.setVisible(false);
			myAccountItem.setVisible(false);
		}
		else {
			loginItem.setVisible(false);
		}
		if (getLocalClassName().equals("FragmentMainActivity")) {
			myAccountItem.setVisible(false);
		}
		if (getLocalClassName().equals("MainActivity")) {
			loginItem.setVisible(false);
		}
		// Get the SearchView and set the searchable configuration
		MenuItem searchItem = menu.findItem(R.id.action_search);
		if (getLocalClassName().equals("SplashActivity")){
			searchItem.setVisible(false);
			loginItem.setVisible(false);
		}
		searchItem.setOnActionExpandListener(new OnActionExpandListener() {

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
	    		if (getResources().getConfiguration().orientation ==
	    				Configuration.ORIENTATION_LANDSCAPE) {
	    			if (getLocalClassName().equals("MainActivity")) {
		    		//	TextView t = (TextView) findViewById(R.id.introductoryText);
		    	//		t.setVisibility(View.GONE);
	    			}
	    			else if (getLocalClassName().equals("FragmentMainActivity")) {
	    				TextView t2 = (TextView) findViewById(R.id.privacyPolicyTextView);
	    				t2.setVisibility(View.GONE);
	    			}
	    		}
    			return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
	    		if (getResources().getConfiguration().orientation ==
	    				Configuration.ORIENTATION_LANDSCAPE) {
	    			if (getLocalClassName().equals("MainActivity")) {
		    	//		TextView t = (TextView) findViewById(R.id.introductoryText);
		    		//	t.setVisibility(View.VISIBLE);
	    			}
	    			else if (getLocalClassName().equals("FragmentMainActivity")) {
	    				TextView t2 = (TextView) findViewById(R.id.privacyPolicyTextView);
	    				t2.setVisibility(View.VISIBLE);
	    			}
	    		}
    			return true;
			}
		});
		final SearchView searchView = (SearchView) MenuItemCompat
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
	    		new SimpleCursorAdapter(getApplicationContext(), R.layout.list_item, cursor, from, to, 0);
        searchView.setOnQueryTextListener(searchQueryListener);
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setOnSuggestionListener(new OnSuggestionListener() {

           @Override
           public boolean onSuggestionClick(int position) {
        	   if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
        		   InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        		   imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        	   }
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
					String englishName = value;
					String hebrewName = c.getString(c.getColumnIndexOrThrow("hebrewProfessorName"));
					Intent i = new Intent(SearchResults.this, ProfessorView.class);
					if (englishName.equals("")) {
						i.putExtra("professorName", hebrewName);
						i.putExtra("faculty", "");
					}
					else {
						i.putExtra("professorName", hebrewName);
						i.putExtra("faculty", facultyMap.get(hebrewName));
					}
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
        case android.R.id.home: 
            onBackPressed();
            return true;
        case R.id.action_my_account:
			Intent i1 = new Intent(SearchResults.this, FragmentMainActivity.class);
			String globalStudentName = ((ApplicationWithGlobalVariables) getApplication()).getStudentName();
			i1.putExtra("the username", globalStudentName);
			startActivity(i1);
			return true;
		case R.id.action_logout:
	    	((ApplicationWithGlobalVariables) this.getApplication()).setLoggedIn(false);
			Intent i = new Intent(SearchResults.this, MainActivity.class);
			startActivity(i);
			return true;
		case R.id.action_login:
			Intent i2 = new Intent(SearchResults.this, MainActivity.class);
			startActivity(i2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private OnQueryTextListener searchQueryListener = new OnQueryTextListener() {
	    @Override
	    public boolean onQueryTextSubmit(String query) {
	        //search(query);
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
	private class ClientAsync extends AsyncTask<List<Professor>, Void, String> {

		public ClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d(getLocalClassName(), "Starting SearchResults Async...");
		}

		/**
		 * This is the method that does the database call.  Comment
		 * everything in this method to ignore the database.
		 */
		@Override
		protected String doInBackground(List<Professor>... params) {
			String result = null;
			//result = db.dropAllProfessorComments().toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null)
				Log.d(getLocalClassName(), "SearchResults async unsuccessful");
			else {
				Log.d(getLocalClassName(), "Dropping professor comments: " + res);
			}
		}
		
	}
}


