package model;

public class RawMaterial {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RawMaterial(String name) {
        this.name = name;
    }
}
