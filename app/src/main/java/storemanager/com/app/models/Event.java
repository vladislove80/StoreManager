package storemanager.com.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable{
    private String date;
    private float amount;

    public Event() {
    }

    public Event(String date, float amount) {
        this.date = date;
        this.amount = amount;
    }

    protected Event(Parcel in) {
        date = in.readString();
        amount = in.readInt();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeFloat(amount);
    }
}
