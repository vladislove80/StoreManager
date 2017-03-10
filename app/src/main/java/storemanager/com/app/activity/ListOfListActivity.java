package storemanager.com.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import storemanager.com.app.ActionItem;
import storemanager.com.app.QuickAction;
import storemanager.com.app.R;

public class ListOfListActivity extends AppCompatActivity {

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

        // Add action item
        ActionItem addAction = new ActionItem();

        addAction.setTitle("Phone");
        //addAction.setIcon(getResources().getDrawable(R.drawable.phone));

        // Accept action item
        ActionItem accAction = new ActionItem();

        accAction.setTitle("Gmail");
        //accAction.setIcon(getResources().getDrawable(R.drawable.gmail));

        // Upload action item
        ActionItem upAction = new ActionItem();

        upAction.setTitle("Talk");
        //upAction.setIcon(getResources().getDrawable(R.drawable.talk));

        final QuickAction mQuickAction = new QuickAction(this);

        mQuickAction.addActionItem(addAction);
        mQuickAction.addActionItem(accAction);
        mQuickAction.addActionItem(upAction);

        // setup the action item click listener
        mQuickAction
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    public void onItemClick(int pos) {

                        /*if (pos == 0) { // Add item selected
                            Toast.makeText(MainActivity.this,
                                    "PHONE item selected", Toast.LENGTH_SHORT)
                                    .show();
                        } else if (pos == 1) { // Accept item selected
                            Toast.makeText(MainActivity.this,
                                    "GMAIL item selected", Toast.LENGTH_SHORT)
                                    .show();
                        } else if (pos == 2) { // Upload item selected
                            Toast.makeText(MainActivity.this, "TALK selected",
                                    Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });

        /*ImageView ivPic1 = (ImageView) this.findViewById(R.id.ivPic1);
        ivPic1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mQuickAction.show(v);
                mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
            }
        });*/

        /*Button btClickMe = (Button) this.findViewById(R.id.button1);
        btClickMe.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mQuickAction.show(v);
                mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
            }
        });*/

        itemNamesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickAction.show(v);
                mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
            }
        });
    }

}
