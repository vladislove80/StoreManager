package storemanager.com.app.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.Ingredient;

/**
 * Created by PlayWeb-15 on 06.03.2017.
 */

public class MenuIngredientAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private List<Ingredient> ingredientList;

    public MenuIngredientAdapter(Context context, List<Ingredient> ingredientList) {
        super(context, R.layout.ingredient_item_row, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }
}
