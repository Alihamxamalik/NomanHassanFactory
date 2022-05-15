package model;

public class ProductionItem {
    private long id,itemId,productionId;
    private double weight;

    public long getId() {
        return id;
    }

    public long getItemId() {
        return itemId;
    }

    public long getProductionId() {
        return productionId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public ProductionItem(long itemId, double weight) {
        this.itemId = itemId;
        this.weight = weight;
    }

    public ProductionItem(long itemId, long productionId, double weight) {
        this.itemId = itemId;
        this.productionId = productionId;
        this.weight = weight;
    }

    public ProductionItem(long id, long itemId, long productionId, double weight) {
        this.id = id;
        this.itemId = itemId;
        this.productionId = productionId;
        this.weight = weight;
    }
}
