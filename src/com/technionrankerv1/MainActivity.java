package com.technionrankerv1;

import java.io.IOException;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private String username;
    private String password;

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
					Connection.Response res = Jsoup
							.connect("https://ug3.technion.ac.il/rishum/login")
							.data("OP", "LI", "UID", username, "PWD", password,
									"Login.x", "%D7%94%D7%AA%D7%97%D7%91%D7%A8")
							.method(Method.POST).execute();
					doc = res.parse();
					int x = 1;
					Log.d(getLocalClassName(), doc.toString().length()+"");
					if (doc.toString().length() < 4920){
						//Log.d(getLocalClassName(), "fail");
						x = 0;
						reportError(x);
					}
					
					// Log.d(getLocalClassName(),doc.toString());
					// Log.d(getLocalClassName(), doc.toString().substring(4920, 4930));
					if (!doc.toString().substring(4609, 4618)
							.equals("error-msg") && x == 1) {
						// myText.getText().toString());
						
						String temp[] = null;
						Log.d(getLocalClassName(), "Log in Sucessful");
						
						temp = doc.toString().substring(5325, 5350)
								.split(" ");
						
								
						// Log.d(getLocalClassName(),
						// doc.toString().substring(5325, 5350));
						String name = "visitor";
							if(temp[2] != null && temp[3] != null)
								name = temp[2] + " " + temp[3];
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
						
					} else if (x != 0){
						Log.d(getLocalClassName(), "Log in unsucessful");
						reportError(1);
						// Log.d(getLocalClassName(),
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

	protected void reportError(final int x) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final TextView errorM = (TextView) findViewById(R.id.textView1);
				errorM.setMaxLines(1);
				if (x==1){
					errorM.setText("Incorrect username or password. Please try again.");
				}else{
					errorM.setText("The Technion UG website is down. Please try again Later");
				}
			}
		});
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
