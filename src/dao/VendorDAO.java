package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Vendor;

public class VendorDAO {

    private static ObservableList<Vendor> list;
    public VendorDAO(){
        list = FXCollections.observableArrayList();

        Vendor r = new Vendor(1,"Ali");
        Vendor r1 = new Vendor(2,"Usama");
        Vendor r2 = new Vendor(3,"Hassan");

        list.add(r);
        list.add(r1);
        list.add(r2);
    }
    public static VendorDAO instance;
    public static VendorDAO getInstance(){
        if(instance==null)
            instance = new VendorDAO();
       return  instance;
    }

    public Vendor getById(Double id){

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

    public ObservableList<Vendor> GetAll(){
        return list;
    }

    public void insert(Vendor Vendor){
        list.add(Vendor);
    }

    public void delete(Vendor Vendor){

        if(Vendor!=null)
        list.remove(Vendor);

    }

    public long getLastId(){
        return list.get(list.size()-1).getId();
    }
}
