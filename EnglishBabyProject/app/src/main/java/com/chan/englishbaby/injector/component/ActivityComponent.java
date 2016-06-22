package com.chan.englishbaby.injector.component;

import android.app.Activity;
import android.content.Context;

import com.chan.englishbaby.MainActivity;
import com.chan.englishbaby.injector.annotation.PerActivity;
import com.chan.englishbaby.injector.module.ActivityModule;
import com.chan.englishbaby.injector.annotation.ContextLife;
import com.chan.englishbaby.model.rx.RxBook;
import com.chan.englishbaby.model.rx.RxWordLevel;
import com.chan.englishbaby.views.impls.InitActivity;
import com.chan.englishbaby.views.impls.LessonActivity;
import com.chan.englishbaby.views.impls.SplashActivity;
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

    RxBook getRxBook();

    RxWordLevel getRxWordLevel();

    void inject(MainActivity activity);
    void inject(SplashActivity activity);
    void inject(InitActivity activity);
    void inject(LessonActivity activity);
}
