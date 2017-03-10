package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import storemanager.com.app.R;

public class ShopDataActivity extends AppCompatActivity {
    public final static String TAG = ShopDataActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent = getIntent();
        String shopName = intent.getStringExtra(TAG);
        TextView textView = (TextView) findViewById(R.id.shop_name);
        textView.setText(shopName);
    }
}
