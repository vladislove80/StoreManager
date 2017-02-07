package storemanager.com.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import storemanager.com.app.R;
import storemanager.com.app.models.CoffeItemInSummary;
import storemanager.com.app.models.Summary;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class SummaryViewerActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView mListView;
    private List<Summary> summaryList;
    private TextView label;
    private SummaryViewerAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_viewer);
        Log.v(Utils.LOG_TAG, "SummaryViewerActivity");
        mListView = (ListView) findViewById(R.id.full_summury);
        label = (TextView) findViewById(R.id.viewer_label);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        label.setText("Summary: ");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        summaryList = new ArrayList<>();
        adapter = new SummaryViewerAdapter(getBaseContext(), summaryList);
        mListView.setAdapter(adapter);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Summary object and use the values to update the UI

                Log.e(Utils.LOG_TAG ,"dataSnapshot.getChildrenCount: " + dataSnapshot.getChildrenCount());
                for (String shop : Utils.cShops) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.hasChild(shop)) {
                            Summary summary = postSnapshot.child(shop).getValue(Summary.class);
                            if (summary != null) {
                                summary.setShop(shop);
                                summaryList.add(summary);
                            }
                        if (summary != null) {
                            String date = summary.getDate();
                            User user = summary.getUser();
                            List<CoffeItemInSummary> coffeItemslist = summary.getItemInSummary();
                            Log.v(Utils.LOG_TAG, "Summury: " + ": " + shop + ", " + user.getName());
                            for (CoffeItemInSummary item : coffeItemslist) {
                                Log.v(Utils.LOG_TAG, item.getItem().getName() + " " + item.getItem().getSize() + " " + item.getAmount() + " " + item.getItemsPrice());
                            }
                        }
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
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
