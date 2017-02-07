package storemanager.com.app.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.CoffeItemInSummary;
import storemanager.com.app.models.Summary;

public class SummaryViewerAdapter extends ArrayAdapter<Summary> {

    private Context context;
    private List<Summary> summaryList;

    static class ViewHolder {
        protected TextView shopTextView;
        protected TextView nameTextView;
        protected TextView dateTextView;
        protected TableLayout table;
    }

    public SummaryViewerAdapter(Context context, List<Summary> summaryList) {
        super(context, R.layout.summary_viewer_item, summaryList);
        this.context = context;
        this.summaryList = summaryList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SummaryViewerAdapter.ViewHolder viewHolder;
        Summary summary = summaryList.get(position);
        List<CoffeItemInSummary> itemInSummary = summary.getItemInSummary();
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.summary_viewer_item, null);
            viewHolder = new SummaryViewerAdapter.ViewHolder();
            viewHolder.shopTextView = (TextView) convertView.findViewById(R.id.viewer_shop);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.viewer_name);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.viewer_date);
            viewHolder.table = (TableLayout) convertView.findViewById(R.id.summary_table);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SummaryViewerAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.shopTextView.setText(summary.getShop());
        viewHolder.nameTextView.setText(summary.getUser().getName());
        viewHolder.dateTextView.setText(summary.getDate());
        /*int i = 1;
        for (CoffeItemInSummary item : itemInSummary) {
            final TableRow tableRow = (TableRow) convertView.inflate(context, R.layout.summary_viewer_row, null);
            TextView tv;
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            CoffeItem coffeItem = item.getItem();
            tv = (TextView) tableRow.findViewById(R.id.viewer_number_row);
            tv.setText(String.valueOf(i));
            tv.setLayoutParams(layoutParams);
            tv = (TextView) tableRow.findViewById(R.id.viewer_item_name);
            tv.setText(coffeItem.getName());
            tv.setLayoutParams(layoutParams);
            tv = (TextView) tableRow.findViewById(R.id.viewer_item_amount);
            tv.setText(String.valueOf(item.getAmount()));
            tv.setLayoutParams(layoutParams);
            tv = (TextView) tableRow.findViewById(R.id.viewer_item_size);
            tv.setText(String.valueOf(coffeItem.getSize()));
            tv.setLayoutParams(layoutParams);
            tv = (TextView) tableRow.findViewById(R.id.viewer_item_price);
            tv.setText(String.valueOf(item.getItemsPrice()));
            tv.setLayoutParams(layoutParams);
            viewHolder.table.addView(tableRow);
            i++;
        }*/
        return convertView;
    }
}
