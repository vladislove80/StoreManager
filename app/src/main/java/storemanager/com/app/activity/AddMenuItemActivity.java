package storemanager.com.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.MenuIngredientAdapter;
import storemanager.com.app.models.Ingredient;

public class AddMenuItemActivity extends AppCompatActivity {
    public static final String TAG = "add_menu_item";

    private ListView ingredientListView;
    private MenuIngredientAdapter ingridientAdapter;
    private List<Ingredient> ingredientList;
    private Button addIngredientButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_menu_item);

        ingredientList = new ArrayList<>();
        ingredientListView = (ListView) findViewById(R.id.ingridient_list);
        ingridientAdapter = new MenuIngredientAdapter(getBaseContext(), ingredientList);
        ingredientListView.setAdapter(ingridientAdapter);

        addIngredientButton = (Button) findViewById(R.id.add_ingredient_item);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
