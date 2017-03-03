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
import storemanager.com.app.activity.ShopActivity;
import storemanager.com.app.models.Shop;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder> {

    private static Context context;
    private List<Shop> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            Intent intent = new Intent(context, ShopActivity.class);
            intent.putExtra(ShopActivity.TAG, mShopName);
            context.startActivity(intent);
        }

        public void setItem(String mShopName) {
            this.mShopName = mShopName;
            mNameTextView.setText(mShopName);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShopsAdapter(Context context, List<Shop> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShopsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Shop shop = mDataset.get(position);
        String shopName = shop.getName();
        holder.setItem(shopName);
        //holder.mNameTextView.setText(shopName);
        holder.mDateTextView.setText(shop.getCreationDate());
        holder.mIndicatorImageView.setImageResource((shop.isSummaryTooday())? R.drawable.ic_check_box_black_48dp : R.drawable.ic_indeterminate_check_box_black_48dp);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
