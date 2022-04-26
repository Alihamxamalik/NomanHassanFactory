package utility;

import java.util.List;

public interface DataItemCallback<T> {
    public void OnSuccess(T t);
    public void OnFailed(String msg);
}
