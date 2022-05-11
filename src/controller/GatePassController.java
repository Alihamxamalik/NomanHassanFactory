package controller;

import dao.GatePassDAO;
import dao.ItemDAO;
import dao.VendorDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.GatePass;
import model.GatePassItem;
import model.Item;
import model.Vendor;
import utility.ActionCallback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class GatePassController implements Initializable {

    @FXML
    ChoiceBox<String> itemChoice;
    @FXML
    ChoiceBox<String> vendorChoice;
    @FXML
    TextField weightEditText;
    @FXML
    TextField bardanaEditText;
    @FXML
    TextField priceEditText;
    @FXML
    DatePicker entryDatePicker;
    @FXML
    ToggleButton priceToggle;
    ObservableList gatePassItemList;
    @FXML
    TableView<GatePassItem> gatePassItemTable;
    @FXML
    TableColumn<GatePassItem, String>
            indexColumn, nameColumn, weightColumn, bardanaColumn, netWeightColumn, priceColumn, totalPriceColumn;

    GatePass currentGatePass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVendorChoiceBox();
        initItemChoiceBox();
        gatePassItemList = FXCollections.observableArrayList();
        initGatePassItemTable();
        onPriceToggle();
        currentGatePass = GatePassDAO.getInstance().currentGatePass;
        if (currentGatePass != null) {
            System.out.println("Not Null");
            setCurrentGatePassView();
        }
    }

    void setCurrentGatePassView() {

        //Set Vendor
        VendorDAO.getInstance().getAll(new DataListCallback<Vendor>() {
            @Override
            public void OnSuccess(ObservableList<Vendor> list) {

                for (int vendorIndex=0;vendorIndex<list.size();vendorIndex++) {
                    if(list.get(vendorIndex).getId()==currentGatePass.getVendorId()){
                        vendorChoice.getSelectionModel().select(vendorIndex);
                    }
                }
            }

            @Override
            public void OnFailed(String msg) {

            }
        });
        //Set Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(currentGatePass.getDate(), formatter);
        entryDatePicker.setValue(localDate);
        // Set Items

        GatePassDAO.getInstance().getGatePassItemListById(currentGatePass.getId(), new DataListCallback<GatePassItem>() {
            @Override
            public void OnSuccess(ObservableList<GatePassItem> list) {
                for (GatePassItem item:list) {
                    gatePassItemList.add(item);
                }
                gatePassItemTable.refresh();
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

    @FXML
    public void AddButton() {

        if (vendorChoice.getSelectionModel().isEmpty()) {
            UtilityClass.getInstance().showAlert("Please Select Vendor");
            return;
        }

        if (itemChoice.getSelectionModel().isEmpty()) {
            UtilityClass.getInstance().showAlert("Please Select Item");
            return;
        }

        String weightText = weightEditText.getText();
        double weight = StringToDouble(weightText);
        if (weight <= 0) {
            UtilityClass.getInstance().showAlert("Wrong Weight Input");
            return;
        }

        String bardanaText = bardanaEditText.getText();
        double bardana = StringToDouble(bardanaText);

        if (bardana < 0) {
            UtilityClass.getInstance().showAlert("Wrong Bardana Input");
            return;
        }

        String priceText = priceEditText.getText();
        double price = StringToDouble(priceText);

        if (price <= 0) {
            UtilityClass.getInstance().showAlert("Wrong Price Input");
            return;
        }

        AddItem();

    }

    @FXML
    public void onPriceToggle() {
        if (priceToggle.isSelected())
            priceToggle.setText("Price in 37.324 KG");
        else
            priceToggle.setText("Price in 1 KG");

    }

    @FXML
    public void saveGatePass() {

        if (entryDatePicker.getValue() == null && entryDatePicker.isEditable()) {
            UtilityClass.getInstance().showAlert("Please set date first");
            return;
        } else {
            System.out.println(entryDatePicker.getValue().toString());
        }

        if (gatePassItemList.size() < 1) {
            UtilityClass.getInstance().showAlert("Please Add Item First");
            return;
        }

        int vendorIndex = vendorChoice.getSelectionModel().getSelectedIndex();
        if (currentGatePass == null) {
            currentGatePass = new GatePass
                    (VendorDAO.getInstance().getByListIndex(vendorIndex).getId(), entryDatePicker.getValue().toString());
            GatePassDAO.getInstance().insert(currentGatePass, new DataItemCallback<GatePass>() {
                @Override
                public void OnSuccess(GatePass gatePass) {

                    GatePassDAO.getInstance().insertGatePassItemList(gatePassItemList, gatePass.getId(), new utility.Callback() {
                        @Override
                        public void OnSuccess() {
                            clearScreen();
                            UtilityClass.getInstance().showAlert("GatePass Save Successfully");
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
        } else {

            GatePassDAO.getInstance().update(currentGatePass, gatePassItemList, new DataItemCallback<GatePass>() {
                @Override
                public void OnSuccess(GatePass gatePass) {
                    clearScreen();
                    UtilityClass.getInstance().showAlert("GatePass Save Successfully");
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

    }


    void clearScreen() {
        vendorChoice.getSelectionModel().clearSelection();
        entryDatePicker.getEditor().clear();
        gatePassItemList.clear();
        clearItem();
    }

    void clearItem() {
        weightEditText.clear();
        bardanaEditText.clear();
        priceEditText.clear();
        priceToggle.setSelected(false);
        itemChoice.getSelectionModel().clearSelection();
        onPriceToggle();

    }

    @FXML
    private void closeWindow() {
        GatePassDAO.getInstance().setCurrentGatePassNull();
        Stage stage = (Stage) itemChoice.getScene().getWindow();
        stage.close();
    }

    double StringToDouble(String s) {
        double value = 0;
        try {
            value = Double.parseDouble((s));
        } catch (Exception e) {
            value = 0;
        }
        return value;
    }


    void AddItem() {
        int itemIndex = itemChoice.getSelectionModel().getSelectedIndex();
        int vendorIndex = vendorChoice.getSelectionModel().getSelectedIndex();
        double weightValue = Double.parseDouble(weightEditText.getText());
        double bardanaValue = Double.parseDouble(bardanaEditText.getText());
        double priceValue = Double.parseDouble(priceEditText.getText());

        GatePassItem item = new GatePassItem(
                ItemDAO.instance.getByListIndex(itemIndex).getId(),
                weightValue,
                bardanaValue,
                priceValue,
                !priceToggle.isSelected()
        );

        gatePassItemList.add(item);
        System.out.println(gatePassItemList.size());
        clearItem();
    }


    void initGatePassItemTable() {

        indexColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePassItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePassItem, String> param) {
                return new SimpleStringProperty(gatePassItemList.indexOf(param.getValue()) + 1 + "");
            }
        });
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePassItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePassItem, String> param) {
                return new SimpleStringProperty(ItemDAO.getInstance().getById(param.getValue().getItemId()).getName());
            }
        });
        weightColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePassItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePassItem, String> param) {
                return new SimpleStringProperty(param.getValue().getWeight() + "");
            }
        });
        bardanaColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePassItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePassItem, String> param) {
                return new SimpleStringProperty(param.getValue().getBardana() + "");
            }
        });
        netWeightColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePassItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePassItem, String> param) {
                return new SimpleStringProperty(param.getValue().getNetWeight() + "");
            }
        });

        priceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePassItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePassItem, String> param) {

                String s = "";
                if (param.getValue().isIn1KG())
                    s = "(1KG)";
                else
                    s = "(Maund)";

                String text = param.getValue().getPrice() + " " + s;
                return new SimpleStringProperty(text);
            }
        });
        totalPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GatePassItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GatePassItem, String> param) {
                return new SimpleStringProperty(Math.round(param.getValue().getTotalPrice()) + "");
            }
        });

        gatePassItemTable.setItems(gatePassItemList);

    }

    @FXML
    public void deleteItem() {
        if (gatePassItemTable.getSelectionModel().getSelectedItem() != null) {
            gatePassItemTable.getItems().remove(gatePassItemTable.getSelectionModel().getSelectedItem());
        }
    }

    void initVendorChoiceBox() {

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


//        vendorChoice.setItems(VendorDAO.getInstance().GetAll());

    }

    void initItemChoiceBox() {

        itemChoice.getItems().clear();

        ItemDAO.getInstance().getAll(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList<Item> list) {
                for (Item r : list) {
                    String s = r.getId() + " : " + r.getName();
                    if (r.isAssemble())
                        s = s + " (Assemble)";

                    itemChoice.getItems().add(s);
                }
            }

            @Override
            public void OnFailed(String msg) {
                closeWindow();
            }
        });
    }

}
