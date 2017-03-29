package storemanager.com.app.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.Ingredient;
import storemanager.com.app.models.MenuItem;

public class FragmentMenuListAdapter extends RecyclerView.Adapter<FragmentMenuListAdapter.ViewHolder> {

    private List<MenuItem> mDataset;
    private MenuItem menuItem;
    private View.OnLongClickListener onLongClickListener;


    public FragmentMenuListAdapter(List<MenuItem> mDataset, View.OnLongClickListener onLongClickListener) {
        this.mDataset = mDataset;
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cardView.setTag(position);
        menuItem = mDataset.get(position);
        holder.itemNameTextView.setText(menuItem.getName());
        holder.itemSizeTextView.setText("Объем: " + Integer.toString(menuItem.getSize()));
        holder.itemPriceTextView.setText("Цена: " + Integer.toString(menuItem.getPrice()));
        String allIngredients = "";
        for (Ingredient ingredientItem : menuItem.getConsist()) {
            allIngredients = allIngredients
                    + ingredientItem.getName() + " "
                    + ingredientItem.getSize() + " "
                    + ingredientItem.getMeasure() + "\n";
        }
        allIngredients = allIngredients.trim();
        holder.itemIngredientsTextView.setText(allIngredients);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView;
        public TextView itemSizeTextView;
        public TextView itemPriceTextView;
        private CardView cardView;
        public TextView itemIngredientLabelTextView;
        public TextView itemIngredientsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.menu_item_card_view);
            itemNameTextView = (TextView) itemView.findViewById(R.id.menu_item_name);
            itemSizeTextView = (TextView) itemView.findViewById(R.id.menu_item_size);
            itemPriceTextView = (TextView) itemView.findViewById(R.id.menu_item_price);
            itemIngredientLabelTextView = (TextView) itemView.findViewById(R.id.menu_item_ingredient_label);
            itemIngredientsTextView = (TextView) itemView.findViewById(R.id.menu_item_ingredients);
            cardView.setOnLongClickListener(onLongClickListener);
        }
    }
}
