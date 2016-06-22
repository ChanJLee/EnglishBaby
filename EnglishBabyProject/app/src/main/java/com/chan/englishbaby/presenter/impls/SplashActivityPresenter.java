package com.chan.englishbaby.presenter.impls;

import android.app.Activity;

import com.chan.englishbaby.MainActivity;
import com.chan.englishbaby.model.sp.PreferenceConfig;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.views.impls.InitActivity;
import com.chan.englishbaby.views.interfaces.ISplashView;
import com.chan.englishbaby.views.interfaces.IView;

import javax.inject.Inject;

/**
 * Created by chan on 16/6/21.
 */
public class SplashActivityPresenter implements IPresenter {
    private ISplashView m_splashView;

    private Activity m_activity;
    private PreferenceConfig m_preferenceConfig;

    @Inject
    public SplashActivityPresenter(Activity activity, PreferenceConfig preferenceConfig) {
        m_activity = activity;
        m_preferenceConfig = preferenceConfig;
    }

    @Override
    public void attachView(IView view) {
        m_splashView = (ISplashView) view;
    }

    @Override
    public void detachView() {
        m_splashView = null;
    }

    public void invokeActivity() {
        if (m_preferenceConfig.isFirstUse()) {
            InitActivity.invoke(m_activity);
            m_preferenceConfig.setHasUsed();
        } else {
            MainActivity.invoke(m_activity);
        }
    }
}
