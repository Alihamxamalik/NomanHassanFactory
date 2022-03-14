package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

public class UtilityClass {



    private static UtilityClass instance;

    public static UtilityClass getInstance(){
        if(instance==null)
            instance = new UtilityClass();
        return  instance;
    }

    public void ShowAlert(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();

    }
}
