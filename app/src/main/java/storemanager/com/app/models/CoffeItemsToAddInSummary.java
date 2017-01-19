package storemanager.com.app.models;

import java.io.Serializable;

public class CoffeItemsToAddInSummary implements Serializable {
    private CoffeItem item;
    private int number;

    public CoffeItemsToAddInSummary(CoffeItem item, int number) {
        this.item = item;
        this.number = number;
    }

    public CoffeItem getItem() {
        return item;
    }

    public void setItem(CoffeItem item) {
        this.item = item;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
