package com.chan.englishbaby.model.rx;

import android.content.Context;
import android.util.Pair;

import com.chan.englishbaby.injector.annotation.ContextLife;
import com.chan.englishbaby.model.parse.WordLevelParser;
import com.chan.englishboby.R;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by chan on 16/6/22.
 */
public class RxWordLevel {

    private Context m_context;
    private static WeakReference<List<Pair<String, Integer>>> s_listWeakReference;

    public RxWordLevel(@ContextLife("application") Context context) {
        m_context = context;
    }

    public Observable<List<Pair<String, Integer>>> getWordsLevel() {
        return Observable.create(new Observable.OnSubscribe<List<Pair<String, Integer>>>() {
            @Override
            public void call(Subscriber<? super List<Pair<String, Integer>>> subscriber) {
                if (s_listWeakReference != null && s_listWeakReference.get() != null) {
                    subscriber.onNext(s_listWeakReference.get());
                } else {
                    try {
                        WordLevelParser wordLevelParser = new WordLevelParser(m_context.getResources().openRawResource(R.raw.nce4_words));
                        List<Pair<String, Integer>> result = wordLevelParser.getWordsLevel();
                        s_listWeakReference = new WeakReference<List<Pair<String, Integer>>>(result);
                        subscriber.onNext(result);
                    } catch (Exception e) {
                        subscriber.onError(e);
                        return;
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }
}
