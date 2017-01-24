package storemanager.com.app.models;

import java.io.Serializable;

public class CoffeItem implements Serializable{

    private boolean oneSize;

    private String name;
    private Integer size;
    private Integer price;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public boolean isOneSize() {
        return oneSize;
    }

    public void setOneSize(boolean oneSize) {
        this.oneSize = oneSize;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        CoffeItem item = (CoffeItem) obj;
        return this.name.equals(item.getName())
                && this.size.equals(item.getSize());
    }
}
