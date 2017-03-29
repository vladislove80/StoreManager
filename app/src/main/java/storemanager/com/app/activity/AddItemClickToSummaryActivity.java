package storemanager.com.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.MenuListAdapter;
import storemanager.com.app.models.MenuItem;
import storemanager.com.app.models.MenuItemsInSummary;
import storemanager.com.app.utils.RecyclerItemClickListener;

public class AddItemClickToSummaryActivity extends AppCompatActivity implements RecyclerItemClickListener {
    public static final String TAG = AddItemClickToSummaryActivity.class.getSimpleName();

    private Button addMenuItemButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private TextView selectedItemTextView;
    private TextView menuLabel;
    private ImageView decreaseImageView;
    private ImageView increaseImageView;
    private EditText itemNumberChooserEditView;

    private List<MenuItem> mDataset;
    private MenuItem selectedItemFromMenu;
    private List<MenuItem> tempDataset;

    private int itemAmount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_summary);

        selectedItemTextView = (TextView) findViewById(R.id.add_menu_item_selected_item);
        menuLabel = (TextView) findViewById(R.id.add_menu_item_label);
        progressBar = (ProgressBar) findViewById(R.id.add_menu_item_activity_progressbar);

        Intent intent = getIntent();
        mDataset = intent.getParcelableArrayListExtra(SummaryComposerActivity.TAG);
        tempDataset = new ArrayList<>(mDataset);
        progressBar.setVisibility(View.GONE);

        addMenuItemButton = (Button) findViewById(R.id.add_menu_item_add_button);
        addMenuItemButton.setEnabled(false);
        addMenuItemButton.setOnClickListener(addMenuItemButtonListener);

        mRecyclerView = (RecyclerView) findViewById(R.id.add_menu_item_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        initRecycler();

        decreaseImageView = (ImageView) findViewById(R.id.decrease_number_textview);
        decreaseImageView.setOnClickListener(decreaseListener);
        increaseImageView = (ImageView) findViewById(R.id.increase_number_textview);
        increaseImageView.setOnClickListener(increaseListener);

        itemNumberChooserEditView = (EditText) findViewById(R.id.item_number_editview);
        itemNumberChooserEditView.setInputType(InputType.TYPE_NULL);
        itemNumberChooserEditView.setOnClickListener(editTextClickListener);
    }

    View.OnClickListener editTextClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            itemNumberChooserEditView.setSelection(0, itemNumberChooserEditView.getText().toString().length());
            itemNumberChooserEditView.setInputType(InputType.TYPE_CLASS_NUMBER);
            itemNumberChooserEditView.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(itemNumberChooserEditView, InputMethodManager.SHOW_FORCED);
        }
    };

    View.OnClickListener addMenuItemButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemAmount = getValueFromEditText(itemNumberChooserEditView);
            if (itemAmount > 0 && itemAmount < 100) {
                // mDataset always has one ItemMenu to pass
                MenuItemsInSummary listToAddInSummary = new MenuItemsInSummary(selectedItemFromMenu, itemAmount);
                int resultCode = 101;
                Intent resultIntent = new Intent();
                resultIntent.putExtra(TAG, listToAddInSummary);
                setResult(resultCode, resultIntent);
                hideEditKeyboard(itemNumberChooserEditView);
                selectedItemFromMenu.setSelected(false);
                finish();
            }
        }
    };

    private void hideEditKeyboard(EditText itemNumberChooserEditView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(itemNumberChooserEditView.getWindowToken(), 0);
    }

    private int getValueFromEditText(EditText itemNumberChooserEditView){
        String value = itemNumberChooserEditView.getText().toString();
        if (!TextUtils.isEmpty(value)) {
            return Integer.parseInt(itemNumberChooserEditView.getText().toString());
        } else {
            return -1;
        }
    }

    View.OnClickListener increaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemAmount = getValueFromEditText(itemNumberChooserEditView);
            hideEditKeyboard(itemNumberChooserEditView);
            if (itemAmount < 100) {
                itemNumberChooserEditView.setText(Integer.toString(++itemAmount));
                if (!addMenuItemButton.isEnabled()) {
                    addMenuItemButton.setEnabled(true);
                }
            } else {
                Toast.makeText(getBaseContext(), "Количество не должно превышать 100!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener decreaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemAmount = getValueFromEditText(itemNumberChooserEditView);
            hideEditKeyboard(itemNumberChooserEditView);
            if (itemAmount > 1) {
                itemNumberChooserEditView.setText(Integer.toString(--itemAmount));
            } else {
                Toast.makeText(getBaseContext(), "Не менее 1", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initRecycler() {
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MenuListAdapter(getBaseContext(), mDataset, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRecyclerItemClick(int pos) {
        selectedItemFromMenu = mDataset.get(pos);
        selectedItemTextView.setText(mDataset.get(pos).getName());
        Toast.makeText(this, mDataset.get(pos).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        MenuListAdapter.selectedPos = -1;
        super.onDestroy();
    }
}

