package com.technionrankerv1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class SearchResults extends Activity {
	//@override
	public void onCreate(Bundle savedInstance){

		super.onCreate(savedInstance);
		Log.d("MyApp", "11111");

		setContentView(R.layout.search_resaults);
		Log.d("MyApp", "22222");
		Bundle b = getIntent().getExtras();
		String query =b.getString("query");
		Log.d("MyApp", "query:"+query);
		SearchableAdapter adapt = new SearchableAdapter(query,SearchResults.this);
		ListView view = (ListView) findViewById(R.id.list);
		view.setAdapter(adapt);
	}
}
