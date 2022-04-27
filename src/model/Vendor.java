package model;

public class Vendor {
    private long id;
    private String name;
    private String phone;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Vendor(String name, String phone) {

        this.phone = phone;
        this.name = name;
    }

    public Vendor(long id, String name,String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}
