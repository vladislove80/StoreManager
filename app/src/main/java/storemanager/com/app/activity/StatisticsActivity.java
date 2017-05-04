package storemanager.com.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.adapter.ShopStoreItemStatisticsAdapter;
import storemanager.com.app.fragment.ShopStoreFragment;
import storemanager.com.app.models.Event;

public class StatisticsActivity extends AppCompatActivity{
    public static String TAG = StatisticsActivity.class.getSimpleName();
    public static String INCOMING_LIST = "incoming list";
    public static String CONSUMPTION_LIST = "consumption list";
    public static String BALANCE_LIST = "balance list";
    public static String STATISTICS_TYPE = "statistics type";
    public static String STORE_ITEM_NAME = "store item name";

    private List<Event> statisticsList;
    private String statisticsType;
    private String storeItemName;

    private TextView shopName;
    private TextView storeItem;
    private TextView storeItemBalanceForPeriod;

    private RadioButton checkBoxWeek;
    private RadioButton checkBoxMonth;
    private RadioButton checkBoxAll;

    private ListView listView;
    private ShopStoreItemStatisticsAdapter statisticsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.8));

        statisticsType = getIntent().getStringExtra(STATISTICS_TYPE);
        storeItemName = getIntent().getStringExtra(STORE_ITEM_NAME);
        switch (statisticsType) {
            case "incoming":
                statisticsList = getIntent().getParcelableArrayListExtra(INCOMING_LIST);
                break;
            case "consumption":
                statisticsList = getIntent().getParcelableArrayListExtra(CONSUMPTION_LIST);
                break;
            case "balance":
                statisticsList = getIntent().getParcelableArrayListExtra(BALANCE_LIST);
                break;
        }


        shopName = (TextView) findViewById(R.id.statistics_shop_name);
        shopName.setText(ShopDataActivity.getShopName());
        storeItem = (TextView) findViewById(R.id.statistics_shop_store_item);
        storeItem.setText(storeItemName);
        storeItemBalanceForPeriod = (TextView) findViewById(R.id.statistics_shop_store_item_balance);

        checkBoxWeek = (RadioButton) findViewById(R.id.statistics_week);
        checkBoxMonth = (RadioButton) findViewById(R.id.statistics_week);
        checkBoxAll = (RadioButton) findViewById(R.id.statistics_week);

        listView = (ListView) findViewById(R.id.statistics_listview);
        statisticsAdapter = new ShopStoreItemStatisticsAdapter(getBaseContext(), statisticsList);
        listView.setAdapter(statisticsAdapter);
    }
}
