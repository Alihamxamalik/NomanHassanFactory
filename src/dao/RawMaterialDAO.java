package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import model.RawMaterial;

public class RawMaterialDAO {

    private static ObservableList<RawMaterial> list;
    public RawMaterialDAO(){
        list = FXCollections.observableArrayList();
        RawMaterial r = new RawMaterial("Dabba");
        RawMaterial r1 = new RawMaterial("Chilka");
        RawMaterial r2 = new RawMaterial("Niyara");

        list.add(r);
        list.add(r1);
        list.add(r2);
    }
    public static RawMaterialDAO instance;
    public static RawMaterialDAO getInstance(){
        if(instance==null)
            instance = new RawMaterialDAO();
       return  instance;
    }

    public RawMaterial getById(int id){

        for (RawMaterial r:list) {
            if(r.getId()==id){
                return r;
            }
        }
        return null;
    }

    public RawMaterial getBylistIndex(int index){

        return list.get(index);
    }

    public ObservableList<RawMaterial> GetAll(){
        return list;
    }

    public void insert(RawMaterial material){
        list.add(material);
    }

    public void delete(RawMaterial material){

        if(material!=null)
        list.remove(material);

    }
}
