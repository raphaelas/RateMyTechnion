package com.technionrankerv1;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener {
	
	private String username;
    private String password;
	ListView listView;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);			
		
		Button loginButton = (Button) findViewById(R.id.button1);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setText();
			}
		});
	}

	public void setText() {
		final EditText Input1 = (EditText) findViewById(R.id.editText1);
		final EditText Input2 = (EditText) findViewById(R.id.editText2);
		username = Input1.getText().toString();
		password = Input2.getText().toString();
		if (username != null && password != null) {
			// Log.d(getLocalClassName(), username);
			// Log.d(getLocalClassName(), password);
			doLogin();
		}
	}

	public void doLogin() {
		Thread downloadThread = new Thread() {
			public void run() {
				Document doc;
				try {
					// Log.d(getLocalClassName(), "in");
					/*
					 * doc = Jsoup.connect("http://en.wikipedia.org/").get();
					 * Elements newsHeadlines = doc.select("#mp-itn b a");
					 * Log.d(getLocalClassName(), newsHeadlines.toString());
					 */
					/*
					 * Connection.Response res =
					 * Jsoup.connect("https://ug3.technion.ac.il/rishum/login")
					 * .data("OP", "LI", "UID", "922130174", "PWD", "43150202",
					 * "Login.x", "%D7%94%D7%AA%D7%97%D7%91%D7%A8")
					 * .method(Method.POST) .execute();
					 * Log.d(getLocalClassName(), "have a res");
					 */
					Connection.Response res = Jsoup
							.connect("https://ug3.technion.ac.il/rishum/login")
							.data("OP", "LI", "UID", "922130141", "PWD", "32016463",
									"Login.x", "%D7%94%D7%AA%D7%97%D7%91%D7%A8")
							.method(Method.POST).execute();
					doc = res.parse();

					// Log.d(getLocalClassName(),doc.toString());
					// Log.d(getLocalClassName(),
					// doc.toString().substring(4609, 4618));
					if (doc.toString().substring(4609, 4618)
							.equals("error-msg")) {
						Log.d(getLocalClassName(), "Log in unsucessful");
						reportError();
						// Log.d(getLocalClassName(),
						// myText.getText().toString());
					} else {
						String temp[] = null;
						Log.d(getLocalClassName(), "Log in Sucessful");
						if (doc.toString().length() > 5350){
							temp = doc.toString().substring(5325, 5350)
								.split(" ");
						}
						// Log.d(getLocalClassName(),
						// doc.toString().substring(5325, 5350));
						String name = temp[2] + " " + temp[3];
						// Log.d(getLocalClassName(), name);

						// LinearLayout v = (LinearLayout) getLayoutInflater()
						// .inflate(R.layout.welcome_view, null);

						// final TextView myText = (TextView) v
						// .findViewById(R.id.textView23);
						// myText.setText("Welcome " + name + "!");
						// Log.d(getLocalClassName(), "in1");
						Intent i = new Intent(MainActivity.this, WelcomeView.class);
						i.putExtra("the username", "שלום " + name + "!");
						startActivity(i);
						// startActivity(new Intent(Login.this,
						// welcomeView.class));
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// OP=LI&UID=922130174&PWD=43150202&Login.x=%D7%94%D7%AA%D7%97%D7%91%D7%A8
			// 32016463
			// 203868179
			// 65374675
		};
		downloadThread.start();
	}

	protected void reportError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final TextView errorM = (TextView) findViewById(R.id.textView1);
				errorM.setMaxLines(1);
				errorM.setText("Incorrect username or password. Please ty again.");
			}
		});
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
		Log.w("MyApp", "In options");

		switch (item.getItemId()) {
		case R.id.action_logout:
			// openLoginPage(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
