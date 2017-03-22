package storemanager.com.app.fragment;

import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AddMenuItemActivity;
import storemanager.com.app.activity.ListOfListActivity;
import storemanager.com.app.adapter.MenuFragmentAdapter;
import storemanager.com.app.models.MenuItem;

public class MenuFragment extends Fragment {
    public static final String TAG = MenuFragment.class.getSimpleName();
    public final static int REQ_CODE_ADD_ITEM = 2;
    public final static int REQ_CODE_ADD_INGREDIENT = 3;

    private DatabaseReference mDatabase;

    private RelativeLayout noDataLayout;
    private Button addMenuButton;
    private Button editListsButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView menuLabel;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    private List<MenuItem> mDataset;

    public static MenuFragment newInstance(int page) {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset = new ArrayList<>();

        getMenuItemListFromDB();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        editListsButton = (Button) view.findViewById(R.id.edit_lists_button);
        menuLabel = (TextView) view.findViewById(R.id.menu_fragment_label);
        progressBar = (ProgressBar) view.findViewById(R.id.menu_fragment_progressbar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        initRecycler();

        /*noDataLayout = (RelativeLayout) view.findViewById(R.id.no_menu_data_layout);
        noDataLayout.setVisibility(View.GONE);*/

        editListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListOfListActivity.class);
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.add_menu_item_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMenuItemActivity.class);
                startActivityForResult(intent, REQ_CODE_ADD_ITEM);
            }
        });
        return view;
    }

    private void initRecycler() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MenuFragmentAdapter(getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
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
        mDatabase.push().child("menu item").setValue(menuItem);
    }

    private void getMenuItemListFromDB() {
        mDatabase = FirebaseDatabase.getInstance().getReference("menus");
        mDatabase.orderByChild("menu item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MenuItem item;
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        item = postSnapshot.child("menu item").getValue(MenuItem.class);
                        if (item != null) {
                            mDataset.add(item);
                            mAdapter.notifyDataSetChanged();
                        }
                        Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
