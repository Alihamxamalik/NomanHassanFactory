package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import model.Item;
import model.Vendor;
import utility.ActionCallback;
import utility.Callback;

import java.util.Optional;

public class UtilityClass {


    private static UtilityClass instance;

    public static UtilityClass getInstance() {
        if (instance == null)
            instance = new UtilityClass();
        return instance;
    }

    public void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.NONE, s, ButtonType.OK);
        alert.setTitle("Alert");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();

    }


    public void showErrorPopup(String msg, ActionCallback callback) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Opps Something went wrong");
        alert.setHeaderText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (callback != null)
                callback.OnAction();
        }
    }


    void confirmDelete(String title, String body, ActionCallback callback) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(body);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            if (callback != null)
                callback.OnAction();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

}
