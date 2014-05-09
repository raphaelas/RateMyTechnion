package com.technionrankerv1;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

	ListView listView;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String viewToStartOn = "course_view";

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
	}

	public void initUserCourseView(View view) {
		Intent intent = new Intent(this, UserCourseViewMain.class);
		startActivity(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
				Toast.LENGTH_SHORT).show();
	}
	
}
