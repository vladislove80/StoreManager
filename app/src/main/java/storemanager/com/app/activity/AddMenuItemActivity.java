package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.Ingredient;

public class AddMenuItemActivity extends AppCompatActivity {
    public static final String TAG = "add_menu_item";

    private MenuItemIngredientsAdapter ingridientAdapter;

    private ListView ingredientListView;
    private List<Ingredient> ingredientList;
    private ArrayList<String> ingredientNamesList;
    private ArrayList<String> ingredientMeasureList;
    private ArrayList<Integer> ingredientSizeList;
    private ArrayList<String> menuItemNamesList;
    private ArrayList<String> itemSizeList;

    private Button addIngredientButton;
    private Button addMenuItemButton;

    private Spinner ingredientsSpinner;
    private Spinner ingredientsMeasureSpinner;
    private Spinner ingredientSizeSpinner;
    private Spinner itemNamesSpinner;
    private Spinner itemSizeSpinner;

    private Integer ingredientSizeFromSpinner;
    private String ingredientNameFromSpinner;
    private String itemNameFromSpinner;
    private String itemSizeFromSpinner;
    private String ingredientMeasureFromSpinner;

    private EditText menuItemPriceEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_menu_item);

        ingredientList = new ArrayList<>();
        addMenuItemButton = (Button) findViewById(R.id.add_menu_item_button);
        addMenuItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllDataSet(menuItemPriceEditText, ingredientList)) {
                    CoffeItem coffeItem = new CoffeItem();
                    coffeItem.setName(itemNameFromSpinner);
                    //coffeItem.setOneSize();
                    coffeItem.setPrice(Integer.parseInt(menuItemPriceEditText.getText().toString()));
                    coffeItem.setSize(Integer.parseInt(itemSizeFromSpinner));
                    coffeItem.setConsist(ingredientList);

                    int resultCode = 101;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(TAG, coffeItem);
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

        ingredientNamesList = new ArrayList<>();
        ingredientNamesList.add("Кофе");
        ingredientNamesList.add("Молоко");
        ingredientNamesList.add("Корица");
        ingredientNamesList.add("Сироп");

        ingredientsSpinner = (Spinner) findViewById(R.id.ingredients_spinner);
        ArrayAdapter<String> ingredientsSpinnerAdapter = new ArrayAdapter<>(this, R.layout.add_menu_item_activity_spinner, ingredientNamesList);
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
        ingredientSizeSpinner = (Spinner) findViewById(R.id.ingredient_size_spinner);
        ArrayAdapter<Integer> ingredientsSizeSpinnerAdapter = new ArrayAdapter<>(this, R.layout.add_menu_item_activity_spinner, ingredientSizeList);
        ingredientSizeSpinner.setAdapter(ingredientsSizeSpinnerAdapter);
        ingredientSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredientSizeFromSpinner = ingredientSizeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        /*private String[] mesuareArray = {"мл", "гр", "кг", "л", "шт", "упак"};*/
        ingredientMeasureList = new ArrayList<>();
        ingredientMeasureList.add("мл");
        ingredientMeasureList.add("гр");
        ingredientMeasureList.add("кг");
        ingredientMeasureList.add("л");
        ingredientMeasureList.add("шт");
        ingredientMeasureList.add("упак");
        ingredientsMeasureSpinner = (Spinner) findViewById(R.id.ingredient_measure_spinner);
        ArrayAdapter<String> ingredientsMeasureSpinnerAdapter = new ArrayAdapter<>(this, R.layout.add_menu_item_activity_spinner, ingredientMeasureList);
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

        menuItemNamesList = new ArrayList<>();
        menuItemNamesList.add("Еспрессо");
        menuItemNamesList.add("Лате");
        menuItemNamesList.add("Американо");
        menuItemNamesList.add("Американо с молоком");
        menuItemNamesList.add("Макиято");

        itemNamesSpinner = (Spinner) findViewById(R.id.menu_item_names_spinner);
        ArrayAdapter<String> itemNamesAdapter = new ArrayAdapter<>(this, R.layout.add_menu_item_activity_spinner, menuItemNamesList);
        itemNamesSpinner.setAdapter(itemNamesAdapter);
        itemNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemNameFromSpinner = menuItemNamesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        itemSizeList = new ArrayList<>();
        itemSizeList.add("250");
        itemSizeList.add("350");
        itemSizeList.add("450");
        itemSizeSpinner = (Spinner) findViewById(R.id.menu_item_size_spinner);
        ArrayAdapter<String> itemSizeAdapter = new ArrayAdapter<String>(this, R.layout.add_menu_item_measure_spinner, itemSizeList);
        itemSizeSpinner.setAdapter(itemSizeAdapter);
        itemSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSizeFromSpinner = itemSizeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private boolean isAllDataSet(EditText menuItemPriceEditText, List<Ingredient> ingredientList) {
        return true;
    }
}
