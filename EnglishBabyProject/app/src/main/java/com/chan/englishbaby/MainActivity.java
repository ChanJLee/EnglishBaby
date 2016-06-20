package com.chan.englishbaby;

import android.os.Bundle;

import com.chan.englishbaby.base.BaseActivity;
import com.chan.englishbaby.injector.component.ActivityComponent;
import com.chan.englishbaby.presenter.impls.MainActivityPresenter;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.views.impls.IMainView;
import com.chan.englishboby.R;
import com.chan.englishbaby.views.interfaces.IView;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements IMainView {

    @Inject
    MainActivityPresenter m_mainActivityPresenter;

    @Override
    protected IView createView() {
        return this;
    }

    @Override
    protected IPresenter createPresenter() {
        return m_mainActivityPresenter;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }
}
