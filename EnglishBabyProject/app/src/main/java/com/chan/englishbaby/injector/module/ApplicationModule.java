package com.chan.englishbaby.injector.module;

import android.app.Application;
import android.content.Context;

import com.chan.englishbaby.injector.annotation.ContextLife;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chan on 16/2/27.
 */
@Module
public class ApplicationModule {

    private Application m_application;

    public ApplicationModule(Application application) {
        m_application = application;
    }

    @ContextLife("application")
    @Singleton
    @Provides
    public Context provideContext() {
        return m_application;
    }
}
