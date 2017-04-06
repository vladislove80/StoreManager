package storemanager.com.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import storemanager.com.app.R;
import storemanager.com.app.adapter.AdminPanelFragmentPagerAdapter;
import storemanager.com.app.utils.Utils;

public class AdminActivity extends AppCompatActivity {
    ViewPager mViewPager;
    private String userName;
    private String userEmail;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(Utils.LOG_TAG, "AdminActivity");

        Intent intent = getIntent();
        userName = intent.getExtras().get(UserEntryOrCreateTeamActivity.TAG_NAME).toString();
        userEmail = intent.getExtras().get(UserEntryOrCreateTeamActivity.TAG_EMAIL).toString();
        teamName = intent.getExtras().get(UserEntryOrCreateTeamActivity.TAG_TEAM).toString();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new AdminPanelFragmentPagerAdapter(getSupportFragmentManager(),
                AdminActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
