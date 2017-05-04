package storemanager.com.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import storemanager.com.app.R;
import storemanager.com.app.adapter.AdminPanelFragmentPagerAdapter;
import storemanager.com.app.adapter.GeneralStoreManager;
import storemanager.com.app.adapter.SummaryHandler;
import storemanager.com.app.utils.Utils;

public class AdminActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = AdminActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private String userName;
    private String userEmail;
    private static String teamName;
    private String userId;
    private GoogleApiClient mGoogleApiClient;
    private SummaryHandler mSummaryHandler;

    private GeneralStoreManager mGeneralStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(Utils.LOG_TAG, "AdminActivity");
        if(savedInstanceState == null){
            Intent intent = getIntent();
            if (intent != null) {
                //get throw NLP when first time entry with empty data
                userName = intent.getExtras().get(Utils.EXTRA_TAG_NAME).toString();
                userEmail = intent.getExtras().get(Utils.EXTRA_TAG_EMAIL).toString();
                userId = intent.getExtras().get(Utils.EXTRA_TAG_ID).toString();
                teamName = intent.getExtras().get(Utils.EXTRA_TAG_TEAM).toString();
                mSummaryHandler = new SummaryHandler(teamName);
                mGeneralStoreManager = new GeneralStoreManager();
                setViewPager();
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sign_out:
                signout();
                return true;
            default : return super.onOptionsItemSelected(item);
        }
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Toast.makeText(getBaseContext(), "Sign out.", Toast.LENGTH_SHORT).show();
                    }
                });
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
        finish();
    }
    public static String getTeamName() {
        return teamName;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(Utils.LOG_TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSummaryHandler.enableSummaryListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSummaryHandler.disableSummaryListener();
    }

    public GeneralStoreManager getGeneralStoreManager() {
        return mGeneralStoreManager;
    }

    public void setGeneralStoreManager(GeneralStoreManager mGeneralStoreManager) {
        this.mGeneralStoreManager = mGeneralStoreManager;
    }
}
