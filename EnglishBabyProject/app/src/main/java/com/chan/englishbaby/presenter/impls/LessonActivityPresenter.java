package com.chan.englishbaby.presenter.impls;

import android.app.Activity;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Pair;

import com.chan.englishbaby.bus.ErrorMessage;
import com.chan.englishbaby.bus.SimpleMessage;
import com.chan.englishbaby.model.rx.RxWordLevel;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.utils.CharUtil;
import com.chan.englishbaby.views.interfaces.ILessonView;
import com.chan.englishbaby.views.interfaces.IView;
import com.chan.englishboby.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by chan on 16/6/22.
 */
public class LessonActivityPresenter implements IPresenter {
    private Activity m_activity;
    private RxWordLevel m_rxWordLevel;
    private ILessonView m_iLessonView;
    private static WeakReference<Map<Integer, List<String>>> s_levelMap;
    private Map<Integer, List<String>> m_levelMap = new HashMap<>();
    private Map<Integer, List<BackgroundColorSpan>> m_spanMap = new HashMap<>();

    private static final short NONE = 0x01;
    private static final short LEVEL_0 = 0x02;
    private static final short LEVEL_1 = 0x04;
    private static final short LEVEL_2 = 0x08;
    private static final short LEVEL_3 = 0x10;
    private static final short LEVEL_4 = 0x100;
    private static final short LEVEL_5 = 0x1000;
    
    private short m_currentMask = NONE;
    
    @Inject
    public LessonActivityPresenter(Activity activity, RxWordLevel rxWordLevel) {
        m_activity = activity;
        m_rxWordLevel = rxWordLevel;
    }

