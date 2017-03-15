package storemanager.com.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import storemanager.com.app.R;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.CoffeItemInSummary;
import storemanager.com.app.utils.Utils;

public class AddISummaryItemsActivity extends AppCompatActivity {

    public static final String TAG = "add_atems";

    private CoffeItemInSummary cofeItemsToAdd;
    private MenuItem cofeItem;
    private int coffeItemAmount = 1;
    private Button buttonAdd;
    private Button buttonCancel;

    private Spinner itemsSpiner;
    private Spinner sizeSpiner;
    private Spinner amountSpiner;

    private ArrayList<String> coffeItemNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Utils.LOG_TAG, "AddISummaryItemsActivity");
        setContentView(R.layout.activity_add_items);

        buttonAdd = (Button) findViewById(R.id.add_shop_button);
        buttonCancel = (Button) findViewById(R.id.cancel_button);

        itemsSpiner = (Spinner) findViewById(R.id.item_spinner);
        sizeSpiner = (Spinner) findViewById(R.id.size_spinner);
        amountSpiner = (Spinner) findViewById(R.id.number_spinner);

        buttonAdd.setOnClickListener(buttonAddClickListener);
        buttonCancel.setOnClickListener(buttonCancelClickListener);

        Intent intent = getIntent();
        coffeItemNames = intent.getStringArrayListExtra(SummaryComposerActivity.MENU_TAG);

        cofeItem = new MenuItem();

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.layout_summary_item_spinner, coffeItemNames);

        itemsSpiner.setAdapter(itemsAdapter);
        itemsSpiner.setPrompt(Utils.coffeItems.get(0));

        itemsSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);

                String itemName = coffeItemNames.get(position);
                itemsSpiner.setPrompt(itemName);
                cofeItem.setName(itemName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cofeItem.setName(Utils.coffeItems.get(0));
            }
        });

        ArrayAdapter<Integer> sizeAdapter = new ArrayAdapter<>(this, R.layout.layout_summary_item_spinner, Utils.coffeSizes);

        sizeSpiner.setAdapter(sizeAdapter);
        sizeSpiner.setPrompt(Utils.coffeSizes.get(0).toString());
        sizeSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                Integer size = Utils.coffeSizes.get(position);
                sizeSpiner.setPrompt(size.toString());
                cofeItem.setSize(size);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cofeItem.setSize(Utils.coffeSizes.get(0));
            }
        });

        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<>(this, R.layout.layout_summary_item_spinner, Utils.coffeNumber);

        amountSpiner.setAdapter(numAdapter);
        amountSpiner.setPrompt(Utils.coffeNumber.get(0).toString());
        amountSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                Integer num = Utils.coffeNumber.get(position);
                sizeSpiner.setPrompt(num.toString());
                coffeItemAmount = num;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener buttonAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CoffeItemInSummary listToAddInSummary = new CoffeItemInSummary(cofeItem, coffeItemAmount);
            int resultCode = 101;
            Intent resultIntent = new Intent();
            resultIntent.putExtra(TAG, listToAddInSummary);
            setResult(resultCode, resultIntent);
            finish();
        }
    };

    View.OnClickListener buttonCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
