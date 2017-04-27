package storemanager.com.app.adapter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import storemanager.com.app.activity.AdminActivity;
import storemanager.com.app.models.Event;
import storemanager.com.app.models.StoreItem;
import storemanager.com.app.utils.ShopStoreManagerNotifier;

public class ShopStoreManager {
    public static String TAG = ShopStoreManager.class.getCanonicalName();

    private DatabaseReference mDatabase;
    private String teamName;
    private String shopName;
    private List<StoreItem> generalStoreItemList = new ArrayList<>();
    private List<StoreItem> shopStoreItemList = new ArrayList<>();
    private StoreItem selectedShopItem;

    private ShopStoreManagerNotifier shopStoreItemListCallback;

    public ShopStoreManager(String shopName) {
        this.shopName = shopName;
        this.teamName = AdminActivity.getTeamName();
        this.mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
    }

    private void getShopStoreItemListFromDatabase(){
        Query query = mDatabase.child("shop store").child(shopName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        shopStoreItemList.add(postSnapshot.getValue(StoreItem.class));
                    }
                    shopStoreItemListCallback.onDownLoaded(shopStoreItemList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "getStoreItemsFromDatabase -> onCancelled = " + databaseError.getMessage());
            }
        });
    }

    private void getGeneralStoreItemListFromDatabase(){
        Query query = mDatabase.child("general store");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        generalStoreItemList.add(postSnapshot.getValue(StoreItem.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "addStoreItemToDatabase -> onCancelled = " + databaseError.getMessage());
            }
        });
    }

    public List<StoreItem> getShopStoreItemList() {
        return shopStoreItemList;
    }

    public StoreItem getSelectedShopItem() {
        return selectedShopItem;
    }

    public void selectStoreItem(int pos) {
        this.selectedShopItem = shopStoreItemList.get(pos);
    }

    public void addEventToStoreItemData(Event event){
        if (event.getAmount() > 0) {
            selectedShopItem.addLastCommingIn(event);
        } else if (event.getAmount() < 0){
            selectedShopItem.addLastConsumption(event);
        }
        addItemListToShopStoreInDatabase(shopStoreItemList);
        editGeneralStoreItem(selectedShopItem);
    }

    private void editGeneralStoreItem(StoreItem selectedShopItem){
        for (StoreItem item : generalStoreItemList) {
            if (item.equals(selectedShopItem)) {
                //convert cominIn for shop to consumption for general store by add "-" to event
                Event event = new Event(selectedShopItem.getLastComingIn().getDate(), - selectedShopItem.getLastComingIn().getAmount());
                item.addLastConsumption(event);
                break;
            }
        }
        addItemListToGeneralStoreInDatabase(generalStoreItemList);
    }

    private void addItemListToShopStoreInDatabase(List<StoreItem> dataset) {
        mDatabase.child("shop store").child(shopName).setValue(dataset);
    }

    private void addItemListToGeneralStoreInDatabase(List<StoreItem> dataset) {
        mDatabase.child("general store").setValue(dataset);
    }

    public void addNewItemsFromGeneralStoreToShopStore(List<StoreItem> datasetToAdd){
        if (!Collections.disjoint(shopStoreItemList, datasetToAdd)) {
            datasetToAdd = removeCoincidence(shopStoreItemList, datasetToAdd);
        }
        shopStoreItemList.addAll(datasetToAdd);
        addItemListToShopStoreInDatabase(shopStoreItemList);
    }

    private List<StoreItem> removeCoincidence(List<StoreItem> dataset, List<StoreItem> datasetToAdd) {
        List<StoreItem> clearList = new ArrayList<>();
        for (StoreItem item : datasetToAdd){
            if (!dataset.contains(item)) {
                clearList.add(item);
            }
        }
        return clearList;
    }

    public void registerDownloadedStoreItemListCallBack(ShopStoreManagerNotifier callback) {
        this.shopStoreItemListCallback = callback;
        getShopStoreItemListFromDatabase();
        getGeneralStoreItemListFromDatabase();
    }

    public ArrayList<Event> getShopStoreItemComingInList(StoreItem storeItem){
        ArrayList<Event> incomingInEventList = new ArrayList<>();
        for (Event event : storeItem.getListEvents()){
            if (event.getAmount() > 0){
                incomingInEventList.add(event);
            }
        }
        Collections.reverse(incomingInEventList);
        return incomingInEventList;
    }

    public List<Event> getShopStoreItemConsumptionList(StoreItem storeItem){
        List<Event> consumptionEventList = new ArrayList<>();
        for (Event event : storeItem.getListEvents()){
            if (event.getAmount() < 0){
                consumptionEventList.add(event);
            }
        }
        return consumptionEventList;
    }
}
