package storemanager.com.app.models;

import java.io.Serializable;
import java.util.List;

public class BaseItem implements Serializable {
    private String id;

    public List<String> itemData;

    public BaseItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getItemData() {
        return itemData;
    }

    public void setItemData(List<String> itemData) {
        this.itemData = itemData;
    }
}
