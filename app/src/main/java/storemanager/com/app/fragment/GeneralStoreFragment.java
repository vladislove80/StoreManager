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
import java.util.Collections;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AddItemToListActivity;
import storemanager.com.app.activity.AddItemToStoreActivity;
import storemanager.com.app.activity.AdminActivity;
import storemanager.com.app.activity.StatisticsActivity;
import storemanager.com.app.adapter.GeneralStoreManager;
import storemanager.com.app.adapter.StoreRecyclerAdapter;
import storemanager.com.app.models.Event;
import storemanager.com.app.models.StoreItem;
import storemanager.com.app.utils.GeneralStoreManagerNotifier;
import storemanager.com.app.utils.Utils;

public class GeneralStoreFragment extends Fragment implements GeneralStoreManagerNotifier {
    public static final String TAG = GeneralStoreFragment.class.getSimpleName();
    public final static int REQ_CODE_ADD_STORE_ITEM = 7;
    public final static int REQ_CODE_ADD_STORE_ITEM_AMAUNT = 8;

    private DatabaseReference mDatabase;
    private String teamName;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    private FloatingActionButton fab;
    private ProgressBar progressBar;

    private StoreItem selectedItem;
    private List<StoreItem> mDataset;

    private GeneralStoreManager generalStoreManager;

    public static GeneralStoreFragment newInstance() {
        GeneralStoreFragment fragment = new GeneralStoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset = new ArrayList<>();
        AdminActivity activity = (AdminActivity) getActivity();
        if (activity instanceof AdminActivity) {
            generalStoreManager = activity.getGeneralStoreManager();
            generalStoreManager.registerNotifierCallBack(this);
        } else {
            generalStoreManager = new GeneralStoreManager();
        }
        Log.v(Utils.LOG_TAG, "GeneralStoreFragment -> onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        teamName = AdminActivity.getTeamName();
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
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
                Intent intent = new Intent(getActivity(), AddItemToStoreActivity.class);
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
        if(data != null) {
            switch (requestCode) {
                case REQ_CODE_ADD_STORE_ITEM:
                    String newStoreItemName = data.getExtras().get(AddItemToStoreActivity.TAG_NAME).toString();
                    String measure = data.getExtras().get(AddItemToStoreActivity.TAG_MEASURE).toString();
                    StoreItem newStoreItem = new StoreItem(newStoreItemName, measure);
                    if (!mDataset.contains(newStoreItem)) {
                        Event zeroEvent = new Event(Utils.getCurrentDateWithoutTime(), 0);
                        newStoreItem.addLastCommingIn(zeroEvent);
                        mDataset.add(newStoreItem);
                        Collections.sort(mDataset);
                        mAdapter.notifyDataSetChanged();
                        generalStoreManager.addStoreItemsToDatabase(mDataset);
                        //addStoreItemToDatabase(mDataset);
                    } else {
                        Toast.makeText(getContext(), "Уже есть на складе!" , Toast.LENGTH_SHORT).show();
                    }
                    break;
                case REQ_CODE_ADD_STORE_ITEM_AMAUNT:
                    String lastComingInAmount = data.getExtras().get(AddItemToListActivity.TAG).toString();
                    Event event = new Event(Utils.getCurrentDateWithoutTime(), Integer.parseInt(lastComingInAmount));
                    generalStoreManager.addEventToStoreItemData(event);
                    mDataset = generalStoreManager.getStoreItemList();
                    mAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            generalStoreManager.selectStoreItem(pos);
            StoreItem selectedStoreItem = generalStoreManager.getSelectedStoreItem();
            switch (v.getId()) {
                case R.id.change_store_item_amount_button :
                    Toast.makeText(getContext(), generalStoreManager.getSelectedStoreItem().getName() + ", Добавить" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AddItemToListActivity.class);
                    intent.putExtra(AddItemToListActivity.TAG, "amount");
                    startActivityForResult(intent, REQ_CODE_ADD_STORE_ITEM_AMAUNT);
                    break;
                case R.id.store_item_balance_stat :
                    Toast.makeText(getContext(), generalStoreManager.getSelectedStoreItem().getName() + ", Баланс" , Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), StatisticsActivity.class);
                    intent.putExtra(StatisticsActivity.STATISTICS_TYPE, "balance");
                    intent.putExtra(StatisticsActivity.STORE_ITEM_NAME, generalStoreManager.getSelectedStoreItem().getName());
                    intent.putParcelableArrayListExtra(StatisticsActivity.BALANCE_LIST, generalStoreManager.getShopStoreItemBalanceList(selectedStoreItem));
                    startActivity(intent);
                    break;
                case R.id.store_item_consumption_num_stat :
                    Toast.makeText(getContext(), generalStoreManager.getSelectedStoreItem().getName() + ", Расход" , Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), StatisticsActivity.class);
                    intent.putExtra(StatisticsActivity.STATISTICS_TYPE, "consumption");
                    intent.putExtra(StatisticsActivity.STORE_ITEM_NAME, generalStoreManager.getSelectedStoreItem().getName());
                    intent.putParcelableArrayListExtra(StatisticsActivity.CONSUMPTION_LIST, generalStoreManager.getShopStoreItemConsumptionList(selectedStoreItem));
                    startActivity(intent);
                    break;
                case R.id.store_item_incoming_num_stat :
                    Toast.makeText(getContext(), generalStoreManager.getSelectedStoreItem().getName() + ", Приход" , Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), StatisticsActivity.class);
                    intent.putExtra(StatisticsActivity.STATISTICS_TYPE, "incoming");
                    intent.putExtra(StatisticsActivity.STORE_ITEM_NAME, generalStoreManager.getSelectedStoreItem().getName());
                    intent.putParcelableArrayListExtra(StatisticsActivity.INCOMING_LIST, generalStoreManager.getShopStoreItemComingInList(selectedStoreItem));
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void onDownLoaded(List<StoreItem> dataset) {
        mDataset.addAll(dataset);
        mAdapter.notifyDataSetChanged();
    }
}
