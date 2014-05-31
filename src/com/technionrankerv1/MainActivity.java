package com.technionrankerv1;

import java.io.IOException;
import java.net.URL;

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
				try {
					Connection.Response res = Jsoup
							.connect("https://ug3.technion.ac.il/rishum/login")
							.data("OP", "LI", "UID", username, "PWD", password,
									"Login.x", "%D7%94%D7%AA%D7%97%D7%91%D7%A8")
							.method(Method.POST).execute();
					doc = res.parse();
					String rishum = res.cookie("rishum");
					String _ga = "GA1.3.1829128590.1396009585";
					Log.d(getLocalClassName(),res.cookies().toString());
					Log.d(getLocalClassName(), rishum);
					Log.d(getLocalClassName(), _ga);
					Connection.Response res1 = Jsoup
							.connect(
									"http://techmvs.technion.ac.il:100/cics/wmn/wmnnut03?OP=WK&SEM=201302")
							.cookie("rishum", rishum).method(Method.POST)
							.cookie("_ga", _ga)
							.execute();
					doc1 = res1.parse();
					Log.d(getLocalClassName(), doc1.toString().length() +"");
					URL url=res1.url();
					Log.d(getLocalClassName(), url.toString());
					int x = 1;
					Log.d(getLocalClassName(), doc.toString().length() + "");
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
						Intent i = new Intent(MainActivity.this,
								WelcomeView.class);
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
}
