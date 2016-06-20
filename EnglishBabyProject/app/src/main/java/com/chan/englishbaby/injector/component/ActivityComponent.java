package com.chan.englishbaby.injector.component;

import android.app.Activity;
import android.content.Context;

import com.chan.englishbaby.MainActivity;
import com.chan.englishbaby.injector.annotation.PerActivity;
import com.chan.englishbaby.injector.module.ActivityModule;
import com.chan.englishbaby.injector.annotation.ContextLife;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import dagger.Component;

/**
 * Created by chan on 16/2/27.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("application")
    Context getApplicationContext();

    @ContextLife("activity")
    Context getActivityContext();

    Activity getActivity();

    SystemBarTintManager getSystemBarTintManager();
    void inject(MainActivity activity);
}
