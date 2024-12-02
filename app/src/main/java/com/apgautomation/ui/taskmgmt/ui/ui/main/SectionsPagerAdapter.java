package com.apgautomation.ui.taskmgmt.ui.ui.main;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;



/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    PlaceholderFragment pf;
    AssignedByMe as;
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position==0)
        {
            pf=PlaceholderFragment.newInstance(position + 1);
            return pf;
        }
        else
        {
            as=AssignedByMe.newInstance();
            return as;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "To Do";
        else
            return  "Created By Me";
       // return  (position+1)+"";  //mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
    public  void filter(int i)
    {
        if(i==0)
            pf.filter();
        else
            as.filter();
    }

    public void setDefault()
    {
        pf.setDefault();

        as.setDefault();
    }
}