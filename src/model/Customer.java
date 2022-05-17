package model;

public class Customer {
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

    public Customer(String name, String phone) {

        this.phone = phone;
        this.name = name;
    }

    public Customer(long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public void setId(long id) {
        this.id = id;
    }
}
