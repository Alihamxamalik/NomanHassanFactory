package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;

public class ProductDAO {

    private static ObservableList<Product> list;
    public ProductDAO(){
        list = FXCollections.observableArrayList();
        Product p = new Product("Silli");
        Product p1 = new Product("Gulla");
        Product p2 = new Product("1 no Silli");

        list.add(p);
        list.add(p1);
        list.add(p2);
    }
    public static ProductDAO instance;
    public static ProductDAO getInstance(){
        if(instance==null)
            instance = new ProductDAO();
       return  instance;
    }

    public ObservableList<Product> GetAll(){
        return list;
    }
    public Product getByIndex(int i ){
        return list.get(i);
    }

    public void insert(Product product){
        list.add(product);
    }

    public void delete(Product product){

        if(product!=null)
        list.remove(product);

    }
}
