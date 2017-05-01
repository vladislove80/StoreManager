package storemanager.com.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.SummaryAdapter;
import storemanager.com.app.models.BaseItem;
import storemanager.com.app.models.Ingredient;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.MenuItemsInSummary;
import storemanager.com.app.models.Shop;
import storemanager.com.app.models.Summary;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class SummaryComposerActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    public static final String TAG = SummaryComposerActivity.class.getSimpleName();

    private String userEmail;
    private String userName;
    private String userId;
    private String shop;
    private String teamName;

    private Button mSaveToDatabaseButton;
    private TextView mDateTextView;
    private TextView mShopTextView;
    private TextView total;

    private ListView summuryListView;
    private List<MenuItemsInSummary> summaryList;
    private List<BaseItem> allDataListLists;
    private SummaryAdapter adapter;
    private int totalPrice = 0;

    private String date;
    public final static int REQ_CODE_CHILD = 1;

    private ArrayList<MenuItem> menu;
    private ArrayList<String> coffeItemNames;
    private ArrayList<String> coffeItemSizes;
    private DatabaseReference mDatabase;
    private GoogleApiClient mGoogleApiClient;
    private FloatingActionButton fab;
    private LinearLayout mainLinerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_composer);
        Log.v(Utils.LOG_TAG, "SummaryComposerActivity");

        summaryList = new ArrayList<>();
        allDataListLists = new ArrayList<>();
        menu = new ArrayList<>();

        mainLinerLayout = (LinearLayout) findViewById(R.id.summary_composer_ll);
        mainLinerLayout.setVisibility(View.GONE);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(Utils.EXTRA_TAG_EMAIL);
        userName = intent.getStringExtra(Utils.EXTRA_TAG_NAME);
        userId = intent.getStringExtra(Utils.EXTRA_TAG_ID);
        teamName = intent.getStringExtra(Utils.EXTRA_TAG_TEAM);
        getMenuFromBD();
        dialogShops();

        date = Utils.getCurrentDate();
        mDateTextView = (TextView) findViewById(R.id.date);
        mShopTextView = (TextView) findViewById(R.id.shop);
        mDateTextView.setText(date);
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
                } else {
                    Toast.makeText(getBaseContext(), "Меню не сформированно! Обратитесь к администратору или зайдите позже.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void dialogShops() {
        final List<Shop> shopList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop shop;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    shop = postSnapshot.getValue(Shop.class);
                    if (shop != null) {shopList.add(shop);
                    }
                }
                if (shopList.size() != 0) {
                    showShopsDialog(shopList);
                } else {
                    Toast.makeText(getApplicationContext(), "Торговые точи не созданы! Обратитесь к администратору.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void showShopsDialog(List<Shop> shopList){

        final String cShopItem[] = new String[1];
        final String[] shopNamesArray = new String[shopList.size()];
        int i = 0;
        for (Shop shop : shopList) {
            shopNamesArray[i] = shop.getName();
            i++;
        }

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Выберите название торговой точки:");
        alt_bld.setSingleChoiceItems(shopNamesArray, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                cShopItem[0] = shopNamesArray[item];
                Toast.makeText(getApplicationContext(), "Торговая точка \"" + cShopItem[0] + "\"", Toast.LENGTH_SHORT).show();
            }
        });

        alt_bld.setPositiveButton("Ok", null);
        alt_bld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alt_bld.setCancelable(false);
        final AlertDialog alert = alt_bld.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(cShopItem[0])) {
                            shop = cShopItem[0];
                            mainLinerLayout.setVisibility(View.VISIBLE);
                            mShopTextView.setText("\"" + shop + "\"");
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Сделайте выбор", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alert.show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.send_button) {
            if (summaryList.size() != 0) {
                fab.setVisibility(View.GONE);
                sendSummaryToDatabase(userId, userName, userEmail);
                mSaveToDatabaseButton.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "Отчет отослан!", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setTitle("Отчет пуст!");
                alt_bld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
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
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName).child("unhandled summaries");
        mDatabase.push().setValue(summary);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE_CHILD) {
            MenuItemsInSummary item = data.getExtras().getParcelable(AddItemsToSummaryActivity.TAG);
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
            alt_bld.setTitle("Вы уверены ?");
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

    private void getMenuFromBD() {
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MenuItem item;
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        item = postSnapshot.getValue(MenuItem.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sign_out:
                signout();
                return true;
            default : return super.onOptionsItemSelected(item);
        }
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Toast.makeText(getBaseContext(), "Sign out.", Toast.LENGTH_SHORT).show();
                    }
                });
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
