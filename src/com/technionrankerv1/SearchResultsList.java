package com.technionrankerv1;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

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
			values = parse();
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
		
		HashMap<String,String> map = new HashMap<String,String>(); //create Hashmap, where the numbers are the keys and the Titles are the values
		
		String inputLine = "";
		String[] temp;
		HashSet toReturn = new HashSet<String>();
		String[] allCourses = new String[0];
		String[] courseFiles = getAssets().list("CourseListings");
		for (int i = 0; i < courseFiles.length; i++) {
			int lineNumber = 0;
		    BufferedReader infile = new BufferedReader(new InputStreamReader(getAssets().open("CourseListings/" + courseFiles[i])));
			//InputStream is = getResources().openRawResource(R.raw.aerospace_engineering);
			//BufferedReader infile = new BufferedReader(new InputStreamReader(is)); // read the text file
			while (infile.ready()) {// while more info exists
				if(lineNumber>=13 && lineNumber%3==1){ // only take numbers 13 and up for every 3
					temp = inputLine.split(" - ");
					for (int t = 0; t<temp.length; t++){
						//Log.d(getLocalClassName(), temp[0]);
						String number = temp[0].trim();//number;
						String name = temp[1].replaceAll("</A>","").trim();//name
						map.put(number,name); // trim and place only the number and name in
					}
				}
				inputLine = infile.readLine(); // read the next line of the text
				lineNumber++;
			}
			
			infile.close();	
			
			Object[] temp1 = map.values().toArray();
			//Collection<String> temp1 = map.values();
			String[] oneFacultyCourses = Arrays.copyOf(temp1, temp1.length, String[].class);
			allCourses = concat(allCourses, oneFacultyCourses);
		}
		
		return allCourses;
		/*
		String[] toReturn = new String[5];
		for (Object key : temp1) { // iterate through the map and print the keys and the values they map to
		{	
			toReturn[]
			System.out.print(key+" ");
			System.out.println(map.get(key));
		}
		*/
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