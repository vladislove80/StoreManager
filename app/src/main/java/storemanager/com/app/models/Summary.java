package storemanager.com.app.models;

import java.util.List;

public class Summary {

    private User user;
    private String date;
    private String shop;
    private List<MenuItemsInSummary> itemInSummary;

    public Summary() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MenuItemsInSummary> getItemInSummary() {
        return itemInSummary;
    }

    public void setItemInSummary(List<MenuItemsInSummary> itemInSummary) {
        this.itemInSummary = itemInSummary;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }
}
