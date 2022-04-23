package model;

public class GatePassItem {
    public long getId() {
        return id;
    }

    public long getGatepassID() {
        return gatepassID;
    }

    public long getItemIndex() {
        return itemId;
    }

    public double getWeight() {
        return weight;
    }

    public double getBardana() {
        return bardana;
    }

    public double getPrice() {
        return price;
    }

    public GatePassItem(long itemId, double weight, double bardana, double price) {
        this.itemId = itemId;
        this.weight = weight;
        this.bardana = bardana;
        this.price = price;
    }

    private long id,gatepassID,itemId;

    private double weight,bardana,price;
}
