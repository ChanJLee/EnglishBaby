package com.chan.englishbaby.base;

import android.app.Application;

import com.chan.englishbaby.injector.component.ApplicationComponent;
import com.chan.englishbaby.injector.component.DaggerApplicationComponent;
import com.chan.englishbaby.injector.module.ApplicationModule;


/**
 * Created by chan on 16/6/7.
 */
public class BaseApplication extends Application {

    private ApplicationComponent m_applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        m_applicationComponent =
                DaggerApplicationComponent.builder()
                        .applicationModule(new ApplicationModule(this)).build();
        m_applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return m_applicationComponent;
    }
}
