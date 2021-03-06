package com.chan.englishbaby.injector.component;

import android.content.Context;


import com.chan.englishbaby.base.BaseApplication;
import com.chan.englishbaby.injector.annotation.ContextLife;
import com.chan.englishbaby.injector.module.ApplicationModule;
import com.chan.englishbaby.model.sp.PreferenceConfig;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by chan on 16/2/27.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ContextLife("application")
    Context getContext();

    PreferenceConfig getPreferenceConfig();

    void inject(BaseApplication application);
}
