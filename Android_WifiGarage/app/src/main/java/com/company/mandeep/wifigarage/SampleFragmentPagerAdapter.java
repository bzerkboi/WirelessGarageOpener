package com.company.mandeep.wifigarage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Mandeep on 1/11/2016.
 */
//The pager adapter is used to do the swiping left right and handles switching the views
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3; //The number of tabs we have
    private String tabTitles[] = new String [] {"Users","Items","Messages"}; //The title of each tab
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context=context;
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return UsersFragment.newInstance(position + 1);
            //break;
            case 1:
                return ItemsFragment.newInstance(position + 1);
            case 2:
                return MessagesFragment.newInstance(position+1);
            default:
                return PageFragment.newInstance(position + 1);
            //break;
        }
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        //Generate title based on item position
        return tabTitles[position];
    }


}
