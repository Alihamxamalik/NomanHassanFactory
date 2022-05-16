package controller;

import dao.GatePassDAO;
import dao.VendorDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.GatePass;
import model.Vendor;
import utility.DataItemCallback;
import utility.DataListCallback;
import utility.UtilityClass;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class VendorController implements Initializable {
    @FXML
    Button btnClose;
    @FXML
    TableView<Vendor> vendorTable;
    @FXML
    TableColumn<Vendor,String> colId,colPhone,colName,colAction;
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
        initVendorTable();
        //showAllVendor();
    }

    void initVendorTable(){

        instance.getAll(new DataListCallback<Vendor>() {
            @Override
            public void OnSuccess(ObservableList<Vendor> list) {
                if(list==null|| list.size()<1)
                    return;
                colId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Vendor, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Vendor, String> param) {
                        return new SimpleStringProperty(param.getValue().getId()+"");
                    }
                });
                colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Vendor, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Vendor, String> param) {
                        return new SimpleStringProperty(param.getValue().getName());
                    }
                });
                colPhone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Vendor, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Vendor, String> param) {
                        return new SimpleStringProperty(param.getValue().getPhone());
                    }
                });

                Callback<TableColumn<Vendor, String>, TableCell<Vendor, String>> cellFactory = new Callback<TableColumn<Vendor, String>, TableCell<Vendor, String>>() {
                    @Override
                    public TableCell<Vendor, String> call(TableColumn<Vendor, String> param) {
                        final TableCell<Vendor, String> cell = new TableCell<Vendor, String>() {

                            final Button btn = new Button("Update");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!empty) {
                                    Vendor vendor = getTableView().getItems().get(getIndex());
                                    HBox box = new HBox(20);
//                                    Label lblName = new Label(""+vendor.getId()+" : \t"+vendor.getName()+"\t : "+vendor.getPhone());
                                    Button btn = new Button("Delete");
                                    Button btnShowStock = new Button("Show Detail");
                                    btn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            confirmDelete(vendor);
                                        }
                                    });
                                    box.getChildren().addAll(btn);
                                    setGraphic(box);
                                    //setText(value);
                                } else
                                    setGraphic(null);
                            }
                        };
                        return cell;
                    }
                };

                colAction.setCellFactory(cellFactory);
                vendorTable.setItems(list);
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
            GatePassDAO.getInstance().searchGatePass("", vendor.getId()+"", new DataListCallback<GatePass>() {
                @Override
                public void OnSuccess(ObservableList<GatePass> list) {
                    if(list==null || list.size()<1){
                        instance.delete(vendor, new DataItemCallback<Vendor>() {
                            @Override
                            public void OnSuccess(Vendor _vendor) {
                                initVendorTable();
                                UtilityClass.getInstance().showAlert("Delete Successfully");
                            }

                            @Override
                            public void OnFailed(String msg) {
                                UtilityClass.getInstance().showErrorPopup("Delete Failed",null);
                            }
                        });

                    }else{
                        UtilityClass.getInstance().showAlert("Cant delete this vendor");
                    }
                }

                @Override
                public void OnFailed(String msg) {
                        UtilityClass.getInstance().showErrorPopup(msg,null);
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
                        initVendorTable();
//                        vendorListView.refresh();
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
