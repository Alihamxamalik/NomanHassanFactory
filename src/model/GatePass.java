package model;

public class GatePass {
    private long id,vendorIndex;
    private String data;

    public long getId() {
        return id;
    }

    public long getVendorIndex() {
        return vendorIndex;
    }

    public String getData() {
        return data;
    }

    public GatePass(long vendorIndex, String data) {
        this.vendorIndex = vendorIndex;
        this.data = data;
    }
}
