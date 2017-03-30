package storemanager.com.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import storemanager.com.app.R;
import storemanager.com.app.activity.AddShopActivity;
import storemanager.com.app.adapter.ShopsFragmentAdapter;
import storemanager.com.app.models.Shop;
import storemanager.com.app.models.Summary;
import storemanager.com.app.utils.Utils;

public class ShopsFragment extends Fragment {
    public static final String TAG = ShopsFragment.class.getSimpleName();
    public final static int REQ_CODE = 1;

    private List<Shop> shopList;
    private RelativeLayout noDataLayout;

    private Button addButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mDatabase;
    private ProgressBar progressBar;

    private List<Summary> summaryList;

    public static ShopsFragment newInstance(int page) {
        ShopsFragment fragment = new ShopsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getShopListFromDatabase();

    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals("summaries")) {
                        Log.d(TAG, "getShopListFromDatabase -> onDataChange = ");
                        Summary summary = postSnapshot.child("summaries").child("summary").getValue(Summary.class);
                        /*HashMap<String, Object> map = (HashMap<String, Object>) postSnapshot.getValue();
                        for (String key : map.keySet()){
                            HashMap<String, Object> mapSummary = (HashMap<String, Object>) map.get(key);
                            for (String keyS : mapSummary.keySet()){
                                Summary summary = (Summary) mapSummary.get(keyS);
                            }
                        }*/
                        Log.d(TAG, "getShopListFromDatabase -> onDataChange = ");
                    }
                }
            }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shops, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.shop_list_progress_bar);
        if (shopList.size() != 0) {progressBar.setVisibility(View.VISIBLE);}

        addButton = (Button) view.findViewById(R.id.add_shop_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddShopActivity.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.shops_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ShopsFragmentAdapter(getContext(), shopList);
        mRecyclerView.setAdapter(mAdapter);

        noDataLayout = (RelativeLayout) view.findViewById(R.id.no_shop_data_layout);
        noDataLayout.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shopList.size() != 0) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        mDatabase.addValueEventListener(postListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mDatabase.removeEventListener(postListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE) {
            //noDataLayout.setVisibility(View.GONE);
            Shop shop = new Shop();
            shop.setName(data.getExtras().get(AddShopActivity.TAG).toString());
            shop.setCreationDate(Utils.getCurrentDateWithoutTime());
            shopList.add(shop);
            mAdapter.notifyDataSetChanged();
            addShopToDatabase(shop);
        }
    }

    private void addShopToDatabase(Shop shop) {
        mDatabase = FirebaseDatabase.getInstance().getReference("shops");
        mDatabase.push().child("shop").setValue(shop);
    }

    private void getShopListFromDatabase() {
        Query query = mDatabase.child("shops").orderByChild("shop");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "getShopListFromDatabase -> onDataChange = ");
                Shop shop;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    shop = postSnapshot.child("shop").getValue(Shop.class);
                    if (shop != null) {
                        shopList.add(shop);
                    }
                }
                if (shopList.size() != 0) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    noDataLayout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "getShopListFromDatabase -> onCancelled = " + databaseError.getMessage());
            }
        });
    }


}
