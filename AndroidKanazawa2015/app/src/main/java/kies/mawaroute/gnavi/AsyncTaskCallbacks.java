package kies.mawaroute.gnavi;

/**
 * Created by owner-PC on 2016/02/03.
 */
public interface AsyncTaskCallbacks {
    //終了
    public void onTaskFinished();

    //キャンセル
    public void onTaskCancelled();
}
