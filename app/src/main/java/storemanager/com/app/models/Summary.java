package storemanager.com.app.models;

import java.util.List;

public class Summary {

    private User user;
    List<CoffeItemInSummary> itemInSummary;

    public Summary() {}

    public Summary(User user, List<CoffeItemInSummary> itemInSummary) {
        this.user = user;
        this.itemInSummary = itemInSummary;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CoffeItemInSummary> getItemInSummary() {
        return itemInSummary;
    }

    public void setItemInSummary(List<CoffeItemInSummary> itemInSummary) {
        this.itemInSummary = itemInSummary;
    }
}
