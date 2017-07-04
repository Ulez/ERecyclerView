package comulez.github.erecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Ulez on 2017/7/4.
 * Email：lcy1532110757@gmail.com
 */


public class MyHeaderView extends LinearLayout implements Header,State{
    private LinearLayout mContainer;
    private EleView eleView;
    private int contentHeight;//内容原始高度；
    private int mState = STATE_NORMAL;
    private String TAG = "lcy";
    private int contentHeight22;


    public MyHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public MyHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.ele_header, null);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        contentHeight = findViewById(R.id.header_content).getLayoutParams().height;
        eleView = (EleView) mContainer.findViewById(R.id.ele);
        contentHeight22 = eleView.getLayoutParams().height;
    }

    @Override
    public void setVisibleHeight(int height) {
        if (height == 0) {
            eleView.release();
        }
        ViewGroup.LayoutParams lp = mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getContentHeight22() {
        return contentHeight22;
    }

    public int getVisibleHeight() {
        return mContainer.getLayoutParams().height;
    }

    public void smoothScrollToContent() {//正则刷新；
        setState(STATE_REFRESHING);
        smoothScrollTo(contentHeight22);
    }

    public void smoothScrollTo(int destHeight) {

        if (destHeight == 0)
            eleView.stopAni();
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
//        scrollToPosition(0);
    }

    @Override
    public void setState(int state) {
        if (state == mState) return;
        if (state == STATE_REFRESHING) {    // 显示进度

        } else {    // 显示箭头图片

        }
        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
//                    eleView.startPathAnim(1000);
                }
                if (mState == STATE_REFRESHING) {
//                    eleView.stopAni();
                }
                break;
            case STATE_READY:
                eleView.onMove(1);
                if (mState != STATE_READY) {
//                    eleView.stopAni();
//                    eleView.startPathAnim(1000);
                }
                break;
            case STATE_REFRESHING:
                eleView.startPathAnim(1000);
                break;
            default:
        }
        mState = state;
    }

    @Override
    public void onMove(float delta) {
        if (delta <= 0 && getVisibleHeight() <= 0)
            return;
        if (delta < 0 && getVisibleHeight() > 0) {
            setVisibleHeight((int) (getVisibleHeight() + delta));
            if (getVisibleHeight() > contentHeight22)
                setState(STATE_READY);
            else {
                setState(STATE_NORMAL);
            }
            return;
        }
        if (getVisibleHeight() < contentHeight) {
            setVisibleHeight((int) (getVisibleHeight() + delta));
            if (getVisibleHeight() > contentHeight22)
                setState(STATE_READY);
            else {
                eleView.onMove(1.0f * getVisibleHeight() / contentHeight22);
                setState(STATE_NORMAL);
            }
            return;
        }
    }

    public void smoothScrollTo0() {
        eleView.stopAni();
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), 0);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
                eleView.onMove(1.0f * getVisibleHeight() / contentHeight22);
            }
        });
        animator.start();
    }
}
