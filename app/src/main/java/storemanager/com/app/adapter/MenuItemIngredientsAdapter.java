package storemanager.com.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.Ingredient;

public class MenuItemIngredientsAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private List<Ingredient> ingredientList;

    public MenuItemIngredientsAdapter(Context context, List<Ingredient> ingredientList) {
        super(context, R.layout.layout_base_ingredient_item_row, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }

    static class ViewHolder {
        protected TextView ingredientName;
        protected TextView ingredientSize;
        protected TextView ingredientMeasure;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_base_ingredient_item_row, null);
            viewHolder = new ViewHolder();
            viewHolder.ingredientName = (TextView) convertView.findViewById(R.id.ingredient_name);
            viewHolder.ingredientSize = (TextView) convertView.findViewById(R.id.base_ingredient_size);
            viewHolder.ingredientMeasure = (TextView) convertView.findViewById(R.id.base_ingredient_measure);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ingredientName.setText(ingredientList.get(position).getName());
        viewHolder.ingredientSize.setText(Integer.toString(ingredientList.get(position).getSize()));
        viewHolder.ingredientMeasure.setText(ingredientList.get(position).getMeasure());
        return convertView;
    }
}
