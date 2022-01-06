package controller;

import dao.PartyDAO;
import dao.RawMaterialDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Party;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PartyController implements Initializable {
    @FXML
    Button btnClose;
    @FXML
    ListView partyListView;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtParty;
    PartyDAO instance;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = PartyDAO.getInstance();
        showAllProduct();
    }

    void showAllProduct() {
        partyListView.setItems(instance.GetAll());


        partyListView.setCellFactory(new Callback<ListView<Party>, ListCell<Party>>() {
            @Override
            public ListCell<Party> call(ListView<Party> param) {

                ListCell<Party> cell = new ListCell<Party>() {
                    @Override
                    protected void updateItem(Party party, boolean empty) {
                        super.updateItem(party, empty);

                        if (!empty) {
                            HBox box = new HBox(20);
                            Label lblName = new Label(party.getId()+" "+party.getName());
                            Button btn = new Button("x");
                            Button btnShowStock = new Button("Show Detail");
                            btn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    confirmDelete(party);
                                }
                            });
                            btnShowStock.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    showDetailScreen();
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

    void showDetailScreen(){



    }

    void confirmDelete(Party party){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Do you want to delete Party "+party.getId()+" "+party.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            instance.delete(party);
        } else {
            // ... user chose CANCEL or closed the dialog
        }



    }

    @FXML
    void insertparty() {
        String partyName = txtParty.getText().toString();
        if(!partyName.equals("")){
            Party party = new Party(txtParty.getText().toString());
            instance.insert(party);
            txtParty.clear();
        }
    }


    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // do what you have to do
        stage.close();
    }




}
