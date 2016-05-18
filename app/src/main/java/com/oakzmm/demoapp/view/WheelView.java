package com.oakzmm.demoapp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oakzmm.demoapp.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WheelView extends ScrollView {
    public static final String TAG = WheelView.class.getSimpleName();
    public static final int OFF_SET_DEFAULT = 1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;
    // private ScrollView scrollView;
    // String[] items;
    List<String> items;
    int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）
    int displayItemCount; // 每页显示的数量
    int selectedIndex = 0;
    int initialY;
    Runnable scrollerTask;
    int newCheck = 50;
    int itemHeight = 0;
    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;
    Paint paint;
    int viewWidth;
    private Context context;
    private LinearLayout views;
    private int scrollDirection = -1;
    private OnWheelViewListener onWheelViewListener;
    private OnItemClickListener onItemClickListener;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private List<String> getItems() {
        return items;
    }

    public void setItems(List<String> list) {
        if (null == items) {
            items = new ArrayList<String>();
        }
        items.clear();
        items.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            items.add(0, "");
            items.add("");
        }

        initData();

    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private void init(Context context) {
        this.context = context;
        this.setVerticalScrollBarEnabled(false);
        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);
        scrollerTask = new Runnable() {

            public void run() {
                int newY = getScrollY();
                if (initialY - newY == 0) { // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    if (remainder == 0) {
                        selectedIndex = divided + offset;
                        onSelectedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY
                                            - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSelectedCallBack();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY
                                            - remainder);
                                    selectedIndex = divided + offset;
                                    onSelectedCallBack();
                                }
                            });
                        }
                    }
                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public void startScrollerTask() {
        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;
        selectedIndex = offset;
        views.removeAllViews();
        for (String item : items) {
            views.addView(createView(item));
        }
        refreshItemView(0);
        int childCount = views.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = views.getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getSelectedIndex());
                    }
                }
            });
        }
//        views.postInvalidateDelayed(500);
//        init(context);
    }

    private TextView createView(String item) {
        final TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(context,50)));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        int padding = DensityUtil.dip2px(context, 10);
        tv.setPadding(padding, padding, padding, padding);
        if (0 == itemHeight) {
//            itemHeight = DensityUtil.getViewMeasuredHeight(tv);
            itemHeight = DensityUtil.dip2px(context,50);
            views.setLayoutParams(new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, itemHeight
                    * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this
                    .getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width,
                    itemHeight * displayItemCount));
        }
        Log.i(TAG,tv.getText().toString());
        return tv;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        refreshItemView(t);
        if (t > oldt) {
            // Logger.d(TAG, "向下滚动");
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
            // Logger.d(TAG, "向上滚动");
            scrollDirection = SCROLL_DIRECTION_UP;

        }
    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;
        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }
        }
        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                itemView.setTextColor(Color.parseColor("#0288ce"));
                itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            } else {
                itemView.setTextColor(Color.parseColor("#bbbbbb"));
                itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }
        }
    }

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackground(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager()
                    .getDefaultDisplay().getWidth();
            // Logger.d(TAG, "viewWidth: " + viewWidth);
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#83cde6"));
            paint.setStrokeWidth(DensityUtil.dip2px(context, 1f));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(viewWidth / 8,
                        obtainSelectedAreaBorder()[0], viewWidth * 7 / 8,
                        obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth / 8,
                        obtainSelectedAreaBorder()[1], viewWidth * 7 / 8,
                        obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };

        super.setBackground(background);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
         Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        setBackground(null);
    }

    /**
     * 选中回调
     */
    private void onSelectedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener.onSelected(getSelectedIndex(), items.get(selectedIndex));
        }
    }

    public void setSelection(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, p * itemHeight);
            }
        });
    }

    public String getSelectedItem() {
        return items.get(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex - offset;
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    public void setOnItemListener(OnItemClickListener onItemListener) {
        this.onItemClickListener = onItemListener;
    }

    public static class OnWheelViewListener {
        public void onSelected(int index, String item) {
        }
    }

    public static class OnItemClickListener {
        public void onItemClick(int index) {
        }
    }

}