    public void load() {

        m_iLessonView.showLoading(m_activity.getString(R.string.lesson_load_message));
        if (s_levelMap != null && s_levelMap.get() != null) {
            m_levelMap = s_levelMap.get();
            m_iLessonView.dismissLoading();
            return;
        }

        m_rxWordLevel.getWordsLevel().subscribe(new Subscriber<List<Pair<String, Integer>>>() {
            @Override
            public void onCompleted() {
                s_levelMap = new WeakReference<Map<Integer, List<String>>>(m_levelMap);
                EventBus.getDefault().post(new SimpleMessage(null));
            }

            @Override
            public void onError(Throwable e) {
                EventBus.getDefault().post(new ErrorMessage(m_activity.getString(R.string.load_error_lesson)));
            }

            @Override
            public void onNext(List<Pair<String, Integer>> pairs) {
                for (Pair<String, Integer> pair : pairs) {
                    List<String> list = m_levelMap.get(pair.second);
                    if (list == null) {
                        list = new ArrayList<String>();
                        m_levelMap.put(pair.second, list);
                    }
                    list.add(pair.first);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void complete(SimpleMessage message) {
        m_iLessonView.dismissLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void error(ErrorMessage message) {
        m_iLessonView.showError(message.m_what);
        m_iLessonView.dismissLoading();
    }

    @Override
    public void attachView(IView view) {
        m_iLessonView = (ILessonView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
        m_iLessonView = null;
    }

    public void setHighLight(Spannable spannable, int level) {

        if (level == NONE) {
            clearAllSpan(spannable);
        } else if (level == LEVEL_0) {
            setHigh0Span(spannable);
        } else if (level == LEVEL_1) {
            setHigh1Span(spannable);
        } else if (level == LEVEL_2) {
            setHigh2Span(spannable);
        } else if (level == LEVEL_3) {
            setHigh3Span(spannable);
        } else if (level == LEVEL_4) {
            setHigh4Span(spannable);
        } else {
            setHigh5Span(spannable);
        }
    }

    public void clearAllSpan(Spannable spannable) {
        clearHigh0Span(spannable);
        clearHigh1Span(spannable);
        clearHigh2Span(spannable);
        clearHigh3Span(spannable);
        clearHigh4Span(spannable);
        clearHigh5Span(spannable);
    }

    public void setHigh0Span(Spannable span) {

        if (!hasLightLevel0()) {
            setSpan(0, span, m_activity.getResources().getColor(R.color.level0));
        }

        clearHigh1Span(span);
        clearHigh2Span(span);
        clearHigh3Span(span);
        clearHigh4Span(span);
        clearHigh5Span(span);
        setLevel0Mask();
    }

    public void setSpan(int key, Spannable span, int color) {

        List<BackgroundColorSpan> list = m_spanMap.get(key);
        if (list == null) {
            list = new ArrayList<>();
            m_spanMap.put(key, list);
        }

        List<String> words = m_levelMap.get(key);
        String content = span.toString();

        boolean isAdd = list.isEmpty();

        int spanIndex = 0;
        for (String word : words) {

            final int index = content.indexOf(word);
            if (index == -1) continue;

            if (index != 0 && (CharUtil.isAscii(content.charAt(index - 1)) ||
                    CharUtil.isAscii(content.charAt(index + word.length())))) {
                continue;
            }

            BackgroundColorSpan backgroundColorSpan = null;
            if (isAdd) {
                backgroundColorSpan = new BackgroundColorSpan(color);
                list.add(backgroundColorSpan);
            } else {
                backgroundColorSpan = list.get(spanIndex);
                spanIndex++;
            }

            span.setSpan(backgroundColorSpan, index, word.length() + index, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    private void setHigh1Span(Spannable span) {
        setHigh0Span(span);

        if (!hasLightLevel0()) {
            setSpan(1, span, m_activity.getResources().getColor(R.color.level1));
        }

        clearHigh2Span(span);
        clearHigh3Span(span);
        clearHigh4Span(span);
        clearHigh5Span(span);
        setLevel1Mask();
    }

    private void setHigh2Span(Spannable span) {
        if (!hasLightLevel2()) {
            setSpan(2, span, m_activity.getResources().getColor(R.color.level2));
        }
        setHigh1Span(span);
        clearHigh3Span(span);
        clearHigh4Span(span);
        clearHigh5Span(span);
        setLevel2Mask();
    }

    private void setHigh3Span(Spannable span) {
        if (!hasLightLevel3()) {
            setSpan(3, span, m_activity.getResources().getColor(R.color.level3));
        }
        setHigh2Span(span);
        clearHigh4Span(span);
        clearHigh5Span(span);
        setLevel3Mask();
    }

    private void setHigh4Span(Spannable span) {
        if (!hasLightLevel4()) {
            setSpan(4, span, m_activity.getResources().getColor(R.color.level4));
        }
        setHigh3Span(span);
        clearHigh5Span(span);
        setLevel4Mask();
    }

    private void setHigh5Span(Spannable span) {
        if (!hasLightLevel5()) {
            setSpan(5, span, m_activity.getResources().getColor(R.color.level5));
        }
        setHigh4Span(span);
        setLevel5Mask();
    }

    private void clearSpan(int key, Spannable spannable) {
        List<BackgroundColorSpan> list = m_spanMap.get(key);
        if (list == null) return;

        for (BackgroundColorSpan span : list) {
            spannable.removeSpan(span);
        }
    }

    private void clearHigh0Span(Spannable span) {

        if (!hasLightLevel0()) return;

        clearSpan(0, span);
        clearLevel0Mask();
    }

    private void clearHigh1Span(Spannable span) {
        if (!hasLightLevel1()) return;

        clearSpan(1, span);
        clearLevel1Mask();
    }

    private void clearHigh2Span(Spannable span) {

        if (!hasLightLevel2()) return;
        clearSpan(2, span);
        clearLevel2Mask();
    }

    private void clearHigh3Span(Spannable span) {
        if (!hasLightLevel3()) return;
        clearSpan(3, span);
        clearLevel3Mask();
    }

    private void clearHigh4Span(Spannable span) {
        if (!hasLightLevel4()) return;
        clearSpan(4, span);
        clearLevel4Mask();
    }

    private void clearHigh5Span(Spannable span) {
        if (!hasLightLevel5()) return;
        clearSpan(5, span);
        clearLevel5Mask();
    }

    private boolean hasLightNone() {
        return (m_currentMask & NONE) != 0;
    }

    private boolean hasLightLevel0() {
        return (m_currentMask & LEVEL_0) != 0;
    }


    private boolean hasLightLevel1() {
        return (m_currentMask & LEVEL_1) != 0;
    }

    private boolean hasLightLevel2() {
        return (m_currentMask & LEVEL_2) != 0;
    }

    private boolean hasLightLevel3() {
        return (m_currentMask & LEVEL_3) != 0;
    }

    private boolean hasLightLevel4() {
        return (m_currentMask & LEVEL_4) != 0;
    }

    private boolean hasLightLevel5() {
        return (m_currentMask & LEVEL_5) != 0;
    }

    private void setNoneMask() {
        m_currentMask |= NONE;
    }

    private void setLevel0Mask() {
        m_currentMask |= LEVEL_0;
    }

    private void setLevel1Mask() {
        m_currentMask |= LEVEL_1;
    }

    private void setLevel2Mask() {
        m_currentMask |= LEVEL_2;
    }

    private void setLevel3Mask() {
        m_currentMask |= LEVEL_3;
    }

    private void setLevel4Mask() {
        m_currentMask |= LEVEL_4;
    }

    private void setLevel5Mask() {
        m_currentMask |= LEVEL_5;
    }

    private void clearNoneMask() {
        if (hasLightNone()) {
            m_currentMask &= (~NONE);
        }
    }

    private void clearLevel0Mask() {
        if (hasLightLevel0()) {
            m_currentMask &= (~LEVEL_0);
        }
    }

    private void clearLevel1Mask() {
        if (hasLightLevel1()) {
            m_currentMask &= (~LEVEL_1);
        }
    }

    private void clearLevel2Mask() {
        if (hasLightLevel2()) {
            m_currentMask &= (~LEVEL_2);
        }
    }

    private void clearLevel3Mask() {
        if (hasLightLevel3()) {
            m_currentMask &= (~LEVEL_3);
        }
    }

    private void clearLevel4Mask() {
        if (hasLightLevel4()) {
            m_currentMask &= (~LEVEL_4);
        }
    }

    private void clearLevel5Mask() {
        if (hasLightLevel5()) {
            m_currentMask &= (~LEVEL_5);
        }
    }
}
