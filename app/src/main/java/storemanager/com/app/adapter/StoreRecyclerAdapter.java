package storemanager.com.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.Event;
import storemanager.com.app.models.StoreItem;
import storemanager.com.app.utils.Utils;

public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.ViewHolder>{

    private List<StoreItem> mDataset;
    private View.OnClickListener mItemListener;

    public StoreRecyclerAdapter(List<StoreItem> mDataset, View.OnClickListener itemListener){
        this.mDataset = mDataset;
        this.mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shop_store, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPosition(position);
        StoreItem storeItem = mDataset.get(position);
        holder.itemNameTextView.setText(storeItem.getName() + ",");
        holder.itemMeasureTextView.setText(storeItem.getMeasure());
        Event consumption = storeItem.getLastConsumption();
        holder.itemConsumptionNumTextView.setText(String.valueOf(consumption.getAmount()));
        holder.itemConsumptionDateTextView.setText(consumption.getDate());
        Event comingIn = storeItem.getLastComingIn();
        holder.itemIncomingNumTextView.setText(String.valueOf(comingIn.getAmount()));
        holder.itemIncomingDateView.setText(comingIn.getDate());
        Event balance = storeItem.getBalance();
        holder.itemBalanceNumTextView.setText(String.valueOf(balance.getAmount()));
        holder.itemBalanceDateTextView.setText(balance.getDate());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemNameTextView;
        public TextView itemMeasureTextView;
        public TextView itemBalanceNumTextView;
        public TextView itemConsumptionNumTextView;
        public TextView itemIncomingNumTextView;
        public TextView itemBalanceDateTextView;
        public TextView itemConsumptionDateTextView;
        public TextView itemIncomingDateView;
        public ImageButton changeItemAmountImageButton;
        public Button itemBalanceStatButton;
        public Button itemConsumptionStatButton;
        public Button itemIncomingStatButton;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = (TextView) itemView.findViewById(R.id.store_item_name);
            itemMeasureTextView = (TextView) itemView.findViewById(R.id.store_item_measure);
            itemBalanceNumTextView = (TextView) itemView.findViewById(R.id.store_item_balance_num);
            itemConsumptionNumTextView = (TextView) itemView.findViewById(R.id.store_item_consumption_num);
            itemIncomingNumTextView = (TextView) itemView.findViewById(R.id.store_item_incoming_num);
            itemBalanceDateTextView = (TextView) itemView.findViewById(R.id.store_item_balance_date);
            itemConsumptionDateTextView = (TextView) itemView.findViewById(R.id.store_item_consumption_num_date);
            itemIncomingDateView = (TextView) itemView.findViewById(R.id.store_item_incoming_num_date);
            changeItemAmountImageButton = (ImageButton) itemView.findViewById(R.id.change_store_item_amount_button);
            itemBalanceStatButton = (Button) itemView.findViewById(R.id.store_item_balance_stat);
            itemConsumptionStatButton = (Button) itemView.findViewById(R.id.store_item_consumption_num_stat);
            itemIncomingStatButton = (Button) itemView.findViewById(R.id.store_item_incoming_num_stat);
            changeItemAmountImageButton.setOnClickListener(mItemListener);
            itemBalanceStatButton.setOnClickListener(mItemListener);
            itemIncomingStatButton.setOnClickListener(mItemListener);
            itemConsumptionStatButton.setOnClickListener(mItemListener);
        }

        public void setPosition(int position) {
            changeItemAmountImageButton.setTag(position);
            itemBalanceStatButton.setTag(position);
            itemIncomingStatButton.setTag(position);
            itemConsumptionStatButton.setTag(position);
        }
    }
}
