package com.technionrankerv1;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SearchableActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("MyApp", "In Searchable");
		// show the
		// results
		// Get the intent, verify the action and get the query

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			// SearchRecentSuggestions suggestions = new
			// SearchRecentSuggestions(this,
			// MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
			// suggestions.saveRecentQuery(query, null);
			Log.d("MyApp", "value:" + query);
			Bundle b = new Bundle();
			b.putString("query", query);
			Intent newIntent = new Intent(getApplicationContext(),
					SearchResults.class);
			newIntent.putExtras(b);
			startActivity(newIntent);
		}

		

	}
}

/*
 * public class MyAdapter { public MyAdapter(Context context, int
 * layoutResourceId, String[] places) { super(context, layoutResourceId,
 * places); this.context = context; >>>>>>> origin/master
 * 
 * // }
 * 
 * /* public void doMySearch(String query) { Context context=getBaseContext();
 * SearchableAdapter sa=new SearchableAdapter(getBaseContext()); sa. }
 */
/*
 * public class MyAdapter { public MyAdapter(Context context, int
 * layoutResourceId, String[] places) { super(context, layoutResourceId,
 * places); this.context = context;
 * 
 * this.data = Arrays.asList(places); this.origData = new
 * ArrayList<String>(this.data);
 * 
 * } }
 */

/*
 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu
 * items for use in the action bar MenuInflater inflater = getMenuInflater();
 * inflater.inflate(R.menu.main_activity_actions, menu); // Get the SearchView
 * and set the searchable configuration MenuItem
 * searchItem=menu.findItem(R.id.action_search); SearchView
 * searchView=(SearchView)MenuItemCompat.getActionView(searchItem);
 * SearchManager searchManager = (SearchManager)
 * getSystemService(Context.SEARCH_SERVICE); //SearchView searchView =
 * (SearchView) menu.findItem(R.id.action_search) // .getActionView(); //
 * Assumes current activity is the searchable activity
 * searchView.setSearchableInfo(searchManager
 * .getSearchableInfo(getComponentName()));
 * searchView.setIconifiedByDefault(false); // Do not iconify the widget; //
 * expand it by default // searchView.setSubmitButtonEnabled(true); return true;
 * }
 *//*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu items for use in the action bar MenuInflater inflater =
	 * getMenuInflater(); inflater.inflate(R.menu.main_activity_actions, menu);
	 * // Get the SearchView and set the searchable configuration MenuItem
	 * searchItem = menu.findItem(R.id.action_search); SearchView searchView =
	 * (SearchView) MenuItemCompat .getActionView(searchItem); SearchManager
	 * searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	 * // SearchView searchView = (SearchView) //
	 * menu.findItem(R.id.action_search) // .getActionView(); // Assumes current
	 * activity is the searchable activity
	 * searchView.setSearchableInfo(searchManager
	 * .getSearchableInfo(getComponentName()));
	 * searchView.setIconifiedByDefault(false); // Do not iconify the // widget;
	 * // expand it by default // searchView.setSubmitButtonEnabled(true);
	 * 
	 * return true; }
	 */
