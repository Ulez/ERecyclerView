package comulez.github.erecyclerview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;

/**
 * Created by Ulez on 2017/6/1.
 * Email：lcy1532110757@gmail.com
 */


public class EleView extends View {

    private Paint paint;
    private Bitmap ele;
    private Bitmap tex;
    private Bitmap iL;
    private Bitmap iR;
    private int mWidth;
    private int mHeight;
    private int eWidth;
    private int eHeight;
    private int tWidth;
    private int tHeight;
    private int iWidth;
    private int iHeight;

    private double scale = 0.4;
    private double scale2 = 0.3;
    private Rect dstE;
    private Rect dstI;
    private Rect dstI2;
    private float currentDegree = 0;
    private float percent = 0;
    private final String TAG = "EleHeadView";
    private Path mPath;
    private ArrayList<PathMeasure> mPathMeasure = new ArrayList<>();
    private int fW;
    private int fH;
    private Rect dstFruit;
    private Path mPath2;
    //    private int curentBitmapPos = 0;
    private float[][] mPosition0 = new float[5][2];
    private ArrayList<Animator> animList = new ArrayList<>();

    AnimatorSet animationSet;
    private boolean stop = false;
    private double currentOffset;
    private ValueAnimator cycleAnimator;


    public EleView(Context context) {
        super(context);
        init(context);
    }

    public EleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

//    HashMap<Bitmap, Boolean> sss = new HashMap<>();

    ArrayList<BitmapBean> foods = new ArrayList();

    private void init(Context context) {
        paint = new Paint();
//        paint.setAntiAlias(true);
        ele = BitmapFactory.decodeResource(getResources(), R.drawable.a3i);
        iL = BitmapFactory.decodeResource(getResources(), R.drawable.a3p);
        iR = BitmapFactory.decodeResource(getResources(), R.drawable.a3q);

        foods.add(new BitmapBean(BitmapFactory.decodeResource(getResources(), R.drawable.a3j), false));
        foods.add(new BitmapBean(BitmapFactory.decodeResource(getResources(), R.drawable.a3l), false));
        foods.add(new BitmapBean(BitmapFactory.decodeResource(getResources(), R.drawable.a3m), false));
        foods.add(new BitmapBean(BitmapFactory.decodeResource(getResources(), R.drawable.a3n), false));
        foods.add(new BitmapBean(BitmapFactory.decodeResource(getResources(), R.drawable.a3o), false));


        eWidth = ele.getWidth();
        eHeight = ele.getHeight();
        iWidth = iL.getHeight();
        iHeight = iL.getWidth();

        fW = (int) (foods.get(0).bitmap.getWidth() * scale2);
        fH = (int) (foods.get(0).bitmap.getHeight() * scale2);

        dstFruit = new Rect();
        dstE = new Rect();
        dstE.left = (int) (-scale * eWidth / 2);
        dstE.top = (int) (-scale * eHeight);
        dstE.right = (int) (scale * eWidth / 2);
        dstE.bottom = 0;

        dstI = new Rect();
        dstI.left = dstE.left;
        dstI.top = (int) (dstE.top - scale * iHeight) + (int) (iWidth * scale);
        dstI.right = (int) (dstE.left + scale * iWidth);
        dstI.bottom = dstE.top + (int) (iWidth * scale);

        dstI2 = new Rect();
        dstI2.left = (int) (dstE.right - scale * iWidth);
        dstI2.top = (int) (dstE.top - scale * iHeight) + (int) (iWidth * scale);
        dstI2.right = dstE.right;
        dstI2.bottom = dstE.top + (int) (iWidth * scale);

        Point startPoint = new Point();
        startPoint.x = (dstE.left + dstE.right) / 2;
        startPoint.y = (int) (-scale * eHeight);

        Point endPointL = new Point();
        endPointL.x = (dstE.left + dstE.right) / 2;
        endPointL.y = 2 * (int) (-scale * eHeight);

        Point endPointR = new Point();
        endPointR.x = (dstE.left + dstE.right) / 2;
        endPointR.y = 2 * (int) (-scale * eHeight);

        int h = (int) (scale * eHeight);
        int w = (int) (scale * eWidth);

        mPath = new Path();
        mPath.moveTo(0, -(float) (0.5 * fH));
        mPath.quadTo(0.5f * w, -2.5f * h, 1.0f * w, -h);

        mPath2 = new Path();
        mPath2.moveTo(0, -(float) (0.5 * fH));
        mPath2.quadTo(-0.5f * w, -2.5f * h, -1.0f * w, -h);

        for (int i = 0; i < 5; i++)
            mPathMeasure.add(new PathMeasure(i % 2 == 0 ? mPath : mPath2, false));
    }

