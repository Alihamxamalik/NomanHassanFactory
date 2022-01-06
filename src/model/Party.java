package model;

public class Party {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Party(String name) {
        this.name = name;
    }
}
