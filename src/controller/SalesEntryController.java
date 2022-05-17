package controller;

import dao.CustomerDAO;
import dao.ItemDAO;
import dao.SalesDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;
import utility.ActionCallback;
import utility.DataItemCallback;
import utility.DataListCallback;
import utility.UtilityClass;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SalesEntryController implements Initializable {

    @FXML
    ChoiceBox<String> itemChoice;
    @FXML
    ChoiceBox<String> customerChoice;
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
    ObservableList<SalesItem> salesItemList;
    @FXML
    TableView<SalesItem> salesItemTable;
    @FXML
    TableColumn<SalesItem, String>
            indexColumn, nameColumn, weightColumn, bardanaColumn, netWeightColumn, priceColumn, totalPriceColumn, actionColumn;

    Sales currentSales;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCustomerChoiceBox();
        initItemChoiceBox();
        salesItemList = FXCollections.observableArrayList();
        initSalesItemTable();
        onPriceToggle();
        currentSales = SalesDAO.getInstance().currentSales;
        if (currentSales != null) {
            System.out.println("Not Null");
            setCurrentSalesView();
        }
//        createUpdatePopup(null);
    }

    void setCurrentSalesView() {

        //Set Customer
        CustomerDAO.getInstance().getAll(new DataListCallback<Customer>() {
            @Override
            public void OnSuccess(ObservableList<Customer> list) {

                for (int customerIndex = 0; customerIndex < list.size(); customerIndex++) {
                    if (list.get(customerIndex).getId() == currentSales.getCustomerId()) {
                        customerChoice.getSelectionModel().select(customerIndex);
                    }
                }
            }

            @Override
            public void OnFailed(String msg) {

            }
        });
        //Set Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(currentSales.getDate(), formatter);
        entryDatePicker.setValue(localDate);
        // Set Items

        SalesDAO.getInstance().getSalesItemListById(currentSales.getId(), new DataListCallback<SalesItem>() {
            @Override
            public void OnSuccess(ObservableList<SalesItem> list) {
                for (SalesItem item : list) {
                    salesItemList.add(item);
                }
                salesItemTable.refresh();
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

        if (customerChoice.getSelectionModel().isEmpty()) {
            UtilityClass.getInstance().showAlert("Please Select Customer");
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
    public void saveSales() {

        if (entryDatePicker.getValue() == null && entryDatePicker.isEditable()) {
            UtilityClass.getInstance().showAlert("Please set date first");
            return;
        } else {
            System.out.println(entryDatePicker.getValue().toString());
        }

        if (salesItemList.size() < 1) {
            UtilityClass.getInstance().showAlert("Please Add Item First");
            return;
        }

        int customerIndex = customerChoice.getSelectionModel().getSelectedIndex();
        if (currentSales == null) {
            currentSales = new Sales
                    (CustomerDAO.getInstance().getByListIndex(customerIndex).getId(), entryDatePicker.getValue().toString());
            SalesDAO.getInstance().insert(currentSales, new DataItemCallback<Sales>() {
                @Override
                public void OnSuccess(Sales sales) {

                    SalesDAO.getInstance().insertSalesItemList(salesItemList, sales.getId(), new utility.Callback() {
                        @Override
                        public void OnSuccess() {
                            clearScreen();
                            UtilityClass.getInstance().showAlert("Sales Save Successfully");
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
            currentSales = new Sales
                    (currentSales.getId(),CustomerDAO.getInstance().getByListIndex(customerIndex).getId(), entryDatePicker.getValue().toString());

            SalesDAO.getInstance().update(currentSales, salesItemList, new DataItemCallback<Sales>() {
                @Override
                public void OnSuccess(Sales sales) {
                    System.out.println(sales.getCustomerId());
                    clearScreen();
                    SalesDAO.getInstance().setCurrentSalesNull();
                    closeWindow();
                    UtilityClass.getInstance().showAlert("Sales Update Successfully");
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
        customerChoice.getSelectionModel().clearSelection();
        entryDatePicker.getEditor().clear();
        salesItemList.clear();
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
        for (SalesItem i: salesItemList) {
            if(ItemDAO.instance.getByListIndex(itemIndex).getId()==i.getItemId())
            {
                UtilityClass.getInstance().showAlert("Item Already Added");
                return;
            }
        }

        SalesItem item = new SalesItem(
                ItemDAO.instance.getByListIndex(itemIndex).getId(),
                weightValue,
                bardanaValue,
                priceValue,
                !priceToggle.isSelected()
        );

        salesItemList.add(item);
        System.out.println(salesItemList.size());
        clearItem();
    }


    void initSalesItemTable() {

        indexColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SalesItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SalesItem, String> param) {
                return new SimpleStringProperty(salesItemList.indexOf(param.getValue()) + 1 + "");
            }
        });
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SalesItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SalesItem, String> param) {
                System.out.println(param.getValue().getItemId()+"");
                String s = "";
                Item item = ItemDAO.getInstance().getById(param.getValue().getItemId());
                if(item!=null){
                    s = item.getName();
                }
                return new SimpleStringProperty(s);
//                return new SimpleStringProperty(param.getValue().getItemId()+"");
            }
        });
        weightColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SalesItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SalesItem, String> param) {
                return new SimpleStringProperty(param.getValue().getWeight() + "");
            }
        });
        bardanaColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SalesItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SalesItem, String> param) {
                return new SimpleStringProperty(param.getValue().getBardana() + "");
            }
        });
        netWeightColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SalesItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SalesItem, String> param) {
                return new SimpleStringProperty(param.getValue().getNetWeight() + "");
            }
        });

        priceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SalesItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SalesItem, String> param) {

                String s = "";
                if (param.getValue().isIn1KG())
                    s = "(1KG)";
                else
                    s = "(Maund)";

                String text = param.getValue().getPrice() + " " + s;
                return new SimpleStringProperty(text);
            }
        });
        totalPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SalesItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SalesItem, String> param) {
                return new SimpleStringProperty(Math.round(param.getValue().getTotalPrice()) + "");
            }
        });

        actionColumn.setCellFactory(actionButton());
        salesItemTable.setItems(salesItemList);

    }

    @FXML
    public void deleteItem() {
        if (salesItemTable.getSelectionModel().getSelectedItem() != null) {
            salesItemTable.getItems().remove(salesItemTable.getSelectionModel().getSelectedItem());
        }
    }

    void initCustomerChoiceBox() {

        customerChoice.getItems().clear();

        CustomerDAO.getInstance().getAll(new DataListCallback<Customer>() {
            @Override
            public void OnSuccess(ObservableList<Customer> list) {
                System.out.println(list.size());
                for (Customer c : list) {
                    customerChoice.getItems().add(c.getId() + " : " + c.getName());
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


    public Callback<TableColumn<SalesItem, String>, TableCell<SalesItem, String>> actionButton() {
        Callback<TableColumn<SalesItem, String>, TableCell<SalesItem, String>> cellFactory
                = //
                new Callback<TableColumn<SalesItem, String>, TableCell<SalesItem, String>>() {
                    @Override
                    public TableCell call(final TableColumn<SalesItem, String> param) {
                        final TableCell<SalesItem, String> cell = new TableCell<SalesItem, String>() {

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

    void createUpdatePopup(SalesItem item) {
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


            for (int i =0;i<salesItemList.size();i++){

                System.out.println(salesItemList.get(i).getItemId()+" "+item.getItemId());
                if(salesItemList.get(i).getItemId()==item.getItemId()){

                    salesItemList.remove(i);
                    System.out.println(salesItemList.size());
                    SalesItem _item = new SalesItem(item.getItemId(),weight,bardana,price,!checkBox.isSelected());
                    salesItemList.add(_item);
                    System.out.println(salesItemList.size());

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
