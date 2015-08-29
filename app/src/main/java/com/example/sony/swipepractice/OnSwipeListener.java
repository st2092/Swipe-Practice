package com.example.sony.swipepractice;

import android.content.Context;
import android.content.res.Configuration;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * This class main purpose is to make it easier to detect mouse/finger swipe motion on a view.
 * An OnSwipeListener has several configuration set methods all of which returns the listener
 * itself so that they can be chained together.
 */
public class OnSwipeListener implements View.OnTouchListener {
    /*
     * Interface that can be implemented by an activity or fragment
     * to make it possible to put the onSwipe____ methods directly
     * in activity class.
     */
    public static interface Swiper {
        void onSwipeUp(float distance);
        void onSwipeDown(float distance);
        void onSwipeLeft(float distance);
        void onSwipeRight(float distance);
    }

    private boolean drag_horizontal = false;
    private boolean drag_vertical = false;
    private boolean drag_snap_back = false;
    private boolean animated = false;
    private boolean exit_screen_on_swipe = false;
    private long animation_delay = 500;
    private float drag_snap_threshold = 50;
    private float swipe_distance_threshold = 100;   // minimum pixel (distance) to be consider as swipe
    private float swipe_velocity_threshold = 100;   // minimum velocity to be consider as a swipe
    private float drag_prev_x;
    private float drag_prev_y;
    private GestureDetector gesture_detector = null;
    private Swiper swiper = null;
    private View dragged_view = null;

    /*
     * Constructs a new OnSwipeListener for the given context (activity or fragment).
     */
    public OnSwipeListener (Context context)
    {
        gesture_detector = new GestureDetector(context, new GestureListener());
        if (context instanceof Swiper)
        {
            swiper = (Swiper) context;
        }
    }

    /*
     * This method gets call when the user swipes the view to the left.
     * Can override this method if you want to subclass OnSwipeListener.
     */
    public void
    onSwipeLeft(float distance)
    {
        if (swiper != null)
        {
            swiper.onSwipeLeft(distance);
        }
    }

    /*
     * This method gets call when the user swipes the view to the right.
     * Can override this method if you want to subclass OnSwipeListener.
     */
    public void
    onSwipeRight(float distance)
    {
        if(swiper != null)
        {
            swiper.onSwipeRight(distance);
        }
    }

    /*
     * This method gets call when the user swipes the view upward.
     * Can override this method if you want to subclass OnSwipeListener.
     */
    public void
    onSwipeUp(float distance)
    {
        if(swiper != null)
        {
            swiper.onSwipeUp(distance);
        }
    }

    /*
     * This method gets call when the user swipes the view downward.
     * Can override this method if you want to subclass OnSwipeListener.
     */
    public void
    onSwipeDown(float distance)
    {
        if(swiper != null)
        {
            swiper.onSwipeDown(distance);
        }
    }

