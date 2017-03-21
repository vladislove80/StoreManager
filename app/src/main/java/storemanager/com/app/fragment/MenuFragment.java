package storemanager.com.app.fragment;

import android.content.Intent;
import android.content.res.Configuration;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AddMenuItemActivity;
import storemanager.com.app.activity.ListOfListActivity;
import storemanager.com.app.adapter.MenuFragmentAdapter;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.Ingredient;

public class MenuFragment extends Fragment {
    public static final String TAG = MenuFragment.class.getSimpleName();
    public final static int REQ_CODE_ADD_ITEM = 2;
    public final static int REQ_CODE_ADD_INGREDIENT = 3;

    private DatabaseReference mDatabase;

    private RelativeLayout noDataLayout;
    private Button addMenuButton;
    private Button addIngredientsButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView menuLabel;

    private List<MenuItem> mDataset;

    public static MenuFragment newInstance(int page) {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset = new ArrayList<>();

        MenuItem testItem = new MenuItem();
        testItem.setName("Тестовое Латте");
        testItem.setPrice(50);
        testItem.setSize(250);

        ArrayList<Ingredient> testIngredients = new ArrayList<>();
        Ingredient testIngredient = new Ingredient();

        testIngredient.setName("Кофе");
        testIngredient.setSize(5);
        testIngredient.setMeasure("мг");
        testIngredients.add(testIngredient);

        testIngredient = new Ingredient();
        testIngredient.setName("Молоко");
        testIngredient.setSize(15);
        testIngredient.setMeasure("мг");
        testIngredients.add(testIngredient);

        testIngredient = new Ingredient();
        testIngredient.setName("Корица");
        testIngredient.setSize(5);
        testIngredient.setMeasure("мг");
        testIngredients.add(testIngredient);

        testItem.setConsist(testIngredients);
        mDataset.add(testItem);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

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
        initRecycler();

        /*noDataLayout = (RelativeLayout) view.findViewById(R.id.no_menu_data_layout);
        noDataLayout.setVisibility(View.GONE);*/

        addIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListOfListActivity.class);
                startActivity(intent);
                /*Intent intent = new Intent(getActivity(), PopActivity.class);
                startActivity(intent);*/
                /*Intent intent = new Intent(getActivity(), AddBaseListsActivity.class);
                intent.putExtra(TAG, allDataLists);
                startActivityForResult(intent, REQ_CODE_ADD_INGREDIENT);*/
                //Toast.makeText(getContext(), "Ингридиенты", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initRecycler() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MenuFragmentAdapter(getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE_ADD_ITEM) {
            MenuItem menuItem = (MenuItem) data.getExtras().getSerializable(AddMenuItemActivity.TAG);
            mDataset.add(menuItem);
            mAdapter.notifyDataSetChanged();
            addMenuItemToDatabase(menuItem);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initRecycler();
    }

    private void addMenuItemToDatabase(MenuItem menuItem) {
        mDatabase = FirebaseDatabase.getInstance().getReference("menus");
        mDatabase.push().child("menu").setValue(menuItem);
    }
}
