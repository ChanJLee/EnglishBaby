package com.chan.englishbaby.injector.module;

import android.app.Activity;
import android.content.Context;

import com.chan.englishbaby.injector.annotation.ContextLife;
import com.chan.englishbaby.injector.annotation.PerActivity;
import com.chan.englishbaby.model.rx.RxBook;
import com.chan.englishbaby.model.rx.RxWordLevel;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chan on 16/2/27.
 */
@Module
public class ActivityModule {
    private Activity m_activity;

    public ActivityModule(Activity activity) {
        m_activity = activity;
    }

    @Provides
    @PerActivity
    @ContextLife("activity")
    public Context provideContext() {
        return m_activity;
    }

    @PerActivity
    @Provides
    public Activity provideActivity() {
        return m_activity;
    }

    @Provides
    @PerActivity
    public SystemBarTintManager provideSystemBarTintManager() {
        return new SystemBarTintManager(m_activity);
    }

    @Provides
    @PerActivity
    public RxBook provideRxBook() {
        return new RxBook(m_activity.getApplication());
    }

    @Provides
    @PerActivity
    public RxWordLevel provideRxWordLevel() {
        return new RxWordLevel(m_activity.getApplication());
    }
}
