package controller;

import dao.ProductDAO;
import dao.RawMaterialDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Product;
import model.RawMaterial;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RawMaterialController implements Initializable {
    @FXML
    Button btnClose;
    @FXML
    ListView materialListView;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtMaterial;
    RawMaterialDAO instance;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = RawMaterialDAO.getInstance();
        showAllProduct();
    }

    void showAllProduct() {
        materialListView.setItems(instance.GetAll());


        materialListView.setCellFactory(new Callback<ListView<RawMaterial>, ListCell<RawMaterial>>() {
            @Override
            public ListCell<RawMaterial> call(ListView<RawMaterial> param) {

                ListCell<RawMaterial> cell = new ListCell<RawMaterial>() {
                    @Override
                    protected void updateItem(RawMaterial material, boolean empty) {
                        super.updateItem(material, empty);

                        if (!empty) {
                            HBox box = new HBox(20);
                            Label lblName = new Label(material.getName());
                            Button btn = new Button("x");
                            Button btnShowStock = new Button("Show Stock");
                            btn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                     confirmDelete(material);
                                }
                            });
                            btnShowStock.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    showStockScreen();
                                }
                            });

                            box.getChildren().addAll(btn,btnShowStock,lblName );
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

    void confirmDelete(RawMaterial material){
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
        String materialName = txtMaterial.getText().toString();
        if(!materialName.equals("")){
            RawMaterial material = new RawMaterial(txtMaterial.getText().toString());
            instance.insert(material);
            txtMaterial.clear();
        }
    }


    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
