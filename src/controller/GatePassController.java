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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
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
    ObservableList<GatePassItem> gatePassItemList;
    @FXML
    TableView<GatePassItem> gatePassItemTable;
    @FXML
    TableColumn<GatePassItem, String>
            indexColumn, nameColumn, weightColumn, bardanaColumn, netWeightColumn, priceColumn, totalPriceColumn, actionColumn;

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
//        createUpdatePopup(null);
    }

    void setCurrentGatePassView() {

        //Set Vendor
        VendorDAO.getInstance().getAll(new DataListCallback<Vendor>() {
            @Override
            public void OnSuccess(ObservableList<Vendor> list) {

                for (int vendorIndex = 0; vendorIndex < list.size(); vendorIndex++) {
                    if (list.get(vendorIndex).getId() == currentGatePass.getVendorId()) {
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
                for (GatePassItem item : list) {
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

        if (bardana < 0 || bardana>=weight) {
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
        double weightValue = Double.parseDouble(weightEditText.getText());
        double bardanaValue = 0;
        try {
            bardanaValue = Double.parseDouble(bardanaEditText.getText());
        }catch (Exception e) {
            bardanaValue = 0;
        }
        double priceValue = 0;
        try {
            priceValue = Double.parseDouble(priceEditText.getText());
        }catch (Exception e){
            priceValue = 0;
        }
        for (GatePassItem i: gatePassItemList) {
            if(ItemDAO.instance.getByListIndex(itemIndex).getId()==i.getItemId())
            {
                UtilityClass.getInstance().showAlert("Item Already Added");
                return;
            }
        }

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

        actionColumn.setCellFactory(actionButton());
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


    public Callback<TableColumn<GatePassItem, String>, TableCell<GatePassItem, String>> actionButton() {
        Callback<TableColumn<GatePassItem, String>, TableCell<GatePassItem, String>> cellFactory
                = //
                new Callback<TableColumn<GatePassItem, String>, TableCell<GatePassItem, String>>() {
                    @Override
                    public TableCell call(final TableColumn<GatePassItem, String> param) {
                        final TableCell<GatePassItem, String> cell = new TableCell<GatePassItem, String>() {

                            final Button btn = new Button("Update");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        createUpdatePopup(getTableView().getItems().get(getIndex()));
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        return cellFactory;
    }

    void createUpdatePopup(GatePassItem item) {
// set title for the stage
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Update Item");





//        TextField _txtItemName = createTextField(ItemDAO.getInstance().getById(item.getItemId()).getName());
        Label lblItemName = createLabel(ItemDAO.getInstance().getById(item.getItemId()).getName());
//        Label lblItemName = createLabel(ItemDAO.getInstance().getById(1).getName());
        lblItemName.setFont(Font.font(30));


        Label lblWeight = createLabel("Weight");
        TextField _txtWeight = createTextField(item.getWeight()+"");
//        TextField _txtWeight = createTextField(500 + "");
        HBox hBoxWeight = createHbox(lblWeight, _txtWeight);


//
        Label lblBardana = new Label("Bardana");
        TextField _txtBardana = createTextField(item.getBardana()+"");
//        TextField _txtBardana = createTextField("100");
        HBox hBoxBardana = createHbox(lblBardana, _txtBardana);


        Label lblPrice = new Label("Price");
        TextField _txtPrice = createTextField(item.getPrice()+"");
//        TextField _txtPrice = createTextField(150 + "");
        HBox hBoxPrice = createHbox(lblPrice, _txtPrice);


        Label lblIsIn1KG = new Label("Price In 37.324");
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(!item.isIn1KG());
//        checkBox.setSelected(true);
        HBox hBoxisInMound = createHbox(lblIsIn1KG, checkBox);


        Button updateButton = new Button("Update");

        updateButton.setOnAction(e -> {
            String weightText = _txtWeight.getText();
            double weight = StringToDouble(weightText);
            if (weight <= 0) {
                UtilityClass.getInstance().showAlert("Wrong Weight Input");
                return;
            }

            String bardanaText = _txtBardana.getText();
            double bardana = StringToDouble(bardanaText);

            if (bardana < 0 || bardana>=weight) {
                UtilityClass.getInstance().showAlert("Wrong Bardana Input");
                return;
            }

            String priceText = _txtPrice.getText();
            double price = StringToDouble(priceText);

            if (price <= 0) {
                UtilityClass.getInstance().showAlert("Wrong Price Input");
                return;
            }


            for (int i =0;i<gatePassItemList.size();i++){

                System.out.println(gatePassItemList.get(i).getItemId()+" "+item.getItemId());
                if(gatePassItemList.get(i).getItemId()==item.getItemId()){

                    gatePassItemList.remove(i);
                    System.out.println(gatePassItemList.size());
                    GatePassItem _item = new GatePassItem(item.getItemId(),weight,bardana,price,!checkBox.isSelected());
                    gatePassItemList.add(_item);
                    System.out.println(gatePassItemList.size());

                }

            }

            popupwindow.close();
        });


        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(lblItemName, hBoxWeight, hBoxBardana, hBoxPrice,hBoxisInMound,updateButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 400, 500);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }


    VBox createVbox(Pos value) {
        VBox vbox = new VBox(0);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0;"
                + "-fx-border-insets: 20px;"
                + "-fx-border-color: black");
        return vbox;

    }

    Label createLabel(String txt) {
        Label lbl = new Label(txt);
        lbl.setAlignment(Pos.CENTER_LEFT);
        lbl.minWidth(500);
        return lbl;
    }

    TextField createTextField(String s) {
        TextField txt = new TextField(s);
        txt.setAlignment(Pos.CENTER_LEFT);
        return txt;
    }

    HBox createHbox(Control c1, Control c2) {
        HBox hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER);
        VBox vbox21 = createVbox(Pos.TOP_LEFT);
        VBox vbox22 = createVbox(Pos.TOP_CENTER);
        vbox22.getChildren().add(c1);
        vbox21.getChildren().add(c2);
        hbox2.getChildren().addAll(vbox22, vbox21);
        return hbox2;
    }
}
