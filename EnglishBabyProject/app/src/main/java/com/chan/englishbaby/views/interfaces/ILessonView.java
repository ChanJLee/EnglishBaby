package com.chan.englishbaby.views.interfaces;

/**
 * Created by chan on 16/6/22.
 */
public interface ILessonView extends IView {
    void showLoading(String message);
    void dismissLoading();
    void showError(String error);
}
