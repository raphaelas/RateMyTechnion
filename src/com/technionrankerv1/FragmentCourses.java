package com.technionrankerv1;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.serverapi.TechnionRankerAPI;

public class FragmentCourses extends Fragment {
//	List<String> yourCourses;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
		String[] tempString = new String[bundle.getStringArray("courseList").length];
		Log.d("Leo inside fragmentcourse", "1");

		tempString=bundle.getStringArray("courseList");
		Log.d("Leo inside fragmentcourse", "2");
		Log.d("The length of the tempstring is ", tempString.length+"");

		for(int i = 0; i <  tempString.length; i++){
			Log.d("Leo inside fragmentcourse", tempString[i]);
		}	
         
		ListView list = (ListView)rootView.findViewById(R.id.leosCourse);

	
		//listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
		//expListView.setAdapter(listAdapter);
		List<String> courseList=new ArrayList<String>();
		((ApplicationWithGlobalVariables) getActivity().
				getApplication()).setRatingsThreshold(tempString.length*2);
		for(int i=0; i<tempString.length; i++){
			GetCourseClientAsync tempbla=new GetCourseClientAsync();
			try{
				List<Course> dbCourseList = tempbla.execute(tempString[i]).get();
				if (dbCourseList.isEmpty()) {
					courseList.add(tempString[i]);
				}
				else {
					courseList.add(tempString[i] + ": " + dbCourseList.get(0).getName());
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
        String[] tempString2=new String[tempString.length];
        for(int i=0; i<courseList.size(); i++){
        	tempString2[i]=courseList.get(i);
        }
		ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(), R.layout.text_item, tempString2);
		list.setAdapter(ad);

        return rootView;
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
				Log.d("FragmentCourses", "The course name: " +
				currentCourse.getName());
			}
		}
	}
}