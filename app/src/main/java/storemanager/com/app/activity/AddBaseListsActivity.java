package storemanager.com.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.BaseIngredientAdapter;
import storemanager.com.app.models.BaseItem;
import storemanager.com.app.utils.Utils;

public class AddBaseListsActivity extends AppCompatActivity {
    public static final String TAG = AddBaseListsActivity.class.getSimpleName();

    private EditText addIngredientEditText;
    private Button addIngredientButton;
    private Button addToDatabaseButton;
    private ListView ingredientListView;
    private List dataList;
    private BaseIngredientAdapter baseIngredientAdapter;
    private ProgressBar progressBar;
    private BaseItem baseItem;

    private String listName;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "AddBaseListsActivity -> onCreate");
        setContentView(R.layout.activity_add_base_list);

        Intent intent = getIntent();
        listName = intent.getStringExtra(ListOfListActivity.TAG);
        baseItem = new BaseItem();
        dataList = new ArrayList<>();

        getDataListFromDatabse();

        addIngredientEditText = (EditText) findViewById(R.id.add_ingredient_edittext);
        setEditText(listName);
        addIngredientButton = (Button) findViewById(R.id.add_ingredient_button);
        addToDatabaseButton = (Button) findViewById(R.id.add_to_database_button);
        ingredientListView = (ListView) findViewById(R.id.ingridients_list);
        baseIngredientAdapter = new BaseIngredientAdapter(getBaseContext(), dataList);
        ingredientListView.setAdapter(baseIngredientAdapter);
        ingredientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(Utils.LOG_TAG, "ItemName - " + dataList.get(position));
                Context wrapper = new ContextThemeWrapper(getBaseContext(), R.style.PopupMenu);
                PopupMenu popupMenu = new PopupMenu(wrapper, view);
                popupMenu.getMenu().add("Удалить");
                popupMenu.getMenu().add("Редактировать");
                popupMenu.getMenuInflater().inflate(R.menu.lists_item_layout, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        Log.v(Utils.LOG_TAG, "AddSummaryItemsActivity->DELETE");
                        switch (item.getTitle().toString()) {
                            case "Удалить":
                                dataList.remove(position);
                                baseIngredientAdapter.notifyDataSetChanged();
                                Collections.sort(dataList);
                                editBaseListsInDatabase();
                                Toast.makeText(getBaseContext(), "Удалить", Toast.LENGTH_SHORT).show();
                                break;
                            /*case "Редактировать":
                                Toast.makeText(getBaseContext(), "Редактировать", Toast.LENGTH_SHORT).show();
                                break;*/
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return false;
            }
        });

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
                Collections.sort(dataList);
                editBaseListsInDatabase();
                finish();
            }
        });
    }

    private void getDataListFromDatabse() {
        mDatabase = FirebaseDatabase.getInstance().getReference("lists");
        mDatabase.child(listName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.getKey().equals("id")) {
                            baseItem.setId(postSnapshot.getValue(String.class));
                        } else if (postSnapshot.getKey().equals("itemData")) {
                            baseItem.setItemData((List) postSnapshot.getValue());
                            dataList.addAll(baseItem.getItemData());
                        }
                        Log.d(TAG, "getDataListFromDatabse -> dataSnapshot.hasChildren()");
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    //editBaseListsInDatabase();
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void editBaseListsInDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference("lists");
        BaseItem item;
        item = new BaseItem();
        item.setId(listName);
        item.setItemData(dataList);
        mDatabase.child(listName).setValue(item);
    }

    private void setEditText(String listName) {
        switch (listName) {
            case "item sizes": addIngredientEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "item ingredient sizes": addIngredientEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "item ingredient measure":addIngredientEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }
}
