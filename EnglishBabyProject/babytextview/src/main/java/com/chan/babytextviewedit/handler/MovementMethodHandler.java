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
            if (editable.charAt(last) == ' ') break;
        }

        for (; first > 0; --first) {
            if (editable.charAt(first) == ' ') {
                first++;
                break;
            }
        }

        Selection.setSelection(editable, first, last);
    }
}
