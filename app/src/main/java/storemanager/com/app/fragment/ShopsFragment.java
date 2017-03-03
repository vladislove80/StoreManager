package storemanager.com.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AddShopActivity;
import storemanager.com.app.adapter.ShopsAdapter;
import storemanager.com.app.models.Shop;
import storemanager.com.app.utils.Utils;

public class ShopsFragment extends Fragment {
    public static final String TAG = ShopsFragment.class.getSimpleName();
    public final static int REQ_CODE = 1;

    private List<Shop> mDataset;
    private RelativeLayout noDataLayout;

    private Button addButtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mPage;

    public static ShopsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        ShopsFragment fragment = new ShopsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(TAG);
        mDataset = new ArrayList<>();
        Shop shop = new Shop();
        shop.setName("Цветочный рынок");
        shop.setCreationDate(Utils.getCurrentDateWithoutTime());
        shop.setSummaryTooday(false);
        mDataset.add(shop);
        shop = new Shop();
        shop.setName("Таврия-В");
        shop.setCreationDate(Utils.getCurrentDateWithoutTime());
        shop.setSummaryTooday(true);
        mDataset.add(shop);
        shop = new Shop();
        shop.setName("Приморский парк");
        shop.setCreationDate(Utils.getCurrentDateWithoutTime());
        shop.setSummaryTooday(true);
        mDataset.add(shop);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shops_fragment, container, false);

        addButtn = (Button) view.findViewById(R.id.add_button);
        addButtn.setOnClickListener(new View.OnClickListener() {
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
        mAdapter = new ShopsAdapter(getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        noDataLayout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        noDataLayout.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE) {
            //noDataLayout.setVisibility(View.GONE);
            Shop shop = new Shop();
            shop.setName(data.getExtras().get(AddShopActivity.TAG).toString());
            shop.setCreationDate(Utils.getCurrentDateWithoutTime());
            mDataset.add(shop);
            mAdapter.notifyDataSetChanged();
        }
    }
}
