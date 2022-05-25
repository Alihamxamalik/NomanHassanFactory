package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.util.List;

public class StockDAO {
    private static ObservableList<StockItem> stockAddList;
    private static ObservableList<StockItem> stockMinusList;

    public StockDAO() {
        stockAddList = FXCollections.observableArrayList();
    }

    public static StockDAO getInstance() {
        if (instance == null)
            instance = new StockDAO();
        return instance;
    }


    public static StockDAO instance;

    public void getStockItemWeight(long itemId, DataItemCallback<Double> callback) {
        //Get GatePass
        GatePassDAO.getInstance().getItemListByItemId(itemId, new DataListCallback<GatePassItem>() {
            @Override
            public void OnSuccess(ObservableList<GatePassItem> list) {
                stockAddList = FXCollections.observableArrayList();
                stockMinusList = FXCollections.observableArrayList();
                for (GatePassItem item : list) {
                    stockAddList.add(new StockItem(ItemType.PURCHASED, item.getNetWeight(), item.getItemId()));
                }
                //Get Production Items
                ProductionDAO.getInstance().getProductionItemsListById(itemId, new DataListCallback<ProductionItem>() {
                    @Override
                    public void OnSuccess(ObservableList<ProductionItem> list) {
                        for (ProductionItem item : list) {
                            stockMinusList.add(new StockItem(ItemType.PURCHASED, item.getWeight(), item.getItemId()));
                        }
                        //Get Production
                        ProductionDAO.getInstance().getProductionsByItemId(itemId,new DataListCallback<Production>() {
                            @Override
                            public void OnSuccess(ObservableList<Production> list) {
                                for (Production item : list) {
                                    stockAddList.add(new StockItem(ItemType.PRODUCED, item.getWeight(), item.getItemId()));
                                }
                                //Get Sales
                                SalesDAO.getInstance().getItemListByItemId(itemId, new DataListCallback<SalesItem>() {
                                    @Override
                                    public void OnSuccess(ObservableList<SalesItem> list) {
                                        for (SalesItem item : list) {
                                            stockMinusList.add(new StockItem(ItemType.PURCHASED, item.getWeight(), item.getItemId()));
                                        }
                                        double weight = 0;
                                        for (StockItem s : stockAddList) {
                                            weight = weight + s.weight;
                                        }
                                        for (StockItem s : stockMinusList) {
                                            weight = weight - s.weight;
                                        }
                                        callback.OnSuccess(weight);
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

}
