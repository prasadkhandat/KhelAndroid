package org.hssus.khel.hsskhel.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.hssus.khel.hsskhel.Fragments.GameListFragment;
import org.hssus.khel.hsskhel.Fragments.UploadNewGame;

/**
 * Created by prasadkhandat on 2/11/18.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private static String[] titles={"Game List","Upload new Game"};

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return GameListFragment.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return UploadNewGame.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}