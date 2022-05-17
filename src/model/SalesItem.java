package model;

public class SalesItem {
    public long getId() {
        return id;
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

    public double getTotalPrice() {
        if(price>0) {
            if (isIn1KG)
                totalPrice = price * getNetWeight();
            else
                totalPrice = (price / 37.324) * getNetWeight();
        }else
            totalPrice = 0;
        return totalPrice;
    }

    public boolean isIn1KG() {
        return isIn1KG;
    }

    public SalesItem
            (long salesId, long itemId, double weight, double bardana, double price, boolean isIn1KG) {
        this.salesId = salesId;
        this.itemId = itemId;
        this.weight = weight;
        this.bardana = bardana;
        this.price = price;
        this.isIn1KG = isIn1KG;
    }

    public SalesItem(long itemId, double weight, double bardana, double price, boolean isIn1KG) {
        this.netWeight = weight-bardana;
        this.itemId = itemId;
        this.weight = weight;
        this.bardana = bardana;
        this.price = price;
        this.isIn1KG = isIn1KG;
        if(isIn1KG)
            totalPrice = price*weight;
        else
            totalPrice = (price/37.324)*weight;

    }

    private long id;

    public long getSalesId() {
        return salesId;
    }

    public void setSalesId(long salesId) {
        this.salesId = salesId;
    }

    private long salesId;
    private long itemId;

    public long getItemId() {
        return itemId;
    }

    public double getNetWeight() {
        netWeight = weight-bardana;
        return netWeight;
    }

    private double weight,bardana,price,netWeight,totalPrice;
    private boolean isIn1KG;

    public void setId(long id) {
        this.id = id;
    }
}
