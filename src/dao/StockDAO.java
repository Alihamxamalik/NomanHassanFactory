package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GatePassItem;
import model.Item;
import model.ItemType;
import model.StockItem;
import utility.DataItemCallback;
import utility.DataListCallback;

public class StockDAO {
    private static ObservableList<StockItem> stockList;

    public StockDAO() {
        stockList = FXCollections.observableArrayList();
    }

    public static StockDAO getInstance() {
        if (instance == null)
            instance = new StockDAO();
        return instance;
    }


    public static StockDAO instance;

    public void getStockItemWeight(long itemId, DataItemCallback<Double> callback) {
        GatePassDAO.getInstance().getItemListByItemId(itemId, new DataListCallback<GatePassItem>() {
            @Override
            public void OnSuccess(ObservableList<GatePassItem> list) {
                stockList = FXCollections.observableArrayList();
                for (GatePassItem item : list) {
                    stockList.add(new StockItem(ItemType.PURCHASED, item.getNetWeight(), item.getItemId()));
                }

                double weight = 0;
                for (StockItem s : stockList) {
                    weight = weight + s.weight;
                }
                callback.OnSuccess(weight);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

}
