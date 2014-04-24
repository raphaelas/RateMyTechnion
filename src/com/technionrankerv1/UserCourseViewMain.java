package com.technionrankerv1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

/**
 * @author raphaelas
 *
 */
public class UserCourseViewMain extends Activity {
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.user_course_view);
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }
    
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Your Courses");
        listDataHeader.add("Your Professors");
 
        // Adding child data
        List<String> yourCourses = new ArrayList<String>();
        yourCourses.add("Algorithms 2");
        yourCourses.add("Object-Oriented Programming");
        yourCourses.add("Database Systems");
        yourCourses.add("Project in Software");
        yourCourses.add("Hebrew 2");
 
        List<String> yourProfessors = new ArrayList<String>();
        yourProfessors.add("Sarah Blom");
        yourProfessors.add("Tom Smith");
        yourProfessors.add("Arthur Moore");
        yourProfessors.add("Jeff Rice");
        yourProfessors.add("Simon Jones");

        listDataChild.put(listDataHeader.get(0), yourCourses); // Header, Child data
        listDataChild.put(listDataHeader.get(1), yourProfessors);
    }
}