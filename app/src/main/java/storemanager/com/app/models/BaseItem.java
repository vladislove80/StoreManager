package storemanager.com.app.models;

import java.util.List;

public class BaseItem {
    private String id;

    public List<?> itemData;

    public BaseItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<?> getItemData() {
        return itemData;
    }

    public void setItemData(List<?> itemData) {
        this.itemData = itemData;
    }
}
