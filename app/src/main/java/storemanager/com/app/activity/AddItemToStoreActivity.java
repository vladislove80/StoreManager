package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import storemanager.com.app.R;
import storemanager.com.app.models.BaseItem;

public class AddItemToStoreActivity extends AppCompatActivity {
    public static final String TAG = AddItemToStoreActivity.class.getSimpleName();
    public static final String TAG_NAME = "new item name";
    public static final String TAG_MEASURE = "new item measure";

    private DatabaseReference mDatabase;

    private Button buttonAddStoreItem;
    private Button buttonCancelAddStoreItem;
    private Spinner spinnerStoreItemMeasure;
    private Spinner spinnerStoreItems;
    private String storeItemMeasureFromSpinner;
    private String storeItemNameFromSpinner;
    private String teamName;
    private BaseItem baseDataList;
    private List<String> storeItemMeasures;
    private List<String> storeItemNames;

    private ArrayAdapter<String> storeItemMeasureAdapter;
    private ArrayAdapter<String> storeItemNamesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store_item);

        teamName = AdminActivity.getTeamName();
        baseDataList = new BaseItem();
        storeItemMeasures = new ArrayList<>();
        storeItemNames = new ArrayList<>();

        buttonAddStoreItem = (Button) findViewById(R.id.add_store_item_button);
        buttonAddStoreItem.setOnClickListener(addClickListener);
        buttonCancelAddStoreItem = (Button) findViewById(R.id.cancel_add_store_item_button);
        buttonCancelAddStoreItem.setOnClickListener(cancelClickListener);
        spinnerStoreItemMeasure = (Spinner) findViewById(R.id.store_item_measure_spinner);
        spinnerStoreItems = (Spinner) findViewById(R.id.store_items_spinner);
        getItemMeasureListFromDB();
        getItemNameListFromDB();

        storeItemNamesAdapter = new ArrayAdapter<>(this, R.layout.layout_add_store_item_activity_spinner, storeItemNames);
        spinnerStoreItems.setAdapter(storeItemNamesAdapter);
        spinnerStoreItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeItemNameFromSpinner = storeItemNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        storeItemMeasureAdapter = new ArrayAdapter<>(this, R.layout.layout_add_store_item_activity_spinner, storeItemMeasures);
        spinnerStoreItemMeasure.setAdapter(storeItemMeasureAdapter);
        spinnerStoreItemMeasure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeItemMeasureFromSpinner =  storeItemMeasures.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    View.OnClickListener addClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(storeItemMeasureFromSpinner)
                    && !TextUtils.isEmpty(storeItemMeasureFromSpinner)) {
                int resultCode = 101;
                Intent resultIntent = new Intent();
                resultIntent.putExtra(TAG_NAME, storeItemNameFromSpinner);
                resultIntent.putExtra(TAG_MEASURE, storeItemMeasureFromSpinner);
                setResult(resultCode, resultIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void getItemMeasureListFromDB() {
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("lists").child("item ingredient measure").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot.hasChildren()");
                    Map map = (Map) dataSnapshot.getValue();
                    baseDataList.setId((String) map.get("id"));
                    baseDataList.setItemData((List<String>) map.get("itemData"));
                    addDataListsToSpinners(baseDataList);
                } else {
                    Toast.makeText(getBaseContext(), "Обратитесь к администратору!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getItemNameListFromDB() {
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("lists").child("item ingredient names").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot.hasChildren()");
                    Map map = (Map) dataSnapshot.getValue();
                    baseDataList.setId((String) map.get("id"));
                    baseDataList.setItemData((List<String>) map.get("itemData"));
                    addDataListsToSpinners(baseDataList);
                } else {
                    Toast.makeText(getBaseContext(), "Обратитесь к администратору!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void addDataListsToSpinners(BaseItem measuresData) {
        if (measuresData.getId().equals("item ingredient measure")){
            storeItemMeasures.addAll(measuresData.getItemData());
            storeItemMeasureAdapter.notifyDataSetChanged();
        } else if (measuresData.getId().equals("item ingredient names")) {
            storeItemNames.addAll(measuresData.getItemData());
            storeItemNamesAdapter.notifyDataSetChanged();
        }
    }
}
