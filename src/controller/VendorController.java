package controller;

import dao.VendorDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Vendor;
import utility.DataItemCallback;
import utility.DataListCallback;

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
    @FXML
    TextField txtPhone;
    VendorDAO instance;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = VendorDAO.getInstance();
        showAllVendor();
    }

    void showAllVendor() {

        instance.getAll(new DataListCallback<Vendor>() {
            @Override
            public void OnSuccess(ObservableList<Vendor> list) {
                vendorListView.setItems(list);

                vendorListView.setCellFactory(new Callback<ListView<Vendor>, ListCell<Vendor>>() {
                    @Override
                    public ListCell<Vendor> call(ListView<Vendor> param) {

                        ListCell<Vendor> cell = new ListCell<Vendor>() {
                            @Override
                            protected void updateItem(Vendor vendor, boolean empty) {
                                super.updateItem(vendor, empty);

                                if (!empty) {
                                    HBox box = new HBox(20);
                                    Label lblName = new Label("\t"+vendor.getId()+" : "+vendor.getName());
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

            @Override
            public void OnFailed(String msg) {

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
            instance.delete(vendor, new DataItemCallback<Vendor>() {
                @Override
                public void OnSuccess(Vendor _vendor) {

                    UtilityClass.getInstance().showAlert("Delete Successfully");
                }

                @Override
                public void OnFailed(String msg) {
                    UtilityClass.getInstance().showErrorPopup("Delete Failed",null);
                }
            });
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    @FXML
    void insertVendor() {
        String vendorName = txtVendor.getText().toString();
        String vendorPhone = txtPhone.getText().toString();
        if(!vendorName.equals("")&&!vendorPhone.equals("")){
            Vendor vendor = new Vendor(vendorName,vendorPhone);
                instance.insert(vendor, new DataItemCallback<Vendor>() {
                    @Override
                    public void OnSuccess(Vendor _vendor) {
                        showAllVendor();
                        vendorListView.refresh();
                        txtVendor.clear();
                        txtPhone.clear();
                    }

                    @Override
                    public void OnFailed(String msg) {
                    UtilityClass.getInstance().showErrorPopup("Something went wrong",null);
                    }
                });


        }
    }


    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // do what you have to do
        stage.close();
    }




}
