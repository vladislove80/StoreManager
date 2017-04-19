package storemanager.com.app.models;

import java.util.List;

public class StoreItem {
    private String name;
    private String Measure;
    private List<Event> listEvents;
    private Event lastComingIn;
    private Event lastConsumption;

    public StoreItem() {
    }

    public StoreItem(String name, String measure, List<Event> listEvents) {
        this.name = name;
        Measure = measure;
        this.listEvents = listEvents;
        setLastConsumption();
        setLastComingIn();
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

    public void setLastComingIn() {
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
            lastComingIn.setDate("***");
        }
        this.lastComingIn = lastComingIn;
    }

    public Event getLastConsumption() {
        return lastConsumption;
    }

    public void setLastConsumption() {
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
            lastConsumption.setDate("***");
        }
        this.lastConsumption = lastConsumption;
    }

    public String getMeasure() {
        return Measure;
    }

    public void setMeasure(String measure) {
        Measure = measure;
    }
}