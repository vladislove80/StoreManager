package storemanager.com.app.models;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private int size;
    private String measure;

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
}
