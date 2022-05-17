package dao;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Vendor;
import utility.DataItemCallback;
import utility.DataListCallback;

public class CustomerDAO {

    private static ObservableList<Customer> list;
    public CustomerDAO(){
        list = FXCollections.observableArrayList();
//
//        Customer r = new Customer("Ali","321321");
//        Customer r1 = new Customer("Usama","321321");
//        Customer r2 = new Customer("Hassan","13213213");
//
//        list.add(r);
//        list.add(r1);
//        list.add(r2);
    }
    public static CustomerDAO instance;
    public static CustomerDAO getInstance(){
        if(instance==null)
            instance = new CustomerDAO();
       return  instance;
    }

    public Customer getById(long id){
//        for (Customer r:list) {
//            if(r.getId()==id){
//                return r;
//            }
//        }
        return Database.getInstance().getCustomerById(id);
    }

    public Customer getByListIndex(int index){

        return list.get(index);
    }

    public void getAll(DataListCallback<Customer> callback){
        list = FXCollections.observableArrayList();
        Database.getInstance().getAllCustomer(new DataListCallback<Customer>() {
            @Override
            public void OnSuccess(ObservableList<Customer> _list) {
                list = _list;
                callback.OnSuccess(list);
            }

            @Override
            public void OnFailed(String msg) {
                callback.OnFailed(msg);
            }
        });
    }

    public void insert(Customer customer,DataItemCallback callback){

        Database.getInstance().insertCustomer(customer, new DataItemCallback<Customer>() {
            @Override
            public void OnSuccess(Customer _cusomer) {
//                list.add(_vendor);
                getAll(new DataListCallback<Customer>() {
                    @Override
                    public void OnSuccess(ObservableList<Customer> list) {
                        callback.OnSuccess(_cusomer);
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

    public void delete(Customer customer, DataItemCallback callback){

        if(customer!=null)
        list.remove(customer);

        if (customer != null)
            Database.getInstance().deleteCustomer(customer, new DataItemCallback<Customer>() {
                @Override
                public void OnSuccess(Customer _customer) {
                    list.remove(_customer);
                    callback.OnSuccess(_customer);
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
