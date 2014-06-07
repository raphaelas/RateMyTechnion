package com.technionrankerv1;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends SearchResults {
	private TextView errorM;
	private String username;
	private String password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		
		errorM = (TextView) findViewById(R.id.textView1);
		final EditText passwordInput = (EditText) findViewById(R.id.editText2);
		passwordInput.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					setText();
					return true;
				}
				return false;
			}
		});

		Button loginButton = (Button) findViewById(R.id.button1);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setText();
				/*Temporary code to test fragments:
				Intent i = new Intent(MainActivity.this,
						FragmentMainActivity.class);
				i.putExtra("the username", "שלום!");
				startActivity(i);*/
			}
		});
	}

	public void setText() {
		errorM.setTextColor(getResources().getColor(R.color.gray));
		errorM.setText("Please wait.  Connecting to UG System.");
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
				Document doc1;
				Document catalogDoc;
				try {
					Connection.Response res = Jsoup
							.connect("https://ug3.technion.ac.il/rishum/login")
							.data("OP", "LI", "UID", "922130174", "PWD", "43150202",
									"Login.x", "%D7%94%D7%AA%D7%97%D7%91%D7%A8")
							.method(Method.POST).execute();
					doc = res.parse();
					//Note to Leo: I moved the code further down in the file so it doesn't crash on failed logins.
					HashSet<String> course_nums = courseNumbers;
					int curr = 0;
					final int STOP = 30;
	                for (String courseNum : course_nums){
	                	if (curr > STOP) break;
	                	curr++;
	                	String URL = "https://ug3.technion.ac.il/rishum/course?MK=" + courseNum + "&CATINFO=&SEM=201302";
	                	Connection.Response catalogRes = Jsoup.connect(URL)
	                			.userAgent("User-Agent: Mozilla/5.0 (Macintosh; Intel"
            					+ " Mac OS X 10.7; rv:29.0) Gecko/20100101 Firefox/29.0").maxBodySize(0)
	                			.timeout(600000).execute();
	                	catalogDoc = catalogRes.parse();
	                	String catalogString = catalogDoc.toString();
	                	int theIndex = catalogString.indexOf("אחראים");
	                	if (theIndex != -1) {
		                	String[] str1 = catalogString.substring(theIndex+35, theIndex + 100).split(" ");
		                	// get the head prof english name
		                	String parsedHeadProfessor = getHeadProf(str1);
		                	if (parsedHeadProfessor == null) {
		                		Log.d(courseNum, "The head professor is empty");
		                	}
		                	else {
		                		Log.d(courseNum, parsedHeadProfessor);
		                		String translatedProfessor = hebrewTranslations.get(parsedHeadProfessor);
		                		if (translatedProfessor != null) {
					                Log.d(getLocalClassName() + "we did it:", translatedProfessor);
		                		}
		                		else {
		                			Log.d(courseNum, "No english name matches the hebrew name: " + parsedHeadProfessor);
		                		}
		                	}
	                	}
	                	else {
	                		Log.d(courseNum, "No head professor exists");
	                	}
	                }
	                
					int x = 1;
					//Log.d(getLocalClassName(), doc.toString().length() + "");
					if (doc.toString().length() < 4920) {
						// Log.d(getLocalClassName(), "fail");
						x = 0;
						reportError(x);
					}

					// Log.d(getLocalClassName(),doc.toString());
					// Log.d(getLocalClassName(), doc.toString().substring(4920,
					// 4930));
					if (!doc.toString().substring(4609, 4618)
							.equals("error-msg")
							&& x == 1) {
						// myText.getText().toString());

						String temp[] = null;
						Log.d(getLocalClassName(), "Log in Sucessful");

						temp = doc.toString().substring(5325, 5350).split(" ");

						// Log.d(getLocalClassName(),
						// doc.toString().substring(5325, 5350));
						String name = "visitor";
						if (temp[2] != null && temp[3] != null)
							name = temp[2] + " " + temp[3];
						// Log.d(getLocalClassName(), name);

						// LinearLayout v = (LinearLayout) getLayoutInflater()
						// .inflate(R.layout.welcome_view, null);

						// final TextView myText = (TextView) v
						// .findViewById(R.id.textView23);
						// myText.setText("Welcome " + name + "!");
						// Log.d(getLocalClassName(), "in1");
						
						String rishum = res.cookie("rishum");
						String _ga = "GA1.3.1829128590.1396009585";
//						Log.d(getLocalClassName(),res.cookies().toString());
//						Log.d(getLocalClassName(), rishum);
//						Log.d(getLocalClassName(), _ga);
						Connection.Response res1 = Jsoup
								.connect(
										"http://techmvs.technion.ac.il:100/cics/wmn/wmnnut03?OP=WK&SEM=201302")
								.cookie("rishum", rishum).method(Method.POST)
								.cookie("_ga", _ga)
								.execute();
						doc1 = res1.parse();
//						Log.d(getLocalClassName(), doc1.toString().length() +"");
//						URL url=res1.url();
//						Log.d(getLocalClassName(), url.toString());
						
						Intent i = new Intent(MainActivity.this,
								FragmentMainActivity.class);
						i.putExtra("the username", "שלום " + name + "!");
						startActivity(i);
						// startActivity(new Intent(Login.this,
						// welcomeView.class));

					} else if (x != 0) {
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
				errorM.setTextColor(getResources().getColor(R.color.red));
				if (x == 1) {
					errorM.setText("Incorrect username or password. Please try again.");
				} else {
					errorM.setText("The Technion UG website is down. Please try again later");
				}
			}
		});
	}			
	String getHeadProf(String[] s){
		
		String name = "";
		
		for (int t = 1; t < s.length; t++){
			// edit to add the different prefixes
			if (!s[t].contains(" ") && !s[t].contains("פרופ") &&
					!s[t].contains("חבר") && !s[t].contains("<") &&
					!s[t].contains("/>") && !s[t].contains("משנה") && 
					!(s[t].length() == 3 && s[t].substring(0, 2).contains("דר"))){
				name = name+ " " + s[t];
			}		
		}
		name = name.trim();
		if (name.length() == 0) return null;
		Pattern p = Pattern.compile("\\p{InHebrew}");
		Matcher m = p.matcher(name.substring(0, 1));
		if (m.matches()) {
			return name;		
		}
		return null;
	}
}
