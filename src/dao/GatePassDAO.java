package dao;

import com.sun.corba.se.spi.ior.IdentifiableBase;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GatePass;
import model.GatePassItem;
import model.Item;
import utility.ActionCallback;
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
    public GatePass currentGatePass;

    public static GatePassDAO getInstance() {
        if (instance == null)
            instance = new GatePassDAO();
        return instance;
    }

    public GatePass getById(long id) {
//
//        for (GatePass r : gatePasslist) {
//            if (r.getId() == id) {
//                return r;
//            }
//        }
        return Database.getInstance().getGatePassById(id);
    }

    public void searchGatePass(String date, String vendorId, DataListCallback<GatePass> callback) {

        Database.getInstance().searchGatePass(date, vendorId, new DataListCallback<GatePass>() {
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

    public void getItemListByItemId(long id, DataListCallback<GatePassItem> callback) {
        Database.getInstance().getGatePassItemListByItemId(id, new DataListCallback<GatePassItem>() {
            @Override
            public void OnSuccess(ObservableList<GatePassItem> list) {
                callback.OnSuccess(list);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }
    ActionCallback onWindowCloses;
    public void setCurrentGatePass(long id,ActionCallback callback) {
        currentGatePass = getById(id);
        onWindowCloses = callback;
    }

    public void setCurrentGatePassNull() {
        currentGatePass = null;
        if(onWindowCloses!=null)
            onWindowCloses.OnAction();
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

    public void update(GatePass gatePass, ObservableList<GatePassItem> itemList, DataItemCallback<GatePass> callback) {

        Database.getInstance().deleteGatePassItems(gatePass.getId());
        Database.getInstance().updateGatePass(gatePass, new DataItemCallback<GatePass>() {
            @Override
            public void OnSuccess(GatePass _gatePass) {
                insertGatePassItemList(itemList, _gatePass.getId(), new Callback() {
                    @Override
                    public void OnSuccess() {
                        callback.OnSuccess(_gatePass);
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

    public void insertGatePassItemList(ObservableList<GatePassItem> list, long gatePassId,Callback callback) {

        for (GatePassItem item : list) {
            item.setGatePassId(gatePassId);
            Database.getInstance().insertGatePassItem(item,callback);
        }
        callback.OnSuccess();
    }

    public void getGatePassItemListById(long gatePassId, DataListCallback<GatePassItem> callback) {

        Database.getInstance().getGatePassItemList(gatePassId, new DataListCallback<GatePassItem>() {
            @Override
            public void OnSuccess(ObservableList<GatePassItem> list) {
                callback.OnSuccess(list);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });

    }


    public void delete(GatePass gatePass) {

        if (gatePass != null)
            gatePasslist.remove(gatePass);

    }

    public long getLastId() {

        return gatePasslist.get(gatePasslist.size() - 1).getId();
    }
}
