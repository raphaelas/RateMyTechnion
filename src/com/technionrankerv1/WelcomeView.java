package com.technionrankerv1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeView extends SearchResults implements
		OnItemClickListener {
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    
    List<String> listDataHeader, yourCourses, yourProfessors;
    HashMap<String, List<String>> listDataChild;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_view);
		//Log.d(getLocalClassName(), "in4");
		
		Bundle bundle = getIntent().getExtras();
		String personName = bundle.getString("the username");
		//Log.d(getLocalClassName(), personName);

		final TextView myText = (TextView) findViewById(R.id.textView23);
		//Log.d(getLocalClassName(), myText.getText().toString());
		myText.setText(personName);
		//Log.d(getLocalClassName(), myText.getText().toString());
		
		// get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        
        expListView.setOnChildClickListener(myListItemClicked);
	}
	
	 private OnChildClickListener myListItemClicked =  new OnChildClickListener() { // clicking on a course of prof name
		 
		  public boolean onChildClick(ExpandableListView parent, View v,
		    int groupPosition, int childPosition, long id) {
			  Log.d(getLocalClassName(),"groupPostion "+groupPosition+" childPosition " + childPosition);
			  // groupPosition == 0 --> courses
			  // groupPosition == 1 --> prof
			  List<String> temp = listDataChild.get(listDataHeader.get(groupPosition));
			  Log.d(getLocalClassName(), listDataHeader.get(groupPosition)+" "+temp.get(childPosition));
			  //openLoginPage();
			  return false;
		  }
	};
        
        
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
				Toast.LENGTH_SHORT).show();

	}
	 private void prepareListData() {
	        listDataHeader = new ArrayList<String>();
	        listDataChild = new HashMap<String, List<String>>();
	 
	        // Adding child data
	        listDataHeader.add("Your Courses");
	        listDataHeader.add("Your Professors");
	 
	        // Adding child data
	        yourCourses = new ArrayList<String>();
	        yourCourses.add("Algorithms 2");
	        yourCourses.add("Object-Oriented Programming");
	        yourCourses.add("Database Systems");
	        yourCourses.add("Project in Software");
	        yourCourses.add("Hebrew 2");
	 
	        yourProfessors = new ArrayList<String>();
	        yourProfessors.add("Sarah Blom");
	        yourProfessors.add("Tom Smith");
	        yourProfessors.add("Arthur Moore");
	        yourProfessors.add("Jeff Rice");
	        yourProfessors.add("Simon Jones");

	        listDataChild.put(listDataHeader.get(0), yourCourses); // Header, Child data
	        listDataChild.put(listDataHeader.get(1), yourProfessors);
	    }
	public void openLoginPage(){
		setContentView(R.layout.sign_in);
	}
}