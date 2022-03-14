package controller;

import dao.ItemDAO;
import dao.VendorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Item;
import model.Vendor;

import java.net.URL;
import java.util.ResourceBundle;

public class GatePassController implements Initializable {

    @FXML
    ChoiceBox<String> itemChoice;
    @FXML
    ChoiceBox<String> factoryChoice;
    @FXML
    ChoiceBox<String> vendorChoice;
    @FXML
    TextField weightEditText;
    @FXML
    TextField bardanaEditText;
    @FXML
    TextField priceEditText;
    @FXML
    DatePicker entryDatePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVendorChoiceBox();
        initItemChoiceBox();
    }

    int itemCount = 0;

    @FXML
    public void AddButton() {

        if (itemChoice.getSelectionModel().isEmpty()) {
             UtilityClass.getInstance().ShowAlert("Please Select Item");
            return;
        }
        String weightText = weightEditText.getText();
        double weight = StringToDouble(weightText);

        if (weight <= 0) {
            UtilityClass.getInstance().ShowAlert("Wrong Weight Input");
            return;
        }
        AddItem();

    }

    @FXML
    public void SaveGatePass() {

        if(itemCount<1) {
            UtilityClass.getInstance().ShowAlert("Please Add Item First");
            return;
        }

        UtilityClass.getInstance().ShowAlert("Item Added");

        itemCount = 0;
    }


    @FXML
    private void closeWindow() {
        Stage stage = (Stage) itemChoice.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    double StringToDouble(String s) {
        double value = 0;
        try {
            value = Double.parseDouble((s));
        } catch (Exception e) {
            value = 0;
        }
        return value;
    }


    void AddItem() {

        itemCount++;

    }


    void initVendorChoiceBox(){

        vendorChoice.getItems().clear();

        for (Vendor v : VendorDAO.getInstance().GetAll()) {
            vendorChoice.getItems().add(v.getName());
        }

//        vendorChoice.setItems(VendorDAO.getInstance().GetAll());

    }

    void initItemChoiceBox(){

        itemChoice.getItems().clear();

        for (Item r : ItemDAO.getInstance().GetAll()) {
            itemChoice.getItems().add(r.getName());
        }
        //        itemChoice.setItems(RawMaterialDAO.getInstance().GetAll());
    }


}
