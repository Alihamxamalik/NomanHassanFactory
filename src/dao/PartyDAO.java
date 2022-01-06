package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Party;
import model.RawMaterial;

public class PartyDAO {

    private static ObservableList<Party> list;
    public PartyDAO(){
        list = FXCollections.observableArrayList();
        Party r = new Party("Ali");
        Party r1 = new Party("Usama");
        Party r2 = new Party("Hassan");

        list.add(r);
        list.add(r1);
        list.add(r2);
    }
    public static PartyDAO instance;
    public static PartyDAO getInstance(){
        if(instance==null)
            instance = new PartyDAO();
       return  instance;
    }

    public Party getById(int id){

        for (Party r:list) {
            if(r.getId()==id){
                return r;
            }
        }
        return null;
    }

    public Party getBylistIndex(int index){

        return list.get(index);
    }

    public ObservableList<Party> GetAll(){
        return list;
    }

    public void insert(Party party){
        list.add(party);
    }

    public void delete(Party party){

        if(party!=null)
        list.remove(party);

    }
}
