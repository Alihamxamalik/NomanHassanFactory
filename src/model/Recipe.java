package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class Recipe {

    public Product getProduct() {
        return product;
    }

    public Recipe(String name, Product product, List<Ingredient> ingredientList) {
        this.name = new SimpleStringProperty(name);
        this.product = product;
        this.ingredientList = ingredientList;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getId() {
        return id;
    }

    public StringProperty name;
    private int id;
    private Product product;
    private List<Ingredient> ingredientList;

}
