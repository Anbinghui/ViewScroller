package com.abh.viewscroll;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by An on 2019/4/22 0022.
 */
public class CircleView extends View {

    private int mLastX;
    private int mLastY;

    private Scroller mScroller= new Scroller(getContext());
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**画了个圆
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#C71E55"));
        canvas.drawCircle(500,1000,50,mPaint);
    }

    private static final int MESSAGE_SCROLL_TO=-1;
    private static final int FRAME_COUNT=30;
    private static final int DELAYED_TIME=33;
    private static final int SCROLL_DELTAX=100;
    private int mCount=0;


    private Handler mHandler = new Handler(){
        /**向左滑动100的距离
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO:
                    mCount++;
                    if (mCount<=FRAME_COUNT) {
                        float fraction = mCount/(float)FRAME_COUNT;
                        int scrollX = (int) (fraction*SCROLL_DELTAX);
                        scrollTo(scrollX,0);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);
                    }
                    break;
            }
        }
    };




    public void scrollByHandler() {
        mHandler.sendEmptyMessage(MESSAGE_SCROLL_TO);
    }

    /**使用scrollTo利用动画的特性实现弹性滑动
     * @param deltaX  横向移动距离,正数向左,负数向右
     */
    public void useAnimScrollTo( final int deltaX) {
        ValueAnimator animator = ValueAnimator.ofInt(0,1).setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                scrollTo( ((int) getX()+(int)(deltaX*fraction)),0);
            }
        });
        animator.start();
    }

    public void smoothScrollTo(int destX,int destY) {

        int scrollX = getScrollX();
        int deltaX = destX-scrollX;
        mScroller.startScroll(scrollX,0,deltaX,0,1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX= x-mLastX;
                int deltaY = y-mLastY;
                Log.d("VIEW","x:==="+x+"  y=="+y+"  deltaX=="+deltaX+"   deltaY=="+deltaY);
                offsetLeftAndRight(deltaX);
                offsetTopAndBottom(deltaY);
         /*       int translationsX = (int) (getTranslationX()+deltaX);
                int translationY = (int) (getTranslationX()+deltaY);
                ObjectAnimator.ofFloat(this,"translationX",translationsX).start();
                ObjectAnimator.ofFloat(this,"translationY",translationY).start();*/
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastX = x;
        mLastY  = y;
        return true;

    }
}
