package storemanager.com.app.activity;

import android.app.DialogFragment;
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
import storemanager.com.app.dialog.AddDataDialog;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener{

    private DialogFragment dlg1;

    private DatabaseReference mDatabase;
    private String userEmail;
    private String userId;

    private Button mAddItemButton;
    private Button mSaveToDatabaseButton;

    int test = 10;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Log.v(Utils.LOG_TAG, "AddDataActivity");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(Utils.EXTRA_TAG_MAIL);
        userId = intent.getStringExtra(Utils.EXTRA_TAG_ID);

        String date = Utils.getCurrentDate();
        mAddItemButton = (Button) findViewById(R.id.add_button);
        mSaveToDatabaseButton = (Button) findViewById(R.id.save_button);

        mAddItemButton.setOnClickListener(this);
        mSaveToDatabaseButton.setOnClickListener(this);

        Log.d(Utils.LOG_TAG, "email = " + userEmail);
        Log.d(Utils.LOG_TAG, "id = " + userId);
        Log.d(Utils.LOG_TAG, "date = " + date);

        dlg1 = new AddDataDialog();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.add_button) {
            dlg1.show(getFragmentManager(), "dlg1");
        } else if (i == R.id.save_button) {
            String userName = "TestName2" + test;
            writeNewUser(userId, userName, userEmail);
            test = test * 2;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        mDatabase.child("users" + name).child(userId).setValue(user);
    }
}
