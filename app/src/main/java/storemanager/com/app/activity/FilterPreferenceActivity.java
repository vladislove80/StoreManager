package storemanager.com.app.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import storemanager.com.app.R;

public class FilterPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.filter_pref);
    }
}
