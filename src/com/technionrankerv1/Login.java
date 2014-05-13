package com.technionrankerv1;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity{

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
			//Log.d(getLocalClassName(), username);
			//Log.d(getLocalClassName(), password);
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
					// Log.d(getLocalClassName(), doc.toString().substring(51,
					// 58));
					if (doc.toString().substring(51, 58).equals("Warning")){
						Log.d(getLocalClassName(), "Log in unsucessful");
						Log.d(getLocalClassName(), doc.toString().substring(51,58));
					}else{
						Log.d(getLocalClassName(), "Log in Sucessful");
						String [] temp =  doc.toString().substring(6058,6095).split(" ");
						String name = temp[1] +" "+ temp[2];
						Log.d(getLocalClassName(), name);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// OP=LI&UID=922130174&PWD=43150202&Login.x=%D7%94%D7%AA%D7%97%D7%91%D7%A8
			//32016463
		};
		downloadThread.start();
	}
}
