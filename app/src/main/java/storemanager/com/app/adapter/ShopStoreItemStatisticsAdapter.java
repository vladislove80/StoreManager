package storemanager.com.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.Event;

public class ShopStoreItemStatisticsAdapter extends BaseAdapter{

    private Context context;
    private List<Event> itemEventList;
    LayoutInflater lInflater;

    public ShopStoreItemStatisticsAdapter (Context context, List<Event> itemEventList){
        this.context = context;
        this.itemEventList = itemEventList;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemEventList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemEventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.layout_shop_store_item_statistics, parent, false);
        }
        Event event = itemEventList.get(position);
        ((TextView) view.findViewById(R.id.store_item_statistics_date)).setText(event.getDate());
        ((TextView) view.findViewById(R.id.store_item_statistics_amount)).setText(String.valueOf(event.getAmount()));

        return view;
    }
}
