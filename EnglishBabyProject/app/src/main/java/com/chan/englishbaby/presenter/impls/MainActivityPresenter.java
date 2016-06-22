package com.chan.englishbaby.presenter.impls;

import android.app.Activity;

import com.chan.englishbaby.model.rx.RxBook;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.proto.EnglishBookProto;
import com.chan.englishbaby.views.interfaces.IMainView;
import com.chan.englishbaby.views.interfaces.IView;
import com.chan.englishboby.R;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by chan on 16/6/20.
 */
public class MainActivityPresenter implements IPresenter {

    private Activity m_activity;
    private IMainView m_mainView;
    private EnglishBookProto.EnglishBook m_englishBook;
    private RxBook m_rxBook;

    @Inject
    public MainActivityPresenter(Activity activity, RxBook rxBook) {
        m_activity = activity;
        m_rxBook = rxBook;
    }

    @Override
    public void attachView(IView view) {
        m_mainView = (IMainView) view;
    }

    @Override
    public void detachView() {
        m_mainView = null;
    }

    public void load() {
        m_rxBook.readEnglishBook().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EnglishBookProto.EnglishBook>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                m_mainView.showError(m_activity.getString(R.string.main_error_load));
            }

            @Override
            public void onNext(EnglishBookProto.EnglishBook englishBook) {
                List<EnglishBookProto.Unit> list = englishBook.getUnitsList();

                if (!list.isEmpty()) {
                    m_mainView.showLessons(list.get(0).getLessonsList());
                }

                m_mainView.showUnits(list);
                m_englishBook = englishBook;
            }
        });
    }
}