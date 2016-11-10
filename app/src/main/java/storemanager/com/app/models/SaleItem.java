package storemanager.com.app.models;

import java.util.HashMap;

public class SaleItem {

    private String name;
    private int price;
    private HashMap consist;

    public SaleItem(){}

    public SaleItem(String name, int price, HashMap consist){
        this.name = name;
        this.price = price;
        this.consist = consist;
    }

}
