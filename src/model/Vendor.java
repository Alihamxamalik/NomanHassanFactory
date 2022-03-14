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

    public Vendor(long id,String name) {
        this.id = id;
        this.name = name;
    }
}
