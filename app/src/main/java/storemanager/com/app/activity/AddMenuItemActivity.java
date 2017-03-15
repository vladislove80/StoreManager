package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.MenuItemIngredientsAdapter;
import storemanager.com.app.fragment.AllDataLists;
import storemanager.com.app.fragment.MenuFragment;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.Ingredient;

public class AddMenuItemActivity extends AppCompatActivity {
    public static final String TAG = "activity_add_menu_item";

    private MenuItemIngredientsAdapter ingridientAdapter;
    private AllDataLists allDataLists;

    private ArrayList<String> menuItemNamesList;
    private ArrayList<String> itemSizeList;
    private List<Ingredient> ingredientList;
    private ArrayList<String> ingredientNamesList;
    private ArrayList<Integer> ingredientSizeList;
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

    private String menuItemNameFromSpinner = "";
    private String menuItemSizeFromSpinner = "";
    private Integer ingredientSizeFromSpinner = 0;
    private String ingredientNameFromSpinner = "";
    private String ingredientMeasureFromSpinner = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        ingredientNamesList = new ArrayList<>();
        ingredientNamesList.add("Кофе");
        ingredientNamesList.add("Молоко");
        ingredientNamesList.add("Корица");
        ingredientNamesList.add("Сироп");

        ingredientSizeList = new ArrayList<>();
        ingredientSizeList.add(5);
        ingredientSizeList.add(10);
        ingredientSizeList.add(15);
        ingredientSizeList.add(20);
        ingredientSizeList.add(25);
        ingredientSizeList.add(30);
        ingredientSizeList.add(50);
        ingredientSizeList.add(100);
        ingredientSizeList.add(150);
        ingredientSizeList.add(200);

        ingredientMeasureList = new ArrayList<>();
        ingredientMeasureList.add("мл");
        ingredientMeasureList.add("гр");
        ingredientMeasureList.add("кг");
        ingredientMeasureList.add("л");
        ingredientMeasureList.add("шт");
        ingredientMeasureList.add("упак");

        menuItemNamesList = new ArrayList<>();
        menuItemNamesList.add("Еспрессо");
        menuItemNamesList.add("Лате");
        menuItemNamesList.add("Американо");
        menuItemNamesList.add("Американо с молоком");
        menuItemNamesList.add("Макиято");

        itemSizeList = new ArrayList<>();
        itemSizeList.add("250");
        itemSizeList.add("350");
        itemSizeList.add("450");

        Intent intent = getIntent();
        allDataLists = (AllDataLists) intent.getSerializableExtra(MenuFragment.TAG);

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
                    resultIntent.putExtra(TAG, menuItem);
                    setResult(resultCode, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Зaаполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
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
                    ingredient.setSize(ingredientSizeFromSpinner);
                    ingredient.setMeasure(ingredientMeasureFromSpinner);
                    if (!ingredientList.contains(ingredient)) {
                        ingredientList.add(ingredient);
                        ingridientAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        ingredientsSpinner = (Spinner) findViewById(R.id.ingredients_spinner);
        ArrayAdapter<String> ingredientsSpinnerAdapter = new ArrayAdapter<>(this, R.layout.layout_add_menu_item_activity_spinner, ingredientNamesList);
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
        ArrayAdapter<Integer> ingredientsSizeSpinnerAdapter = new ArrayAdapter<>(this, R.layout.layout_add_menu_item_activity_spinner, ingredientSizeList);
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
        ArrayAdapter<String> ingredientsMeasureSpinnerAdapter = new ArrayAdapter<>(this, R.layout.layout_add_menu_item_activity_spinner, ingredientMeasureList);
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

        itemNamesSpinner = (Spinner) findViewById(R.id.menu_item_names_spinner);
        ArrayAdapter<String> itemNamesAdapter = new ArrayAdapter<>(this, R.layout.layout_add_menu_item_activity_spinner, menuItemNamesList);
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
        ArrayAdapter<String> itemSizeAdapter = new ArrayAdapter<String>(this, R.layout.layout_add_menu_item_measure_spinner, itemSizeList);
        itemSizeSpinner.setAdapter(itemSizeAdapter);
        itemSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                menuItemSizeFromSpinner = itemSizeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private boolean isAllDataSet(
            String menuItemNameFromSpinner,
            String menuItemSizeFromSpinner,
            Integer ingredientSizeFromSpinner,
            String ingredientNameFromSpinner,
            String ingredientMeasureFromSpinner,
            EditText menuItemPriceEditText,
            List<Ingredient> ingredientList) {

        if (!TextUtils.isEmpty(menuItemNameFromSpinner)
                && !TextUtils.isEmpty(menuItemSizeFromSpinner)
                && ingredientSizeFromSpinner != 0
                && !TextUtils.isEmpty(ingredientNameFromSpinner)
                && !TextUtils.isEmpty(ingredientMeasureFromSpinner)
                && !TextUtils.isEmpty(menuItemPriceEditText.getText().toString())
                && ingredientList.size() > 0 ) {
            return true;
        } else {
            return false;
        }
    }
}
