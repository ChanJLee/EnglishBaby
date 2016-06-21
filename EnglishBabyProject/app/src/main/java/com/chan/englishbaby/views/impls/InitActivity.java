package com.chan.englishbaby.views.impls;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.chan.englishbaby.base.BaseActivity;
import com.chan.englishbaby.injector.component.ActivityComponent;
import com.chan.englishbaby.presenter.impls.InitActivityPresenter;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.views.interfaces.IInitView;
import com.chan.englishbaby.views.interfaces.IView;
import com.chan.englishboby.R;
import com.daimajia.numberprogressbar.NumberProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chan on 16/6/21.
 */
public class InitActivity extends BaseActivity implements IInitView {

    @Bind(R.id.id_processBar)
    NumberProgressBar m_textProgressBar;

    @Inject
    InitActivityPresenter m_initActivityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        m_initActivityPresenter.load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected IView createView() {
        return this;
    }

    @Override
    protected IPresenter createPresenter() {
        return m_initActivityPresenter;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_init;
    }

    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, InitActivity.class);
        activity.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setProcess(Integer process) {
        m_textProgressBar.setProgress(process);
    }

    @Override
    public void showProcess(Integer integer) {
        EventBus.getDefault().post(integer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        EventBus.getDefault().post(message);
    }
}
