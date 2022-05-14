package controller;

import dao.GatePassDAO;
import dao.ItemDAO;
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
import model.GatePassItem;
import model.Item;
import model.Vendor;
import utility.ActionCallback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemController implements Initializable {
    @FXML
    Button btnClose;
    @FXML
    TableView<Item> itemTable;
    @FXML
    TableColumn<Item, String> colId, colName, colAction, colAssemble;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtItem;
    @FXML
    CheckBox checkBoxAssembly;
    ItemDAO instance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = ItemDAO.getInstance();
        initVendorTable();
//        showItemList();
    }


    void initVendorTable() {

        instance.getAll(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList<Item> list) {
                if (list == null || list.size() < 1)
                    return;
                colId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                        return new SimpleStringProperty(param.getValue().getId() + "");
                    }
                });
                colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                        return new SimpleStringProperty(param.getValue().getName());
                    }
                });
                colAssemble.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                        return new SimpleStringProperty(param.getValue().isAssemble() ? "Assemble" : "Non Assemble");
                    }
                });

                Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory = new Callback<TableColumn<Item, String>, TableCell<Item, String>>() {
                    @Override
                    public TableCell<Item, String> call(TableColumn<Item, String> param) {
                        final TableCell<Item, String> cell = new TableCell<Item, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!empty) {
                                    Item _item = getTableView().getItems().get(getIndex());
                                    HBox box = new HBox(20);
//                                    Label lblName = new Label(""+vendor.getId()+" : \t"+vendor.getName()+"\t : "+vendor.getPhone());
                                    Button btn = new Button("Delete");
                                    Button btnShowStock = new Button("Show Detail");
                                    btn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            confirmDelete(_item);
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
                itemTable.setItems(list);
            }

            @Override
            public void OnFailed(String msg) {

            }
        });


    }

    void confirmDelete(Item item) {

        UtilityClass.getInstance().confirmDelete
                ("Are you sure?", "Do you want to delete item " + item.getName(), new ActionCallback() {
                    @Override
                    public void OnAction() {

                        GatePassDAO.getInstance().getGatePassItemListById(item.getId(), new DataListCallback<GatePassItem>() {
                            @Override
                            public void OnSuccess(ObservableList<GatePassItem> list) {
                                if (list == null || list.size() < 1) {
                                    instance.delete(item, new DataItemCallback<Item>() {
                                        @Override
                                        public void OnSuccess(Item _item) {

                                            UtilityClass.getInstance().showAlert("Delete Successful");
                                            initVendorTable();

                                        }

                                        @Override
                                        public void OnFailed(String msg) {
                                            UtilityClass.getInstance().showErrorPopup(msg, new ActionCallback() {
                                                @Override
                                                public void OnAction() {
                                                    initVendorTable();
                                                }

                                                @Override
                                                public void OnCancel() {

                                                }
                                            });
                                        }
                                    });
                                } else {
                                    UtilityClass.getInstance().showAlert("Cant delete this item");
                                }
                            }

                            @Override
                            public void OnFailed(String msg) {
                                UtilityClass.getInstance().showErrorPopup(msg, null);
                            }
                        });


                    }

                    @Override
                    public void OnCancel() {

                    }
                });

//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle();
//        alert.setHeaderText();
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
//            // ... user chose OK
//
//        } else {
//            // ... user chose CANCEL or closed the dialog
//        }
    }

    @FXML
    void insertMaterial() {
        String materialName = txtItem.getText();
        if (!materialName.equals("")) {
            Item material = new Item(txtItem.getText(), checkBoxAssembly.isSelected());
            instance.insert(material, new utility.Callback() {
                @Override
                public void OnSuccess() {
                    txtItem.clear();
                    checkBoxAssembly.setSelected(false);
                    initVendorTable();
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
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
