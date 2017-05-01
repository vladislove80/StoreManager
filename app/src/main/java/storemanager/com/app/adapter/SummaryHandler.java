package storemanager.com.app.adapter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.models.Summary;
import storemanager.com.app.utils.Utils;

public class SummaryHandler {
    public static String TAG = SummaryHandler.class.getSimpleName();

    private String teamName;
    private DatabaseReference mDatabase;
    private List<Summary> unhandledSummaryList;

    public SummaryHandler(String teamName) {
        this.teamName = teamName;
        unhandledSummaryList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName).child("unhandled summaries");
    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                unhandledSummaryList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Summary summary = postSnapshot.getValue(Summary.class);
                    unhandledSummaryList.add(summary);
                }
                Log.d(Utils.LOG_TAG, "SummaryHandler -> onDataChange. unhandledSummaryList.size() = " + unhandledSummaryList.size());
            }

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(Utils.LOG_TAG, "SummaryHandler -> DatabaseError", databaseError.toException());
        }
    };

    public void enableSummaryListener(){
        mDatabase.addValueEventListener(postListener);
        Log.d(Utils.LOG_TAG, "SummaryHandler -> enableSummaryListener" );
    }

    public void disableSummaryListener(){
        mDatabase.removeEventListener(postListener);
    }
}
