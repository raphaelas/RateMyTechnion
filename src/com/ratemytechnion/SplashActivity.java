package com.ratemytechnion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import com.ratemytechnion.R;

public class SplashActivity extends SearchResults {

	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 7; // Sleep for some time

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // Removes
																// notification
																// bar

		setContentView(R.layout.splash);
		((ApplicationWithGlobalVariables) getApplication()).setLoggedIn(false);

		// Start timer and launch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	private class IntentLauncher extends Thread {
		@Override
		/**
		 * Sleep for some time and than start new activity.
		 */
		public void run() {
			try {
				// Sleeping
				Thread.sleep(SLEEP_TIME * 1000);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			// Start main activity
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
	}
	@Override
	public void onBackPressed(){
		
	}
}