package storemanager.com.app.models;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.utils.Utils;

public class StoreItem {
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
        this.listEvents = new ArrayList<>();
        this.lastComingIn = new Event(Utils.getCurrentDateWithoutTime(), 0);
        this.lastConsumption = new Event(Utils.getCurrentDateWithoutTime(), 0);
        listEvents.add(lastComingIn);
        listEvents.add(lastConsumption);
    }

    public StoreItem(String name, String measure, List<Event> listEvents) {
        this.name = name;
        this.measure = measure;
        this.listEvents = listEvents;
        setLastConsumptionFromList();
        setLastComingInFromList();
    }

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
        listEvents.add(lastComingIn);
        this.lastComingIn = lastComingIn;
    }

    public void setLastConsumption(Event lastConsumption) {
        listEvents.add(lastConsumption);
        this.lastConsumption = lastConsumption;
    }
}
