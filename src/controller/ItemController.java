package controller;

import dao.ItemDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Item;

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
        initRawMaterialList();
    }

    void initRawMaterialList() {
        itemListView.setItems(instance.GetAll());
        itemListView.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
            @Override
            public ListCell<Item> call(ListView<Item> param) {

                ListCell<Item> cell = new ListCell<Item>() {
                    @Override
                    protected void updateItem(Item material, boolean empty) {
                        super.updateItem(material, empty);

                        if (!empty) {
                            HBox box = new HBox(20);
                            String s = material.getName();
                            if(material.isAssemble())
                                s= s + " (Assembly Item)";

                            Label lblName = new Label(s);
                            Button btn = new Button("x");
                            Button btnShowStock = new Button("Show Stock");
                            btn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                     confirmDelete(material);
                                }
                            });
//                            btnShowStock.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    showStockScreen();
//                                }
//                            });

                            box.getChildren().addAll(btn,lblName );
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

    void showStockScreen(){



    }

    void confirmDelete(Item material){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Do you want to delete item "+material.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            instance.delete(material);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    void insertMaterial() {
        String materialName = txtItem.getText();
        if(!materialName.equals("")){
            Item material = new Item( txtItem.getText(), checkBoxAssembly.isSelected());
            instance.insert(material);
            txtItem.clear();
            checkBoxAssembly.setSelected(false);
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
