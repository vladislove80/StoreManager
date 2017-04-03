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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private List<Summary> summaryList;

    private RelativeLayout noDataLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    private DatabaseReference mDatabase;
    private Query query;

    public static ShopsFragment newInstance(int page) {
        ShopsFragment fragment = new ShopsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopList = new ArrayList<>();
        summaryList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getShopListFromDatabase();
        query = mDatabase.child("summaries").orderByChild("summary");
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                summaryList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "getShopListFromDatabase -> onDataChange = ");
                    Summary summary = postSnapshot.child("summary").getValue(Summary.class);
                    if (Utils.isCurrentDate(summary.getDate())) {
                        shopList.contains(summary.getShop());
                        if (shopList.size() != 0) {
                            for (Shop shop : shopList) {
                                if (shop.getName().equals(summary.getShop())) {
                                    shop.setSummaryToday(true);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    summaryList.add(summary);
                    Log.d(TAG, "getShopListFromDatabase -> onDataChange = ");
                }
                Toast.makeText(getContext(), "Всего отчетов: " + summaryList.size(), Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shops, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.shop_list_progress_bar);
        if (shopList.size() != 0) {progressBar.setVisibility(View.VISIBLE);}

        mRecyclerView = (RecyclerView) view.findViewById(R.id.shops_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShopsFragmentAdapter(getContext(), shopList);
        mRecyclerView.setAdapter(mAdapter);

        noDataLayout = (RelativeLayout) view.findViewById(R.id.no_shop_data_layout);
        noDataLayout.setVisibility(View.GONE);

        fab = (FloatingActionButton) view.findViewById(R.id.shop_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddShopActivity.class);
                startActivityForResult(intent, REQ_CODE);
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
    public void onResume() {
        super.onResume();
        if (shopList.size() != 0) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        query.removeEventListener(valueEventListener);
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
