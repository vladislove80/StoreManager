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
import java.util.Arrays;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.MenuIngredientAdapter;
import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.Ingredient;

public class AddMenuItemActivity extends AppCompatActivity {
    public static final String TAG = "add_menu_item";
    private String[] mesuareArray = {"мл", "гр", "кг", "л", "шт", "упак"};

    private ListView ingredientListView;
    private MenuIngredientAdapter ingridientAdapter;
    private List<Ingredient> ingredientList;
    private Button addIngredientButton;
    private Button addMenuItemButton;
    private Spinner ingredientsSpiner;
    private Spinner ingredientsMeasureSpiner;
    private Spinner ingredientSizeSpinner;
    private ArrayList<String> ingredientNames;
    private String ingredientNameFromSpinner;
    private Integer ingredientSizeFromSpinner;
    private String ingredientMeasureFromSpinner;

    private EditText menuItemNameEditText;
    private EditText menuItemCategoryEditText;
    private EditText menuItemPriceEditText;
    private EditText menuItemMeasureEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_menu_item);

        ingredientList = new ArrayList<>();
        addMenuItemButton = (Button) findViewById(R.id.add_menu_item_button);
        addMenuItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllDataSet(menuItemNameEditText, menuItemCategoryEditText, menuItemPriceEditText, menuItemMeasureEditText, ingredientList)) {
                    CoffeItem coffeItem = new CoffeItem();
                    coffeItem.setName(menuItemNameEditText.getText().toString());
                    //coffeItem.setOneSize();
                    coffeItem.setPrice(Integer.parseInt(menuItemPriceEditText.getText().toString()));
                    coffeItem.setSize(Integer.parseInt(menuItemMeasureEditText.getText().toString()));
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

        menuItemNameEditText = (EditText)findViewById(R.id.menu_item_name_et);
        menuItemCategoryEditText = (EditText)findViewById(R.id.menu_item_category_et);
        menuItemPriceEditText = (EditText)findViewById(R.id.menu_item_price_et);
        menuItemMeasureEditText = (EditText)findViewById(R.id.menu_item_measure_et);
        ingredientListView = (ListView) findViewById(R.id.ingridient_list);

        ingridientAdapter = new MenuIngredientAdapter(getBaseContext(), ingredientList);
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

        ingredientNames = new ArrayList<>();
        ingredientNames.add("Кофе");
        ingredientNames.add("Молоко");
        ingredientNames.add("Корица");
        ingredientNames.add("Сироп");

        ingredientsSpiner = (Spinner) findViewById(R.id.ingredients_spinner);
        ArrayAdapter<String> ingredientsSpinnerAdapter = new ArrayAdapter<>(this, R.layout.ingredient_item_spinner, ingredientNames);
        ingredientsSpiner.setAdapter(ingredientsSpinnerAdapter);
        ingredientsSpiner.setPrompt("Выбрать ингридиент");
        ingredientsSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredientNameFromSpinner = ingredientNames.get(position);
                ingredientsSpiner.setPrompt(ingredientNameFromSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ingredientSizeSpinner = (Spinner) findViewById(R.id.ingredient_size_spinner);
        Integer[] iArray = {5, 10, 15, 20, 25, 30, 50, 100, 150, 200};
        final ArrayList<Integer> intList = new ArrayList<>();
        intList.add(5);
        intList.add(10);
        intList.add(15);
        intList.add(20);
        intList.add(25);
        intList.add(30);
        intList.add(50);
        intList.add(100);
        intList.add(150);
        intList.add(200);
        final ArrayAdapter<Integer> ingredientsSizeSpinnerAdapter = new ArrayAdapter<>(this, R.layout.ingredient_size_item_spinner, intList);
        ingredientSizeSpinner.setAdapter(ingredientsSizeSpinnerAdapter);
        ingredientSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredientSizeFromSpinner = intList.get(position);
                //ingredientSizeSpinner.setPrompt();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ingredientsMeasureSpiner = (Spinner) findViewById(R.id.ingredient_measure_spinner);
        ArrayAdapter<String> ingredientsMeasureSpinnerAdapter = new ArrayAdapter<>(this, R.layout.ingredient_measure_item_spinner, Arrays.asList(mesuareArray));
        ingredientsMeasureSpiner.setAdapter(ingredientsMeasureSpinnerAdapter);
        ingredientsMeasureSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredientMeasureFromSpinner = mesuareArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean isAllDataSet(EditText menuItemNameEditText, EditText menuItemCategoryEditText, EditText menuItemPriceEditText, EditText menuItemMeasureEditText, List<Ingredient> ingredientList) {
        if (!TextUtils.isEmpty(menuItemNameEditText.getText())
                && !TextUtils.isEmpty(menuItemCategoryEditText.getText())
                && !TextUtils.isEmpty(menuItemPriceEditText.getText())
                && !TextUtils.isEmpty(menuItemMeasureEditText.getText())
                && ingredientList.size() > 0) {
            return true;
        } else return false;
    }
}
