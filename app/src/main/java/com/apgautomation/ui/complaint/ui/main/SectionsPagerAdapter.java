package com.apgautomation.ui.complaint.ui.main;

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


    PlaceholderFragment pf;
    PlaceholderFragmentAll pf1;

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        pf= PlaceholderFragment.newInstance(0);
        pf1= PlaceholderFragmentAll.newInstance(0);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position==0)
            return    pf;
        else
            return    pf1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        if(position==0)
        {
            return "Assigned Complaint";
        }
        else
        {
            return  "All Complaint";
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    public void onComplete(String str, int requestCode, int responseCode)
    {
        pf.onComplete(str,requestCode,responseCode);
        pf1.onComplete(str,requestCode,responseCode);
    }
}