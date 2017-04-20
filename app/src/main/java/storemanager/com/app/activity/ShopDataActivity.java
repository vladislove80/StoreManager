package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.StoreRecyclerAdapter;
import storemanager.com.app.models.Event;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.MenuItemsInSummary;
import storemanager.com.app.models.StoreItem;
import storemanager.com.app.models.Summary;

import static android.view.View.inflate;

public class ShopDataActivity extends AppCompatActivity {
    public final static String TAG = ShopDataActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private Query query;
    private Summary shopSummary;
    private List<StoreItem> mDataset;
    private String shopName;
    private String teamName;

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private RelativeLayout noDataLayout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TableLayout table;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_summaries);
        mDataset = new ArrayList<>();
        setTestStoreData();

        Intent intent = getIntent();
        shopName = intent.getStringExtra(TAG);
        teamName = AdminActivity.getTeamName();
        TextView textView = (TextView) findViewById(R.id.shop_name);
        textView.setText(shopName);

        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        query = mDatabase.child("summaries").orderByChild("shop").equalTo(shopName).limitToLast(1);
        query.addListenerForSingleValueEvent(valueEventListener);

        progressBar = (ProgressBar) findViewById(R.id.shop_summaries_progress_bar);
        noDataLayout = (RelativeLayout) findViewById(R.id.no_shop_data_layout);

        table = (TableLayout) findViewById(R.id.summary_table);
        mRecyclerView = (RecyclerView) findViewById(R.id.shop_store_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StoreRecyclerAdapter(mDataset, itemListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "ShopDataActivity -> onDataChange = ");
                    shopSummary = postSnapshot.getValue(Summary.class);
                    getSummaryTable();
                    Log.d(TAG, "ShopDataActivity -> onDataChange = ");
                }
                progressBar.setVisibility(View.GONE);
                /*Toast.makeText(getBaseContext(), "Всего отчетов: " + summaryList.size(), Toast.LENGTH_SHORT).show();*/
            } else {
                progressBar.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    private void getSummaryTable() {
        List<MenuItemsInSummary> itemInSummary = shopSummary.getItemInSummary();
        int i = 1;
        for (MenuItemsInSummary item : itemInSummary) {
            final TableRow tableRow = (TableRow) inflate(getBaseContext(), R.layout.layout_summary_viewer_row, null);
            TextView tv;
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            MenuItem menuItem = item.getItem();
            tv = (TextView) tableRow.findViewById(R.id.viewer_number_row);
            tv.setText(String.valueOf(i));
            tv.setLayoutParams(layoutParams);
            tv = (TextView) tableRow.findViewById(R.id.viewer_item_name);
            tv.setText(menuItem.getName());
            tv.setLayoutParams(layoutParams);
            tv = (TextView) tableRow.findViewById(R.id.viewer_item_amount);
            tv.setText(String.valueOf(item.getAmount()));
            tv.setLayoutParams(layoutParams);
            tv = (TextView) tableRow.findViewById(R.id.viewer_item_size);
            tv.setText(String.valueOf(menuItem.getSize()));
            tv.setLayoutParams(layoutParams);
            tv = (TextView) tableRow.findViewById(R.id.viewer_item_price);
            tv.setText(String.valueOf(item.getItemsPrice()));
            tv.setLayoutParams(layoutParams);
            table.addView(tableRow);
            i++;
        }
    }

    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            StoreItem selectedItem = mDataset.get(pos);
            switch (v.getId()) {
                case R.id.change_store_item_amount_button :
                    Toast.makeText(getBaseContext(), selectedItem.getName() + ", Add" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.store_item_balance_stat :
                    Toast.makeText(getBaseContext(), selectedItem.getName() + ", Balance" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.store_item_consumption_num_stat :
                    Toast.makeText(getBaseContext(), selectedItem.getName() + ",Consumption" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.store_item_incoming_num_stat :
                    Toast.makeText(getBaseContext(), selectedItem.getName() + ", Incoming" , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void setTestStoreData() {
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
}
