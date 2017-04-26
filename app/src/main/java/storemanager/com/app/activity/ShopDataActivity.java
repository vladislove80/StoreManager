package storemanager.com.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import storemanager.com.app.R;
import storemanager.com.app.adapter.ShopDataPanelFragmentPagerAdapter;
import storemanager.com.app.adapter.ShopStoreManager;

public class ShopDataActivity extends AppCompatActivity {
    public final static String TAG = ShopDataActivity.class.getSimpleName();

    private static String shopName;
    private ViewPager shopDataActivityViewPager;

    private ShopStoreManager shopStoreManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_data);
        Intent intent = getIntent();
        setShopName(intent.getStringExtra(TAG));
        setTitle(shopName);
        shopStoreManager = new ShopStoreManager(shopName);
        setViewPager();
    }

    private void setViewPager() {
        shopDataActivityViewPager = (ViewPager) findViewById(R.id.viewpager_shop_data);
        shopDataActivityViewPager.setAdapter(new ShopDataPanelFragmentPagerAdapter(getSupportFragmentManager(),
                ShopDataActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_shop_data);
        tabLayout.setupWithViewPager(shopDataActivityViewPager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ShopStoreManager getShopStoreManager() {
        return shopStoreManager;
    }

    public void setShopStoreManager(ShopStoreManager shopStoreManager) {
        this.shopStoreManager = shopStoreManager;
    }
}
