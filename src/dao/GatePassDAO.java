package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GatePass;
import model.GatePassItem;
import model.Item;

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

    public void insert(GatePass material){
        gatePasslist.add(material);
    }

    public void delete(GatePass material){

        if(material!=null)
            gatePasslist.remove(material);

    }

    public long getLastId(){

        return gatePasslist.get(gatePasslist.size()-1).getId();
    }
}
