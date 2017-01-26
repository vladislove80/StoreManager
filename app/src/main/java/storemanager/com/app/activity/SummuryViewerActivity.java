package storemanager.com.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import storemanager.com.app.R;
import storemanager.com.app.utils.Utils;

public class SummuryViewerActivity  extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_summary_composer);
        Log.v(Utils.LOG_TAG, "SummuryViewerActivity");
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
}
