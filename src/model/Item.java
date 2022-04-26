package model;

public class Item {
    private long id;
    private String name;
    private boolean assemble;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAssemble() {
        return assemble;
    }

    public Item(String name, boolean assemble) {
        this.name = name;
        this.assemble = assemble;
    }
    public Item(long id,String name, boolean assemble) {
        this.id = id;
        this.name = name;
        this.assemble = assemble;
    }
}
