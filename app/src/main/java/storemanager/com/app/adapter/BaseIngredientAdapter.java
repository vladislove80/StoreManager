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

public class BaseIngredientAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> ingredientBaseList;

    public BaseIngredientAdapter(Context context, List<String> ingredientBaseList) {
        super(context, R.layout.base_ingredient_item_row, ingredientBaseList);
        this.context = context;
        this.ingredientBaseList = ingredientBaseList;
    }

    static class ViewHolder {
        protected TextView baseIngredient;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.base_ingredient_item_row, null);
            viewHolder = new ViewHolder();
            viewHolder.baseIngredient = (TextView) convertView.findViewById(R.id.base_ingredient_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.baseIngredient.setText(ingredientBaseList.get(position));
        return convertView;
    }
}
