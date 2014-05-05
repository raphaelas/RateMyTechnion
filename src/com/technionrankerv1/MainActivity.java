package com.technionrankerv1;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.serverapi.TechnionRankerAPI;

public class MainActivity extends Activity implements OnItemClickListener, OnClickListener {
	Button bSend;
	TextView tvStatus;
	
	private class ClientAsync extends AsyncTask<String, Void, String> {

	    public ClientAsync() {
	    }

	    @Override
	    protected void onPreExecute() {
	      // TODO Auto-generated method stub
	      super.onPreExecute();
	      tvStatus.setText("wait..");
	    }

	    @Override
	    protected String doInBackground(String... params) {
	      Course c = new Course("01", "Geology", (long) 5, "Spring 2011", true);
	      return new TechnionRankerAPI().insertCourse(c).toString();
	    }

	    @Override
	    protected void onPostExecute(String res) {
	      if (res == null)
	        tvStatus.setText("null");
	      else
	        tvStatus.setText(res);
	    }
	  }

	ListView listView;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String viewToStartOn = "welcome_view";

		if (viewToStartOn.equals("course_view")) {
			setContentView(R.layout.course_view);

		} else if (viewToStartOn.equals("search_view")) {
			setContentView(R.layout.search_view2);
			listView = (ListView) findViewById(R.id.search_results_view);
			listView.setOnItemClickListener(this);
		} else if (viewToStartOn.equals("welcome_view")) {
			setContentView(R.layout.welcome_view);
		} else if (viewToStartOn.equals("log_in")) {
			setContentView(R.layout.sign_in);

		} else if (viewToStartOn.equals("main")) {
			setContentView(R.layout.fragment_main);

		}
		else if (viewToStartOn.equals("prof_view")){
			setContentView(R.layout.prof_view);
		}
	    tvStatus = (TextView) findViewById(R.id.tvStatus);
	    bSend = (Button) findViewById(R.id.bSend);
	    bSend.setOnClickListener(this);
	}

	public void initUserCourseView(View view) {
		Intent intent = new Intent(this, UserCourseViewMain.class);
		startActivity(intent);
	}

	public void showSearchResults(View view) {
		Intent intent = new Intent(this, SearchResultsList.class);
		startActivity(intent);
		/*
		 * From Android Developer's website: EditText editText = (EditText)
		 * findViewById(R.id.edit_message); String message =
		 * editText.getText().toString(); intent.putExtra(EXTRA_MESSAGE,
		 * message);
		 */
	}
	

	
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		tvStatus.setText("wait..");
		ClientAsync as = new ClientAsync();
		as.execute("");
	}
	
	
}