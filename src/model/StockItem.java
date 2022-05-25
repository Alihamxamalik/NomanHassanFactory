package model;

public class StockItem {
    public StockItem(ItemType type, double weight, long itemId) {
        this.type = type;
        this.weight = weight;
        this.itemId = itemId;
    }

    public ItemType type;
    public double weight;
    public long itemId;

    public ItemType getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public long getItemId() {
        return itemId;
    }

    public StockItem(double weight, long itemId) {
        this.weight = weight;
        this.itemId = itemId;
    }
}
