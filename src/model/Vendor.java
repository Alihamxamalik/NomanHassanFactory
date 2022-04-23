package model;

public class Vendor {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Vendor(String name) {

        this.name = name;
    }
}
