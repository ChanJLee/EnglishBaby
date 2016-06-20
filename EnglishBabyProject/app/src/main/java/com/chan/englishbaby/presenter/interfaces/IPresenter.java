package com.chan.englishbaby.presenter.interfaces;

import com.chan.englishbaby.views.interfaces.IView;

/**
 * Created by chan on 16/6/7.
 */
public interface IPresenter {
    void attachView(IView view);
    void detachView();
}
