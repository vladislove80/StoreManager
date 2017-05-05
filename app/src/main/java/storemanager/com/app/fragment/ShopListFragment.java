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
import android.widget.RelativeLayout;
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
import storemanager.com.app.activity.AdminActivity;
import storemanager.com.app.adapter.ShopsFragmentAdapter;
import storemanager.com.app.models.Shop;
import storemanager.com.app.models.Summary;
import storemanager.com.app.utils.Utils;

public class ShopListFragment extends Fragment {
    public static final String TAG = ShopListFragment.class.getSimpleName();
    public final static int REQ_CODE = 1;

    private List<Shop> shopList;
    private List<Summary> summaryList;
    private String teamName;

    private RelativeLayout noDataLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    private DatabaseReference mDatabase;
    private Query query;

    public static ShopListFragment newInstance(int page) {
        ShopListFragment fragment = new ShopListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopList = new ArrayList<>();
        summaryList = new ArrayList<>();
        teamName = AdminActivity.getTeamName();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getShopListFromDatabase();
        query = mDatabase.child(teamName).child("summaries");
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                summaryList.clear();
                //Log.d(Utils.LOG_TAG, "getShopListFromDatabase -> onDataChange ");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Summary summary = postSnapshot.getValue(Summary.class);
                    if (summary != null && Utils.isCurrentDate(summary.getDate())) {
                        shopList.contains(summary.getShop());
                        if (shopList.size() != 0) {
                            for (Shop shop : shopList) {
                                if (shop.getName().equals(summary.getShop())) {
                                    shop.setSummaryToday(true);
                                    mAdapter.notifyDataSetChanged();
                                    //TODO
                                    //break;
                                }
                            }
                        }
                        summaryList.add(summary);
                    }
                    //Log.d(Utils.LOG_TAG, "getShopListFromDatabase -> onDataChange = ");
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
                Intent intent = new Intent(getActivity(), AddItemToListActivity.class);
                intent.putExtra(AddItemToListActivity.TAG, "text");
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
            noDataLayout.setVisibility(View.VISIBLE);
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
            shop.setName(data.getExtras().get(AddItemToListActivity.TAG).toString());
            shop.setCreationDate(Utils.getCurrentDateWithoutTime());
            shopList.add(shop);
            if (shopList.size() == 1) {
                noDataLayout.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();
            addShopToDatabase(shop);
        }
    }

    private void addShopToDatabase(Shop shop) {
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("shops").push().setValue(shop);
    }

    private void getShopListFromDatabase() {
        Query query = mDatabase.child(teamName).child("shops");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d(Utils.LOG_TAG, "getShopListFromDatabase -> onDataChange = ");
                if (dataSnapshot.hasChildren()) {
                    Shop shop;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        shop = postSnapshot.getValue(Shop.class);
                        //Log.d(Utils.LOG_TAG, "getShopListFromDatabase -> shop.getName() = " + shop.getName());
                        shopList.add(shop);
                    }
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG, "getShopListFromDatabase -> shopList.size() = " + shopList.size());
                }
                if (shopList.size() != 0) {
                    noDataLayout.setVisibility(View.GONE);
                } else {
                    noDataLayout.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(Utils.LOG_TAG, "getShopListFromDatabase -> onCancelled = " + databaseError.getMessage());
            }
        });
    }


}
