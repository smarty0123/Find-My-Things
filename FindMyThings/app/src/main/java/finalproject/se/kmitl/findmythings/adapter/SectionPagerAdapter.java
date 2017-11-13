package finalproject.se.kmitl.findmythings.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import finalproject.se.kmitl.findmythings.fragment.NewsFeedFragment;
import finalproject.se.kmitl.findmythings.fragment.ThingsFindingFragment;
import finalproject.se.kmitl.findmythings.fragment.ThingsFoundFragment;

/**
 * Created by SMART on 10/11/2560.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter{
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
                return newsFeedFragment;
            case 1:
                ThingsFindingFragment thingsFindingFragment = new ThingsFindingFragment();
                return thingsFindingFragment;
            case 2:
                ThingsFoundFragment thingsFoundFragment = new ThingsFoundFragment();
                return thingsFoundFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "News Feed";
            case 1:
                return "Finding";
            case 2:
                return "Found Things";
            default:
                return null;
        }
    }
}
