package com.grayraven.project1;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/*
 * Adapted from code provided by StackOverflow user Thomas Fankhauser, question #937313
 */

class ActivitySwipeDetector implements View.OnTouchListener {

    private static final String TAG = "MovieSwipeDetector";
    private final SwipeInterface activity;
    private static final int MIN_DISTANCE = 100;
    private float downX;
    private float downY;

    public ActivitySwipeDetector(SwipeInterface activity){
        this.activity = activity;
    }

   private void onLeftToRightSwipe(View v){
        Log.i(TAG, "LeftToRightSwipe!");
        activity.left2right(v);
    }

    // the following functions are not needed in this application
  /*
    public void onTopToBottomSwipe(View v){
        Log.i(TAG, "onTopToBottomSwipe!");
        activity.top2bottom(v);
    }

   public void onRightToLeftSwipe(View v){
        Log.i(TAG, "RightToLeftSwipe!");
        activity.right2left(v);
    }

    public void onBottomToTopSwipe(View v){
        Log.i(TAG, "onBottomToTopSwipe!");
        activity.bottom2top(v);
    }*/

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                float upX = event.getX();
                float upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > MIN_DISTANCE){
                    // left or right
                    if(deltaX < 0) { this.onLeftToRightSwipe(v); return true; }
             //       if(deltaX > 0) { this.onRightToLeftSwipe(v); return true; }
                }
                else {
                    Log.i(TAG, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                }

                // swipe vertical?
                //noinspection StatementWithEmptyBody
                if(Math.abs(deltaY) > MIN_DISTANCE){
                    // top or down not needed right now
                 //   if(deltaY < 0) { this.onTopToBottomSwipe(v); return true; }
                 //   if(deltaY > 0) { this.onBottomToTopSwipe(v); return true; }
                }
                else {
                    Log.i(TAG, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    v.performClick();
                }
            }
        }
        return false;
    }

}