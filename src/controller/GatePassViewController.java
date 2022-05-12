package controller;

import dao.GatePassDAO;
import dao.ItemDAO;
import dao.VendorDAO;
import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.GatePass;
import model.GatePassItem;
import model.Vendor;
import utility.ActionCallback;
import utility.DataListCallback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GatePassViewController implements Initializable {

    @FXML
    TableView<GatePass> gatePassTable;
    ObservableList<GatePass> gatePassObservableList;
    @FXML
    TableColumn<GatePass, String> gatePassId, vendorColumn, dateColumn, actionColumn;

    @FXML
    TextField txtGatePassId;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox vendorChoice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initGatePass();

    }


    void initGatePass() {
        if (gatePassObservableList != null)
            gatePassObservableList.removeAll();

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
        vendorChoice.getItems().clear();
        VendorDAO.getInstance().getAll(new DataListCallback<Vendor>() {
            @Override
            public void OnSuccess(ObservableList<Vendor> list) {
                for (Vendor v : list) {
                    vendorChoice.getItems().add(v.getId() + " : " + v.getName());
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

        GatePassDAO.getInstance().setCurrentGatePass(gatePassId);
        OpenGatePassScreen();
    }

    @FXML
    private void findGatePassById() {

        String gatePassIdText = txtGatePassId.getText();

        for (int i = 0; i < gatePassTable.getItems().size(); i++) {
            gatePassTable.getItems().clear();
        }

        gatePassObservableList.removeAll();
        try {
            long gatePassId = Long.parseLong(gatePassIdText);
            GatePass gatePass = GatePassDAO.getInstance().getById(gatePassId);
            if (gatePass != null) {
                gatePassObservableList.add(gatePass);
            } else {
                UtilityClass.getInstance().showAlert("No Gate Pass Found");
            }
        } catch (Exception e) {
            UtilityClass.getInstance().showAlert("No Gate Pass Found");

        }

    }

    @FXML
    private void searchGatePass() {
        String dateString = "";
        try {
            dateString = datePicker.getValue().toString().trim();
            System.out.println(dateString);
        }catch (Exception e){
            dateString = "";
        }
        int vendorIndex = vendorChoice.getSelectionModel().getSelectedIndex();
        String vendorId = "";
        try {
            vendorId = VendorDAO.getInstance().getByListIndex(vendorIndex).getId() + "";
        }catch (Exception e){
            vendorId = "";
        }

        if(dateString==""&&vendorId=="")
            return;
        GatePassDAO.getInstance().searchGatePass(dateString, vendorId, new DataListCallback<GatePass>() {
            @Override
            public void OnSuccess(ObservableList<GatePass> list) {
                System.out.println(list.size());
                for (int i = 0; i < gatePassTable.getItems().size(); i++)
                    gatePassTable.getItems().clear();

                gatePassObservableList.removeAll();

                for (int i = 0; i < list.size(); i++)
                    gatePassObservableList.add(list.get(i));
            }

            @Override
            public void OnFailed(String msg) {
                UtilityClass.getInstance().showErrorPopup(msg, new ActionCallback() {
                    @Override
                    public void OnAction() {

                    }

                    @Override
                    public void OnCancel() {

                    }
                });
            }
        });

    }

    @FXML
    private void refresh() {
        txtGatePassId.clear();
        datePicker.getEditor().clear();
        datePicker.setValue(null);
        vendorChoice.getSelectionModel().clearSelection();
        initGatePass();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) gatePassTable.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void OpenGatePassScreen() {
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
}