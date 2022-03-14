package model;

public class RawMaterialEntry {

    private Item material;
    private double weight;
    private double priceInRupees;
    private double bardana;
    private double finalWeight;

    public RawMaterialEntry(){
        finalWeight = weight-bardana;
    }
    public Item getMaterial() {
        return material;
    }

    public void setMaterial(Item material) {
        this.material = material;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPriceInRupees() {
        return priceInRupees;
    }

    public void setPriceInRupees(double priceInRupees) {
        this.priceInRupees = priceInRupees;
    }

    public double getBardana() {
        return bardana;
    }

    public void setBardana(double bardana) {
        this.bardana = bardana;
    }
    public double getFinalWeight(){

        return weight - bardana;
    }
}
