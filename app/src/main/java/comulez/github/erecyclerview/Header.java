package comulez.github.erecyclerview;

/**
 * Created by Ulez on 2017/6/9.
 * Email：lcy1532110757@gmail.com
 */


public interface Header {
    void setVisibleHeight(int height);

    int getVisibleHeight();

    void setState(int state);

    void onMove(float delta);
}
