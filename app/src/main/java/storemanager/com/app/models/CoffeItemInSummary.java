package storemanager.com.app.models;

import java.io.Serializable;

public class CoffeItemInSummary implements Serializable {
    private MenuItem item;
    private int amount;
    private int itemsPrice;

    public CoffeItemInSummary() {}

    public CoffeItemInSummary(MenuItem item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getItemsPrice() {
        int price = item.getPrice();
        if (amount != 0 && price != 0) {
            return amount*price;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        CoffeItemInSummary items = (CoffeItemInSummary) obj;
        return this.item.equals(items.getItem());
    }
}
