package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import storemanager.com.app.adapter.MenuItemIngredientsAdapter;
import storemanager.com.app.models.BaseItem;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.Ingredient;

public class AddMenuItemActivity extends AppCompatActivity {
    public static final String TAG = "activity_add_menu_item";

    private MenuItemIngredientsAdapter ingridientAdapter;

    private ArrayList<String> menuItemNamesList;
    private ArrayList<String> itemSizeList;
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<String> ingredientNamesList;
    private ArrayList<String> ingredientSizeList;
    private ArrayList<String> ingredientMeasureList;
    private Button addIngredientButton;

    private Button addMenuItemButton;
    private Spinner ingredientsSpinner;
    private ListView ingredientListView;
    private Spinner ingredientsMeasureSpinner;
    private Spinner ingredientSizeSpinner;
    private Spinner itemNamesSpinner;
    private Spinner itemSizeSpinner;
    private EditText menuItemPriceEditText;
    private ProgressBar progressBar;

    private ArrayAdapter<String> itemNamesAdapter;
    private ArrayAdapter<String> itemSizeAdapter;
    private ArrayAdapter<String> ingredientsSpinnerAdapter;
    private ArrayAdapter<String> ingredientsSizeSpinnerAdapter;
    private ArrayAdapter<String> ingredientsMeasureSpinnerAdapter;

    private String listName;
    private DatabaseReference mDatabase;
    private List<BaseItem> allDataListLists;

