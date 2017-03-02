package storemanager.com.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.Shop;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder> {

    private List<Shop> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNameTextView;
        public TextView mDateTextView;
        public ImageView mIndicatorImageView;
        public ViewHolder(View v) {
            super(v);
            mNameTextView = (TextView) v.findViewById(R.id.shop_name);
            mDateTextView = (TextView) v.findViewById(R.id.shop_create_date);
            mIndicatorImageView = (ImageView) v.findViewById(R.id.summary_indicator);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShopsAdapter(List<Shop> myDataset) {
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
        holder.mNameTextView.setText(shop.getName());
        holder.mDateTextView.setText(shop.getCreationDate());
        holder.mIndicatorImageView.setImageResource((shop.isSummaryTooday())? R.drawable.ic_check_box_black_48dp : R.drawable.ic_indeterminate_check_box_black_48dp);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
