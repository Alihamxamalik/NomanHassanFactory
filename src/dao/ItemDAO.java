package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;

public class ItemDAO {

    private static ObservableList<Item> list;
    public ItemDAO(){
        list = FXCollections.observableArrayList();
        Item r = new Item(1,"Dabba",false);
        Item r1 = new Item(2,"Chilka",false);
        Item r2 = new Item(3,"Niyara",false);

        list.add(r);
        list.add(r1);
        list.add(r2);
    }
    public static ItemDAO instance;
    public static ItemDAO getInstance(){
        if(instance==null)
            instance = new ItemDAO();
       return  instance;
    }

    public Item getById(int id){

        for (Item r:list) {
            if(r.getId()==id){
                return r;
            }
        }
        return null;
    }

    public Item getByListIndex(int index){

        return list.get(index);
    }

    public ObservableList<Item> GetAll(){
        return list;
    }

    public void insert(Item material){
        list.add(material);
    }

    public void delete(Item material){

        if(material!=null)
        list.remove(material);

    }

    public long getLastId(){

        return list.get(list.size()-1).getId();
    }
}
