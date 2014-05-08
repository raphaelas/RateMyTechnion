package com.technionrankerv1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author raphaelas
 * TODO this isn't actually being used yet.
 */
public class SearchResultsList extends ListActivity {
	ListView resultsListView;
	View headerView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Get ListView object from XML
		//resultsListView = (ListView) findViewById(R.id.listview);
		String[] values = null;
		try {
			values = parseProfessors();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//resultsListView.setAdapter(null);

		/*New Adapter:
		First parameter - Context
        Second parameter - Layout for the row
        Third parameter - ID of the TextView to which the data is written
        Fourth - the Array of data
        */
		Log.d(getLocalClassName(), values.toString());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		//resultsListView.setAdapter(adapter);
	    setListAdapter(adapter);
	    
	    
	    /*
		resultsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int itemPosition = position;
				String itemValue = (String) resultsListView.getItemAtPosition(position);
				//Show alert:
				Toast.makeText(getApplicationContext(),
						"Position: " + itemPosition+" ListItem: " + itemValue, Toast.LENGTH_SHORT)
						.show();
			}
			});
		*/
		}
	
	public String[] parse() throws Exception { 
		 //create Hashmap, where the numbers are the keys and the Titles are the values
		HashMap<String,String> map = new HashMap<String,String>();
		String inputLine = "";
		String[] temp;
		String[] allCourses = new String[0];
		String[] courseFiles = getAssets().list("CourseListings");
		for (int i = 0; i < courseFiles.length; i++) {
			int lineNumber = 0;
		    BufferedReader infile = new BufferedReader(new InputStreamReader(getAssets().open("CourseListings/" + courseFiles[i])));
			while (infile.ready()) {// while more info exists
				if(lineNumber>=13 && lineNumber%3==1){ // only take numbers 13 and up for every 3
					temp = inputLine.split(" - ");
					for (int t = 0; t<temp.length; t++){
						String number = temp[0].trim();//number;
						String name = temp[1].replaceAll("</A>","").trim();//name
						map.put(number,name); // trim and place only the number and name in
					} //for temp
				} //if
				inputLine = infile.readLine(); // read the next line of the text
				lineNumber++;
			} //while infile
			infile.close();	
			Object[] temp1 = map.values().toArray();
			String[] oneFacultyCourses = Arrays.copyOf(temp1, temp1.length, String[].class);
			allCourses = concat(allCourses, oneFacultyCourses);
		} //for courseFiles
		return allCourses;
	} //parse()
	
	public String[] parseProfessors() throws Exception {
		ArrayList<String> profList= new ArrayList<String>();
		String inputLine = "";
		String[] temp;
		String[] profListArray = null;
		String[] professorFiles = getAssets().list("ProfessorListings");
		for (int i = 0; i < professorFiles.length; i++) {
		    BufferedReader infile = new BufferedReader(new InputStreamReader(getAssets().open("ProfessorListings/" + professorFiles[i])));
			while (infile.ready()) {// while more info exists
			inputLine = infile.readLine();
			if(inputLine.startsWith("<td><a href=")){
				inputLine = inputLine.substring(1,inputLine.length()-9);
				temp = inputLine.split(">");
				profList.add(temp[2]);
			}
		}
		profListArray = profList.toArray(new String[profList.size()]);
		infile.close();	
		}
	return profListArray;
	}
	
	
	String[] concat(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c= new String[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
}