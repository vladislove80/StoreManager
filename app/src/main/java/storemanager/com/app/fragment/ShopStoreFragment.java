package storemanager.com.app.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AdminActivity;
import storemanager.com.app.adapter.FragmentShopStoreAdapter;
import storemanager.com.app.models.Event;
import storemanager.com.app.models.StoreItem;

public class ShopStoreFragment extends Fragment{
    public static String TAG = ShopStoreFragment.class.getSimpleName();

    private List<StoreItem> mDataset;
    private String teamName;

    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset = new ArrayList<>();
        teamName = AdminActivity.getTeamName();

        List<Event> listEvent = new ArrayList<>();
        Event event = new Event("11.04.2017", 500);
        listEvent.add(event);
        event = new Event("11.04.2017", -25);
        listEvent.add(event);
        event = new Event("12.04.2017", -37);
        listEvent.add(event);
        event = new Event("13.04.2017", -45);
        listEvent.add(event);
        event = new Event("14.04.2017", 100);
        listEvent.add(event);
        event = new Event("14.04.2017", -74);
        listEvent.add(event);
        StoreItem item = new StoreItem("Кофе", "кг", listEvent);
        mDataset.add(item);

        listEvent = new ArrayList<>();
        event = new Event("11.04.2017", 200);
        listEvent.add(event);
        event = new Event("11.04.2017", -7);
        listEvent.add(event);
        event = new Event("12.04.2017", -9);
        listEvent.add(event);
        event = new Event("13.04.2017", -8);
        listEvent.add(event);
        event = new Event("14.04.2017", 25);
        listEvent.add(event);
        event = new Event("14.04.2017", -11);
        listEvent.add(event);
        item = new StoreItem("Молоко", "л", listEvent);
        mDataset.add(item);

        listEvent = new ArrayList<>();
        event = new Event("11.04.2017", 5);
        listEvent.add(event);
        event = new Event("11.04.2017", -1);
        listEvent.add(event);
        event = new Event("12.04.2017", -1);
        listEvent.add(event);
        event = new Event("13.04.2017", -1);
        listEvent.add(event);
        event = new Event("14.04.2017", -2);
        listEvent.add(event);
        event = new Event("14.04.2017", 3);
        listEvent.add(event);
        event = new Event("14.04.2017", -1);
        listEvent.add(event);
        item = new StoreItem("Какао", "кг", listEvent);
        mDataset.add(item);

        listEvent = new ArrayList<>();
        event = new Event("11.04.2017", 120);
        listEvent.add(event);
        event = new Event("11.04.2017", -3);
        listEvent.add(event);
        event = new Event("12.04.2017", -4);
        listEvent.add(event);
        event = new Event("13.04.2017", 25);
        listEvent.add(event);
        event = new Event("14.04.2017", -5);
        listEvent.add(event);
        event = new Event("14.04.2017", -5);
        listEvent.add(event);
        item = new StoreItem("Сахар", "кг", listEvent);

        mDataset.add(item);
        listEvent = new ArrayList<>();
        event = new Event("11.04.2017", 10);
        listEvent.add(event);
        event = new Event("11.04.2017", -1);
        listEvent.add(event);
        event = new Event("12.04.2017", -1);
        listEvent.add(event);
        event = new Event("13.04.2017", -1);
        listEvent.add(event);
        event = new Event("14.04.2017", -1);
        listEvent.add(event);
        event = new Event("14.04.2017", -1);
        listEvent.add(event);
        item = new StoreItem("Сироп", "л", listEvent);
        mDataset.add(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_store, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.shop_store_fragment_progressbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.shop_store_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FragmentShopStoreAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
