package controller;

import dao.ItemDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Item;
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
    ListView itemListView;
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
        showItemList();
    }

    void showItemList() {
        instance.getAll(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList<Item> list) {
                itemListView.setItems(list);
                itemListView.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
                    @Override
                    public ListCell<Item> call(ListView<Item> param) {

                        ListCell<Item> cell = new ListCell<Item>() {
                            @Override
                            protected void updateItem(Item item, boolean empty) {
                                super.updateItem(item, empty);

                                if (!empty) {
                                    HBox box = new HBox(20);
                                    String s = "\t"+item.getId()+" : "+item.getName();
                                    if (item.isAssemble())
                                        s = s + "     (Assembly Item)";

                                    Label lblName = new Label(s);
                                    Button btn = new Button("x");
                                    Button btnShowStock = new Button("Show Stock");
                                    btn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            confirmDelete(item);
                                        }
                                    });
//                            btnShowStock.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    showStockScreen();
//                                }
//                            });

                                    box.getChildren().addAll(btn, lblName);
                                    setGraphic(box);
                                    //setText(value);
                                } else
                                    setGraphic(null);
                            }
                        };
                        return cell;
                    }
                });
            }

            @Override
            public void OnFailed(String msg) {
                closeWindow();
            }
        });


    }

    void showStockScreen() {


    }

    void confirmDelete(Item item) {

        UtilityClass.getInstance().confirmDelete
                ("Are you sure?", "Do you want to delete item " + item.getName(), new ActionCallback() {
                    @Override
                    public void OnAction() {
                        instance.delete(item, new DataItemCallback<Item>() {
                            @Override
                            public void OnSuccess(Item _item) {
                                UtilityClass.getInstance().showAlert("Delete Successful");
                                itemListView.refresh();

                            }

                            @Override
                            public void OnFailed(String msg) {
                                UtilityClass.getInstance().showErrorPopup(msg, new ActionCallback() {
                                    @Override
                                    public void OnAction() {
                                        itemListView.refresh();
                                    }

                                    @Override
                                    public void OnCancel() {

                                    }
                                });
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
                    showItemList();
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
