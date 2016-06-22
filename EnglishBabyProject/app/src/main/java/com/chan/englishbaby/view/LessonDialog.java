package com.chan.englishbaby.view;

import android.app.Dialog;
import android.content.Context;

import com.chan.englishbaby.injector.annotation.ContextLife;
import com.chan.englishboby.R;

import javax.inject.Inject;

/**
 * Created by chan on 16/6/22.
 */
public class LessonDialog extends Dialog {

    @Inject
    public LessonDialog(@ContextLife("activity") Context context) {
        super(context, R.style.SimpleDialogTheme);
        setContentView(R.layout.dialog_lesson);
    }
}
