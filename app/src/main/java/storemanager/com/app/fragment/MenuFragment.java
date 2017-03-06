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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AddBaseIngredientActivity;
import storemanager.com.app.activity.AddMenuItemActivity;
import storemanager.com.app.adapter.MenuFragmentAdapter;
import storemanager.com.app.models.CoffeItem;

public class MenuFragment extends Fragment {
    public static final String TAG = MenuFragment.class.getSimpleName();
    public final static int REQ_CODE_ADD_ITEM = 2;
    public final static int REQ_CODE_ADD_INGREDIENT = 3;

    private int mPage;
    private RelativeLayout noDataLayout;
    private Button addMenuButton;
    private Button addIngredientsButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView menuLabel;

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
        mDataset = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        addMenuButton = (Button) view.findViewById(R.id.add_menu_item_button);
        addIngredientsButton = (Button) view.findViewById(R.id.add_ingredients_button);
        menuLabel = (TextView) view.findViewById(R.id.menu_fragment_label);


        addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMenuItemActivity.class);
                startActivityForResult(intent, REQ_CODE_ADD_ITEM);
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MenuFragmentAdapter(getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        /*noDataLayout = (RelativeLayout) view.findViewById(R.id.no_menu_data_layout);
        noDataLayout.setVisibility(View.GONE);*/

        addIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBaseIngredientActivity.class);
                startActivityForResult(intent, REQ_CODE_ADD_INGREDIENT);
                //Toast.makeText(getContext(), "Ингридиенты", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE_ADD_ITEM) {
            /*mDataset.add();
            mAdapter.notifyDataSetChanged();*/
        }
    }
}
