package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import storemanager.com.app.R;
import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.CoffeItemsToAddInSummary;
import storemanager.com.app.utils.Utils;

public class AddItemsActivity extends AppCompatActivity {

    public static final String TAG = "add_atems";

    private LinearLayout listContainer;
    private TextView itemTextView;
    private TextView sizeTextView;
    private TextView numberTextView;

    private CoffeItemsToAddInSummary cofeItemsToAdd;
    private CoffeItem cofeItem;
    private int coffeItemNumber = 0;
    private Button buttonAdd;
    private Button buttonCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Utils.LOG_TAG, "AddItemsActivity");
        setContentView(R.layout.add_items_activity);

        listContainer = (LinearLayout) findViewById(R.id.items_data_container);

        itemTextView = (TextView) findViewById(R.id.item);
        sizeTextView = (TextView) findViewById(R.id.size);
        numberTextView = (TextView) findViewById(R.id.number);
        buttonAdd = (Button) findViewById(R.id.add_button);
        buttonCancel = (Button) findViewById(R.id.cancel_button);

        buttonAdd.setOnClickListener(buttonAddClickListener);
        buttonCancel.setOnClickListener(buttonCancelClickListener);

        itemTextView.setOnClickListener(itemClickListener);
        sizeTextView.setText("250");
        sizeTextView.setOnClickListener(sizeClickListener);
        numberTextView.setText("1");
        numberTextView.setOnClickListener(numberClickListener);

        cofeItem = new CoffeItem();
    }

    View.OnClickListener buttonAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CoffeItemsToAddInSummary listToAddInSummary = new CoffeItemsToAddInSummary(cofeItem, coffeItemNumber);
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

    View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener");
            showItemPopupMenu(v);
        }
    };

    private void showItemPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v, Gravity.CENTER);
        popupMenu.getMenu().add("Эспрессо");
        popupMenu.getMenu().add("Эспрессо 2x");
        popupMenu.getMenu().add("Американо");
        popupMenu.getMenu().add("Кофе с молоком");
        popupMenu.getMenu().add("Кофе по Ирландски");popupMenu.getMenu().add("Эспрессо");
        popupMenu.getMenu().add("Эспрессо 2x");
        popupMenu.getMenu().add("Американо");
        popupMenu.getMenu().add("Кофе с молоком");
        popupMenu.getMenu().add("Кофе по Ирландски");popupMenu.getMenu().add("Эспрессо");
        popupMenu.getMenu().add("Эспрессо 2x");
        popupMenu.getMenu().add("Американо");
        popupMenu.getMenu().add("Кофе с молоком");
        popupMenu.getMenu().add("Кофе по Ирландски");popupMenu.getMenu().add("Эспрессо");
        popupMenu.getMenu().add("Эспрессо 2x");
        popupMenu.getMenu().add("Американо");
        popupMenu.getMenu().add("Кофе с молоком");
        popupMenu.getMenu().add("Кофе по Ирландски");popupMenu.getMenu().add("Эспрессо");
        popupMenu.getMenu().add("Эспрессо 2x");
        popupMenu.getMenu().add("Американо");
        popupMenu.getMenu().add("Кофе с молоком");
        popupMenu.getMenu().add("Кофе по Ирландски");
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener->onMenuItemClick");
                cofeItem.setName(item.getTitle().toString());
                itemTextView.setText(item.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }

    View.OnClickListener sizeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener");
            showSizePopupMenu(v);
        }
    };

    private void showSizePopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v, Gravity.CENTER);
        popupMenu.getMenu().add("250");
        popupMenu.getMenu().add("350");
        popupMenu.getMenu().add("450");

        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener->onMenuItemClick");
                cofeItem.setSize(item.getTitle().toString());
                sizeTextView.setText(item.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }

    View.OnClickListener numberClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener");
            showNumberPopupMenu(v);
        }
    };

    private void showNumberPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v, Gravity.CENTER);
        popupMenu.getMenu().add("1");
        popupMenu.getMenu().add("2");
        popupMenu.getMenu().add("3");
        popupMenu.getMenu().add("4");
        popupMenu.getMenu().add("5");
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener->onMenuItemClick");
                coffeItemNumber = Integer.parseInt(item.getTitle().toString());
                numberTextView.setText(item.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }

}
