package storemanager.com.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.ShopDataActivity;
import storemanager.com.app.models.Shop;

public class ShopsFragmentAdapter extends RecyclerView.Adapter<ShopsFragmentAdapter.ViewHolder> {

    private static Context context;
    private List<Shop> mDataset;

    public ShopsFragmentAdapter(Context context, List<Shop> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public ShopsFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shop shop = mDataset.get(position);
        String shopName = shop.getName();
        holder.setItem(shopName);
        //holder.mNameTextView.setText(shopName);
        holder.mDateTextView.setText(shop.getCreationDate());
        holder.mIndicatorImageView.setImageResource((shop.isSummaryTooday())? R.drawable.ic_check_box_black_48dp : R.drawable.ic_indeterminate_check_box_black_48dp);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String mShopName;
        public TextView mNameTextView;
        public TextView mDateTextView;
        public ImageView mIndicatorImageView;
        private CardView cardView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
            mNameTextView = (TextView) v.findViewById(R.id.shop_name);
            mDateTextView = (TextView) v.findViewById(R.id.shop_create_date);
            mIndicatorImageView = (ImageView) v.findViewById(R.id.summary_indicator);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ShopDataActivity.class);
            intent.putExtra(ShopDataActivity.TAG, mShopName);
            context.startActivity(intent);
        }

        public void setItem(String mShopName) {
            this.mShopName = mShopName;
            mNameTextView.setText(mShopName);
        }
    }

}
