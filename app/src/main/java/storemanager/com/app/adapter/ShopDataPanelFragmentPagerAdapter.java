package storemanager.com.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import storemanager.com.app.fragment.ShopStoreFragment;
import storemanager.com.app.fragment.ShopSummaryFragment;

public class ShopDataPanelFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Отчеты", "Склад магазина" };
    private Context context;

    public ShopDataPanelFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0 : fragment = ShopSummaryFragment.newInstance();
                break;
            case 1 : fragment = ShopStoreFragment.newInstance();
                break;
            default: fragment = ShopSummaryFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
