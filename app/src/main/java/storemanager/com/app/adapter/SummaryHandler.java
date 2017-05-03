package storemanager.com.app.adapter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import storemanager.com.app.models.Event;
import storemanager.com.app.models.Ingredient;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.MenuItemsInSummary;
import storemanager.com.app.models.StoreItem;
import storemanager.com.app.models.Summary;
import storemanager.com.app.utils.Utils;

public class SummaryHandler {
    public static String TAG = SummaryHandler.class.getSimpleName();

    private String teamName;
    private DatabaseReference mDatabase;
    private List<Summary> unhandledSummaryList;
    private HashMap<String, Float> summaryIngredientBalance;
    private List<StoreItem> shopStoreItemList = new ArrayList<>();

    public SummaryHandler(String teamName) {
        this.teamName = teamName;
        unhandledSummaryList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                unhandledSummaryList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Summary summary = postSnapshot.getValue(Summary.class);
                    unhandledSummaryList.add(summary);
                }
                copyUnhandledToHandledSummary();
                deleteUnhandledSummaries();
                Log.d(Utils.LOG_TAG, "SummaryHandler -> onDataChange. unhandledSummaryList.size() = " + unhandledSummaryList.size());
                if (unhandledSummaryList.size() > 0) {
                    handleSummaryList(unhandledSummaryList);
                }
            }

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(Utils.LOG_TAG, "SummaryHandler -> DatabaseError", databaseError.toException());
        }
    };

    private void handleSummaryList(List<Summary> summaryList) {
        Log.d(Utils.LOG_TAG, "SummaryHandler -> handleSummaryList");
        int amount;
        MenuItem menuItem;
        String shopName;
        String date;
        for(Summary summary : summaryList){
            shopName = summary.getShop();
            date = summary.getDate();
            summaryIngredientBalance = new HashMap<>();
            for(MenuItemsInSummary itemFromSummary : summary.getItemInSummary()){
                amount = itemFromSummary.getAmount();
                menuItem = itemFromSummary.getItem();
                for(Ingredient ingredient : menuItem.getConsist()){
                    String ingredientName = ingredient.getName();
                    if(!summaryIngredientBalance.containsKey(ingredientName)){
                        summaryIngredientBalance.put(ingredientName, ingredient.getSize() * amount);
                    } else {
                        float temp = summaryIngredientBalance.get(ingredientName);
                        temp = temp + ingredient.getSize() * amount;
                        summaryIngredientBalance.put(ingredientName, temp);
                    }
                }
            }
            /*Log.d(Utils.LOG_TAG, "SummaryHandler -> handleSummaryList -> Shop = " + shopName + ", ");
            for (Map.Entry entry : summaryIngredientBalance.entrySet()) {
                Log.w(Utils.LOG_TAG, "Key -> " + entry.getKey() + ", val = " + entry.getValue());
            }*/
            Log.d(Utils.LOG_TAG, "SummaryHandler -> handleSummaryList -> editShopStoreItemList for " + shopName);
            editShopStoreItemList(shopName, summaryIngredientBalance, date);
        }
    }

    private void editShopStoreItemList(final String shopName, final HashMap<String, Float> summaryIngredientBalance, final String date){
        Query query = FirebaseDatabase.getInstance().getReference(teamName).child("shop store").child(shopName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    shopStoreItemList.clear();
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        StoreItem storeItem = postSnapshot.getValue(StoreItem.class);
                        float amount = 0;
                        if(summaryIngredientBalance.containsKey(storeItem.getName())){
                            amount = summaryIngredientBalance.get(storeItem.getName());
                            //recount ml/gr in to l/kg
                            Event event = new Event(date, - amount/1000);
                            Log.d(Utils.LOG_TAG, "storeItem = " + storeItem.getName() + ", amount = " + event.getAmount());
                            storeItem.addLastConsumption(event);
                        }
                        shopStoreItemList.add(storeItem);
                    }
                    Log.d(Utils.LOG_TAG, "Shop name for update in FB - " + shopName + "(updated " + shopStoreItemList.size() + " items).");
                    addItemListToShopStoreInDatabase(shopStoreItemList, shopName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(Utils.LOG_TAG, "addStoreItemToDatabase -> onCancelled = " + databaseError.getMessage());
            }
        });
    }

    private void addItemListToShopStoreInDatabase(List<StoreItem> dataset, String shopName) {
        Log.d(Utils.LOG_TAG, "SummaryHandler -> addItemListToShopStoreInDatabase");
        mDatabase.child("shop store").child(shopName).setValue(dataset);
    }

    private void deleteUnhandledSummaries(){
        Log.d(Utils.LOG_TAG, "SummaryHandler -> deleteUnhandledSummaries");
        mDatabase.child("unhandled summaries").removeValue();
    }

    public void copyUnhandledToHandledSummary(){
        Log.d(Utils.LOG_TAG, "SummaryHandler -> copyUnhandledToHandledSummary");
        for (Summary summary : unhandledSummaryList) {
            mDatabase.child("summaries").push().setValue(summary);
        }
    }

    public void enableSummaryListener(){
        mDatabase.child("unhandled summaries").addValueEventListener(postListener);
    }

    public void disableSummaryListener(){
        mDatabase.child("unhandled summaries").removeEventListener(postListener);
    }
}
