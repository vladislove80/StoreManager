package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.CoffeItemToAddInSummary;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.CoffeMenu;
import storemanager.com.app.utils.Utils;

public class SummaryComposerActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase;
    private String userEmail;
    private String userId;

    private Button mAddItemButton;
    private Button mSaveToDatabaseButton;
    private TextView mDateTextView;
    private TextView total;

    private ListView summuryListView;
    private List<CoffeItemToAddInSummary> summaryList;
    private SummaryAdapter adapter;

    private int test = 10;
    private int totalPrice = 0;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(Utils.EXTRA_TAG_MAIL);
        userId = intent.getStringExtra(Utils.EXTRA_TAG_ID);

        String date = Utils.getCurrentDate();
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
            String userName = "TestName2" + test;
            writeNewUser(userId, userName, userEmail);
            test = test * 2;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users" + name).child(userId).setValue(user);
        mDatabase.child("users" + name).child(userId).setValue(summaryList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE_CHILD) {
            CoffeItemToAddInSummary item = (CoffeItemToAddInSummary) data.getExtras().getSerializable(AddItemsActivity.TAG);
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

    private CoffeItemToAddInSummary setPrice(List<CoffeItem> menu, CoffeItemToAddInSummary item) {
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

    private boolean isItemInSummary(List<CoffeItemToAddInSummary> summuryList, CoffeItemToAddInSummary addedItem) {
        for (CoffeItemToAddInSummary summaryItem : summuryList) {
            if (summaryItem.equals(addedItem)) {
                return true;
            }
        }
        return false;
    }

    private void changeItemsAmountInSummary(List<CoffeItemToAddInSummary> summuryList, CoffeItemToAddInSummary item) {
        for (CoffeItemToAddInSummary summaryItem : summuryList) {
            if (summaryItem.equals(item)) {
                int newAmount = summaryItem.getAmount() + item.getAmount();
                summaryItem.setAmount(newAmount);
            }
        }
    }
}
