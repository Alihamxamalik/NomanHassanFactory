package model;

public class Ingredient {
    private int id;

    public int getId() {
        return id;
    }

    public Ingredient(RawMaterial rawMaterial, float quantity) {
        this.rawMaterial = rawMaterial;
        this.quantity = quantity;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }


    public float getQuantity() {
        return quantity;
    }



    private RawMaterial rawMaterial;
    private float quantity;
}
