package com.technionrankerv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentCourses extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        String[] tempString2 = ((FragmentMainActivity) getActivity()).getCourseValues();
		ListView list = (ListView)rootView.findViewById(R.id.leosCourse);
		ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(), R.layout.text_item, tempString2);
		list.setAdapter(ad);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	            String value = (String)parent.getItemAtPosition(position);
	            String[] splitted = value.split(": ");
				Intent i = new Intent(getActivity(), CourseView.class);
				i.putExtra("courseNumber", splitted[0]);
				i.putExtra("courseName", splitted[1]);
				i.putExtra("faculty", ((FragmentMainActivity) getActivity()).facultyMap.get(splitted[0]));
				startActivity(i);
			}
		});
        return rootView;
    }
}