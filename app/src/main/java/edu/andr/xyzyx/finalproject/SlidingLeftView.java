package edu.andr.xyzyx.finalproject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * TODO: document your custom view class.
 */
public class SlidingLeftView extends HorizontalScrollView {
    /**
     * 主布局界面
     */
    private ViewGroup mMainLayout;
    /**
     * 侧滑界面
     */
    private ViewGroup mRightLayout;
    /**
     * 侧滑界面宽度
     */
    private int mRightLayoutWidth;
    /**
     * 侧滑界面距离屏幕左边的距离
     */
    private int mRightLayoutMarginLeft = 200; //px
    /**
     * 是否展示侧滑
     */
    private boolean isOpen;

    private int mScreenWidth;
    private int mScreenHeight;

    private Context mContext;


    public SlidingLeftView(Context context) {
        this(context, null);
    }

    public SlidingLeftView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLeftView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        getScreenWidthAndHeight();
    }

    private void getScreenWidthAndHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
        Log.i("TAG", "mScreenWidth=" + mScreenWidth + "mScreenHeight=" + mScreenHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LinearLayout v = (LinearLayout) this.getChildAt(0);
        mMainLayout = (ViewGroup) v.getChildAt(0);
        mRightLayout = (ViewGroup) v.getChildAt(1);

        mMainLayout.getLayoutParams().width = mScreenWidth;
        mRightLayout.getLayoutParams().width = mRightLayoutWidth = mScreenWidth - mRightLayoutMarginLeft;
        Log.i("TAG", " mRightLayout.getLayoutParams().width=" + mRightLayout.getLayoutParams().width);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // scrollX为水平滚动条滚动的宽度
                int scrollX = getScrollX();
                if (scrollX >= mRightLayoutWidth / 2) {
                    this.smoothScrollTo(mRightLayoutWidth, 0);
                    isOpen = true;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = false;
                }
                // 拦截事件
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 抽屉开关
     */
    public void toggleRightLayout() {
        if (isOpen) {
            closeRightLayout();
            isOpen = false;
        } else {
            openRightLayout();
            isOpen = true;
        }
    }

    /**
     * 打开抽屉
     */
    public void openRightLayout() {
        if (isOpen) {
            return;
        }
        this.smoothScrollTo(mRightLayoutWidth, 0);
        isOpen = true;
    }

    /**
     * 关闭抽屉
     */
    public void closeRightLayout() {
        if (!isOpen) {
            return;
        }
        this.smoothScrollTo(0, 0);
        isOpen = false;
    }

}
