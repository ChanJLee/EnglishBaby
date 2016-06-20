package com.chan.englishbaby.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.chan.englishbaby.injector.component.DaggerActivityComponent;
import com.chan.englishbaby.injector.module.ActivityModule;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.utils.ActivityStack;
import com.chan.englishboby.R;
import com.chan.englishbaby.injector.component.ActivityComponent;
import com.chan.englishbaby.views.interfaces.IView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by chan on 16/6/7.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent m_activityComponent;
    private IPresenter m_iPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        initDagger2();
        initBar();
        initContentView();

        ActivityStack.push(this);
    }


    protected void initBar() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager systemBarTintManager = m_activityComponent.getSystemBarTintManager();
            systemBarTintManager.setStatusBarTintColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setNavigationBarTintEnabled(true);
        }
    }

    private void initDagger2() {
        BaseApplication baseApplication = (BaseApplication) getApplication();
        m_activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(baseApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this)).build();
        inject(m_activityComponent);
        m_iPresenter = createPresenter();

        if (m_iPresenter != null) {
            m_iPresenter.attachView(createView());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m_iPresenter != null) {
            m_iPresenter.detachView();
        }

        ActivityStack.pop(this);
    }

    private void initContentView() {
        setContentView(getContentViewId());
    }

    protected abstract IView createView();
    protected abstract IPresenter createPresenter();
    protected abstract void inject(ActivityComponent activityComponent);
    protected abstract int getContentViewId();

    protected void quitApplication() {
        ActivityStack.forEach(new ActivityStack.Predicate() {
            @Override
            public void func(Activity activity) {
                if (activity.isFinishing()) {
                    activity.finish();
                }
            }
        });
    }
}
