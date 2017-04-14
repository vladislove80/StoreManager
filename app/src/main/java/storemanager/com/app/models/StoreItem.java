package storemanager.com.app.models;

import java.util.List;

public class StoreItem {
    private String name;
    private List<Event> listEvents;
    private Event lastIn;
    private Event lastOut;

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

    public Event getLastIn() {
        return lastIn;
    }

    public void setLastIn(Event lastIn) {
        this.lastIn = lastIn;
    }

    public Event getLastOut() {
        return lastOut;
    }

    public void setLastOut(Event lastOut) {
        this.lastOut = lastOut;
    }
}
