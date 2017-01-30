package storemanager.com.app.models;

import java.util.List;

public class Summary {

    private User user;
    private String date;
    private List<CoffeItemInSummary> itemInSummary;

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

    public List<CoffeItemInSummary> getItemInSummary() {
        return itemInSummary;
    }

    public void setItemInSummary(List<CoffeItemInSummary> itemInSummary) {
        this.itemInSummary = itemInSummary;
    }
}
