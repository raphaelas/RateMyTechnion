package com.technionrankerv1;
 
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
    	Log.d("TabsPagerAdapter", index + "");
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new FragmentWelcomeView();
        case 1:
            // Games fragment activity
            return new FragmentCourses();
        case 2:
            // Movies fragment activity
            return new FragmentProfessors();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
}