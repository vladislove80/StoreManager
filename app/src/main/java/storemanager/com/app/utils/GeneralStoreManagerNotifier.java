package storemanager.com.app.utils;

import java.util.List;

import storemanager.com.app.models.StoreItem;

public interface GeneralStoreManagerNotifier {
    void onDownLoaded(List<StoreItem> dataset);
}
