package controller;

import dao.VendorDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Vendor;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class VendorController implements Initializable {
    @FXML
    Button btnClose;
    @FXML
    ListView vendorListView;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtVendor;
    VendorDAO instance;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = VendorDAO.getInstance();
        showAllProduct();
    }

    void showAllProduct() {
        vendorListView.setItems(instance.GetAll());

//
//        partyListView.setCellFactory(new Callback<ListView, ListCell>() {
//            @Override
//            public ListCell call(ListView param) {
//                return null;
//            }
//        });


        vendorListView.setCellFactory(new Callback<ListView<Vendor>, ListCell<Vendor>>() {
            @Override
            public ListCell<Vendor> call(ListView<Vendor> param) {

                ListCell<Vendor> cell = new ListCell<Vendor>() {
                    @Override
                    protected void updateItem(Vendor vendor, boolean empty) {
                        super.updateItem(vendor, empty);

                        if (!empty) {
                            HBox box = new HBox(20);
                            Label lblName = new Label(vendor.getId()+" "+vendor.getName());
                            Button btn = new Button("x");
                            Button btnShowStock = new Button("Show Detail");
                            btn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    confirmDelete(vendor);
                                }
                            });
                            btnShowStock.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    showDetailScreen();
                                }
                            });

                            box.getChildren().addAll(btn,btnShowStock,lblName );
                            setGraphic(box);
                            //setText(value);
                        } else
                            setGraphic(null);
                    }
                };
                return cell;
            }
        });


    }

    void showDetailScreen(){



    }

    void confirmDelete(Vendor vendor){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Do you want to delete Vendor "+vendor.getId()+" "+vendor.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            instance.delete(vendor);
        } else {
            // ... user chose CANCEL or closed the dialog
        }



    }

    @FXML
    void insertVendor() {
        String VendorName = txtVendor.getText().toString();
        if(!VendorName.equals("")){
                Vendor Vendor = new Vendor(txtVendor.getText().toString());
                instance.insert(Vendor);
                txtVendor.clear();


        }
    }


    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // do what you have to do
        stage.close();
    }




}
