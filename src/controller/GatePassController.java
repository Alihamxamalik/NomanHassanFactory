package controller;

import dao.ItemDAO;
import dao.VendorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.GatePassItem;
import model.Item;
import model.Vendor;
import utility.ActionCallback;
import utility.DataListCallback;

import java.net.URL;
import java.util.ResourceBundle;

public class GatePassController implements Initializable {

    @FXML
    ChoiceBox<String> itemChoice;
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

    ObservableList gatePassItemList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVendorChoiceBox();
        initItemChoiceBox();
        gatePassItemList = FXCollections.observableArrayList();
    }

    @FXML
    public void AddButton() {

        if (vendorChoice.getSelectionModel().isEmpty()) {
            UtilityClass.getInstance().showAlert("Please Select Vendor");
            return;
        }

        if (itemChoice.getSelectionModel().isEmpty()) {
            UtilityClass.getInstance().showAlert("Please Select Item");
            return;
        }

        String weightText = weightEditText.getText();
        double weight = StringToDouble(weightText);
        if (weight <= 0) {
            UtilityClass.getInstance().showAlert("Wrong Weight Input");
            return;
        }

        String bardanaText = bardanaEditText.getText();
        double bardana = StringToDouble(bardanaText);

        if (bardana <= 0) {
            UtilityClass.getInstance().showAlert("Wrong Bardana Input");
            return;
        }

        String priceText = priceEditText.getText();
        double price = StringToDouble(priceText);

        if (price <= 0) {
            UtilityClass.getInstance().showAlert("Wrong Price Input");
            return;
        }


        AddItem();

    }


    @FXML
    public void SaveGatePass() {

        if (gatePassItemList.size() < 1) {
            UtilityClass.getInstance().showAlert("Please Add Item First");
            return;
        }

        UtilityClass.getInstance().showAlert("Item Added");


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
        int itemIndex = itemChoice.getSelectionModel().getSelectedIndex();
        int vendorIndex = vendorChoice.getSelectionModel().getSelectedIndex();
        double weightValue = Double.parseDouble(weightEditText.getText());
        double bardanaValue = Double.parseDouble(bardanaEditText.getText());
        double priceValue = Double.parseDouble(priceEditText.getText());

        GatePassItem item = new GatePassItem(
                ItemDAO.instance.getByListIndex(itemIndex).getId(),
                weightValue,
                bardanaValue,
                priceValue
        );

        gatePassItemList.add(item);
    }


    void initVendorChoiceBox() {

        vendorChoice.getItems().clear();

        VendorDAO.getInstance().getAll(new DataListCallback<Vendor>() {
            @Override
            public void OnSuccess(ObservableList<Vendor> list) {
                for (Vendor v : list) {
                    vendorChoice.getItems().add(v.getId()+" : "+v.getName());
                }
            }

            @Override
            public void OnFailed(String msg) {
                UtilityClass.getInstance().showErrorPopup("Something went wrong", new ActionCallback() {
                    @Override
                    public void OnAction() {
                        closeWindow();
                    }

                    @Override
                    public void OnCancel() {

                    }
                });
            }
        });


//        vendorChoice.setItems(VendorDAO.getInstance().GetAll());

    }

    void initItemChoiceBox() {

        itemChoice.getItems().clear();

        ItemDAO.getInstance().getAll(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList<Item> list) {
                for (Item r : list) {
                    String s = r.getId()+" : "+r.getName();
                    if (r.isAssemble())
                        s = s + " (Assemble)";

                    itemChoice.getItems().add(s);
                }
            }

            @Override
            public void OnFailed(String msg) {
                closeWindow();
            }
        });
    }


}
