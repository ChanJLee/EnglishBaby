package com.chan.englishbaby.presenter.impls;

import android.app.Activity;

import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.views.impls.IMainView;
import com.chan.englishbaby.views.interfaces.IView;

import javax.inject.Inject;

/**
 * Created by chan on 16/6/20.
 */
public class MainActivityPresenter implements IPresenter {

    private Activity m_activity;
    private IMainView m_mainView;

    @Inject
    public MainActivityPresenter(Activity activity) {
        m_activity = activity;
    }

    @Override
    public void attachView(IView view) {
        m_mainView = (IMainView) view;
    }

    @Override
    public void detachView() {
        m_mainView = null;
    }
}