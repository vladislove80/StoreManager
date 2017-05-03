package storemanager.com.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.utils.Utils;

public class StoreItem implements Parcelable, Comparable {
    private String name;
    private String measure;
    private Event lastComingIn;
    private Event lastConsumption;
    private List<Event> listEvents;
    private ArrayList<Event> balanceListEvents = new ArrayList<>();
    private Event balance = new Event(Utils.getCurrentDateWithoutTime(), 0);

    public StoreItem() {
    }

    public StoreItem(String name, String measure) {
        setBalance(new Event(Utils.getCurrentDateWithoutTime(), 0));
        this.name = name;
        this.measure = measure;
        this.lastConsumption = new Event(Utils.getCurrentDateWithoutTime(), 0);
        this.lastComingIn = new Event(Utils.getCurrentDateWithoutTime(), 0);
        this.listEvents = new ArrayList<>();
        this.balanceListEvents = new ArrayList<>();
    }

    public StoreItem(String name, String measure, List<Event> listEvents) {
        this.balance = new Event(Utils.getCurrentDateWithoutTime(), 0);
        this.name = name;
        this.measure = measure;
        this.listEvents = listEvents;
        setLastConsumptionFromList();
        setLastComingInFromList();
    }

    protected StoreItem(Parcel in) {
        name = in.readString();
        measure = in.readString();
        listEvents = new ArrayList<Event>();
        in.readTypedList(listEvents, Event.CREATOR);
        lastComingIn = (Event) in.readValue(Event.class.getClassLoader());
        lastConsumption = (Event) in.readValue(Event.class.getClassLoader());
        balanceListEvents = new ArrayList<>();
        in.readTypedList(balanceListEvents, Event.CREATOR);
        balance = (Event) in.readValue(Event.class.getClassLoader());

    }

    public static final Creator<StoreItem> CREATOR = new Creator<StoreItem>() {
        @Override
        public StoreItem createFromParcel(Parcel in) {
            return new StoreItem(in);
        }

        @Override
        public StoreItem[] newArray(int size) {
            return new StoreItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getListEvents() {
        return listEvents;
    }

    public void setListEvents(List<Event> listEvents) {
        this.listEvents = listEvents;
    }

    public Event getLastComingIn() {
        return lastComingIn;
    }

    public void setLastComingInFromList() {
        Event lastComingIn = new Event();
        lastComingIn.setAmount(0);
        int i = listEvents.size() - 1;
        while (lastComingIn.getAmount() == 0 || i > 0) {
            if (listEvents.get(i).getAmount() > 0){
                lastComingIn = listEvents.get(i);
            }
            i--;
        }
        if (i < 0) {
            lastComingIn.setDate(Utils.getCurrentDateWithoutTime());
        }
        this.lastComingIn = lastComingIn;
    }

    public Event getLastConsumption() {
        return lastConsumption;
    }

    public void setLastConsumptionFromList() {
        Event lastConsumption = new Event();
        lastConsumption.setAmount(0);
        int i = listEvents.size() - 1;
        while (lastConsumption.getAmount() == 0 || i > 0) {
            if (listEvents.get(i).getAmount() < 0){
                lastConsumption = listEvents.get(i);
            }
            i--;
        }
        if (i < 0) {
            lastConsumption.setDate(Utils.getCurrentDateWithoutTime());
        }
        this.lastConsumption = lastConsumption;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setLastComingIn(Event lastComingIn) {
        this.lastComingIn = lastComingIn;
    }

    public void addLastCommingIn(Event lastComingIn) {
        listEvents.add(lastComingIn);
        setBalance(lastComingIn);
        Event newBalanceEvent = new Event(balance.getDate(), balance.getAmount());
        balanceListEvents.add(newBalanceEvent);
        this.lastComingIn = lastComingIn;
    }

    public void setLastConsumption(Event lastConsumption) {
        this.lastConsumption = lastConsumption;
    }

    public void addLastConsumption(Event lastConsumption) {
        listEvents.add(lastConsumption);
        setBalance(lastConsumption);
        Event newBalanceEvent = new Event(balance.getDate(), balance.getAmount());
        balanceListEvents.add(newBalanceEvent);
        this.lastConsumption = lastConsumption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(measure);
        dest.writeTypedList(listEvents);
        dest.writeValue(lastComingIn);
        dest.writeValue(lastConsumption);
        dest.writeTypedList(balanceListEvents);
        dest.writeValue(balance);
    }

    @Override
    public boolean equals(Object obj) {
        boolean same = false;
        StoreItem item = (StoreItem) obj;
        if (obj != null && obj instanceof StoreItem) {
            same = this.name.equals(item.getName());
        }
        return same;
    }

    public Event getBalance() {
        return balance;
    }

    public void setBalance(Event event) {
        this.balance.setAmount(balance.getAmount() + event.getAmount());
        this.balance.setDate(event.getDate());
    }

    public ArrayList<Event> getBalanceListEvents() {
        return balanceListEvents;
    }

    public void setBalanceListEvents(ArrayList<Event> balanceListEvents) {
        this.balanceListEvents = balanceListEvents;
    }

    @Override
    public int compareTo(@NonNull Object o) {

        return name.compareTo(((StoreItem) o).getName());
    }
}
