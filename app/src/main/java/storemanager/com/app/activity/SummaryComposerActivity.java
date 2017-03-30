package storemanager.com.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import storemanager.com.app.R;
import storemanager.com.app.adapter.SummaryAdapter;
import storemanager.com.app.models.BaseItem;
import storemanager.com.app.models.Ingredient;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.MenuItemsInSummary;
import storemanager.com.app.models.Summary;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class SummaryComposerActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = SummaryComposerActivity.class.getSimpleName();

    private String userEmail;
    private String userName;
    private String userId;
    private String shop;

    private Button mSaveToDatabaseButton;
    private TextView mDateTextView;
    private TextView mShopTextView;
    private TextView total;

    private ListView summuryListView;
    private List<MenuItemsInSummary> summaryList;
    private SummaryAdapter adapter;

    private int totalPrice = 0;
    private String date;

    public final static int REQ_CODE_CHILD = 1;
    public final static String MENU_NAMES_TAG = "item names";
    public final static String MENU_SIZES_TAG = "item sizes";

    private ArrayList<MenuItem> menu;
    private ArrayList<String> coffeItemNames;
    private ArrayList<String> coffeItemSizes;
    private DatabaseReference mDatabase;
    private List<BaseItem> allDataListLists;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_composer);
        Log.v(Utils.LOG_TAG, "SummaryComposerActivity");

        summaryList = new ArrayList<>();
        allDataListLists = new ArrayList<>();
        menu = new ArrayList<>();
        getDataListsFromDB();
        getMenuFromBD();

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(Utils.EXTRA_TAG_MAIL);
        userName = intent.getStringExtra(Utils.EXTRA_TAG_NAME);
        userId = intent.getStringExtra(Utils.EXTRA_TAG_ID);
        shop = intent.getStringExtra(Utils.EXTRA_TAG_SHOP);

        date = Utils.getCurrentDate();
        mDateTextView = (TextView) findViewById(R.id.date);
        mShopTextView = (TextView) findViewById(R.id.shop);
        mDateTextView.setText(date);
        mShopTextView.setText("\"" + shop + "\"");
        mSaveToDatabaseButton = (Button) findViewById(R.id.send_button);
        summuryListView = (ListView) findViewById(R.id.summury);
        total = (TextView) findViewById(R.id.total);
        total.setText("0");

        adapter = new SummaryAdapter(getBaseContext(), summaryList);
        summuryListView.setAdapter(adapter);
        summuryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(Utils.LOG_TAG, "ItemName - " + summaryList.get(position).getItem().getName());
                Context wrapper = new ContextThemeWrapper(getBaseContext(), R.style.PopupMenu);
                PopupMenu popupMenu = new PopupMenu(wrapper, view);
                popupMenu.getMenu().add("Delete");
                popupMenu.getMenuInflater().inflate(R.menu.summary_item_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        Log.v(Utils.LOG_TAG, "SummaryComposerActivity->DELETE");
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

        summuryListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int btn_initPosY = fab.getScrollY();
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    fab.animate().cancel();
                    fab.animate().translationYBy(150);
                } else {
                    fab.animate().cancel();
                    fab.animate().translationY(btn_initPosY);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mSaveToDatabaseButton.setOnClickListener(this);

        Log.d(Utils.LOG_TAG, "email = " + userEmail);
        Log.d(Utils.LOG_TAG, "id = " + userId);
        Log.d(Utils.LOG_TAG, "date = " + date);

        coffeItemNames = new ArrayList<>();
        coffeItemSizes = new ArrayList<>();

        fab = (FloatingActionButton) findViewById(R.id.summary_composer_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.size() != 0) {
                    Intent intent = new Intent(SummaryComposerActivity.this, AddItemsToSummaryActivity.class);
                    intent.putParcelableArrayListExtra(TAG, menu);
                    startActivityForResult(intent, REQ_CODE_CHILD);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.send_button) {
            if (summaryList.size() != 0) {
                fab.setVisibility(View.GONE);
                sendSummaryToDatabase(userId, userName, userEmail);
                mSaveToDatabaseButton.setVisibility(View.GONE);
            } else {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setTitle("Отчет пуст!");
                alt_bld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        }
    }

    private void sendSummaryToDatabase(String userId, String name, String email) {
        User user = new User();
        user.setId(userId);
        user.setName(name);
        user.setEmail(email);

        Summary summary = new Summary();
        summary.setUser(user);
        summary.setDate(date);
        summary.setShop(shop);
        summary.setItemInSummary(summaryList);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("summaries");
/*        mDatabase.child(shop).setValue(user);
        mDatabase.child(shop).setValue(date);*/
        mDatabase.push().child(shop).setValue(summary);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE_CHILD) {
            MenuItemsInSummary item = (MenuItemsInSummary) data.getExtras().getSerializable(AddItemsToSummaryActivity.TAG);
            if (menu.size() != 0 && item != null) {
                item = setDataFromMenuToAddedItem(menu, item);
            }
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

    private MenuItemsInSummary setDataFromMenuToAddedItem(List<MenuItem> menu, MenuItemsInSummary item) {
        MenuItem itemWithoutPrice = item.getItem();
        for (MenuItem menuItem : menu) {
            if (menuItem.getName().equals(itemWithoutPrice.getName())) {
                if (menuItem.isOneSize()) {
                    itemWithoutPrice.setOneSize(true);
                    itemWithoutPrice.setSize(menuItem.getSize());
                    itemWithoutPrice.setConsist((ArrayList<Ingredient>) menuItem.getConsist());
                    itemWithoutPrice.setPrice(menuItem.getPrice());
                    return item;
                } else {
                    if (menuItem.getSize() == itemWithoutPrice.getSize()) {
                        itemWithoutPrice.setPrice(menuItem.getPrice());
                        itemWithoutPrice.setConsist((ArrayList<Ingredient>) menuItem.getConsist());
                        return item;
                    }
                }
            }
        }
        return item;
    }

    private boolean isItemInSummary(List<MenuItemsInSummary> summuryList, MenuItemsInSummary addedItem) {
        for (MenuItemsInSummary summaryItem : summuryList) {
            if (summaryItem.equals(addedItem)) {
                return true;
            }
        }
        return false;
    }

    private void changeItemsAmountInSummary(List<MenuItemsInSummary> summuryList, MenuItemsInSummary item) {
        for (MenuItemsInSummary summaryItem : summuryList) {
            if (summaryItem.equals(item)) {
                int newAmount = summaryItem.getAmount() + item.getAmount();
                summaryItem.setAmount(newAmount);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mSaveToDatabaseButton.getVisibility() == View.VISIBLE) {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
            alt_bld.setTitle("Отчет не пуст. Выйти ?");
            alt_bld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alt_bld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = alt_bld.create();
            alert.show();
        } else {
            super.onBackPressed();
        }
    }

    private void getDataListsFromDB() {
        mDatabase = FirebaseDatabase.getInstance().getReference("lists");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        Log.d(TAG, "getDataListFromDatabse -> dataSnapshot.hasChildren()");
                        Map map = (Map) postSnapshot.getValue();
                        BaseItem item = new BaseItem();
                        item.setId((String) map.get("id"));
                        item.setItemData((List<String>) map.get("itemData"));

                        if (item.getId().equals("item names")){
                            coffeItemNames.addAll(item.getItemData());
                        }
                        if (item.getId().equals("item sizes")){
                            coffeItemSizes.addAll(item.getItemData());
                        }
                        // to delete ?
                        allDataListLists.add(item);
                    }
                    Log.d(TAG, "getDataListFromDatabse -> allDataListLists");
                } else {
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMenuFromBD() {
        mDatabase = FirebaseDatabase.getInstance().getReference("menus");
        mDatabase.orderByChild("menu item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MenuItem item;
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        item = postSnapshot.child("menu item").getValue(MenuItem.class);
                        if (item != null) {
                            menu.add(item);
                        }
                        Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                    }
                } else {
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
