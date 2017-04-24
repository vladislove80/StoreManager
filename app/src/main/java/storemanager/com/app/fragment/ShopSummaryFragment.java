package storemanager.com.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AdminActivity;
import storemanager.com.app.adapter.SummaryViewerAdapter;
import storemanager.com.app.models.StoreItem;
import storemanager.com.app.models.Summary;
import storemanager.com.app.utils.Utils;

public class ShopSummaryFragment extends Fragment {
    public static String TAG = ShopSummaryFragment.class.getSimpleName();


    private DatabaseReference mDatabase;
    private Query query;
    private List<StoreItem> mDataset;

    private ProgressBar progressBar;
    private RelativeLayout noDataLayout;

    private ListView mListView;
    private List<Summary> summaryList;
    private SummaryViewerAdapter adapter;
    private String teamName;

    public static ShopSummaryFragment newInstance(){
        ShopSummaryFragment fragment = new ShopSummaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summaryList = new ArrayList<>();
        teamName = AdminActivity.getTeamName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_summary, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.shop_summary_progress_bar);

        mListView = (ListView) view.findViewById(R.id.shop_summury_list);
        progressBar = (ProgressBar) view.findViewById(R.id.shop_summary_progress_bar);

        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        adapter = new SummaryViewerAdapter(getContext(), summaryList);
        mListView.setAdapter(adapter);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "getShopListFromDatabase -> onDataChange = ");
                        Summary summary = postSnapshot.getValue(Summary.class);
                        summaryList.add(summary);
                    }
                    Toast.makeText(getContext(), "Всего отчетов: " + summaryList.size(), Toast.LENGTH_SHORT).show();
                }
                Collections.reverse(summaryList);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(Utils.LOG_TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load post.", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.child("summaries").addListenerForSingleValueEvent(postListener);

        return view;
    }
}
