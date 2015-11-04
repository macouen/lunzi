package com.oakzmm.viewdemo.view;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 *
 */
public class Physics {

    public static final float NO_GRAVITY = 0.0f;
    public static final float MOON_GRAVITY = 1.6f;
    public static final float EARTH_GRAVITY = 9.8f;
    public static final float JUPITER_GRAVITY = 24.8f;
    public static final int FORWARD = 0001;
    public static final int BACKWARD = 0002;
    private static final String TAG = Physics.class.getSimpleName();
    private static final float FRAME_RATE = 1 / 100f;
    private OnFlingListener onFlingListener;
    private ViewGroup viewGroup;
    private float density;
    private int width;
    private int height;
    private int type;
    private PhysicsViewDragHelper viewDragHelper;
    private View viewBeingDragged;
    private float x1, y1;
    private double T;
    private int asin0;
    private float evX;
    private float evY;
    private final PhysicsViewDragHelper.Callback viewDragHelperCallback = new PhysicsViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            x1 = left;
            y1 = top;
//            viewGroup.invalidate();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            viewBeingDragged = capturedChild;
            System.out.println("onViewCaptured");
            if (onFlingListener != null) {
                onFlingListener.onGrabbed(capturedChild);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            viewBeingDragged = null;
            x1 = 0;
            y1 = 0;
            evX = 0;
            evY = 0;
            viewGroup.removeView(releasedChild);
            if (onFlingListener != null) {
                onFlingListener.onReleased(releasedChild);
            }
        }
    };
    private double omgea;
    private double t;


    public Physics(ViewGroup viewGroup) {
        this(viewGroup, null);
    }


    public Physics(ViewGroup viewGroup, AttributeSet attrs) {
        this.viewGroup = viewGroup;
        viewDragHelper = PhysicsViewDragHelper.create(viewGroup, 1.0f, viewDragHelperCallback);
        density = viewGroup.getResources().getDisplayMetrics().density;
        if (attrs != null) {
//            TypedArray a = viewGroup.getContext().obtainStyledAttributes(attrs, R.styleable.Physics);
//            a.recycle();
        }
    }

    private float radiansToDegrees(float radians) {
        return (float) (radians / Math.PI * 180f);
    }

    /**
     *
     * @param width
     * @param height
     */
    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public void onLayout(boolean changed) {
        Log.i(TAG, "onLayout");
    }

    /**
     *
     * @param ev
     * @return
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel();
            return false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    /**
     * @param ev
     * @return
     */
    public boolean onTouchEvent(MotionEvent ev) {
        viewDragHelper.processTouchEvent(ev);
        if (viewBeingDragged != null) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                System.out.println("ACTION_DOWN--");
                evX = ev.getX();
                evY = ev.getY();
                int x0 = (int) (viewBeingDragged.getLeft() + viewBeingDragged.getWidth() / 2);
                int y0 = (int) (viewBeingDragged.getTop() + viewBeingDragged.getHeight() / 2);
                System.out.println("evX:" + evX + "--evY:" + evY);
                System.out.println("x0:" + x0 + "--y0:" + y0);
                final double L = Math.sqrt((evX - x0) * (evX - x0) + (evY - y0) * (evY - y0));
                double Lsin = Math.abs(evX - x0);
                asin0 = (int) radiansToDegrees((float) Math.asin(Lsin / L));
                if (evY > y0) {
                    asin0 = 180 - asin0;
                }
                System.out.println("L：" + L + "--Lsin：" + Lsin + "--asin0：" + asin0);
                T = 2 * Math.PI * Math.sqrt(L / EARTH_GRAVITY);
                omgea = Math.sqrt(EARTH_GRAVITY / L);
                t = 0;
                if (evX < x0) {
                    type = FORWARD;
                } else if (evX > x0) {
                    type = BACKWARD;
                }
            }
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                System.out.println("--ACTION_MOVE");
                evX = ev.getX();
                evY = ev.getY();
            }
        }
        return true;
    }

    /**
     * @param canvas
     */
    public void onDraw(Canvas canvas) {
        View view = null;
////        for (int i = 0; i < viewGroup.getChildCount(); i++) {
////            view = viewGroup.getChildAt(i);
//            if (view != viewBeingDragged) {
//                continue;
//            }
        if (viewBeingDragged != null) {
            view = viewBeingDragged;
            if (x1 == 0)
                x1 = view.getX();
            if (y1 == 0)
                y1 = view.getY();
            view.setX(x1);
            view.setY(y1);
            t += T * FRAME_RATE;
            //TODO 不同坐标域不同公式
            double asin = 0;
            if (type == BACKWARD) {
                asin = asin0 * Math.sin(omgea * (t + T / 4)) - asin0;
            } else if (type == FORWARD) {
                asin = asin0 * Math.sin(omgea * (t - T / 4)) + asin0;
            }
//            System.out.println("asin:" + asin);
            view.setPivotX(evX - x1);
            view.setPivotY(evY - y1);
            view.setRotation((float) asin);
        }
        viewGroup.invalidate();
    }


    /**
     * Sets the fling listener
     *
     * @param onFlingListener listener that will respond to fling events
     */
    public void setOnFlingListener(OnFlingListener onFlingListener) {
        this.onFlingListener = onFlingListener;
    }


    /**
     * A controller that will receive the drag events.
     */
    public interface OnFlingListener {
        void onGrabbed(View grabbedView);
        void onReleased(View releasedView);
    }

}
