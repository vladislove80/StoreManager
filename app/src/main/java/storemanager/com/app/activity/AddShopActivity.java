package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import storemanager.com.app.R;

public class AddShopActivity extends AppCompatActivity {
    public static final String TAG = "add_shop";

    private Button buttonAddShop;
    private Button buttonCancelAddShop;
    private EditText editShopName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitu_add_shop_activity);
        buttonAddShop = (Button) findViewById(R.id.ok_add_shop_button);
        buttonCancelAddShop = (Button) findViewById(R.id.cancel_shop_button);
        buttonAddShop.setOnClickListener(buttonAddClickListener);
        buttonCancelAddShop.setOnClickListener(buttonCancelClickListener);
        editShopName = (EditText) findViewById(R.id.edit_name_shop);
    }

    View.OnClickListener buttonAddClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String shopName = editShopName.getText().toString();
            if (!shopName.equals("")) {
                int resultCode = 101;
                Intent resultIntent = new Intent();
                resultIntent.putExtra(TAG, shopName);
                setResult(resultCode, resultIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Введите имя", Toast.LENGTH_SHORT).show();
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
