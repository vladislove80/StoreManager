package storemanager.com.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuItemsInSummary implements Parcelable {
    private MenuItem item;
    private int amount;
    private int itemsPrice;

    public MenuItemsInSummary() {}

    public MenuItemsInSummary(MenuItem item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    protected MenuItemsInSummary(Parcel in) {
        item = in.readParcelable(MenuItem.class.getClassLoader());
        amount = in.readInt();
        itemsPrice = in.readInt();
    }

    public static final Creator<MenuItemsInSummary> CREATOR = new Creator<MenuItemsInSummary>() {
        @Override
        public MenuItemsInSummary createFromParcel(Parcel in) {
            return new MenuItemsInSummary(in);
        }

        @Override
        public MenuItemsInSummary[] newArray(int size) {
            return new MenuItemsInSummary[size];
        }
    };

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getItemsPrice() {
        int price = item.getPrice();
        if (amount != 0 && price != 0) {
            return amount*price;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        MenuItemsInSummary items = (MenuItemsInSummary) obj;
        return this.item.equals(items.getItem());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(item, flags);
        dest.writeInt(amount);
        dest.writeInt(itemsPrice);
    }
}
