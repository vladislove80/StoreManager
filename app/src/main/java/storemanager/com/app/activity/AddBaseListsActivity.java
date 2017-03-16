package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.BaseIngredientAdapter;
import storemanager.com.app.models.BaseItem;

public class AddBaseListsActivity extends AppCompatActivity {
    public static final String TAG = AddBaseListsActivity.class.getSimpleName();

    private EditText addIngredientEditText;
    private Button addIngredientButton;
    private Button addToDatabaseButton;
    private ListView ingredientListView;
    private List dataList;
    private BaseIngredientAdapter baseIngredientAdapter;
    private ProgressBar progressBar;

    private String listName;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "AddBaseListsActivity -> onCreate");
        setContentView(R.layout.activity_add_base_list);

        Intent intent = getIntent();
        listName = intent.getStringExtra(ListOfListActivity.TAG);

        getDataListFromDatabse();
        dataList = new ArrayList<>();
        addIngredientEditText = (EditText) findViewById(R.id.add_ingredient_edittext);
        addIngredientButton = (Button) findViewById(R.id.add_ingredient_button);
        addToDatabaseButton = (Button) findViewById(R.id.add_to_database_button);
        ingredientListView = (ListView) findViewById(R.id.ingridients_list);
        baseIngredientAdapter = new BaseIngredientAdapter(getBaseContext(), dataList);
        ingredientListView.setAdapter(baseIngredientAdapter);

        progressBar = (ProgressBar) findViewById(R.id.add_list_data_progressbar);
        //getDataListFromDatabse();

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = addIngredientEditText.getText().toString();
                addIngredientEditText.setText("");
                if (!ingredient.equals("")) {
                    dataList.add(ingredient);
                    baseIngredientAdapter.notifyDataSetChanged();
                }
            }
        });

        addToDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBaseListsInDatabase();
                finish();
            }
        });
    }

    private void getDataListFromDatabse() {
        mDatabase = FirebaseDatabase.getInstance().getReference("lists");
        /*Query query = mDatabase.orderByKey().endAt(listName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //postSnapshot.child(listName).getValue();
                        Log.d(TAG, "getDataListFromDatabse -> dataSnapshot.hasChildren()");
                        progressBar.setVisibility(View.GONE);
                    }
            }

        }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/

        mDatabase.child(listName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //postSnapshot.child(listName).getValue();
                        Log.d(TAG, "getDataListFromDatabse -> dataSnapshot.hasChildren()");
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    //addBaseListsInDatabase();
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addBaseListsInDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference("lists");
        BaseItem list;
        list = new BaseItem();
        list.setId(listName);
        list.setItemData(dataList);
        mDatabase.child(listName).setValue(list);
    }
}
