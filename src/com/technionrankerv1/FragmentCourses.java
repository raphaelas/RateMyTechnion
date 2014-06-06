package com.technionrankerv1;
 
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technionrankerv1.R;
 
public class FragmentCourses extends Fragment {   
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	Log.d("FragmentCourses", "Here");
 
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);
        //setHasOptionsMenu(true);
         
        return rootView;
    }
}
