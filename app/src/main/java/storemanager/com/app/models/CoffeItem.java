package storemanager.com.app.models;

import java.io.Serializable;

public class CoffeItem implements Serializable{

    private boolean oneSize;

    private String name;
    private Integer size;
    private String price;

    public CoffeItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isOneSize() {
        return oneSize;
    }

    public void setOneSize(boolean oneSize) {
        this.oneSize = oneSize;
    }
}
