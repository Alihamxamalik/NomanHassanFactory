package controller;

import dao.CustomerDAO;
import dao.SalesDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Customer;
import model.Sales;
import utility.ActionCallback;
import utility.DataListCallback;
import utility.UtilityClass;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SalesViewController implements Initializable {

    @FXML
    TableView<Sales> salesTable;
    ObservableList<Sales> salesObservableList;
    @FXML
    TableColumn<Sales, String> salesId, customerColumn, dateColumn, actionColumn;

    @FXML
    TextField txtSalesId;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox customerChoice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initSales();

    }


    void initSales() {
        if (salesObservableList != null)
            salesObservableList.removeAll();

        SalesDAO.getInstance().getAll(new DataListCallback<Sales>() {
            @Override
            public void OnSuccess(ObservableList<Sales> list) {
                salesObservableList = list;
                initSalesTable();
//                SalesDAO.getInstance().getAll(new DataListCallback<Sales>() {
//                    @Override
//                    public void OnSuccess(ObservableList<Vendor> list) {
//
//                    }
//
//                    @Override
//                    public void OnFailed(String msg) {
//                        UtilityClass.getInstance().showAlert(msg);
//                    }
//                });

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
        customerChoice.getItems().clear();
        CustomerDAO.getInstance().getAll(new DataListCallback<Customer>() {
            @Override
            public void OnSuccess(ObservableList<Customer> list) {
                for (Customer v : list) {
                    customerChoice.getItems().add(v.getId() + " : " + v.getName());
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


    void initSalesTable() {


        salesId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Sales, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Sales, String> param) {
                return new SimpleStringProperty(param.getValue().getId() + "");
            }
        });
        customerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Sales, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Sales, String> param) {
                return new SimpleStringProperty(CustomerDAO.getInstance().getById(param.getValue().getCustomerId()).getName());
            }
        });
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Sales, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Sales, String> param) {
                return new SimpleStringProperty(param.getValue().getDate() + "");
            }
        });
        Callback<TableColumn<Sales, String>, TableCell<Sales, String>> cellFactory
                = //
                new Callback<TableColumn<Sales, String>, TableCell<Sales, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Sales, String> param) {
                        final TableCell<Sales, String> cell = new TableCell<Sales, String>() {

                            final Button btn = new Button("Show Details");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Sales _sales = getTableView().getItems().get(getIndex());
                                        showDetail(_sales.getId());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionColumn.setCellFactory(cellFactory);
        salesTable.setItems(salesObservableList);
    }

    void showDetail(long salesId) {

        SalesDAO.getInstance().setCurrentSales(salesId, new ActionCallback() {
            @Override
            public void OnAction() {
                initSales();
            }

            @Override
            public void OnCancel() {

            }
        });
        OpenSalesScreen();
    }

    @FXML
    private void findSalesById() {

        String salesIdText = txtSalesId.getText();

        for (int i = 0; i < salesTable.getItems().size(); i++) {
            salesTable.getItems().clear();
        }

        salesObservableList.removeAll();
        try {
            long salesId = Long.parseLong(salesIdText);
            Sales sales = SalesDAO.getInstance().getById(salesId);
            if (sales != null) {
                salesObservableList.add(sales);
            } else {
                UtilityClass.getInstance().showAlert("No Sales Found");
            }
        } catch (Exception e) {
            UtilityClass.getInstance().showAlert("No Sales Found");

        }

    }

    @FXML
    private void searchSales() {
        String dateString = "";
        try {
            dateString = datePicker.getValue().toString().trim();
            System.out.println(dateString);
        }catch (Exception e){
            dateString = "";
        }
        int customerIndex = customerChoice.getSelectionModel().getSelectedIndex();
        String customerId = "";
        try {
            customerId = CustomerDAO.getInstance().getByListIndex(customerIndex).getId() + "";
        }catch (Exception e){
            customerId = "";
        }

        if(dateString==""&&customerId=="")
            return;
        SalesDAO.getInstance().searchSales(dateString, customerId, new DataListCallback<Sales>() {
            @Override
            public void OnSuccess(ObservableList<Sales> list) {
                System.out.println(list.size());
                for (int i = 0; i < salesTable.getItems().size(); i++)
                    salesTable.getItems().clear();

                salesObservableList.removeAll();

                for (int i = 0; i < list.size(); i++)
                    salesObservableList.add(list.get(i));
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

    @FXML
    private void refresh() {
        txtSalesId.clear();
        datePicker.getEditor().clear();
        datePicker.setValue(null);
        customerChoice.getSelectionModel().clearSelection();
        initSales();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) salesTable.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void OpenSalesScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/sales_entry_layout.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Gate Pass");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}