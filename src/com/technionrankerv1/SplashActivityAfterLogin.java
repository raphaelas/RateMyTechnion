package com.technionrankerv1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.serverapi.TechnionRankerAPI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class SplashActivityAfterLogin extends SearchResults {
	ApplicationWithGlobalVariables a;
	Bundle bundle;
	public HashMap<String, String> facultyMap = new HashMap<String, String>();

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		a = ((ApplicationWithGlobalVariables) getApplication());
		professorsPart();
		coursesPart();
		bundle = getIntent().getExtras();
		String name = bundle.getString("the username");
		Intent i = new Intent(SplashActivityAfterLogin.this, FragmentMainActivity.class);
		i.putExtra("the username", name);
		i.putExtra("facultyMap", facultyMap);
		startActivity(i);
	}

	public void professorsPart() {
		String[] tempString = new String[a.courseList.length];
		tempString=a.courseList;
		List<String> profList = new ArrayList<String>();
		int professorCount = 0;
		a.setRatingsThreshold(tempString.length*2);
		for(int i =0; i<tempString.length; i++){
			GetProfessorClientAsync gpca = new GetProfessorClientAsync();
			try {
				Professor dbProfessor = gpca.execute(tempString[i]).get();
				if (dbProfessor == null) {
					// profList.add(tempString[i]);
					Log.d("FragmentProfessors", "We don't have that professor.");
					a.decrementRatingsThreshold();
				} else {
					String hebNameToUse = StringEscapeUtils
							.unescapeJava(dbProfessor.getHebrewName());
					profList.add(hebNameToUse);
					professorCount++;
					facultyMap.put(hebNameToUse, dbProfessor.getFaculty());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String[] professorValues = new String[professorCount];
		for (int i = 0; i < profList.size(); i++) {
			professorValues[i] = profList.get(i);
		}
		bundle.putStringArray("professorValues", professorValues);
	}

	public void coursesPart() {
		String[] tempString = new String[a.courseList.length];

		tempString=a.courseList;

		List<String> courseList=new ArrayList<String>();
		int courseCount = 0;
		for (int i = 0; i < tempString.length; i++) {
			GetCourseClientAsync gcca = new GetCourseClientAsync();
			try {
				List<Course> dbCourseList = gcca.execute(tempString[i]).get();
				if (dbCourseList == null || dbCourseList.isEmpty()) {
					Log.d(getLocalClassName(), tempString[i] + ": We don't have that course.");
				} else {
					Course currCourse = dbCourseList.get(0);
					courseList.add(tempString[i] + ": " + currCourse.getName());
					facultyMap.put(currCourse.getNumber(),
							currCourse.getFaculty());
					courseCount++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String[] courseValues = new String[courseCount];
		for (int i = 0; i < courseList.size(); i++) {
			courseValues[i] = courseList.get(i);
		}
		bundle.putStringArray("courseValues", courseValues);
	}

	private class GetProfessorClientAsync extends
	AsyncTask<String, Void, Professor> {
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
				Log.d("FragmentProfessors",
						"Get of professor for course failed.");
				a.decrementRatingsThreshold();
			} else {
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
			}
		}
	}

	@Override
	public void onBackPressed(){
		
	}

}
