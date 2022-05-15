package controller;

import dao.ItemDAO;
import dao.StockDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.GatePassItem;
import model.Item;
import model.ProductionItem;
import utility.ActionCallback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML
    ChoiceBox<String> finalProductChoice, itemChoice;
    @FXML
    DatePicker datePicker;
    @FXML
    TextField txtWeight, txtTotalWeight, txtTotalProduction;
    @FXML
    TableView<ProductionItem> productionItemTable;
    @FXML
    TableColumn<ProductionItem, String> colIndex, colName, colWeight;

    ObservableList<ProductionItem> productionItemList;
    ObservableList<Item> assembleItemList, itemList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productionItemList = FXCollections.observableArrayList();
        iniItemTable();
        initProductionChoiceBox();
        initItemChoiceBox();

    }


    void initProductionChoiceBox() {

        ItemDAO.getInstance().getAll(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList<Item> list) {
                finalProductChoice.getItems().clear();
                assembleItemList = FXCollections.observableArrayList();
                for (Item item : list) {
                    if (item.isAssemble())
                        assembleItemList.add(item);
                }

                for (Item i : assembleItemList) {
                    finalProductChoice.getItems().add(i.getId() + " : " + i.getName());
                }
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

    void initItemChoiceBox() {
        ItemDAO.getInstance().getAll(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList<Item> list) {
                itemList = list;
                itemChoice.getItems().clear();

                for (Item i : itemList) {
                    itemChoice.getItems().add(i.getId() + " : " + i.getName());
                }
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
        itemChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("Change");
                setItemWeight();
            }
        });
    }

    public void closeWindow() {
        Stage stage = (Stage) itemChoice.getScene().getWindow();
        stage.close();
    }

    public void addItem() {
        int itemIndex = itemChoice.getSelectionModel().getSelectedIndex();
        long itemId = ItemDAO.instance.getByListIndex(itemIndex).getId();
        String weightText = txtWeight.getText();

        for (ProductionItem pi:productionItemList) {
            if (pi.getItemId() == itemId) {
                UtilityClass.getInstance().showAlert("Item Already Added");
                return;
            }
        }
        double weight = 0;
        try {
            weight = Double.parseDouble(weightText);
        } catch (Exception e) {
            weight = 0;
        }
        if (weight < 1) {
            UtilityClass.getInstance().showAlert("Wrong weight");
            return;
        }



        double finalWeight = weight;

        StockDAO.getInstance().getStockItemWeight(itemId, new DataItemCallback<Double>() {
            @Override
            public void OnSuccess(Double _weight) {
                if (finalWeight <= _weight) {
                    productionItemList.add(new ProductionItem(itemId, finalWeight));

                } else {
                    UtilityClass.getInstance().showAlert("Stock Ran out");
                }

                double totalWeight = 0;
                for (ProductionItem i:productionItemList) {
                    totalWeight = totalWeight+i.getWeight();
                }

                txtTotalWeight.setText(totalWeight+"");
//                itemChoice.getSelectionModel().clearSelection();
                txtWeight.clear();
                txtWeight.setPromptText("0");
            }

            @Override
            public void OnFailed(String msg) {
                UtilityClass.getInstance().showErrorPopup(msg, null);
            }
        });
    }

    public void deleteItem(){

        if (productionItemTable.getSelectionModel().getSelectedItem() != null) {
            productionItemTable.getItems().remove(productionItemTable.getSelectionModel().getSelectedItem());
        }

    }

    void setItemWeight() {

        int itemIndex = itemChoice.getSelectionModel().getSelectedIndex();
        long itemId = ItemDAO.instance.getByListIndex(itemIndex).getId();

        StockDAO.getInstance().getStockItemWeight(itemId, new DataItemCallback<Double>() {
            @Override
            public void OnSuccess(Double _weight) {
                System.out.println(itemId+" "+_weight);

                if (_weight > 0)
                    txtWeight.setPromptText(_weight + "");
                else
                    txtWeight.setPromptText("0");

            }

            @Override
            public void OnFailed(String msg) {
                UtilityClass.getInstance().showErrorPopup(msg, null);
            }
        });

    }

    void iniItemTable() {
        colIndex.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductionItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductionItem, String> param) {
                return new SimpleStringProperty(productionItemList.indexOf(param.getValue()) + 1 + "");
            }
        });
        colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductionItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductionItem, String> param) {
                return new SimpleStringProperty(ItemDAO.getInstance().getById(param.getValue().getItemId()).getName());
            }
        });
        colWeight.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductionItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductionItem, String> param) {
                return new SimpleStringProperty(param.getValue().getWeight() + " kg");
            }
        });
        productionItemTable.setItems(productionItemList);
    }


    private void saveProduction(){
        if (finalProductChoice.getSelectionModel().isEmpty()) {
            UtilityClass.getInstance().showAlert("Please Select Final Product");
            return;
        }
        if (datePicker.getValue() == null && datePicker.isEditable()) {
            UtilityClass.getInstance().showAlert("Please set date first");
            return;
        }
        if(productionItemList.size()<1)
        {
            UtilityClass.getInstance().showAlert("Please add at least one item in this list");
            return;
        }
        String weightText = txtTotalWeight.getText();
        double weight = 0;
        try {
            weight = Double.parseDouble(weightText);
        }catch (Exception e){
            weight = 0;
        }
        if (weight <= 0) {
            UtilityClass.getInstance().showAlert("Wrong Weight Input");
            return;
        }
    }

}
