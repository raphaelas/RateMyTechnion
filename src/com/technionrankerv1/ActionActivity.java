package com.technionrankerv1;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;


public class ActionActivity extends ActionBarActivity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
}
