package comulez.github.erecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by Ulez on 2017/7/4.
 * Email：lcy1532110757@gmail.com
 */

public class MyFootView extends LinearLayout {

    public MyFootView(Context context) {
        super(context);
        initView(context);
    }

    public MyFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//必须添加否则宽度不对
        addView(LayoutInflater.from(context).inflate(R.layout.item_footer, null), lp);
    }
}
