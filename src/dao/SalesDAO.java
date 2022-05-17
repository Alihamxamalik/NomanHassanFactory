package dao;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Sales;
import model.SalesItem;
import utility.ActionCallback;
import utility.Callback;
import utility.DataItemCallback;
import utility.DataListCallback;

public class SalesDAO {

    private static ObservableList<Sales> salesObservableList;

    public SalesDAO() {
        salesObservableList = FXCollections.observableArrayList();
    }

    public static SalesDAO instance;
    public Sales currentSales;

    public static SalesDAO getInstance() {
        if (instance == null)
            instance = new SalesDAO();
        return instance;
    }

    public Sales getById(long id) {

        return Database.getInstance().getSalesById(id);
    }

    public void searchSales(String date, String customerId, DataListCallback<Sales> callback) {
        Database.getInstance().searchSales(date, customerId, callback);

    }

    public void getItemListByItemId(long id, DataListCallback<SalesItem> callback) {
        Database.getInstance().getSalesItemListByItemId(id, callback);
    }
    ActionCallback onWindowCloses;
    public void setCurrentSales(long id,ActionCallback callback) {
        currentSales = getById(id);
        onWindowCloses = callback;
    }

    public void setCurrentSalesNull() {
        currentSales = null;
        if(onWindowCloses!=null)
            onWindowCloses.OnAction();
    }

    public Sales getByListIndex(int index) {

        return salesObservableList.get(index);
    }

    public void GetAll(DataListCallback callback) {
        Database.getInstance().getAllSales(new DataListCallback<Sales>() {
            @Override
            public void OnSuccess(ObservableList<Sales> list) {
                callback.OnSuccess(list);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

    public void insert(Sales sales, DataItemCallback<Sales> callback) {
        Database.getInstance().insertSales(sales, new DataItemCallback<Sales>() {
            @Override
            public void OnSuccess(Sales _sales) {
                System.out.println("ID " + _sales.getId());
                salesObservableList.add(_sales);
                callback.OnSuccess(_sales);
            }

            @Override
            public void OnFailed(String msg) {

            }
        });
    }

    public void update(Sales sales, ObservableList<SalesItem> itemList, DataItemCallback<Sales> callback) {

        Database.getInstance().deleteSalesItems(sales.getId());
        Database.getInstance().updateSales(sales, new DataItemCallback<Sales>() {
            @Override
            public void OnSuccess(Sales _sales) {
                insertSalesItemList(itemList, _sales.getId(), new Callback() {
                    @Override
                    public void OnSuccess() {
                        callback.OnSuccess(_sales);
                    }

                    @Override
                    public void OnFailed(String msg) {
                        callback.OnFailed(msg);
                    }
                });
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });


    }

    public void insertSalesItemList(ObservableList<SalesItem> list, long salesId, Callback callback) {

        for (SalesItem item : list) {
            item.setSalesId(salesId);
            Database.instance.insertSalesItem(item, new DataItemCallback<SalesItem>() {
                @Override
                public void OnSuccess(SalesItem salesItem) {

                }

                @Override
                public void OnFailed(String msg) {
                    Database.getInstance().deleteSalesItems(item.getSalesId());
                    return;
                }
            });
        }
        callback.OnSuccess();
    }

    public void getSalesItemListById(long salesId, DataListCallback<SalesItem> callback) {
        Database.getInstance().getSalesItemList(salesId, callback);
    }


    public void delete(Sales sales) {

        if (sales != null)
            salesObservableList.remove(sales);

    }

    public long getLastId() {

        return salesObservableList.get(salesObservableList.size() - 1).getId();
    }
}
