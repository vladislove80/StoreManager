package storemanager.com.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AddItemToListActivity;
import storemanager.com.app.activity.AddStoreItemActivity;
import storemanager.com.app.activity.AdminActivity;
import storemanager.com.app.adapter.StoreRecyclerAdapter;
import storemanager.com.app.models.StoreItem;
import storemanager.com.app.utils.Utils;

public class GeneralStoreFragment extends Fragment {
    public static final String TAG = GeneralStoreFragment.class.getSimpleName();
    public final static int REQ_CODE_ADD_STORE_ITEM = 7;
    public final static int REQ_CODE_ADD_STORE_ITEM_AMAUNT = 8;

    private DatabaseReference mDatabase;
    private List<StoreItem> mDataset;
    private String teamName;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private ProgressBar progressBar;

    public static GeneralStoreFragment newInstance() {
        GeneralStoreFragment fragment = new GeneralStoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset = new ArrayList<>();
        Log.v(Utils.LOG_TAG, "GeneralStoreFragment");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        teamName = AdminActivity.getTeamName();
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        if (mDataset.size() == 0) {
            getStoreItemsFromDatabase();
        }
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.general_store_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StoreRecyclerAdapter(mDataset, itemListener);
        mRecyclerView.setAdapter(mAdapter);

        fab = (FloatingActionButton) view.findViewById(R.id.general_store_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddStoreItemActivity.class);
                startActivityForResult(intent, REQ_CODE_ADD_STORE_ITEM);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE_ADD_STORE_ITEM) {
            String newStoreItemName = data.getExtras().get(AddStoreItemActivity.TAG_NAME).toString();
            String measure = data.getExtras().get(AddStoreItemActivity.TAG_MEASURE).toString();
            StoreItem newStoreItem = new StoreItem(newStoreItemName, measure);
            mDataset.add(newStoreItem);
            mAdapter.notifyDataSetChanged();
            addStoreItemToDatabase(mDataset);
        }
    }

    private void addStoreItemToDatabase(List<StoreItem> dataset) {
        mDatabase.child("general store").setValue(dataset);
    }

    private void getStoreItemsFromDatabase(){
        Query query = mDatabase.child("general store");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        mDataset.add(postSnapshot.getValue(StoreItem.class));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "addStoreItemToDatabase -> onCancelled = " + databaseError.getMessage());
            }
        });
    }

    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            StoreItem selectedItem = mDataset.get(pos);
            switch (v.getId()) {
                case R.id.change_store_item_amount_button :
                    Toast.makeText(getContext(), selectedItem.getName() + ", Добавить" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AddItemToListActivity.class);
                    intent.putExtra(AddItemToListActivity.TAG, "amount");
                    startActivityForResult(intent, REQ_CODE_ADD_STORE_ITEM_AMAUNT);
                    break;
                case R.id.store_item_balance_stat :
                    Toast.makeText(getContext(), selectedItem.getName() + ", Баланс" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.store_item_consumption_num_stat :
                    Toast.makeText(getContext(), selectedItem.getName() + ", Расход" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.store_item_incoming_num_stat :
                    Toast.makeText(getContext(), selectedItem.getName() + ", Приход" , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
