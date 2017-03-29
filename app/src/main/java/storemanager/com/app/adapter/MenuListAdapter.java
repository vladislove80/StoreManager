package storemanager.com.app.adapter;

import android.content.Context;
import android.graphics.Color;
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
import storemanager.com.app.utils.RecyclerItemClickListener;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    private Context context;
    private List<MenuItem> mDataset;
    private RecyclerItemClickListener listener;
    public static int selectedPos = -1;
    private MenuItem menuItem;

    public MenuListAdapter(Context context, List<MenuItem> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    public MenuListAdapter(Context context, List<MenuItem> myDataset, RecyclerItemClickListener listener) {
        this.context = context;
        mDataset = myDataset;
        this.listener = listener;
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
        if(menuItem.isSelected()){
            holder.cardView.setBackgroundColor(Color.LTGRAY);
        }else{
            holder.cardView.setBackgroundColor(Color.WHITE);
        }
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
            if (listener != null) {
                listener.onRecyclerItemClick(position);
                if (selectedPos != -1) {
                    menuItem = mDataset.get(selectedPos);
                    menuItem.setSelected(!menuItem.isSelected());
                }
                selectedPos = position;
                menuItem = mDataset.get(position);
                menuItem.setSelected(!menuItem.isSelected());
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "onRecyclerItemClick in pos " + position, Toast.LENGTH_SHORT).show();
            }
        }

        public void setItem(int position) {
            this.position = position;
        }
    }
}
