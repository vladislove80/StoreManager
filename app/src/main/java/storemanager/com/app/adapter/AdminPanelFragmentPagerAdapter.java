package storemanager.com.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import storemanager.com.app.fragment.MenuFragment;
import storemanager.com.app.fragment.ShopsFragment;
import storemanager.com.app.fragment.GeneralStoreFragment;
import storemanager.com.app.fragment.TeamFragment;

public class AdminPanelFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Точки", "Команда", "Меню", "Склад" };
    private Context context;

    public AdminPanelFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0 : fragment = ShopsFragment.newInstance(1);
                break;
            case 1 : fragment = TeamFragment.newInstance();
                break;
            case 2 : fragment = MenuFragment.newInstance(2);
                break;
            case 3 : fragment = GeneralStoreFragment.newInstance();
                break;
            default: fragment = GeneralStoreFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
