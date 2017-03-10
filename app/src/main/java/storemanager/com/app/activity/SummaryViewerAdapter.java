package storemanager.com.app.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.CoffeItemInSummary;
import storemanager.com.app.models.Summary;

import static android.view.View.inflate;

public class SummaryViewerAdapter extends ArrayAdapter<Summary> implements View.OnClickListener {

    private Context context;
    private List<Summary> summaryList;
    private TableLayout table;

    class ViewHolder {
        TextView shopTextView;
        TextView nameTextView;
        TextView dateTextView;
        LinearLayout summaryContainer;

        void setOnClickListener(View.OnClickListener listener){
            summaryContainer.setOnClickListener(listener);
        }
    }

    public SummaryViewerAdapter(Context context, List<Summary> summaryList) {
        super(context, R.layout.layout_summary_viewer_item, summaryList);
        this.context = context;
        this.summaryList = summaryList;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        table = (TableLayout) v.findViewById(R.id.summary_table);
        if (table.getVisibility() == View.GONE) {
            table.setVisibility(View.VISIBLE);
            if (table.getChildAt(0) == null) {
                Summary summary = summaryList.get(position);
                List<CoffeItemInSummary> itemInSummary = summary.getItemInSummary();
                int i = 1;
                for (CoffeItemInSummary item : itemInSummary) {
                    final TableRow tableRow = (TableRow) inflate(context, R.layout.layout_summary_viewer_row, null);
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
                    table.addView(tableRow);
                    i++;
                }
            }

        } else {
            table.setVisibility(View.GONE);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SummaryViewerAdapter.ViewHolder viewHolder;
        Summary summary = summaryList.get(position);
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_summary_viewer_item, null);
            viewHolder = new SummaryViewerAdapter.ViewHolder();
            viewHolder.shopTextView = (TextView) convertView.findViewById(R.id.viewer_shop);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.viewer_name);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.viewer_date);
            viewHolder.shopTextView.setText(summary.getShop());
            viewHolder.nameTextView.setText(summary.getUser().getName());
            viewHolder.dateTextView.setText(summary.getDate());
            viewHolder.summaryContainer = (LinearLayout) convertView.findViewById(R.id.ll_summary_container);
            viewHolder.summaryContainer.setTag(position);
            viewHolder.setOnClickListener(this);
        }
        return convertView;
    }
}
