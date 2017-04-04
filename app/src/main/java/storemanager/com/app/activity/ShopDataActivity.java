package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.Summary;

public class ShopDataActivity extends AppCompatActivity {
    public final static String TAG = ShopDataActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private ListView mListView;
    private List<Summary> summaryList;
    private TextView label;
    private SummaryViewerAdapter adapter;
    private ProgressBar progressBar;
    private Query query;
    private RelativeLayout noDataLayout;
    private String shopName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_summaries);

        Intent intent = getIntent();
        shopName = intent.getStringExtra(TAG);
        TextView textView = (TextView) findViewById(R.id.shop_name);
        textView.setText(shopName);

        mDatabase = FirebaseDatabase.getInstance().getReference("summaries");
        query = mDatabase.orderByChild("shop").equalTo(shopName);
        query.addListenerForSingleValueEvent(valueEventListener);

        progressBar = (ProgressBar) findViewById(R.id.shop_summaries_progress_bar);
        noDataLayout = (RelativeLayout) findViewById(R.id.no_shop_data_layout);
        mListView = (ListView) findViewById(R.id.shop_summaries_listview);

        summaryList = new ArrayList<>();
        adapter = new SummaryViewerAdapter(getBaseContext(), summaryList);
        mListView.setAdapter(adapter);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "ShopDataActivity -> onDataChange = ");
                    Summary summary = postSnapshot.getValue(Summary.class);
                    summaryList.add(summary);
                    Log.d(TAG, "ShopDataActivity -> onDataChange = ");
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                /*Toast.makeText(getBaseContext(), "Всего отчетов: " + summaryList.size(), Toast.LENGTH_SHORT).show();*/
            } else {
                progressBar.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

}
