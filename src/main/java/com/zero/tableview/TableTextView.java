package com.zero.tableview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 16-11-16.
 */
public class TableTextView extends View {
    private String TAG = this.getClass().getSimpleName();
    private List<Rect> mRectList = new ArrayList<>();
    private ArrayList<String> textArray = new ArrayList<>();
    private String[] mTableHead = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private float mCellMinHeight = 80f;
    private int mCellHeight = 80;
    private int mCellWidth = 180;
    private int mWidth = 0; //控件的宽度
    private int mHeight = 0; // 控件的高度
    private int columnNum;
    private int lineNum;
    private int defaultColumnNum = 5;// 默认列数
    private int defaultLineNum = 1; // 默认行数
    private float defaultTextSize = 23f;//默认字体大小
    private float defaultLineWidth = 2;//线的宽度
    private String defaultColor = "#EBEBEB";
    private int lineColor;
    private int textColor;
    private float textSize;
    private float lineWidth = 2;
    private Paint textPaint;
    private Paint linePaint;
    private Paint.FontMetrics fontMetrics;
    private OnTouchTableListener listener;

    public TableTextView(Context context) {
        this(context, null);
    }

    public TableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tableViewStyle);
        columnNum = a.getInteger(R.styleable.tableViewStyle_columnNum, defaultColumnNum);
        lineNum = a.getInteger(R.styleable.tableViewStyle_lineNum, defaultLineNum);
        lineColor = a.getColor(R.styleable.tableViewStyle_lineColor, Color.parseColor(defaultColor));
        textColor = a.getColor(R.styleable.tableViewStyle_textColor, Color.parseColor(defaultColor));
        textSize = a.getDimension(R.styleable.tableViewStyle_textSize, defaultTextSize);
        mCellHeight = (int) a.getDimension(R.styleable.tableViewStyle_columnHeight, mCellMinHeight);
        mCellWidth = (int) a.getDimension(R.styleable.tableViewStyle_columnWidth, mCellWidth);
        lineWidth = a.getDimension(R.styleable.tableViewStyle_lineWidth, defaultLineWidth);
        a.recycle();
        initPaint();
        addRect();
        addText();
        setClickable(true);
    }

    private void addText() {
        for (int i = 0; i < 50; i++) {
            textArray.add("表格"+i);
        }
    }

    private void addRect() {
        int currX;
        int currY;
        int lineX = 0;
        int lineY = 0;
        for (int i = 0; i < lineNum; i++) {
            for (int j = 0; j < columnNum; j++) {
                currX = mCellWidth * j;
                currY = mCellHeight * i;
                lineX = mCellWidth * (j + 1);
                lineY = mCellHeight * (i + 1);
                Rect rect = new Rect(currX, currY, lineX, lineY);
                mRectList.add(rect);
            }
            mWidth = lineX;
            mHeight = lineY;
        }
    }

    private void initPaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);
        fontMetrics = textPaint.getFontMetrics();
//        textPaint.setShadowLayer(0.0f, 0.5f, 0.5f, 0xb0000000);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setTextAlign(Paint.Align.CENTER);
        linePaint.setStyle(Paint.Style.STROKE);
//        linePaint.setShadowLayer(0.0f, 0.5f, 0.5f, 0xb0000000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mRectList.size(); i++) {
            Rect rect = mRectList.get(i);
            textPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(rect, linePaint);
        }
        for (int i = 0; i < mRectList.size(); i++) {
            String text = "";
            if (i < textArray.size()) {
                text = textArray.get(i);
                if (TextUtils.isEmpty(text)) {
                    continue;
                }
            }
            Rect rect = mRectList.get(i);
            float textSize = textPaint.getTextSize();
            float textWidth = textPaint.measureText(text);
            if (textWidth > mCellWidth) {
                float scale = mCellWidth * 1.0f / textWidth;
                textPaint.setTextSize(textSize * scale);
            }
            canvas.drawText(text, (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2, textPaint);
        }
        Log.i(TAG, "onDraw: i come here ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int index;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                index = getIndexByTouch(x, y);
                if (index != -1) {
                    String text = "";
                    if (index < textArray.size()) {
                        text = textArray.get(index);
                    }
                    if (listener != null) {
                        listener.onTouchTable(index, text);
                    }
                    Log.i(TAG, "onTouchEvent: index:" + index + "===text=" + text);
                }
                break;
        }
        return true;
    }

    /**
     * 根据当前的坐标去查找对应的index
     *
     * @param x
     * @param y
     * @return
     */
    private int getIndexByTouch(int x, int y) {
        int index;
        if (mRectList == null || mRectList.size() == 0) {
            return -1;
        }
        for (index = 0; index < mRectList.size(); index++) {
            Rect rect = mRectList.get(index);
            if (rect.contains(x, y)) {
                return index;
            }
        }
        if (index == mRectList.size()) {
            index = -1;
        }
        return index;
    }

    private int dp2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public interface OnTouchTableListener {
        void onTouchTable(int position, String clickContent);
    }

    public void setOnTouchTableListener(OnTouchTableListener listener) {
        this.listener = listener;
    }
}
