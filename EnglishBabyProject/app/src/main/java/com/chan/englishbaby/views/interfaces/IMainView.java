package com.chan.englishbaby.views.interfaces;

import com.chan.englishbaby.proto.EnglishBookProto;
import com.chan.englishbaby.views.interfaces.IView;

import java.util.List;

/**
 * Created by chan on 16/6/20.
 */
public interface IMainView extends IView {
    void showUnits(List<EnglishBookProto.Unit> units);
    void showError(String message);
    void showLessons(List<EnglishBookProto.Lesson> lessons);
}
