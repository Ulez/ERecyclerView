package comulez.github.erecyclerview;

/**
 * Created by Ulez on 2017/6/9.
 * Email：lcy1532110757@gmail.com
 */


public interface Header {
    int STATE_NORMAL = 0;
    int STATE_READY = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;


    void setVisibleHeight(int height);

    int getVisibleHeight();

    void setState(int state);

    void onMove(float delta);

    void refreshComplete();

    boolean releaseAction();
}
