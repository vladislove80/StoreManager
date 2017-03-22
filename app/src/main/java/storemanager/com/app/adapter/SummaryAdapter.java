package storemanager.com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.MenuItemsInSummary;

public class SummaryAdapter extends ArrayAdapter<MenuItemsInSummary> {

    private Context context;
    private List<MenuItemsInSummary> coffeItem;

    public SummaryAdapter(Context context, List<MenuItemsInSummary> coffeItem) {
        super(context, R.layout.layout_items_row, coffeItem);
        this.context = context;
        this.coffeItem = coffeItem;
    }

    static class ViewHolder {
        protected TextView itemNum;
        protected TextView itemName;
        protected TextView itemSize;
        protected TextView itemAmount;
        protected TextView itemPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_items_row, null);
            viewHolder = new ViewHolder();
            viewHolder.itemNum = (TextView) convertView.findViewById(R.id.item_number_row);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.itemSize = (TextView) convertView.findViewById(R.id.item_size);
            viewHolder.itemAmount = (TextView) convertView.findViewById(R.id.item_amount);
            viewHolder.itemPrice = (TextView) convertView.findViewById(R.id.item_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemNum.setText(String.valueOf(position + 1));
        viewHolder.itemName.setText(coffeItem.get(position).getItem().getName());
        viewHolder.itemAmount.setText(String.valueOf(coffeItem.get(position).getAmount()));
        viewHolder.itemSize.setText(String.valueOf(coffeItem.get(position).getItem().getSize()));
        viewHolder.itemPrice.setText(String.valueOf(coffeItem.get(position).getItem().getPrice() * coffeItem.get(position).getAmount()));

        return convertView;
    }
}
