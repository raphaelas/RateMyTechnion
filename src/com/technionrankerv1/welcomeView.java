package com.technionrankerv1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class welcomeView extends ActionBarActivity implements
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
}