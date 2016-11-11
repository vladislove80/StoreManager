package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import storemanager.com.app.R;
import storemanager.com.app.utils.Utils;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener{


    private DatabaseReference mDatabase;
    private String userEmail;
    private String userId;

    private Button mAddItemButton;
    private Button mSaveToDatabaseButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(Utils.EXTRA_TAG_MAIL);
        userId = intent.getStringExtra(Utils.EXTRA_TAG_ID);

        /*User user = new User(userEmail, userId);
        mDatabase.child("users").child(userId).setValue(user);*/

        String date = Utils.getCurrentDate();
        mAddItemButton = (Button) findViewById(R.id.add_button);
        mSaveToDatabaseButton = (Button) findViewById(R.id.save_button);

        mAddItemButton.setOnClickListener(this);
        mSaveToDatabaseButton.setOnClickListener(this);

        Log.d(Utils.LOG_TAG, "email = " + userEmail);
        Log.d(Utils.LOG_TAG, "id = " + userId);
        Log.d(Utils.LOG_TAG, "date = " + date);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.add_button) {

        } else if (i == R.id.save_button) {

        }
    }
}
