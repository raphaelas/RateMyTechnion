package com.technionrankerv1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author raphaelas
 * TODO this isn't actually being used yet.
 */
public class SearchResultsList extends Activity {
	ListView resultsListView;
	View headerView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Get ListView object from XML
		resultsListView = (ListView) findViewById(R.id.search_results_view);
		//Array values to appear in the list.
		String[] values = new String[] {
				"Tom Smith",
				"Robotics",
				"James Moore",
				"Project in Software",
				"Jason Wilson"
		};
		resultsListView.setAdapter(null);

		/*New Adapter:
		First parameter - Context
        Second parameter - Layout for the row
        Third parameter - ID of the TextView to which the data is written
        Fourth - the Array of data
        */
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		resultsListView.setAdapter(adapter);
		resultsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int itemPosition = position;
				String itemValue = (String) resultsListView.getItemAtPosition(position);
				//Show alert:
				Toast.makeText(getApplicationContext(),
						"Position: " + itemPosition+" ListItem: " + itemValue, Toast.LENGTH_SHORT)
						.show();
			}
			});
		}



}