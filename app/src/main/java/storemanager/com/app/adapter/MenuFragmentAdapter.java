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
import storemanager.com.app.models.CoffeItem;
import storemanager.com.app.models.Ingredient;

public class MenuFragmentAdapter extends RecyclerView.Adapter<MenuFragmentAdapter.ViewHolder> {

    private Context context;
    private List<CoffeItem> mDataset;

    public MenuFragmentAdapter(Context context, List<CoffeItem> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(position);
        CoffeItem coffeItem = mDataset.get(position);
        holder.itemNameTextView.setText(coffeItem.getName());
        holder.itemSizeTextView.setText("Объем: " + Integer.toString(coffeItem.getSize()));
        holder.itemPriceTextView.setText("Цена: " + Integer.toString(coffeItem.getPrice()));
        String allIngredients = "";
        for (Ingredient ingredientItem : coffeItem.getConsist()) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int position;
        public TextView itemNameTextView;
        public TextView itemSizeTextView;
        public TextView itemPriceTextView;
        private CardView cardView;
        public TextView itemIngredientLabelTextView;
        public TextView itemIngredientsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.menu_item_card_view);
            cardView.setOnClickListener(this);
            itemNameTextView = (TextView) itemView.findViewById(R.id.menu_item_name);
            itemSizeTextView = (TextView) itemView.findViewById(R.id.menu_item_size);
            itemPriceTextView = (TextView) itemView.findViewById(R.id.menu_item_price);
            itemIngredientLabelTextView = (TextView) itemView.findViewById(R.id.menu_item_ingredient_label);
            itemIngredientsTextView = (TextView) itemView.findViewById(R.id.menu_item_ingredients);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "onClick in pos " + position, Toast.LENGTH_SHORT).show();
        }

        public void setItem(int position) {
            this.position = position;
        }
    }
}
