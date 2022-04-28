package model;

public class GatePass {
    private long id,vendorId;
    private String date;

    public long getId() {
        return id;
    }

    public long getVendorId() {
        return vendorId;
    }

    public String getDate() {
        return date;
    }

    public GatePass(long vendorId, String date) {
        this.vendorId = vendorId;
        this.date = date;
    }
    public GatePass(long id,long vendorId, String date) {
        this.id = id;
        this.vendorId = vendorId;
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }
}
