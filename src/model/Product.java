package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private StringProperty name;
    private IntegerProperty id;

    public Product(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.getValue().toString();
    }

    public int getId() {
        return  id.getValue().intValue();
    }

}
