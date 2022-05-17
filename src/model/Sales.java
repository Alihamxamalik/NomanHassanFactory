package model;

public class Sales {
    private long id, customerId;
    private String date;

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getDate() {
        return date;
    }

    public Sales(long vendorId, String date) {
        this.customerId = vendorId;
        this.date = date;
    }
    public Sales(long id, long vendorId, String date) {
        this.id = id;
        this.customerId = vendorId;
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }
}
