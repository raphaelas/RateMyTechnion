package com.technionrankerv1;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
		String viewToStartOn = "sign_in";
		
		if (viewToStartOn.equals("course_view")) {
			setContentView(R.layout.course_view);
		} else if (viewToStartOn.equals("search_view")) {
			setContentView(R.layout.search_view2);
			listView = (ListView) findViewById(R.id.search_results_view);
			listView.setOnItemClickListener(this);
		} else if (viewToStartOn.equals("welcome_view")) {
			setContentView(R.layout.welcome_view);
		} else if (viewToStartOn.equals("sign_in")) {
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
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items

		switch (item.getItemId()) {
		case R.id.action_search:
			// openSearch();
			return true;
		case R.id.action_logout:
			// openLoginPage(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void goToLogin(View v) {
		Intent intent = new Intent(this, Login.class);;
		startActivity(intent);
	}
	/*
	 * public void openLoginPage(MenuItem item) { Intent intent = new
	 * Intent(this, Login.class); startActivity(intent); }
	 */

}
