package utility;

import javafx.collections.ObservableList;

import java.util.List;

public interface DataListCallback<T> {
    public void OnSuccess(ObservableList<T> list);
    public void OnFailed(String msg);
}
