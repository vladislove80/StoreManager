package storemanager.com.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Ingredient implements Parcelable, Serializable {
    private String name;
    private int size;
    private String measure;

    public Ingredient() {
    }

    protected Ingredient(Parcel in) {
        name = in.readString();
        size = in.readInt();
        measure = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
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

    public void setSize(int size) {
        this.size = size;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public boolean equals(Object obj) {
        boolean same = false;
        Ingredient ingredient = (Ingredient) obj;
        if (obj != null && obj instanceof Ingredient) {
            same = this.name.equals(ingredient.getName());
        }
        return same;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(size);
        dest.writeString(measure);
    }
}
