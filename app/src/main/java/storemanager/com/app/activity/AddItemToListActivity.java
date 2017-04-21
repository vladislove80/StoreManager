package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import storemanager.com.app.R;

public class AddItemToListActivity extends AppCompatActivity {
    public static final String TAG = AddItemToListActivity.class.getSimpleName();

    private Button buttonAddItem;
    private Button buttonCancelAddItem;
    private EditText editItemName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_list);

        String editType = getIntent().getStringExtra(TAG);
        buttonAddItem = (Button) findViewById(R.id.ok_add_item_button);
        buttonCancelAddItem = (Button) findViewById(R.id.cancel_item_button);
        buttonAddItem.setOnClickListener(buttonAddClickListener);
        buttonCancelAddItem.setOnClickListener(buttonCancelClickListener);
        editItemName = (EditText) findViewById(R.id.edit_name_item);
        switch (editType) {
            case "text":
                editItemName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                setTitle(R.string.manifest_label_add_shop);
                break;
            case "amount":
                editItemName.setInputType(InputType.TYPE_CLASS_NUMBER);
                setTitle(R.string.manifest_label_add_amount);
                break;
            default:
                editItemName.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    View.OnClickListener buttonAddClickListener =
            new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String shopName = editItemName.getText().toString();
            if (!shopName.equals("")) {
                int resultCode = 101;
                Intent resultIntent = new Intent();
                resultIntent.putExtra(TAG, shopName);
                setResult(resultCode, resultIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener buttonCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
