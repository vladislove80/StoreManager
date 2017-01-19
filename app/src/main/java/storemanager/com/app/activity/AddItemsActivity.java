package storemanager.com.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import storemanager.com.app.R;
import storemanager.com.app.utils.Utils;

public class AddItemsActivity extends AppCompatActivity {

    private LinearLayout listContainer;
    private TextView itemTextView;
    private TextView sizeTextView;
    private TextView numberTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Utils.LOG_TAG, "AddItemsActivity");
        setContentView(R.layout.add_items_activity);
        listContainer = (LinearLayout) findViewById(R.id.items_data_container);
        itemTextView = (TextView) findViewById(R.id.item);
        sizeTextView = (TextView) findViewById(R.id.size);
        numberTextView = (TextView) findViewById(R.id.number);

        itemTextView.setOnClickListener(itemClickListener);
        sizeTextView.setOnClickListener(sizeClickListener);
        numberTextView.setOnClickListener(numberClickListener);
    }

    View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener");
            showItemPopupMenu(v);
        }
    };

    private void showItemPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v, Gravity.CENTER);
        popupMenu.getMenu().add("Menu1 Label");
        popupMenu.getMenu().add("Menu2 Label");
        popupMenu.getMenu().add("Menu3 Label");
        popupMenu.getMenu().add("Menu4 Label");
        popupMenu.getMenu().add("Menu5 Label");
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener->onMenuItemClick");
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
        popupMenu.getMenu().add("Menu1 Size");
        popupMenu.getMenu().add("Menu2 Size");

        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener->onMenuItemClick");
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
        popupMenu.getMenu().add("Menu1 Number");
        popupMenu.getMenu().add("Menu2 Number");
        popupMenu.getMenu().add("Menu3 Number");
        popupMenu.getMenu().add("Menu4 Number");
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v(Utils.LOG_TAG, "AddItemsActivity->itemClickListener->onMenuItemClick");
                numberTextView.setText(item.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }
}
