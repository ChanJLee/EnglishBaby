package com.chan.englishbaby.model.rx;

import android.content.Context;
import android.util.Log;

import com.chan.englishbaby.bean.EnglishBookProto;
import com.chan.englishbaby.injector.annotation.ContextLife;
import com.chan.englishbaby.model.parse.BookParser;
import com.chan.englishboby.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by chan on 16/6/21.
 */
public class RxBook {

    private String m_protoFileAbsolutePath;
    private Context m_context;
    private static WeakReference<EnglishBookProto.EnglishBook> s_englishBookWeakReference;

    @Inject
    public RxBook(@ContextLife("application") Context context) {
        m_context = context;
        File dir = context.getDir("proto_cache", Context.MODE_PRIVATE);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "english_book_cache");
        m_protoFileAbsolutePath = file.getAbsolutePath();
    }

    public Observable<EnglishBookProto.EnglishBook> readEnglishBook() {
        return Observable.create(new Observable.OnSubscribe<EnglishBookProto.EnglishBook>() {
            @Override
            public void call(Subscriber<? super EnglishBookProto.EnglishBook> subscriber) {

                try {
                    EnglishBookProto.EnglishBook englishBook = null;

                    if (s_englishBookWeakReference != null && s_englishBookWeakReference.get() != null) {
                        englishBook = s_englishBookWeakReference.get();
                    } else {
                        File file = new File(m_protoFileAbsolutePath);
                        englishBook = EnglishBookProto.EnglishBook.parseFrom(new FileInputStream(file));
                        s_englishBookWeakReference = new WeakReference<EnglishBookProto.EnglishBook>(englishBook);
                    }

                    subscriber.onNext(englishBook);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Integer> loadBook() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                try {

                    InputStream inputStream = m_context.getResources().openRawResource(R.raw.book);
                    subscriber.onNext(10);

                    BookParser bookParser = new BookParser(inputStream);
                    subscriber.onNext(40);

                    EnglishBookProto.EnglishBook englishBook = bookParser.loadBook();
                    subscriber.onNext(80);

                    for (EnglishBookProto.Unit unit : englishBook.getUnitsList()) {
                        Log.d("chan_debug unit", unit.getName());

                    }

                    s_englishBookWeakReference = new WeakReference<EnglishBookProto.EnglishBook>(englishBook);

                    englishBook.writeTo(new FileOutputStream(m_protoFileAbsolutePath));
                    subscriber.onNext(100);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).distinct();
    }
}
