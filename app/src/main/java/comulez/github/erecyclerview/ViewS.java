package comulez.github.erecyclerview;

import android.graphics.Bitmap;

/**
 * Created by Ulez on 2017/6/14.
 * Email：lcy1532110757@gmail.com
 */


public class ViewS {
    public Bitmap bitmap;
    public boolean showing = false;

    public ViewS(Bitmap bitmap, boolean showing) {
        this.bitmap = bitmap;
        this.showing = showing;
    }
}
