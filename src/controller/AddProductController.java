package controller;

import dao.ItemDAO;
import dao.ProductionDAO;
import dao.StockDAO;
import database.Database;
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
import model.Item;
import model.Production;
import model.ProductionItem;
import utility.ActionCallback;
import utility.DataItemCallback;
import utility.DataListCallback;
import utility.UtilityClass;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    Production currentProduction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentProduction = ProductionDAO.getInstance().currentProduction;
        productionItemList = FXCollections.observableArrayList();
        iniItemTable();
        initProductionChoiceBox();
        initItemChoiceBox();
        if (currentProduction != null) {
            setDate();
            setTableItems();
        }
    }


    public void setDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(currentProduction.getDate(), formatter);
        datePicker.setValue(localDate);
    }

    public void setCurrentProductionAssemble() {
        if (currentProduction != null) {
            int itemIndex = assembleItemList.indexOf(ItemDAO.getInstance().getById(currentProduction.getItemId()));
            finalProductChoice.getSelectionModel().select(itemIndex);
//            System.out.println(itemIndex);

        }
    }

    public void setTableItems() {
        ProductionDAO.getInstance().getProductionItemsListByProductionId(currentProduction.getId(), new DataListCallback<ProductionItem>() {
            @Override
            public void OnSuccess(ObservableList<ProductionItem> list) {
                productionItemList = list;
                productionItemTable.setItems(productionItemList);
                double weight = 0;
                for (ProductionItem pi : list) {
                    weight = weight+ pi.getWeight();
                }
                txtTotalWeight.setText(weight+"");
                txtTotalProduction.setText(currentProduction.getWeight()+"");
            }

            @Override
            public void OnFailed(String msg) {

            }
        });
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
                setCurrentProductionAssemble();
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
        long itemId = itemList.get(itemIndex).getId();
        String weightText = txtWeight.getText();

        for (ProductionItem pi : productionItemList) {
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
                for (ProductionItem i : productionItemList) {
                    totalWeight = totalWeight + i.getWeight();
                }

                txtTotalWeight.setText(totalWeight + "");
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

    public void deleteItem() {

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
                System.out.println(itemId + " " + _weight);

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

    @FXML
    private void saveProduction() {
        if (finalProductChoice.getSelectionModel().isEmpty()) {
            UtilityClass.getInstance().showAlert("Please Select Final Product");
            return;
        }
        if (datePicker.getValue() == null && datePicker.isEditable()) {
            UtilityClass.getInstance().showAlert("Please set date first");
            return;
        }
        if (productionItemList.size() < 1) {
            UtilityClass.getInstance().showAlert("Please add at least one item in this list");
            return;
        }
        String weightText = txtTotalProduction.getText();
        double weight = 0;
        try {
            weight = Double.parseDouble(weightText);
        } catch (Exception e) {
            weight = 0;
        }
        System.out.println("weight" + weight);
        if (weight <= 0) {
            UtilityClass.getInstance().showAlert("Wrong Weight Input");
            return;
        }
        int itemIndex = finalProductChoice.getSelectionModel().getSelectedIndex();

        long itemId = assembleItemList.get(itemIndex).getId();
        String date = datePicker.getValue().toString();

        if(currentProduction==null) {
            System.out.println(itemId);
            Production production = new Production(itemId, weight, date);
            ProductionDAO.getInstance().insertProduction(production, new DataItemCallback<Production>() {
                @Override
                public void OnSuccess(Production production) {

                    ProductionDAO.getInstance().insertProductionItem(productionItemList, production.getId(), new ActionCallback() {
                        @Override
                        public void OnAction() {
                            System.out.println(production.getItemId());

                            UtilityClass.getInstance().showAlert("Production Saved Successfully");
                            closeWindow();
                        }

                        @Override
                        public void OnCancel() {
                            UtilityClass.getInstance().showErrorPopup("Something went wrong", null);
                        }
                    });

                }

                @Override
                public void OnFailed(String msg) {
                    UtilityClass.getInstance().showErrorPopup(msg, null);
                }
            });
        }else
        {
            System.out.println(itemId);
            Production production = new Production(currentProduction.getId(),itemId, weight, date);
            ProductionDAO.getInstance().updateProduction(production, productionItemList, new DataItemCallback<Production>() {
                @Override
                public void OnSuccess(Production production) {
                    ProductionDAO.getInstance().setCurrentProductionNull();
                    System.out.println(production.getItemId());
                    UtilityClass.getInstance().showAlert("Production Update Successfully");
                    closeWindow();
                }

                @Override
                public void OnFailed(String msg) {
                    UtilityClass.getInstance().showErrorPopup(msg, null);
                }
            });

        }
    }


}
