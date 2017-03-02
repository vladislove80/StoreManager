package storemanager.com.app.models;

public class Shop {

    private String name;
    private String creationDate;
    private boolean isSummaryTooday = false;

    public Shop(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isSummaryTooday() {
        return isSummaryTooday;
    }

    public void setSummaryTooday(boolean summaryTooday) {
        isSummaryTooday = summaryTooday;
    }
}
