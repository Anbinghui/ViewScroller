#### View弹性滑动 ####

##### Scroller #####

本身不能实现滑动,需要配合View的comuteScroll方法才能完成弹性滑动,他不断的让View重绘,每一次重绘距滑动起始时间有一个时间间隔,通过这个时间间隔Scroller就可以得出View的当前的滑动位置,知道了滑动位置就可以通过scrollTo方法来文成View的滑动.就这样,View的每一次重绘都会导致View进行小幅度的滑动,多次小幅度的滑动就组成了弹性滑动,这就是Scroller的工作机制

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



##### 动画 #####


动画本身就是一个渐进的过程,因此通过它实现滑动天然就具有弹性效果.  
我们可以利用动画的特性实现一些动哈不能实现的效果,比如利用动画的特性,使用scrollTo模仿Scroller的效果

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


> 这里的滑动针对的是View的内容非View本身



##### 使用延时策略 #####

通过发送一系列的延时消息达到一种渐进的效果.

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


上面是利用handler实现弹性滑动的效果



[点击查看代码](https://github.com/Anbinghui/ViewScroller)
