package com.technionrankerv1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.serverapi.TechnionRankerAPI;

public class FragmentMainActivity extends SearchResults implements TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Home", "Courses", "Professors" };
	private String[] professorValuesToPassToAdapter;
	private String[] courseValuesToPassToAdapter;
	public HashMap<String, String> facultyMap = new HashMap<String, String>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main_activity);
		
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.blueish)));
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
        Bundle bundle = getIntent().getExtras();
		String[] tempString = new String[bundle.getStringArray("courseList").length];
		tempString=bundle.getStringArray("courseList");

		List<String> profList=new ArrayList<String>();
		int professorCount = 0;
		for(int i =0; i<tempString.length; i++){
			GetProfessorClientAsync tempbla=new GetProfessorClientAsync();
			try{
				Professor dbProfessor = tempbla.execute(tempString[i]).get();
				if (dbProfessor==null) {
					//profList.add(tempString[i]);
					Log.d("FragmentProfessors", "We don't have that professor.");
				}
				else {
					String hebNameToUse = StringEscapeUtils.unescapeJava(dbProfessor.getHebrewName());
					profList.add(hebNameToUse);
					professorCount++;
					facultyMap.put(hebNameToUse, dbProfessor.getFaculty());
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		professorValuesToPassToAdapter=new String[professorCount];
        for(int i=0; i<profList.size(); i++){
        	professorValuesToPassToAdapter[i]=profList.get(i);
        }
        
        
        
        coursesPart();
		resetGlobalVariables();
	}
	
	public void coursesPart() {
        Bundle bundle = getIntent().getExtras();
		String[] tempString = new String[bundle.getStringArray("courseList").length];

		tempString=bundle.getStringArray("courseList");
        
		List<String> courseList=new ArrayList<String>();
		((ApplicationWithGlobalVariables) getApplication()).
		setRatingsThreshold(tempString.length*2);
		int courseCount = 0;
		for(int i=0; i<tempString.length; i++){
			GetCourseClientAsync tempbla=new GetCourseClientAsync();
			try{
				List<Course> dbCourseList = tempbla.execute(tempString[i]).get();
				if (dbCourseList == null || dbCourseList.isEmpty()) {
					//courseList.add(tempString[i]);
					Log.d(getLocalClassName(), "We don't have that course.");
				}
				else {
					Course currCourse = dbCourseList.get(0);
					courseList.add(tempString[i] + ": " + currCourse.getName());
					facultyMap.put(currCourse.getNumber(), currCourse.getFaculty());
					courseCount++;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
        courseValuesToPassToAdapter=new String[courseCount];
        for(int i=0; i<courseList.size(); i++){
        	courseValuesToPassToAdapter[i]=courseList.get(i);
        }
	}
	
	public String[] getProfessorValues() {
		return professorValuesToPassToAdapter;
	}
	
	public String[] getCourseValues() {
		return courseValuesToPassToAdapter;
	}
	
	private void resetGlobalVariables() {
		ApplicationWithGlobalVariables a = ((ApplicationWithGlobalVariables) getApplication());
		boolean isExistingStudent = false;
		a.setCourseCommentsLiked(new HashSet<CourseComment>());
		a.setProfessorCommentsLiked(new HashSet<ProfessorComment>());
		Set<String> studentNameSet = a.studentsToRatingsSubmitted.keySet();
		for (String studentName : studentNameSet) {
			if (studentName.equals(a.getStudentName())) {
				a.setRatingsSubmitted(a.studentsToRatingsSubmitted.get(a.getStudentName()));
				isExistingStudent = true;
			}
		}
		if (!isExistingStudent) {
			a.setRatingsSubmitted(0);
			a.resetStudentID();
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition(), true);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	private class GetProfessorClientAsync extends
	AsyncTask<String, Void,Professor> {
		public GetProfessorClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Professor doInBackground(String... params) {
			String courseNumber = params[0];
			Course lookup = new Course(null, null, courseNumber, null, null,
					null, true);
			Professor result = new TechnionRankerAPI()
			.getProfessorForCourse(lookup);
			return result;
		}

		@Override
		protected void onPostExecute(Professor res) {
			if (res == null) {
				Log.d("FragmentProfessors", "Get of professor for course failed.");
			} 
			else {
				Professor currentProfessor = res;
			}
		}
	}

	private class GetCourseClientAsync extends
	AsyncTask<String, Void, List<Course>> {
		public GetCourseClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Course> doInBackground(String... params) {
			String courseNumber = params[0];
			Course lookup = new Course(null, null, courseNumber, null, null,
					null, true);
			List<Course> result = new TechnionRankerAPI()
			.getCourseByCourseNumber(lookup);
			return result;
		}

		@Override
		protected void onPostExecute(List<Course> res) {
			if (res == null) {
				Log.d("FragmentCourses", "Get of course failed.");
			} else if (res.size() == 0) {
				Log.d("FragmentCourses", "Get of course returned empty.");
			} else {
				Course currentCourse = res.get(0);
			}
		}
}
}
