package controller;

import dao.GatePassDAO;
import dao.ItemDAO;
import dao.VendorDAO;
import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.GatePass;
import model.GatePassItem;
import model.Vendor;
import utility.ActionCallback;
import utility.DataListCallback;

import java.net.URL;
import java.util.ResourceBundle;

public class GatePassViewController implements Initializable {

    @FXML
    TableView<GatePass> gatePassTable;
    ObservableList<GatePass> gatePassObservableList;
    @FXML
    TableColumn<GatePass, String> gatePassId, vendorColumn, dateColumn, actionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        GatePassDAO.getInstance().GetAll(new DataListCallback<GatePass>() {
            @Override
            public void OnSuccess(ObservableList<GatePass> list) {
                gatePassObservableList = list;
                VendorDAO.getInstance().getAll(new DataListCallback<Vendor>() {
                    @Override
                    public void OnSuccess(ObservableList<Vendor> list) {
                        initGatePassTable();
                    }

                    @Override
                    public void OnFailed(String msg) {
                        UtilityClass.getInstance().showAlert(msg);
                    }
                });

            }

            @Override
            public void OnFailed(String msg) {
                UtilityClass.getInstance().showErrorPopup(msg, new ActionCallback() {
                    @Override
                    public void OnAction() {
                        closeWindow();
                    }

                    @Override
                    public void OnCancel() {
                        closeWindow();
                    }
                });
            }
        });

    }

    void initGatePassTable() {


        gatePassId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePass, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePass, String> param) {
                return new SimpleStringProperty(param.getValue().getId() + "");
            }
        });
        vendorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePass, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePass, String> param) {
                return new SimpleStringProperty(VendorDAO.getInstance().getById(param.getValue().getVendorId()).getName());
            }
        });
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePass, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePass, String> param) {
                return new SimpleStringProperty(param.getValue().getDate() + "");
            }
        });
        Callback<TableColumn<GatePass, String>, TableCell<GatePass, String>> cellFactory
                = //
                new Callback<TableColumn<GatePass, String>, TableCell<GatePass, String>>() {
                    @Override
                    public TableCell call(final TableColumn<GatePass, String> param) {
                        final TableCell<GatePass, String> cell = new TableCell<GatePass, String>() {

                            final Button btn = new Button("Show Details");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        GatePass _gatePass = getTableView().getItems().get(getIndex());
                                        showDetail(_gatePass.getId());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionColumn.setCellFactory(cellFactory);
        gatePassTable.setItems(gatePassObservableList);
    }

    void showDetail(long gatePassId) {


    }


    @FXML
    private void closeWindow() {
        Stage stage = (Stage) gatePassTable.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}