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
import storemanager.com.app.utils.GeneralStoreManagerNotifier;

public class GeneralStoreManager {
    public static String TAG = GeneralStoreManager.class.getCanonicalName();

    private DatabaseReference mDatabase;
    private String teamName;
    private List<StoreItem> generalStoreItemList = new ArrayList<>();
    private StoreItem selectedStoreItem;

    private GeneralStoreManagerNotifier notifier;

    public GeneralStoreManager() {
        this.teamName = AdminActivity.getTeamName();
        this.mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
    }

    private void getStoreItemsFromDatabase(){
        Query query = mDatabase.child("general store");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    generalStoreItemList.clear();
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        generalStoreItemList.add(postSnapshot.getValue(StoreItem.class));
                    }
                    notifier.onDownLoaded(generalStoreItemList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "addStoreItemsToDatabase -> onCancelled = " + databaseError.getMessage());
            }
        });
    }

    public void registerNotifierCallBack(GeneralStoreManagerNotifier notifier){
        this.notifier = notifier;
        getStoreItemsFromDatabase();
    }

    public void addStoreItemsToDatabase(List<StoreItem> dataset) {
        mDatabase.child("general store").setValue(dataset);
    }

    public List<StoreItem> getStoreItemList() {
        return generalStoreItemList;
    }

    public StoreItem getSelectedStoreItem() {
        return selectedStoreItem;
    }

    public void selectStoreItem(int pos) {
        this.selectedStoreItem = generalStoreItemList.get(pos);
    }

    public void addEventToStoreItemData(Event event){
        if (event.getAmount() > 0) {
            selectedStoreItem.addLastCommingIn(event);
        } else if (event.getAmount() < 0){
            selectedStoreItem.addLastConsumption(event);
        }
        addStoreItemsToDatabase(generalStoreItemList);
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

    public ArrayList<Event> getShopStoreItemConsumptionList(StoreItem storeItem){
        ArrayList<Event> consumptionEventList = new ArrayList<>();
        for (Event event : storeItem.getListEvents()){
            if (event.getAmount() < 0){
                consumptionEventList.add(event);
            }
        }
        Collections.reverse(consumptionEventList);
        return consumptionEventList;
    }

    public ArrayList<Event> getShopStoreItemBalanceList(StoreItem storeItem){
        ArrayList<Event> balanceList = new ArrayList<>();
        balanceList.addAll(storeItem.getBalanceListEvents());
        Collections.reverse(balanceList);
        return balanceList;
    }
}
