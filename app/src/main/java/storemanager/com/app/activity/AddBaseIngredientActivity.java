package storemanager.com.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.BaseIngredientAdapter;

public class AddBaseIngredientActivity extends AppCompatActivity {
    public static final String TAG = "add_ingredient_item";

    private EditText addIngredientEditText;
    private Button addIngredientButton;
    private ListView ingredientListView;
    private List<String> ingredientList;
    private BaseIngredientAdapter baseIngredientAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        ingredientList = new ArrayList<>();
        addIngredientEditText = (EditText) findViewById(R.id.add_ingredient_edittext);
        addIngredientButton = (Button) findViewById(R.id.add_ingredient_button);
        ingredientListView = (ListView) findViewById(R.id.ingridients_list);
        baseIngredientAdapter = new BaseIngredientAdapter(getBaseContext(), ingredientList);
        ingredientListView.setAdapter(baseIngredientAdapter);

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = addIngredientEditText.getText().toString();
                addIngredientEditText.setText("");
                if (!ingredient.equals("")) {
                    ingredientList.add(ingredient);
                    baseIngredientAdapter.notifyDataSetChanged();
                }
            }
        });
    }


}
