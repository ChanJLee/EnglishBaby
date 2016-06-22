package com.chan.englishbaby.presenter.impls;

import android.app.Activity;

import com.chan.englishbaby.model.rx.RxWordLevel;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.views.interfaces.ILessonView;
import com.chan.englishbaby.views.interfaces.IView;

import javax.inject.Inject;

/**
 * Created by chan on 16/6/22.
 */
public class LessonActivityPresenter implements IPresenter {
    private Activity m_activity;
    private RxWordLevel m_rxWordLevel;
    private ILessonView m_iLessonView;

    @Inject
    public LessonActivityPresenter(Activity activity, RxWordLevel rxWordLevel) {
        m_activity = activity;
        m_rxWordLevel = rxWordLevel;
    }

    @Override
    public void attachView(IView view) {
        m_iLessonView = (ILessonView) view;
    }

    @Override
    public void detachView() {
        m_iLessonView = null;
    }
}
