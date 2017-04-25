package storemanager.com.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.utils.Utils;

public class StoreItem implements Parcelable {
    private String name;
    private String measure;
    private List<Event> listEvents;
    private Event lastComingIn;
    private Event lastConsumption;

    public StoreItem() {
    }

    public StoreItem(String name, String measure) {
        this.name = name;
        this.measure = measure;
        this.lastConsumption = new Event(Utils.getCurrentDateWithoutTime(), 0);
        this.lastComingIn = new Event(Utils.getCurrentDateWithoutTime(), 0);
        this.listEvents = new ArrayList<>();
    }

    public StoreItem(String name, String measure, List<Event> listEvents) {
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
        this.lastComingIn = lastComingIn;
    }

    public void setLastConsumption(Event lastConsumption) {
        this.lastConsumption = lastConsumption;
    }

    public void addLastConsumption(Event lastComingIn) {
        listEvents.add(lastConsumption);
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
}
