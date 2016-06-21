package com.chan.englishbaby.presenter.impls;

import android.app.Activity;

import com.chan.englishbaby.MainActivity;
import com.chan.englishbaby.model.rx.RxBook;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.views.interfaces.IInitView;
import com.chan.englishbaby.views.interfaces.IView;
import com.chan.englishboby.R;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by chan on 16/6/21.
 */
public class InitActivityPresenter implements IPresenter {

    private IInitView m_initView;
    private Activity m_activity;
    private RxBook m_rxBook;

    @Inject
    public InitActivityPresenter(Activity activity, RxBook rxBook) {
        m_activity = activity;
        m_rxBook = rxBook;
    }

    @Override
    public void attachView(IView view) {
        m_initView = (IInitView) view;
    }

    @Override
    public void detachView() {
        m_initView = null;
    }

    public void load() {
        m_rxBook.loadBook().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                invokeMainActivity();
            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }

            @Override
            public void onNext(Integer integer) {
                doNext(integer);
            }
        });
    }

    private void doNext(Integer process) {
        m_initView.showProcess(process);
    }

    private void invokeMainActivity() {
        MainActivity.invoke(m_activity);
        m_activity.finish();
    }

    private void handleError(Throwable e) {
        m_initView.showError(m_activity.getString(R.string.load_error));
        m_activity.finish();
    }
}
