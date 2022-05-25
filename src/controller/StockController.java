package controller;

import dao.ItemDAO;
import dao.StockDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Item;
import model.ProductionItem;
import model.StockItem;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {
    @FXML
    TableView<StockItem> stockTableView;
    @FXML
    TableColumn<StockItem, String> idColumn, nameColumn, weightColumn;
    ObservableList<StockItem> stockItemList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initStockItemList();
        initTableItems();
    }

    void initStockItemList() {
        stockItemList = FXCollections.observableArrayList();
        ItemDAO.getInstance().getAll(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList<Item> list) {
                for (Item i : list) {
                    StockDAO.getInstance().getStockItemWeight(i.getId(), new DataItemCallback<Double>() {
                        @Override
                        public void OnSuccess(Double aDouble) {
                            stockItemList.add(new StockItem(aDouble, i.getId()));
                        }

                        @Override
                        public void OnFailed(String msg) {

                        }
                    });
                }
            }

            @Override
            public void OnFailed(String msg) {

            }
        });
    }

    void initTableItems() {
        idColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<StockItem, String> param) {
                return new SimpleStringProperty(param.getValue().itemId + "");
            }
        });
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<StockItem, String> param) {
                return new SimpleStringProperty(ItemDAO.getInstance().getById(param.getValue().getItemId()).getName());
            }
        });
        weightColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<StockItem, String> param) {
                return new SimpleStringProperty(param.getValue().getWeight() + "");
            }
        });
        stockTableView.setItems(stockItemList);
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) stockTableView.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

}
