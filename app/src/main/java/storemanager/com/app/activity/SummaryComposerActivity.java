package storemanager.com.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.CoffeItemInSummary;
import storemanager.com.app.models.Summary;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.CoffeMenu;
import storemanager.com.app.utils.Utils;

public class SummaryComposerActivity extends AppCompatActivity implements View.OnClickListener{

    //private DatabaseReference mDatabase;
    private String userEmail;
    private String userName;
    private String userId;

    private Button mAddItemButton;
    private Button mSaveToDatabaseButton;
    private TextView mDateTextView;
    private TextView total;

    private ListView summuryListView;
    private List<CoffeItemInSummary> summaryList;
    private SummaryAdapter adapter;

    private int test = 10;
    private int totalPrice = 0;
    private String date;

    public final static int REQ_CODE_CHILD = 1;
    public final static String MENU_TAG = "names";

    private CoffeMenu priceList;
    private List<CoffeItem> menu;
    private ArrayList<String> coffeItemNames;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_composer);
        Log.v(Utils.LOG_TAG, "SummaryComposerActivity");
        //mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(Utils.EXTRA_TAG_MAIL);
        userName = intent.getStringExtra(Utils.EXTRA_TAG_NAME);
        userId = intent.getStringExtra(Utils.EXTRA_TAG_ID);

        date = Utils.getCurrentDate();
        mDateTextView = (TextView) findViewById(R.id.date);
        mDateTextView.setText(date);
        mAddItemButton = (Button) findViewById(R.id.add_button);
        mSaveToDatabaseButton = (Button) findViewById(R.id.send_button);
        summuryListView = (ListView) findViewById(R.id.summury);
        total = (TextView) findViewById(R.id.total);
        total.setText("0");

        summaryList = new ArrayList<>();
        adapter = new SummaryAdapter(getBaseContext(), summaryList);
        summuryListView.setAdapter(adapter);
        summuryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(Utils.LOG_TAG, "ItemName - " + summaryList.get(position).getItem().getName());
                Context wrapper = new ContextThemeWrapper(getBaseContext(), R.style.PopupMenu);
                PopupMenu popupMenu = new PopupMenu(wrapper, view);
                popupMenu.getMenu().add("Delete");
                popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        Log.v(Utils.LOG_TAG, "AddItemsActivity->DELETE");
                        totalPrice = totalPrice - summaryList.get(position).getItem().getPrice() * summaryList.get(position).getAmount();
                        summaryList.remove(position);
                        adapter.notifyDataSetChanged();
                        total.setText(String.valueOf(totalPrice));
                        return true;
                    }
                });
                popupMenu.show();
                return false;
            }
        });

        mAddItemButton.setOnClickListener(this);
        mSaveToDatabaseButton.setOnClickListener(this);

        Log.d(Utils.LOG_TAG, "email = " + userEmail);
        Log.d(Utils.LOG_TAG, "id = " + userId);
        Log.d(Utils.LOG_TAG, "date = " + date);

        priceList = new CoffeMenu();
        menu = priceList.getMenu();
        coffeItemNames = new ArrayList<>();
        for (CoffeItem item : menu) {
            String itemName = item.getName();
            if (!coffeItemNames.contains(itemName)) {
                coffeItemNames.add(itemName);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.add_button) {
            Intent intent = new Intent(SummaryComposerActivity.this, AddItemsActivity.class);
            intent.putStringArrayListExtra(MENU_TAG, coffeItemNames);
            startActivityForResult(intent, REQ_CODE_CHILD);
        } else if (i == R.id.send_button) {
            setSummaryToDatabase(userId, userName, userEmail);
            mAddItemButton.setVisibility(View.GONE);
        }
    }

    private void setSummaryToDatabase(String userId, String name, String email) {
        User user = new User();
        user.setId(userId);
        user.setName(name);
        user.setEmail(email);

        Summary summary = new Summary();
        summary.setUser(user);
        summary.setDate(date);
        summary.setItemInSummary(summaryList);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("test").setValue(user);
        mDatabase.child("test").setValue(date);
        mDatabase.child("test").setValue(summary);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE_CHILD) {
            CoffeItemInSummary item = (CoffeItemInSummary) data.getExtras().getSerializable(AddItemsActivity.TAG);
            item = setPrice(menu, item);
            if (!isItemInSummary(summaryList, item)) {
                summaryList.add(item);
            } else {
                changeItemsAmountInSummary(summaryList, item);
            }
            totalPrice = totalPrice + item.getItemsPrice();
            Log.v(Utils.LOG_TAG, "SummaryComposerActivity-> Item = " + item.getItem().getName() + " Num = " + item.getAmount());
            adapter.notifyDataSetChanged();
            total.setText(String.valueOf(totalPrice));
        }
    }

    private CoffeItemInSummary setPrice(List<CoffeItem> menu, CoffeItemInSummary item) {
        CoffeItem itemWithoutPrice = item.getItem();
        for (CoffeItem menuItem : menu) {
            if (menuItem.getName().equals(itemWithoutPrice.getName())) {
                if (menuItem.isOneSize()) {
                    itemWithoutPrice.setOneSize(true);
                    itemWithoutPrice.setSize(menuItem.getSize());
                    item.getItem().setPrice(menuItem.getPrice());
                    return item;
                } else {
                    if (menuItem.getSize() == itemWithoutPrice.getSize()) {
                        item.getItem().setPrice(menuItem.getPrice());
                        return item;
                    }
                }
            }
        }

        return item;
    }

    private boolean isItemInSummary(List<CoffeItemInSummary> summuryList, CoffeItemInSummary addedItem) {
        for (CoffeItemInSummary summaryItem : summuryList) {
            if (summaryItem.equals(addedItem)) {
                return true;
            }
        }
        return false;
    }

    private void changeItemsAmountInSummary(List<CoffeItemInSummary> summuryList, CoffeItemInSummary item) {
        for (CoffeItemInSummary summaryItem : summuryList) {
            if (summaryItem.equals(item)) {
                int newAmount = summaryItem.getAmount() + item.getAmount();
                summaryItem.setAmount(newAmount);
            }
        }
    }

    private void deleteItemFromSummary(List<CoffeItemInSummary> summuryList, CoffeItemInSummary item) {

    }
}
