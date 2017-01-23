package storemanager.com.app.models;

import java.io.Serializable;

public class CoffeItemsToAddInSummary implements Serializable {
    private CoffeItem item;
    private int amount;

    public CoffeItemsToAddInSummary() {}

    public CoffeItemsToAddInSummary(CoffeItem item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public CoffeItem getItem() {
        return item;
    }

    public void setItem(CoffeItem item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
