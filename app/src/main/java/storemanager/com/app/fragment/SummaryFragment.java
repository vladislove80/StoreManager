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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.SummaryViewerAdapter;
import storemanager.com.app.models.Summary;
import storemanager.com.app.utils.Utils;

public class SummaryFragment extends Fragment {
    public static final String TAG = SummaryFragment.class.getSimpleName();

    private int mPage;

    private DatabaseReference mDatabase;
    private ListView mListView;
    private List<Summary> summaryList;
    private List<Summary> summaryListWithFilter;
    private TextView label;
    private SummaryViewerAdapter adapter;
    private ProgressBar progressBar;

    public static SummaryFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        SummaryFragment fragment = new SummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(TAG);

        Log.v(Utils.LOG_TAG, "SummaryFragment");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        mListView = (ListView) view.findViewById(R.id.full_summury);
        label = (TextView) view.findViewById(R.id.viewer_label);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        summaryList = new ArrayList<>();
        summaryListWithFilter = new ArrayList<>();
        adapter = new SummaryViewerAdapter(getContext(), summaryList);
        mListView.setAdapter(adapter);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(Utils.LOG_TAG ,"dataSnapshot.getChildrenCount: " + dataSnapshot.getChildrenCount());
                for (String shop : Utils.cShops) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.hasChild(shop)) {
                            Summary summary = postSnapshot.child(shop).getValue(Summary.class);
                            if (summary != null) {
                                summary.setShop(shop);
                                summaryList.add(summary);
                            }
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(Utils.LOG_TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load post.", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
        return view;
    }
}
