package controller;

import dao.RawMaterialDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.RawMaterial;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GatePassController implements Initializable {

    @FXML
    ChoiceBox<RawMaterial> itemChoice;
    @FXML
    ChoiceBox<RawMaterial> partyChoice;
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
    }

    int itemCount = 0;

    @FXML
    public void AddButton() {

        if (itemChoice.getValue().getId() == 0) {
            ShowAlert("Please Select Item");
            return;
        }
        String weightText = weightEditText.getText();
        double weight = StringToDouble(weightText);

        if (weight <= 0) {
            ShowAlert("Wrong Weight Input");
            return;
        }
        AddItem();

    }

    @FXML
    public void SaveGatePass() {

        if(itemCount<1) {
            ShowAlert("Please Add Item First");
            return;
        }

        ShowAlert("Item Added");

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

    public  void ShowAlert(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();

    }

}
