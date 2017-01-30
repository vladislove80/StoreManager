package storemanager.com.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.CoffeItemInSummary;
import storemanager.com.app.models.Summary;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class SummaryViewerActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_viewer);
        Log.v(Utils.LOG_TAG, "SummaryViewerActivity");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Summary object and use the values to update the UI

                Summary summary = dataSnapshot.child("test").getValue(Summary.class);

                User user = summary.getUser();
                List<CoffeItemInSummary> coffeItemslist = summary.getItemInSummary();
                Log.v(Utils.LOG_TAG, "Summury: " + user.getName());
                for (CoffeItemInSummary item : coffeItemslist) {

                    Log.v(Utils.LOG_TAG, item.getItem().getName() + " " + item.getItem().getSize() + " " + item.getAmount() + " " + item.getItemsPrice());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(Utils.LOG_TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(getBaseContext(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mDatabase.addValueEventListener(postListener);

    }
}
