package storemanager.com.app.fragment;

import java.io.Serializable;
import java.util.ArrayList;

public class AllDataLists implements Serializable {

    private ArrayList<String> menuItemNamesList;
    private ArrayList<String> itemSizeList;
    private ArrayList<String> ingredientNamesList;
    private ArrayList<Integer> ingredientSizeList;
    private ArrayList<String> ingredientMeasureList;

    public ArrayList<String> getMenuItemNamesList() {
        return menuItemNamesList;
    }

    public void setMenuItemNamesList(ArrayList<String> menuItemNamesList) {
        this.menuItemNamesList = menuItemNamesList;
    }

    public ArrayList<String> getItemSizeList() {
        return itemSizeList;
    }

    public void setItemSizeList(ArrayList<String> itemSizeList) {
        this.itemSizeList = itemSizeList;
    }

    public ArrayList<String> getIngredientNamesList() {
        return ingredientNamesList;
    }

    public void setIngredientNamesList(ArrayList<String> ingredientNamesList) {
        this.ingredientNamesList = ingredientNamesList;
    }

    public ArrayList<Integer> getIngredientSizeList() {
        return ingredientSizeList;
    }

    public void setIngredientSizeList(ArrayList<Integer> ingredientSizeList) {
        this.ingredientSizeList = ingredientSizeList;
    }

    public ArrayList<String> getIngredientMeasureList() {
        return ingredientMeasureList;
    }

    public void setIngredientMeasureList(ArrayList<String> ingredientMeasureList) {
        this.ingredientMeasureList = ingredientMeasureList;
    }
}
