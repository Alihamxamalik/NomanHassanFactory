package model;

public class Worker {
    private long id;

    public Worker(String name,String phone) {
    this.name = name;
    this.phone = phone;
    }
    public Worker(long id,String name,String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
    public Worker(String name) {
        this.name = name;
    }

    private String name;

    public long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    private String phone;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