    /*
     * Internal method to implement mouse touch events.
     * This method is NOT meant to be call directly by clients.
     */
    @Override
    public final boolean
    onTouch(View view, MotionEvent motion_event)
    {
        if (view != null)
        {
            dragged_view = view;
        }

        boolean gesture = gesture_detector.onTouchEvent(motion_event);

        int action = motion_event.getAction();
        if (drag_horizontal || drag_vertical)
        {
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)
            {
                // nothing for now
            }
            else if (action == MotionEvent.ACTION_MOVE)
            {
                float current_x = motion_event.getX();
                float current_y = motion_event.getY();
                if (drag_horizontal)
                {
                    view.setTranslationX(view.getTranslationX() + current_x - drag_prev_x);
                }
                if (drag_vertical)
                {
                    view.setTranslationY(view.getTranslationY() + current_y - drag_prev_y);
                }
            }
            else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_POINTER_UP)
            {
                if (drag_snap_back)
                {
                    float dx = motion_event.getRawX() - drag_prev_x;
                    float dy = motion_event.getRawY() - drag_prev_y;

                    boolean should_snap_back_x = Math.abs(dx) <= drag_snap_threshold || drag_snap_threshold <= 0;
                    boolean should_snap_back_y = Math.abs(dy) <= drag_snap_threshold || drag_snap_threshold <= 0;

                    if (animated)
                    {
                        ViewPropertyAnimator animator = view.animate();
                        if (should_snap_back_x)
                        {
                            animator.translationX(0);
                        }
                        if (should_snap_back_y)
                        {
                            animator.translationY(0);
                        }
                        animator.setDuration(animation_delay);
                        animator.start();
                    }
                    else
                    {
                        if (should_snap_back_x)
                        {
                            view.setTranslationX(0);
                        }
                        if (should_snap_back_y)
                        {
                            view.setTranslationY(0);
                        }
                    }
                }
            }
            // update previous x and y coordinate
            drag_prev_x = motion_event.getRawX();
            drag_prev_y = motion_event.getRawY();
        }
        return gesture;
    }

    /*
     * Sets the number of pixel before the listener considers the user to have swiped.
     */
    public OnSwipeListener
    setDistanceThreshold(float pixels)
    {
        swipe_distance_threshold = pixels;
        return this;
    }

    /*
     * Sets the rate before the listener considers the user to have swiped.
     */
    public OnSwipeListener
    setVelocityThreshold(float rate)
    {
        swipe_velocity_threshold = rate;
        return this;
    }

    /*
     * Sets the number of pixels in which the view will snap back if dragged.
     */
    public OnSwipeListener
    setDragSnapBackThreshold(float pixels)
    {
        drag_snap_threshold = pixels;
        if (drag_snap_threshold > 0)
        {
            setDragSnapBack(true);
        }
        return this;
    }

    /*
     * Sets whether the view should snap back into position when user stops dragging it.
     */
    public OnSwipeListener
    setDragSnapBack(boolean snap_back)
    {
        drag_snap_back = snap_back;
        return this;
    }

    /*
     * Sets whether the view should track the user's finger as it drags horizontally.
     */
    public OnSwipeListener
    setDragHorizontal(boolean drag)
    {
        drag_horizontal = drag;
        return this;
    }

    /*
     * Sets whether the view should track the user's finger as it drags vertically.
     */
    public OnSwipeListener
    setDragVertical(boolean drag)
    {
        drag_vertical = drag;
        return this;
    }

    /*
     * Sets whether the view should animate itself when it snaps back or slides off the screen.
     */
    public OnSwipeListener
    setAnimated(boolean animate)
    {
        animated = animate;
        return this;
    }

    /*
     * Sets the number of milliseconds that each drag/snap animation will take.
     */
    public OnSwipeListener
    setAnimationDelay(long ms)
    {
        animation_delay = ms;
        return this;
    }

    /*
     * Sets whether the view should slide off when it is swiped.
     */
    public OnSwipeListener
    setExitScreenOnSwipe(boolean exit_screen)
    {
        exit_screen_on_swipe = exit_screen;
        return this;
    }

    /*
     * Internal class to implement finger gesture tracking.
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent motion_event)
        {
            return true;
        }

        @Override
        public boolean
        onFling(MotionEvent me1, MotionEvent me2, float velocity_x, float velocity_y)
        {
            float dx = me2.getRawX() - me1.getRawX();
            float dy = me2.getRawY() - me1.getRawY();

            Configuration config = dragged_view.getContext().getApplicationContext().getResources().getConfiguration();
            int screen_width = config.screenWidthDp;
            int screen_height = config.screenHeightDp;

            // check for swipe horizontally
            if (Math.abs(dx) > Math.abs(dy)
                    && Math.abs(dx) > swipe_distance_threshold
                    && Math.abs(velocity_x) > swipe_velocity_threshold)
            {
                if (dx > 0)
                {
                    onSwipeRight(dx);
                    dragEdgeHelper(screen_width * 2, true, 0, false);
                }
                else
                {
                    onSwipeLeft(-dx);
                    dragEdgeHelper(-screen_width, true, 0, false);
                }
                return true;
            }
            // check for swipe vertically
            else if (Math.abs(dy) > Math.abs(dx)
                    && Math.abs(dy) > swipe_distance_threshold
                    && Math.abs(velocity_y) > swipe_velocity_threshold )
            {
                if (dy > 0)
                {
                    onSwipeDown(dy);
                    dragEdgeHelper(0, false, screen_height * 2, true);
                }
                else
                {
                    onSwipeUp(-dy);
                    dragEdgeHelper(0, false, -screen_height, true);
                }
                return true;
            }

            // not consider a swipe
            return false;
        }

        /*
         * This method is a helper function for when the view is dragged out of the screen.
         * Sets up the animation for views that are animated; otherwise set the view to invisible.
         */
        private void
        dragEdgeHelper(float translation_x, boolean use_translation_x, float translation_y, boolean use_translation_y)
        {
            if (exit_screen_on_swipe && dragged_view != null)
            {
                if (animated)
                {
                    ViewPropertyAnimator animator = dragged_view.animate().setDuration(animation_delay);
                    if (use_translation_x)
                    {
                        // sets horizontal position of view relative to its left position, in pixels (translation_x)
                        animator.translationX(translation_x);
                    }
                    if (use_translation_y)
                    {
                        // sets vertical position of view relative to its top position, in pixels (translation_y)
                        animator.translationY(translation_y);
                    }
                    animator.start();
                }
                else
                {
                    dragged_view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
