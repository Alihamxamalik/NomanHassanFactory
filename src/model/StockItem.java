package model;

public class StockItem {
    public ItemType type;
    public double weight;
    public long itemId;

    public StockItem(ItemType type, double weight, long itemId) {
        this.type = type;
        this.weight = weight;
        this.itemId = itemId;
    }
}
