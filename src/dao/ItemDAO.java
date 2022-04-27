package dao;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import utility.Callback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.util.List;

public class ItemDAO {

    private static ObservableList<Item> list;

    public ItemDAO() {
        list = FXCollections.observableArrayList();


//        Item r = new Item("Dabba",false);
//        Item r1 = new Item("Chilka",false);
//        Item r2 = new Item("Niyara",false);
//
//        list.add(r);
//        list.add(r1);
//        list.add(r2);
    }

    public static ItemDAO instance;

    public static ItemDAO getInstance() {
        if (instance == null)
            instance = new ItemDAO();
        return instance;
    }

    public Item getById(int id) {

        for (Item r : list) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public Item getByListIndex(int index) {

        return list.get(index);
    }

    public void getAll(DataListCallback<Item> callback) {

        Database.getInstance().getAllItem(new DataListCallback<Item>() {
            @Override
            public void OnSuccess(ObservableList _list) {
                list = _list;
                callback.OnSuccess(list);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

    public void update(Item item,DataItemCallback callback){
        Database.getInstance().updateItem(item, new DataItemCallback<Item>() {
            @Override
            public void OnSuccess(Item _item) {
                list.remove(item);
                list.add(_item);
                callback.OnSuccess(_item);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

    public void insert(Item item, Callback callback) {
        Database.getInstance().insertItem(item, new DataItemCallback<Item>() {
            @Override
            public void OnSuccess(Item _item) {
                list.add(_item);
                callback.OnSuccess();
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

    public void delete(Item item, DataItemCallback callback) {

        if (item != null)
            Database.getInstance().deleteItem(item, new DataItemCallback<Item>() {
                @Override
                public void OnSuccess(Item _item) {
                    list.remove(_item);
                    callback.OnSuccess(_item);
                }

                @Override
                public void OnFailed(String msg) {
                    callback.OnFailed(msg);
                }
            });

    }

    public long getLastId() {

        return list.get(list.size() - 1).getId();
    }
}