    private String menuItemNameFromSpinner = "";
    private String menuItemSizeFromSpinner = "";
    private String ingredientSizeFromSpinner = "";
    private String ingredientNameFromSpinner = "";
    private String ingredientMeasureFromSpinner = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        ingredientNamesList = new ArrayList<>();
        ingredientSizeList = new ArrayList<>();
        ingredientMeasureList = new ArrayList<>();
        menuItemNamesList = new ArrayList<>();
        itemSizeList = new ArrayList<>();
        allDataListLists = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.add_menu_item_progressbar);

        getDataListsFromDB();

        ingredientList = new ArrayList<>();
        addMenuItemButton = (Button) findViewById(R.id.add_menu_item_button);
        addMenuItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllDataSet(
                        menuItemNameFromSpinner,
                        menuItemSizeFromSpinner,
                        ingredientSizeFromSpinner,
                        ingredientNameFromSpinner,
                        ingredientMeasureFromSpinner,
                        menuItemPriceEditText,
                        ingredientList)
                        ) {
                    MenuItem menuItem = new MenuItem();
                    menuItem.setName(menuItemNameFromSpinner);
                    //menuItem.setOneSize();
                    menuItem.setPrice(Integer.parseInt(menuItemPriceEditText.getText().toString()));
                    menuItem.setSize(Integer.parseInt(menuItemSizeFromSpinner));
                    menuItem.setConsist(ingredientList);

                    int resultCode = 101;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(TAG, (Parcelable) menuItem);
                    setResult(resultCode, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Зaаполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        itemNamesSpinner = (Spinner) findViewById(R.id.menu_item_names_spinner);
        itemNamesAdapter = new ArrayAdapter<>(this, R.layout.layout_add_menu_item_activity_spinner, menuItemNamesList);
        itemNamesSpinner.setAdapter(itemNamesAdapter);
        itemNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                menuItemNameFromSpinner = menuItemNamesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        itemSizeSpinner = (Spinner) findViewById(R.id.menu_item_size_spinner);
        itemSizeAdapter = new ArrayAdapter<String>(this, R.layout.layout_add_menu_item_measure_spinner, itemSizeList);
        itemSizeSpinner.setAdapter(itemSizeAdapter);
        itemSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                menuItemSizeFromSpinner = itemSizeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        menuItemPriceEditText = (EditText)findViewById(R.id.menu_item_price_et);
        ingredientListView = (ListView) findViewById(R.id.ingridient_list);
        ingridientAdapter = new MenuItemIngredientsAdapter(getBaseContext(), ingredientList);
        ingredientListView.setAdapter(ingridientAdapter);
        addIngredientButton = (Button) findViewById(R.id.add_ingredient_item_button);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ingredientNameFromSpinner.equals("")) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(ingredientNameFromSpinner);
                    ingredient.setSize(Integer.parseInt(ingredientSizeFromSpinner));
                    ingredient.setMeasure(ingredientMeasureFromSpinner);
                    if (!ingredientList.contains(ingredient)) {
                        ingredientList.add(ingredient);
                        ingridientAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        ingredientsSpinner = (Spinner) findViewById(R.id.ingredients_spinner);
        ingredientsSpinnerAdapter = new ArrayAdapter<>(this, R.layout.layout_add_menu_item_activity_spinner, ingredientNamesList);
        ingredientsSpinner.setAdapter(ingredientsSpinnerAdapter);
        ingredientsSpinner.setPrompt("Выбрать ингридиент");
        ingredientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredientNameFromSpinner = ingredientNamesList.get(position);
                ingredientsSpinner.setPrompt(ingredientNameFromSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ingredientSizeSpinner = (Spinner) findViewById(R.id.ingredient_size_spinner);
        ingredientsSizeSpinnerAdapter = new ArrayAdapter<>(this, R.layout.layout_add_menu_item_activity_spinner, ingredientSizeList);
        ingredientSizeSpinner.setAdapter(ingredientsSizeSpinnerAdapter);
        ingredientSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredientSizeFromSpinner = ingredientSizeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ingredientsMeasureSpinner = (Spinner) findViewById(R.id.ingredient_measure_spinner);
        ingredientsMeasureSpinnerAdapter = new ArrayAdapter<>(this, R.layout.layout_add_menu_item_activity_spinner, ingredientMeasureList);
        ingredientsMeasureSpinner.setAdapter(ingredientsMeasureSpinnerAdapter);
        ingredientsMeasureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredientMeasureFromSpinner = ingredientMeasureList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                        allDataListLists.add(item);
                    }
                    addDataListsToSpinners(allDataListLists);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "getDataListFromDatabse -> dataSnapshot not hasChildren()");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isAllDataSet(
            String menuItemNameFromSpinner,
            String menuItemSizeFromSpinner,
            String ingredientSizeFromSpinner,
            String ingredientNameFromSpinner,
            String ingredientMeasureFromSpinner,
            EditText menuItemPriceEditText,
            List<Ingredient> ingredientList) {

        if (!TextUtils.isEmpty(menuItemNameFromSpinner)
                && !TextUtils.isEmpty(menuItemSizeFromSpinner)
                && !TextUtils.isEmpty(ingredientSizeFromSpinner)
                && !TextUtils.isEmpty(ingredientNameFromSpinner)
                && !TextUtils.isEmpty(ingredientMeasureFromSpinner)
                && !TextUtils.isEmpty(menuItemPriceEditText.getText().toString())
                && ingredientList.size() > 0 ) {
            return true;
        } else {
            return false;
        }
    }

    private void addDataListsToSpinners(List<BaseItem> allDataListLists) {

        for (BaseItem item : allDataListLists) {
            String id = item.getId();
            switch (id) {
                case "item names":
                    menuItemNamesList.addAll(item.getItemData());
                    itemNamesAdapter.notifyDataSetChanged();
                    break;
                case "item sizes":
                    itemSizeList.addAll(item.getItemData());
                    itemSizeAdapter.notifyDataSetChanged();
                    break;
                case "item ingredient names":
                    ingredientNamesList.addAll(item.getItemData());
                    ingredientsSpinnerAdapter.notifyDataSetChanged();
                    break;
                case "item ingredient sizes":
                    ingredientSizeList.addAll(item.getItemData());
                    ingredientsSizeSpinnerAdapter.notifyDataSetChanged();
                    break;
                case "item ingredient measure":
                    ingredientMeasureList.addAll(item.getItemData());
                    ingredientsMeasureSpinnerAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
