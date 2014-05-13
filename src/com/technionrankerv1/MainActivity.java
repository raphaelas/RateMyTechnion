package com.technionrankerv1;

import java.util.HashMap;
import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity implements
		OnItemClickListener {

	ListView listView;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String viewToStartOn = "welcome_view";

		if (viewToStartOn.equals("course_view")) {
			setContentView(R.layout.course_view);
		} else if (viewToStartOn.equals("search_view")) {
			setContentView(R.layout.search_view2);
			listView = (ListView) findViewById(R.id.search_results_view);
			listView.setOnItemClickListener(this);
		} else if (viewToStartOn.equals("welcome_view")) {
			setContentView(R.layout.welcome_view);
		} else if (viewToStartOn.equals("log_in")) {
			setContentView(R.layout.sign_in);

		} else if (viewToStartOn.equals("main")) {
			setContentView(R.layout.fragment_main);
		} else if (viewToStartOn.equals("prof_view")) {
			setContentView(R.layout.prof_view);
		}
	}

	public void initUserCourseView(View view) {
		Intent intent = new Intent(this, UserCourseViewMain.class);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		// Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
	    searchView.setSubmitButtonEnabled(true);
		return true;
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.main_activity_actions, menu); MenuItem
	 * searchItem = menu.findItem(R.id.action_search); SearchView searchView =
	 * (SearchView) MenuItemCompat.getActionView(searchItem); // Configure the
	 * search info and add any event listeners ... return
	 * super.onCreateOptionsMenu(menu); }
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * options menu from XML MenuInflater inflater = getMenuInflater();
	 * inflater.inflate(R.menu.options_menu, menu);
	 * 
	 * // Get the SearchView and set the searchable configuration SearchManager
	 * searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	 * SearchView searchView = (SearchView)
	 * menu.findItem(R.id.menu_search).getActionView(); // Assumes current
	 * activity is the searchable activity
	 * searchView.setSearchableInfo(searchManager
	 * .getSearchableInfo(getComponentName()));
	 * searchView.setIconifiedByDefault(false); // Do not iconify the widget;
	 * expand it by default
	 * 
	 * return true; }
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items

		switch (item.getItemId()) {
		case R.id.action_search:
			// openSearch();
			return true;
		case R.id.action_logout:
			openLoginPage(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openLoginPage(MenuItem item) {
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
	}
}
