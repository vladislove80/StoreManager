package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class AddDataActivity extends AppCompatActivity{

    private DatabaseReference mDatabase;
    private String useEmail;
    private String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        useEmail = intent.getStringExtra(Utils.EXTRA_TAG_MAIL);
        userId = intent.getStringExtra(Utils.EXTRA_TAG_ID);

        User user = new User(useEmail, userId);
        mDatabase.child("users").child(userId).setValue(user);

    }
}
