package com.chan.englishbaby.model.parse;


import android.text.TextUtils;

import com.chan.englishbaby.proto.EnglishBookProto;
import com.chan.englishbaby.core.AbstractBookParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 16/6/21.
 */
public class BookParser extends AbstractBookParser {

    private EnglishBookProto.EnglishBook.Builder m_englishBuilder;
    private List<EnglishBookProto.Unit.Builder> m_unitBuilderContainer = new ArrayList<>();
    private List<EnglishBookProto.Lesson.Builder> m_lessonBuilderContainer = new ArrayList<>();
    private EnglishBookProto.Lesson.Builder m_currentLessonBuilder;
    private EnglishBookProto.Word.Builder m_currentWordBuilder;
    private EnglishBookProto.Unit.Builder m_currentUnitBuilder;

    public BookParser(InputStream inputStream) {
        super(inputStream);
    }

    public EnglishBookProto.EnglishBook loadBook() throws IOException {
        m_englishBuilder = EnglishBookProto.EnglishBook.newBuilder();
        parse();
        return m_englishBuilder.build();
    }

    @Override
    protected void parse() throws IOException {
        m_currentUnitBuilder = null;
        m_unitBuilderContainer.clear();
        m_lessonBuilderContainer.clear();
        super.parse();
    }

    @Override
    protected void parsedUnit(String unit) {

        //不是第一次分析Unit
        if (m_currentUnitBuilder != null) {

            //之后要把上一次的lesson入栈
            pushBackLesson();

            //一个单元分析完 直接返回
            m_lessonBuilderContainer.clear();
        }

        EnglishBookProto.Unit.Builder builder = EnglishBookProto.Unit.newBuilder();
        builder.setName(unit);
        m_currentUnitBuilder = builder;
        m_unitBuilderContainer.add(builder);
    }

    private void pushBackLesson() {
        EnglishBookProto.Unit.Builder builder = m_unitBuilderContainer.get(m_unitBuilderContainer.size() - 1);

        final int size = m_lessonBuilderContainer.size();
        for (int i = 0; i < size; ++i) {
            EnglishBookProto.Lesson.Builder lessonBuilder = m_lessonBuilderContainer.get(i);
            builder.addLessons(lessonBuilder.build());
        }
    }

    @Override
    protected void parsedLesson(String lesson) {
        EnglishBookProto.Lesson.Builder builder = EnglishBookProto.Lesson.newBuilder();
        builder.setName(lesson);
        m_currentLessonBuilder = builder;
        m_lessonBuilderContainer.add(builder);
    }

    @Override
    protected void parsedEnglishTitle(String title) {
        m_currentLessonBuilder.setTitleEnglish(title);
    }

    @Override
    protected void parsedChineseTitle(String title) {
        m_currentLessonBuilder.setTitleChinese(title);
    }

    @Override
    protected void parsedListenQuestion(String QuestionContent) {
        m_currentLessonBuilder.setListenQuestion(QuestionContent);
    }

    @Override
    protected void parsedContent(String segment) {
        String previous = m_currentLessonBuilder.getContent();
        if (TextUtils.isEmpty(previous)) {
            m_currentLessonBuilder.setContent(segment);
        } else {
            m_currentLessonBuilder.setContent(previous + "\n" + segment);
        }
    }

    @Override
    protected void parsedWord(String word) {
        m_currentWordBuilder = EnglishBookProto.Word.newBuilder();
        m_currentWordBuilder.setContent(word);
    }

    @Override
    protected void parsedWordAttribute(String attribute) {
        m_currentWordBuilder.setAttribute(attribute);
        m_currentLessonBuilder.addWords(m_currentWordBuilder.build());
    }

    @Override
    protected void parsedWordTranslation(String segment) {
        String previous = m_currentLessonBuilder.getTranslation();
        if (TextUtils.isEmpty(previous)) {
            m_currentLessonBuilder.setTranslation(segment);
        } else {
            m_currentLessonBuilder.setTranslation(previous + "\n" + segment);
        }
    }

    @Override
    protected void parseFinished() {

        //最后一个分析的 unit是不会入栈的哦！ 要加进去
        //因为我们永远不知道一个unit何时会分析结束 除非我们已经知道
        pushBackLesson();

        for (EnglishBookProto.Unit.Builder builder : m_unitBuilderContainer) {
            m_englishBuilder.addUnits(builder.build());
        }
    }
}
