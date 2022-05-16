package dao;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GatePass;
import model.Production;
import model.ProductionItem;
import utility.ActionCallback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.lang.reflect.AccessibleObject;

public class ProductionDAO {

    private static ObservableList<Production> productionList;

    public ProductionDAO() {
        productionList = FXCollections.observableArrayList();
    }

    public static ProductionDAO instance;
    public Production currentProduction;

    public static ProductionDAO getInstance() {
        if (instance == null)
            instance = new ProductionDAO();
        return instance;
    }
    public void setCurrentProduction(long id) {
        currentProduction = getById(id);
    }
    public void insertProduction(Production production, DataItemCallback<Production> callback){
        Database.getInstance().insertProduction(production,callback);
    }
    public void insertProductionItem(ObservableList<ProductionItem> items,long productionId, ActionCallback callback){
        Database.getInstance().insertProductionItem(items,productionId,callback);
    }
    public void getAllProduction(DataListCallback<Production> callback){
        Database.getInstance().getAllProduction(callback);
    }
    public void searchProduction(String date,String itemId,DataListCallback<Production> callback){
        Database.getInstance().searchProduction(date,itemId,callback);
    }
    public Production getById(long id) {
        return Database.getInstance().getProductionById(id);
    }
    public void getProductionItemsListById(long productionId,DataListCallback<ProductionItem> callback){
        Database.getInstance().getProductionItemList(productionId,callback);

    }
}
