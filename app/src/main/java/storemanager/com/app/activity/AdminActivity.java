package storemanager.com.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import storemanager.com.app.R;
import storemanager.com.app.adapter.AdminPanelFragmentPagerAdapter;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class AdminActivity extends AppCompatActivity {
    ViewPager mViewPager;
    private String userName;
    private String userEmail;
    private static String teamName;
    private String userId;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(Utils.LOG_TAG, "AdminActivity");

        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getExtras().get(Utils.EXTRA_TAG_NAME).toString();
            userEmail = intent.getExtras().get(Utils.EXTRA_TAG_EMAIL).toString();
            userId = intent.getExtras().get(Utils.EXTRA_TAG_ID).toString();
            teamName = intent.getExtras().get(Utils.EXTRA_TAG_TEAM).toString();

            if (!TextUtils.isEmpty(userName)
                    && !TextUtils.isEmpty(userEmail)
                    && !TextUtils.isEmpty(userId)
                    && !TextUtils.isEmpty(teamName)) {
                createTeamWithAdminInFireBase(userName, userEmail, userId, "Admin", teamName);
                // Get the ViewPager and set it's PagerAdapter so that it can display items
            } else {
            }
        }
    }

    private void setViewPager() {
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

    private void createTeamWithAdminInFireBase(final String name, final String email, final String id, final String userStatus, final String teamName) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(teamName)) {
                    User userNew = new User();
                    userNew.setName(name);
                    userNew.setEmail(email);
                    userNew.setId(id);
                    userNew.setRegistrationDate(Utils.getCurrentDate());
                    userNew.setStatus(userStatus);
                    mDatabase.child(teamName).child("users").push().setValue(userNew);

                    setViewPager();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public static String getTeamName() {
        return teamName;
    }
}
