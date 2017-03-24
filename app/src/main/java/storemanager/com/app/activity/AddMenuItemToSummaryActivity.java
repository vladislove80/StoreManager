package storemanager.com.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.MenuFragmentAdapter;
import storemanager.com.app.models.MenuItem;

public class AddMenuItemToSummaryActivity  extends AppCompatActivity {
    private static final String TAG = AddMenuItemToSummaryActivity.class.getSimpleName();

    private Button addMenuButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private TextView menuLabel;
    private EditText itemNumberEditText;

    private List<MenuItem> mDataset;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item_new);
        getMenuItemListFromDB();

        addMenuButton = (Button) findViewById(R.id.add_menu_item_add_button);
        addMenuButton.setOnClickListener(addItemListener);
        itemNumberEditText = (EditText) findViewById(R.id.add_menu_item_edit);
        progressBar = (ProgressBar) findViewById(R.id.add_menu_item_progressbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.add_menu_item_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        initRecycler();
    }

    View.OnClickListener addItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void initRecycler() {
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MenuFragmentAdapter(getBaseContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);
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
