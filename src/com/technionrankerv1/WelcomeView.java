package com.technionrankerv1;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeView extends ActionBarActivity implements
		OnItemClickListener {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_view);
		//Log.d(getLocalClassName(), "in4");
		
		Bundle bundle = getIntent().getExtras();
		String personName = bundle.getString("the username");
		//Log.d(getLocalClassName(), personName);

		final TextView myText = (TextView) findViewById(R.id.textView23);
		//Log.d(getLocalClassName(), myText.getText().toString());
		myText.setMaxLines(1);
		myText.setText(personName);
		//Log.d(getLocalClassName(), myText.getText().toString());
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
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
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.action_search)
		// .getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the
		// widget;
		// expand it by default
		// searchView.setSubmitButtonEnabled(true);

		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Log.w("MyApp", "In options");

		switch (item.getItemId()) {
		case R.id.action_logout:
			openLoginPage();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public void openLoginPage(){
		setContentView(R.layout.sign_in);
	}
}