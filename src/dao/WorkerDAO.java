package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Worker;

public class WorkerDAO {

    private static ObservableList<Worker> list;
    public WorkerDAO(){
        list = FXCollections.observableArrayList();

    }
    public static WorkerDAO instance;
    public static WorkerDAO getInstance(){
        if(instance==null)
            instance = new WorkerDAO();
       return  instance;
    }

    public ObservableList<Worker> GetAll(){
        return list;
    }

    public void insert(Worker worker){
        list.add(worker);
    }

    public void delete(Worker worker){

        if(worker!=null)
        list.remove(worker);

    }
}
