package controller;
import dao.CustomerDAO;
import dao.GatePassDAO;
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
import model.Customer;
import model.GatePass;
import utility.DataItemCallback;
import utility.DataListCallback;
import utility.UtilityClass;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    Button btnClose;
    @FXML
    TableView<Customer> customerTable;
    @FXML
    TableColumn<Customer,String> colId,colPhone,colName,colAction;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtCustomer;
    @FXML
    TextField txtPhone;
    CustomerDAO instance;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = CustomerDAO.getInstance();
        initCustomerTable();
    }

    void initCustomerTable(){

        instance.getAll(new DataListCallback<Customer>() {
            @Override
            public void OnSuccess(ObservableList<Customer> list) {
                if(list==null|| list.size()<1)
                    return;
                colId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> param) {
                        return new SimpleStringProperty(param.getValue().getId()+"");
                    }
                });
                colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> param) {
                        return new SimpleStringProperty(param.getValue().getName());
                    }
                });
                colPhone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> param) {
                        return new SimpleStringProperty(param.getValue().getPhone());
                    }
                });

                Callback<TableColumn<Customer, String>, TableCell<Customer, String>> cellFactory = new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {
                    @Override
                    public TableCell<Customer, String> call(TableColumn<Customer, String> param) {
                        final TableCell<Customer, String> cell = new TableCell<Customer, String>() {

                            final Button btn = new Button("Update");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!empty) {
                                    Customer customer = getTableView().getItems().get(getIndex());
                                    HBox box = new HBox(20);
                                    Button btn = new Button("Delete");
                                    Button btnShowStock = new Button("Show Detail");
                                    btn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            confirmDelete(customer);
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
                customerTable.setItems(list);
            }

            @Override
            public void OnFailed(String msg) {

            }
        });



    }
    void confirmDelete(Customer customer){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Do you want to delete Customer "+customer.getId()+" "+customer.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            GatePassDAO.getInstance().searchGatePass("", customer.getId()+"", new DataListCallback<GatePass>() {
                @Override
                public void OnSuccess(ObservableList<GatePass> list) {
                    if(list==null || list.size()<1){
                        instance.delete(customer, new DataItemCallback<Customer>() {
                            @Override
                            public void OnSuccess(Customer _customer) {
                                initCustomerTable();
                                UtilityClass.getInstance().showAlert("Delete Successfully");
                            }

                            @Override
                            public void OnFailed(String msg) {
                                UtilityClass.getInstance().showErrorPopup("Delete Failed",null);
                            }
                        });

                    }else{
                        UtilityClass.getInstance().showAlert("Cant delete this customer");
                    }
                }

                @Override
                public void OnFailed(String msg) {
                        UtilityClass.getInstance().showErrorPopup(msg,null);
                }
            });
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    @FXML
    void insertCustomer() {
        String customerName = txtCustomer.getText().toString();
        String customerPhone = txtPhone.getText().toString();
        if(!customerName.equals("")&&!customerPhone.equals("")){
            Customer customer = new Customer(customerName,customerPhone);
                instance.insert(customer, new DataItemCallback<Customer>() {
                    @Override
                    public void OnSuccess(Customer _customer) {
                        initCustomerTable();
                        txtCustomer.clear();
                        txtPhone.clear();
                    }

                    @Override
                    public void OnFailed(String msg) {
                    UtilityClass.getInstance().showErrorPopup("Something went wrong",null);
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
