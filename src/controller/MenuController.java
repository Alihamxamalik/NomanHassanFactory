package controller;

import dao.GatePassDAO;
import dao.ProductionDAO;
import database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Item;
import sun.text.normalizer.Utility;
import utility.ActionCallback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Database.getInstance().getConnection()!=null){
            System.out.println("Connected");
        }
        else
            System.out.println("Not Connected");
    }
    @FXML
    void OpenProductionMenu(){
        try {
            ProductionDAO.getInstance().currentProduction = null;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/add_product_layout.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Product");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenRawMaterialMenu(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/item_layout.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Raw Material");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenPartyMenu(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/vendor_layout.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Parties");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenCustomerMenu(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/customer_layout.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Parties");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenStockMenu(){
        try {
            System.out.println("Cloci");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/stock_layout.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Parties");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenSalesMenu(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/sales_entry_layout.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Parties");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenSalesView(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/sales_view.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Parties");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenGatePassScreen(){
        GatePassDAO.getInstance().setCurrentGatePassNull();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/gate_pass_entry_layout.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Gate Pass");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenGatePassViewScreen(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/gate_pass_view.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Gate Pass View");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OpenProductionDetails(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/production_details_view.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Recipe");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
