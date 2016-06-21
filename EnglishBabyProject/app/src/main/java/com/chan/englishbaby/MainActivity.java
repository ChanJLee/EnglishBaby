package com.chan.englishbaby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chan.babytextviewedit.BabyTextView;
import com.chan.englishbaby.base.BaseActivity;
import com.chan.englishbaby.injector.component.ActivityComponent;
import com.chan.englishbaby.presenter.impls.MainActivityPresenter;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.views.interfaces.IMainView;
import com.chan.englishbaby.views.interfaces.IView;
import com.chan.englishboby.R;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements IMainView {

    @Inject
    MainActivityPresenter m_mainActivityPresenter;

    @Bind(R.id.id_text)
    BabyTextView m_babyTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

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

    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
