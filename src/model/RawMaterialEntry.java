package model;

public class RawMaterialEntry {

    private RawMaterial material;
    private double weight;
    private double priceInRupees;
    private double bardana;
    private double finalWeight;

    public RawMaterialEntry(){
        finalWeight = weight-bardana;
    }
    public RawMaterial getMaterial() {
        return material;
    }

    public void setMaterial(RawMaterial material) {
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
