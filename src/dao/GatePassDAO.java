package dao;

import com.sun.corba.se.spi.ior.IdentifiableBase;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GatePass;
import model.GatePassItem;
import model.Item;
import utility.Callback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.util.List;

public class GatePassDAO {

    private static ObservableList<GatePass> gatePasslist;

    public GatePassDAO() {
        gatePasslist = FXCollections.observableArrayList();
    }

    public static GatePassDAO instance;

    public static GatePassDAO getInstance() {
        if (instance == null)
            instance = new GatePassDAO();
        return instance;
    }

    public GatePass getById(int id) {

        for (GatePass r : gatePasslist) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public GatePass getByListIndex(int index) {

        return gatePasslist.get(index);
    }

    public void GetAll(DataListCallback callback) {
        Database.getInstance().getAllGatePass(new DataListCallback<GatePass>() {
            @Override
            public void OnSuccess(ObservableList<GatePass> list) {
                callback.OnSuccess(list);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

    public void insert(GatePass gatePass, DataItemCallback<GatePass> callback) {
        Database.getInstance().insertGatePass(gatePass, new DataItemCallback<GatePass>() {
            @Override
            public void OnSuccess(GatePass _gatePass) {
                System.out.println("ID " + _gatePass.getId());
                gatePasslist.add(_gatePass);
                callback.OnSuccess(_gatePass);
            }

            @Override
            public void OnFailed(String msg) {

            }
        });
    }

    public void insertGatePassItemList(ObservableList<GatePassItem> list, long gatePassId, Callback callback) {

        for (GatePassItem item : list) {
            item.setGatePassId(gatePassId);
            Database.instance.insertGatePassItem(item, new DataItemCallback<GatePassItem>() {
                @Override
                public void OnSuccess(GatePassItem gatePassItem) {

                }

                @Override
                public void OnFailed(String msg) {
                    Database.getInstance().deleteGatePassItems(item, new DataItemCallback<GatePassItem>() {
                        @Override
                        public void OnSuccess(GatePassItem _item) {
                            callback.OnFailed(msg);

                        }

                        @Override
                        public void OnFailed(String msg) {
                            callback.OnFailed(msg);

                        }
                    });
                    return;
                }
            });
        }
        callback.OnSuccess();
    }

    public void delete(GatePass gatePass) {

        if (gatePass != null)
            gatePasslist.remove(gatePass);

    }

    public long getLastId() {

        return gatePasslist.get(gatePasslist.size() - 1).getId();
    }
}
