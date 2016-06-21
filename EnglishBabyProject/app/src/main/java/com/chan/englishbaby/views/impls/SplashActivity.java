package com.chan.englishbaby.views.impls;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chan.englishbaby.base.BaseActivity;
import com.chan.englishbaby.injector.component.ActivityComponent;
import com.chan.englishbaby.presenter.impls.SplashActivityPresenter;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.views.interfaces.ISplashView;
import com.chan.englishbaby.views.interfaces.IView;
import com.chan.englishboby.R;

import javax.inject.Inject;

/**
 * Created by chan on 16/6/21.
 */
public class SplashActivity extends BaseActivity implements ISplashView {

    @Inject
    SplashActivityPresenter m_splashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_splashPresenter.invokeActivity();
        finish();
    }

    @Override
    protected IView createView() {
        return this;
    }

    @Override
    protected IPresenter createPresenter() {
        return m_splashPresenter;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {

        activityComponent.inject(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }
}
