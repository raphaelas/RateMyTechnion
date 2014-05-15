package com.technionrankerv1;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

	private String username;
	private String password;

	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
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
							.data("OP", "LI", "UID", username, "PWD", password,
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
						Log.d(getLocalClassName(), "Log in Sucessful");
						String[] temp = doc.toString().substring(5325, 5350)
								.split(" ");
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
						Intent i = new Intent(Login.this, welcomeView.class);
						i.putExtra("the username", "Welcome " + name + "!");
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
}
