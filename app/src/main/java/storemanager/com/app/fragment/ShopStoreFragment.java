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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AddItemToListActivity;
import storemanager.com.app.activity.AddItemsToShopStoreActivity;
import storemanager.com.app.activity.AdminActivity;
import storemanager.com.app.activity.ShopDataActivity;
import storemanager.com.app.adapter.ShopStoreManager;
import storemanager.com.app.adapter.StoreRecyclerAdapter;
import storemanager.com.app.models.Event;
import storemanager.com.app.models.StoreItem;
import storemanager.com.app.utils.GetShopStoreItemListFromDataBase;
import storemanager.com.app.utils.Utils;

public class ShopStoreFragment extends Fragment implements GetShopStoreItemListFromDataBase{
    public static String TAG = ShopStoreFragment.class.getSimpleName();
    public final static int REQ_CODE_ADD_STORE_ITEM = 9;
    public final static int REQ_CODE_ADD_STORE_ITEM_AMAUNT = 10;

    private DatabaseReference mDatabase;
    private Query query;
    private List<StoreItem> mDataset;
    private List<StoreItem> mDatasetToAdd;

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private RelativeLayout noDataLayout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton fab;
    private String teamName;
    private String shopName;

    private ShopStoreManager shopStoreManager;

    public static ShopStoreFragment newInstance(){
        ShopStoreFragment fragment = new ShopStoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ShopStoreFragment -> onCreate = ");
        mDataset = new ArrayList<>();
        mDatasetToAdd = new ArrayList<>();
        teamName = AdminActivity.getTeamName();
        shopName = ShopDataActivity.getShopName();
        ShopDataActivity activity = (ShopDataActivity) getActivity();
        if(activity instanceof ShopDataActivity){
            ShopDataActivity shopDataActivity = activity;
            shopStoreManager = shopDataActivity.getShopStoreManager();
            shopStoreManager.registerDownloadedStoreItemListCallBack(this);
        } else {
            shopStoreManager = new ShopStoreManager(shopName);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_store, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.shop_store_progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.shop_store_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StoreRecyclerAdapter(mDataset, itemListener);
        mRecyclerView.setAdapter(mAdapter);

        fab = (FloatingActionButton) view.findViewById(R.id.shop_store_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddItemsToShopStoreActivity.class);
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

    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            shopStoreManager.selectStoreItem(pos);
            switch (v.getId()) {
                case R.id.change_store_item_amount_button :
                    Toast.makeText(getContext(), shopStoreManager.getSelectedItem().getName() + ", Добавить" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AddItemToListActivity.class);
                    intent.putExtra(AddItemToListActivity.TAG, "amount");
                    startActivityForResult(intent, REQ_CODE_ADD_STORE_ITEM_AMAUNT);
                    break;
                case R.id.store_item_balance_stat :
                    Toast.makeText(getContext(), shopStoreManager.getSelectedItem().getName() + ", Balance" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.store_item_consumption_num_stat :
                    Toast.makeText(getContext(), shopStoreManager.getSelectedItem().getName() + ",Consumption" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.store_item_incoming_num_stat :
                    Toast.makeText(getContext(), shopStoreManager.getSelectedItem().getName() + ", Incoming" , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            switch (requestCode) {
                case REQ_CODE_ADD_STORE_ITEM:
                    mDatasetToAdd = data.getParcelableArrayListExtra(AddItemsToShopStoreActivity.TAG);
                    shopStoreManager.addNewItemsFromGeneralStoreToShopStore(mDatasetToAdd);
                    mDataset.clear();
                    mDataset.addAll(shopStoreManager.getShopStoreItemList());
                    Toast.makeText(getContext(), "Size " + mDataset.size(), Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                    break;
                case REQ_CODE_ADD_STORE_ITEM_AMAUNT:
                    String lastComingInAmount = data.getExtras().get(AddItemToListActivity.TAG).toString();
                    Event event = new Event(Utils.getCurrentDateWithoutTime(), Integer.parseInt(lastComingInAmount));
                    shopStoreManager.addEventToStoreItemData(event);
                    mDataset = shopStoreManager.getShopStoreItemList();
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onDownLoaded(List<StoreItem> dataset) {
        mDataset.addAll(dataset);
        mAdapter.notifyDataSetChanged();
    }
}
