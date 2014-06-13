package com.technionrankerv1;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

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

public class FragmentProfessors extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_professors, container, false);
        
        Bundle bundle = getActivity().getIntent().getExtras();
		String[] tempString = new String[bundle.getStringArray("courseList").length];
		tempString=bundle.getStringArray("courseList");

		List<String> profList=new ArrayList<String>();
		int professorCount = 0;
		for(int i =0; i<tempString.length; i++){
			GetProfessorClientAsync tempbla=new GetProfessorClientAsync();
			try{
				Professor dbProfessorList = tempbla.execute(tempString[i]).get();
				if (dbProfessorList==null) {
					//profList.add(tempString[i]);
					Log.d("FragmentProfessors", "We don't have that professor.");
				}
				else {
					profList.add(StringEscapeUtils.unescapeJava(dbProfessorList.getHebrewName()));
					professorCount++;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		String[] tempString2=new String[professorCount];
        for(int i=0; i<profList.size(); i++){
        	tempString2[i]=profList.get(i);
        }
		ListView list = (ListView)rootView.findViewById(R.id.leosProfs);
		ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(), R.layout.text_item, tempString2);
		list.setAdapter(ad);
        return rootView;
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
				Log.d("FragmentProfessors",
						"The professor name: " + currentProfessor.getHebrewName());
			}
		}
	}

}