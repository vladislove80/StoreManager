package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
import storemanager.com.app.models.StoreItem;

public class AddItemsToShopStoreActivity extends AppCompatActivity {
    public static String TAG = AddItemsToShopStoreActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private List<StoreItem> mDataset;
    private ArrayList<StoreItem> mDatasetToAdd;
    private String teamName;
    private LinearLayout checkBoxesLayout;
    private CheckBox selectAllCheckBox;
    private ArrayList<CheckBox> checkBoxList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items_to_shop_store);
        checkBoxList = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7),(int) (height*.7));
        selectAllCheckBox = (CheckBox) findViewById(R.id.select_all);
        selectAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBoxList.size() != 0) {
                    for (CheckBox box : checkBoxList) {
                        if (isChecked) {
                            box.setChecked(true);
                        } else {
                            box.setChecked(false);
                        }
                    }
                }
            }
        });

        teamName = AdminActivity.getTeamName();
        mDataset = new ArrayList<>();
        mDatasetToAdd = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        if (mDataset.size() == 0) {
            getStoreItemsFromDatabase();
        }
        checkBoxesLayout = (LinearLayout) findViewById(R.id.check_boxes_layout);
        findViewById(R.id.add_item_to_shop_store_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatasetToAdd = isCheckedBox();
                if (selectAllCheckBox.isChecked() || mDatasetToAdd.size() != 0) {
                    int resultCode = 101;
                    Intent resultIntent = new Intent();
                    resultIntent.putParcelableArrayListExtra(TAG, mDatasetToAdd);
                    setResult(resultCode, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Выбор не сделан", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getStoreItemsFromDatabase(){
        Query query = mDatabase.child("general store");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        StoreItem item = postSnapshot.getValue(StoreItem.class);
                        mDataset.add(item);
                    }
                    inflateCheckBoxList(mDataset);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "addStoreItemToDatabase -> onCancelled = " + databaseError.getMessage());
            }
        });
    }

    private void inflateCheckBoxList(List<StoreItem> mDataset) {
        CheckBox box;
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for(StoreItem item : mDataset){
            box = new CheckBox(this);
            box.setText(item.getName());
            box.setTextSize(24);
            box.setPadding(5, 5, 5, 5);
            box.setLayoutParams(lparams);
            checkBoxesLayout.addView(box);
            checkBoxList.add(box);
        }
    }

    public ArrayList<StoreItem> isCheckedBox(){
        ArrayList<StoreItem> checkedBoxList = new ArrayList<>();
        for (int i=0; i<checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isChecked()) {
                checkedBoxList.add(mDataset.get(i));
            }
        }
        return checkedBoxList;
    }
}
