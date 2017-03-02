package storemanager.com.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.ShopsAdapter;

public class ShopsFragment extends Fragment {
    public static final String TAG = ShopsFragment.class.getSimpleName();

    private List<String> mDataset;

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
        mDataset.add("Цветочный рынок");
        mDataset.add("Таврия-В");
        mDataset.add("Приморский парк");
        mDataset.add("Цветочный рынок");
        mDataset.add("Таврия-В");
        mDataset.add("Приморский парк");
        mDataset.add("Цветочный рынок");
        mDataset.add("Таврия-В");
        mDataset.add("Приморский парк");
        mDataset.add("Цветочный рынок");
        mDataset.add("Таврия-В");
        mDataset.add("Приморский парк");
        mDataset.add("Цветочный рынок");
        mDataset.add("Таврия-В");
        mDataset.add("Приморский парк");
        mDataset.add("Цветочный рынок");
        mDataset.add("Таврия-В");
        mDataset.add("Приморский парк");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shops_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.shops_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ShopsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
        return view;
    }
}
