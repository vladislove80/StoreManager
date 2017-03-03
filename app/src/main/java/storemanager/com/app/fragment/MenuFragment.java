package storemanager.com.app.fragment;

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
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.MenuFragmentAdapter;
import storemanager.com.app.models.CoffeItem;

public class MenuFragment extends Fragment {
    public static final String TAG = MenuFragment.class.getSimpleName();

    private int mPage;
    private RelativeLayout noDataLayout;
    private Button addButtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<CoffeItem> mDataset;

    public static MenuFragment newInstance(int page) {
        /*Bundle args = new Bundle();
        args.putInt(TAG, page);*/
        MenuFragment fragment = new MenuFragment();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        addButtn = (Button) view.findViewById(R.id.add_menu_item_button);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MenuFragmentAdapter(getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        noDataLayout = (RelativeLayout) view.findViewById(R.id.no_menu_data_layout);
        noDataLayout.setVisibility(View.GONE);
        return view;
    }
}
