package com.chan.englishbaby.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.RadioGroup;

import com.chan.englishbaby.injector.annotation.ContextLife;
import com.chan.englishbaby.utils.ConstUtil;
import com.chan.englishboby.R;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chan on 16/6/22.
 */
public class LessonDialog extends Dialog {
    

    private OnLevelSelected m_onLevelSelected;

    @Bind(R.id.id_radio)
    RadioGroup m_radioGroup;

    @Inject
    public LessonDialog(@ContextLife("activity") Context context) {
        super(context, R.style.SimpleDialogTheme);
        setContentView(R.layout.dialog_lesson);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_accept)
    void onAccept() {
        if (m_onLevelSelected == null) return;

        switch (m_radioGroup.getCheckedRadioButtonId()) {
            case R.id.id_none:
                m_onLevelSelected.onLevelSelected(ConstUtil.ITEM_NONE);
                break;
            case R.id.id_level_0:
                m_onLevelSelected.onLevelSelected(ConstUtil.ITEM_LEVEL_0);
                break;
            case R.id.id_level_1:
                m_onLevelSelected.onLevelSelected(ConstUtil.ITEM_LEVEL_1);
                break;
            case R.id.id_level_2:
                m_onLevelSelected.onLevelSelected(ConstUtil.ITEM_LEVEL_2);
                break;
            case R.id.id_level_3:
                m_onLevelSelected.onLevelSelected(ConstUtil.ITEM_LEVEL_3);
                break;
            case R.id.id_level_4:
                m_onLevelSelected.onLevelSelected(ConstUtil.ITEM_LEVEL_4);
                break;
            default: m_onLevelSelected.onLevelSelected(ConstUtil.ITEM_LEVEL_5);
                break;
        }

        dismiss();
    }

    @OnClick(R.id.id_cancel)
    void onCancel() {
        dismiss();
    }

    public void setOnLevelSelected(OnLevelSelected onLevelSelected) {
        m_onLevelSelected = onLevelSelected;
    }

    public interface OnLevelSelected {
        void onLevelSelected(short level);
    }
}
