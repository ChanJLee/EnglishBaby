package com.chan.babytextviewedit;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chan.babytextviewedit.handler.MovementMethodHandler;
import com.chan.babytextviewedit.utils.TextJustification;

/**
 * Created by chan on 16/6/20.
 */
public class BabyTextView extends EditText {

    private MovementMethodHandler m_handler;
    private int m_lastY = 0;
    private boolean m_shouldShowSelect = true;

    private int m_slop;

    public BabyTextView(Context context) {
        super(context);
        init();
    }

    public BabyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BabyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BabyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        m_handler = new MovementMethodHandler(this);
        m_slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private boolean isJustify = false;

    @Override
    protected void onDraw(Canvas canvas) {

        if (!isJustify) {

            final int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();

            setText(TextJustification.justify(getText().toString(), this, contentWidth), BufferType.EDITABLE);
            isJustify = true;
        }

        super.onDraw(canvas);
    }

    @Override
    protected boolean getDefaultEditable() {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                m_lastY = (int) event.getY();
                m_shouldShowSelect = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() - m_lastY >= m_slop) {
                    m_shouldShowSelect = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (m_shouldShowSelect) {
                    selectWords(event);
                }
        }

        //因为涉及滚动 所以还是要使用到super的method
        super.onTouchEvent(event);
        return true;
    }

    /**
     * 单击选择单词
     * @param event 事件
     */
    private void selectWords(MotionEvent event) {
        m_handler.selectWord(event);
    }

    /**
     * @return 获得可见文字范围
     */
    public Pair<Integer, Integer> getVisualScope() {

        int first = getOffsetForPosition(getPaddingLeft(),getPaddingTop());
        int last = getOffsetForPosition(getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());

        if (first < 0) {
            first = 0;
        }

        final int length = getEditableText().length();
        if (last >= length) {
            last = length - 1;
        }

        return new Pair<>(first, last);
    }

    /**
     * 提醒text内容已经发生变化
     */
    public void notifyTextHasChanged() {
        isJustify = false;
        invalidate();
    }
}
