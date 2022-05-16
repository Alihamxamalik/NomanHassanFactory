package controller;

import dao.GatePassDAO;
import dao.ItemDAO;
import dao.ProductionDAO;
import dao.VendorDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import model.Item;
import model.Production;
import utility.DataListCallback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductionDetailsController implements Initializable {

    @FXML
    TableView<Production> productionTable;
    ObservableList<Production> productionObservableList;
    @FXML
    TableColumn<Production, String> gatePassId, itemColumn, dateColumn, weightColumn, actionColumn;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<String> itemChoice;
    ObservableList<Item> itemObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProduction();
        initItemChoice();
    }

    void initItemChoice() {
        ItemDAO.getInstance().getAll(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList<Item> list) {
                ObservableList<String> stringObservableList = FXCollections.observableArrayList();
                itemObservableList = FXCollections.observableArrayList();
                for (Item i : list) {
                    if (i.isAssemble()) {
                        itemObservableList.add(i);
                        stringObservableList.add(i.getName());
                    }
                }
//                itemObservableList = list;

                itemChoice.setItems(stringObservableList);

            }

            @Override
            public void OnFailed(String msg) {

            }
        });
    }

    void initProduction() {
        ProductionDAO.getInstance().getAllProduction(new DataListCallback<Production>() {
            @Override
            public void OnSuccess(ObservableList<Production> list) {
                productionObservableList = list;
            }

            @Override
            public void OnFailed(String msg) {

            }
        });
        initProductionTable();
    }

    void initProductionTable() {
        gatePassId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Production, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Production, String> param) {
                return new SimpleStringProperty(param.getValue().getId() + "");
            }
        });
        itemColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Production, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Production, String> param) {
                return new SimpleStringProperty(ItemDAO.getInstance().getById(param.getValue().getItemId()).getName());
            }
        });
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Production, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Production, String> param) {
                return new SimpleStringProperty(param.getValue().getDate() + "");
            }
        });
        weightColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Production, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Production, String> param) {
                return new SimpleStringProperty(param.getValue().getWeight() + " KG");
            }
        });

        Callback<TableColumn<Production, String>, TableCell<Production, String>> cellFactory
                = //
                new Callback<TableColumn<Production, String>, TableCell<Production, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Production, String> param) {
                        final TableCell<Production, String> cell = new TableCell<Production, String>() {

                            final Button btn = new Button("Show Details");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Production _production = getTableView().getItems().get(getIndex());
                                        showDetail(_production.getId());
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
        productionTable.setItems(productionObservableList);

    }

    public void refresh(ActionEvent actionEvent) {
        initProduction();
        datePicker.getEditor().clear();
        itemChoice.getSelectionModel().clearSelection();
    }

    public void searchProduction(ActionEvent actionEvent) {
        String dateString = "";
        try {
            dateString = datePicker.getValue().toString().trim();
            System.out.println(dateString);
        } catch (Exception e) {
            dateString = "";
        }
        int itemIndex = itemChoice.getSelectionModel().getSelectedIndex();
        String itemId = "";
        try {
            itemId = itemObservableList.get(itemIndex).getId() + "";
        } catch (Exception e) {
            itemId = "";
        }

        if (dateString == "" && itemId == "")
            return;

        ProductionDAO.getInstance().searchProduction(dateString, itemId, new DataListCallback<Production>() {
            @Override
            public void OnSuccess(ObservableList<Production> list) {
                System.out.println(list.size());
                productionObservableList = FXCollections.observableArrayList();
                for (Production p : list) {
                    productionObservableList.add(p);
                }
                initProductionTable();
            }

            @Override
            public void OnFailed(String msg) {

            }
        });

    }
    void showDetail(long productionId) {
        ProductionDAO.getInstance().setCurrentProduction(productionId);
        OpenProductionMenu();
    }
    void OpenProductionMenu(){
        try {
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
    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) productionTable.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
