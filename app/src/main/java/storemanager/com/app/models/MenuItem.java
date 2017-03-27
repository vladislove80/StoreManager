package storemanager.com.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItem implements Parcelable, Serializable {

    private boolean oneSize;

    private String name;
    private ArrayList<Ingredient> consist;
    private int size;
    private int price;

    public MenuItem() {
    }

    protected MenuItem(Parcel in) {
        oneSize = in.readByte() != 0;
        name = in.readString();
        size = in.readInt();
        price = in.readInt();
        consist = new ArrayList<>();
        in.readTypedList(consist, Ingredient.CREATOR);
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public int getPrice() {
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

    public List<Ingredient> getConsist() {
        return consist;
    }

    public void setConsist(ArrayList<Ingredient> consist) {
        this.consist = consist;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        MenuItem item = (MenuItem) obj;
        return this.name.equals(item.getName()) && this.size == item.getSize();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (oneSize ? 1 : 0));
        dest.writeString(name);
        dest.writeInt(size);
        dest.writeInt(price);
        dest.writeTypedList(consist);
    }
}
