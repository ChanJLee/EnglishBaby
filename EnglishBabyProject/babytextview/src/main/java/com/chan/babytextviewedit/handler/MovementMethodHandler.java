package com.chan.babytextviewedit.handler;

import android.text.Editable;
import android.text.Selection;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by chan on 16/6/20.
 *
 * 部分思路来自 http://stackoverflow.com/questions/8644649/full-text-justification-in-android/17807828#17807828
 */
public class MovementMethodHandler {
    private TextView m_widget;

    public MovementMethodHandler(TextView widget) {
        m_widget = widget;
    }

    /**
     * 获得当前选中的文字
     * @param event
     */
    public void selectWord(MotionEvent event) {

        int first = m_widget.getOffsetForPosition(event.getX(), event.getY());
        Editable editable = m_widget.getEditableText();
        final int length = editable.length();

        if (first >= length) {
            first = length - 1;
            if (first < 0) first = 0;
        }

        int last = first;

        for (; last < length; ++last) {
            if (!isAscii(editable.charAt(last))) break;
        }

        for (; first > 0; --first) {
            if (!isAscii(editable.charAt(first))) {
                first++;
                break;
            }
        }

        //高亮末尾 会被取消掉 也不会高亮空格
        if (first >= last) return;

        Selection.setSelection(editable, first, last);
    }

    public boolean isAscii(char c) {
        return (c >= 'a' && c < 'z') || (c >= 'A' && c < 'Z');
    }
}
