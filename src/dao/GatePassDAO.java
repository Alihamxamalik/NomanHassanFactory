package dao;
import com.sun.corba.se.spi.ior.IdentifiableBase;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GatePass;
import model.GatePassItem;
import model.Item;
import utility.DataItemCallback;

public class GatePassDAO {

    private static ObservableList<GatePass> gatePasslist;
    public GatePassDAO(){
        gatePasslist = FXCollections.observableArrayList();
    }
    public static GatePassDAO instance;
    public static GatePassDAO getInstance(){
        if(instance==null)
            instance = new GatePassDAO();
       return  instance;
    }

    public GatePass getById(int id){

        for (GatePass r:gatePasslist) {
            if(r.getId()==id){
                return r;
            }
        }
        return null;
    }

    public GatePass getByListIndex(int index){

        return gatePasslist.get(index);
    }

    public ObservableList<GatePass> GetAll(){
        return gatePasslist;
    }

    public void insert(GatePass gatePass){
        Database.getInstance().insertGatePass(gatePass, new DataItemCallback<GatePass>() {
            @Override
            public void OnSuccess(GatePass _gatePass) {
                System.out.println("ID "+_gatePass.getId());
                gatePasslist.add(_gatePass);

            }

            @Override
            public void OnFailed(String msg) {

            }
        });
    }

    public void delete(GatePass gatePass){

        if(gatePass!=null)
            gatePasslist.remove(gatePass);

    }

    public long getLastId(){

        return gatePasslist.get(gatePasslist.size()-1).getId();
    }
}