    int iCurStep = 0;// current step
    //don't forget to initialize
//    Path pathMoveAlong = new Path();
    private static Bitmap fruit = null;
    private float offsetY = 0;
    private float maxOffsetY = (float) (mHeight * 9.0 / 10.0);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, offsetY);
        for (int i = 0; i < 5; i++) {
            dstFruit.left = (int) (mPosition0[i][0] - 0.5 * fW);
            dstFruit.top = (int) (mPosition0[i][1] - 0.5 * fH);
            dstFruit.right = (int) (mPosition0[i][0] + 0.5 * fW);
            dstFruit.bottom = (int) (mPosition0[i][1] + 0.5 * fH);
            if (foods.get(i).showing) {
                canvas.drawBitmap(foods.get(i).bitmap, null, dstFruit, paint);
            }
        }
        canvas.save();
        int offsetC = (int) (currentOffset * 5);
        canvas.rotate(currentDegree, (float) (dstE.right - iWidth * scale / 4), (float) (dstE.top + scale * iWidth));
        canvas.drawBitmap(iR, null, new Rect(dstI2.left, dstI2.top - offsetC, dstI2.right, dstI2.bottom + offsetC), paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(-currentDegree, (float) (dstE.left + iWidth * scale / 4), (float) (dstE.top + scale * iWidth));
        canvas.drawBitmap(iL, null, new Rect(dstI.left, dstI.top - offsetC, dstI.right, dstI.bottom + offsetC), paint);
        canvas.restore();
        canvas.drawBitmap(ele, null, new Rect(dstE.left, dstE.top - offsetC, dstE.right, dstE.bottom + offsetC), paint);
    }

    // 开启路径动画
    public void startPathAnim(final long duration) {
        currentDegree = 150;
        offsetY = maxOffsetY;
        stop = false;
        if (animationSet != null && animationSet.isRunning())
            return;
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.get(finalI).getLength());
            valueAnimator.setDuration(duration);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    // 获取当前点坐标封装到mCurrentPosition
                    mPathMeasure.get(finalI).getPosTan(value, mPosition0[finalI], null);
                    postInvalidate();
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    foods.get(finalI).showing = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    foods.get(finalI).showing = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    foods.get(finalI).showing = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    foods.get(finalI).showing = true;
                }
            });
            valueAnimator.setStartDelay(finalI * 180);
            animList.add(valueAnimator);
        }
        cycleAnimator = ValueAnimator.ofFloat(-1, 1);
        cycleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentOffset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        cycleAnimator.setDuration(200);
        cycleAnimator.setRepeatCount(10000);
        cycleAnimator.start();
//        animList.add(cycleAnimator);
        animationSet = new AnimatorSet();
        animationSet.setDuration(1000);
        animationSet.playTogether(animList);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!stop)
                    animationSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animationSet.start();
    }

    public void stopAni() {
        if (animationSet != null && animationSet.isRunning()) {
            stop = true;
            animationSet.cancel();
            cycleAnimator.cancel();
        }
    }

    public void release() {
        currentDegree = 0;
        currentOffset = 0;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        maxOffsetY = (float) (mHeight * 9.0 / 10.0);
    }

    public void onMove(float percent) {
        if (percent >= 0.6) {
            currentDegree = (float) (150 * (percent - 0.6) * 2.5);
        }
        if (percent <= 1) {
            offsetY = percent * maxOffsetY;
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
