package com.technionrankerv1;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Login extends Activity{
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.sign_in);
	//	doLogin();
	}
	
	public void doLogin(){
		Thread downloadThread = new Thread() {
			public void run() {
				Document doc; 
				try {
					Log.d(getLocalClassName(), "in");
					/*
					doc = Jsoup.connect("http://en.wikipedia.org/").get();
					Elements newsHeadlines = doc.select("#mp-itn b a");
					Log.d(getLocalClassName(), newsHeadlines.toString());
					 */
			Connection.Response res = Jsoup.connect("https://ug3.technion.ac.il/rishum/login")
				    .data("OP", "LI", "UID", "922130174", "PWD", "43150202", "Login.x", "%D7%94%D7%AA%D7%97%D7%91%D7%A8")
				    .method(Method.POST)
				    .execute();
			Log.d(getLocalClassName(), "have a res");
			doc = res.parse();
			
			//Log.d(getLocalClassName(),doc.toString());
			Log.d(getLocalClassName(),doc.toString().substring(51, 58));
		
			} catch (IOException e) {
			e.printStackTrace();
			}
		}
				// OP=LI&UID=922130174&PWD=43150202&Login.x=%D7%94%D7%AA%D7%97%D7%91%D7%A8
		};
		downloadThread.start();
	}
}
