package com.technionrankerv1;
 
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) { 
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
    
    @Override
    public Object instantiateItem(View collection, final int position) {

        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        final FrameLayout imageLayout = (FrameLayout) inflater.inflate(R.layout.loading_bar_view_pager, null);
        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
        spinner.setIndeterminate(true);
        ((ViewPager) collection).addView(imageLayout,0);
        return imageLayout;
}
}