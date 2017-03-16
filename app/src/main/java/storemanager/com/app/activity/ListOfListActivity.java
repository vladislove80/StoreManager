package storemanager.com.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import storemanager.com.app.R;

public class ListOfListActivity extends AppCompatActivity {
    public static final String TAG = ListOfListActivity.class.getSimpleName();

    private TextView itemNamesTextView;
    private TextView itemSizesTextView;
    private TextView itemIngredientNamesTextView;
    private TextView itemIngredientSizesTextView;
    private TextView itemIngredientMeasuresTextView;

    private Button itemNamesButton;
    private Button itemSizesButton;
    private Button itemIngredientNamesButton;
    private Button itemIngredientSizesButton;
    private Button itemIngredientMeasuresButton;
    private List<String> namesList;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        itemNamesTextView = (TextView) findViewById(R.id.lists_names_textview);
        itemSizesTextView = (TextView) findViewById(R.id.lists_sizes_textview);
        itemIngredientNamesTextView = (TextView) findViewById(R.id.lists_ingredient_names_textview);
        itemIngredientSizesTextView = (TextView) findViewById(R.id.lists_ingredient_sizes_textview);
        itemIngredientMeasuresTextView = (TextView) findViewById(R.id.lists_ingredient_measure_textview);

        itemNamesButton = (Button) findViewById(R.id.lists_names_edit_button);
        itemSizesButton = (Button) findViewById(R.id.lists_size_edit_button);
        itemIngredientNamesButton = (Button) findViewById(R.id.lists_ingredient_names_edit_button);
        itemIngredientSizesButton = (Button) findViewById(R.id.lists_ingredient_sizes_edit_button);
        itemIngredientMeasuresButton = (Button) findViewById(R.id.lists_ingredient_measures_edit_button);

        itemNamesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PopActivity.class);
                startActivity(intent);
            }
        });

        itemNamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBaseListActivity("item names");
            }
        });
        itemSizesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBaseListActivity("item sizes");
            }
        });
        itemIngredientNamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBaseListActivity("item ingredient names");
            }
        });
        itemIngredientSizesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBaseListActivity("item ingredient sizes");
            }
        });
        itemIngredientMeasuresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBaseListActivity("item ingredient measure");
            }
        });
    }

    private void showBaseListActivity(String name) {
        Intent intent = new Intent(getApplicationContext(), AddBaseListsActivity.class);
        intent.putExtra(TAG, name);
        startActivity(intent);
    }
}
