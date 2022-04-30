package dao;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import model.Vendor;
import utility.DataItemCallback;
import utility.DataListCallback;

public class VendorDAO {

    private static ObservableList<Vendor> list;
    public VendorDAO(){
        list = FXCollections.observableArrayList();
//
//        Vendor r = new Vendor("Ali","321321");
//        Vendor r1 = new Vendor("Usama","321321");
//        Vendor r2 = new Vendor("Hassan","13213213");
//
//        list.add(r);
//        list.add(r1);
//        list.add(r2);
    }
    public static VendorDAO instance;
    public static VendorDAO getInstance(){
        if(instance==null)
            instance = new VendorDAO();
       return  instance;
    }

    public Vendor getById(long id){
        for (Vendor r:list) {
            if(r.getId()==id){
                return r;
            }
        }
        return null;
    }

    public Vendor getByListIndex(int index){

        return list.get(index);
    }

    public void getAll(DataListCallback<Vendor> callback){
        list = FXCollections.observableArrayList();
        Database.getInstance().getAllVendor(new DataListCallback<Vendor>() {
            @Override
            public void OnSuccess(ObservableList<Vendor> _list) {
                list = _list;
                callback.OnSuccess(list);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

    public void insert(Vendor vendor,DataItemCallback callback){

        Database.getInstance().insertVendor(vendor, new DataItemCallback<Vendor>() {
            @Override
            public void OnSuccess(Vendor _vendor) {
//                list.add(_vendor);
                getAll(new DataListCallback<Vendor>() {
                    @Override
                    public void OnSuccess(ObservableList<Vendor> list) {
                        callback.OnSuccess(_vendor);
                    }

                    @Override
                    public void OnFailed(String msg) {

                    }
                });

            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

    public void delete(Vendor vendor,DataItemCallback callback){

        if(vendor!=null)
        list.remove(vendor);

        if (vendor != null)
            Database.getInstance().deleteVendor(vendor, new DataItemCallback<Vendor>() {
                @Override
                public void OnSuccess(Vendor _vendor) {
                    list.remove(_vendor);
                    callback.OnSuccess(_vendor);
                }

                @Override
                public void OnFailed(String msg) {
                    callback.OnFailed(msg);
                }
            });


    }

    public long getLastId(){
        return list.get(list.size()-1).getId();
    }
}
