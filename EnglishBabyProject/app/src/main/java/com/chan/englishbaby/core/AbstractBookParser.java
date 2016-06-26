package com.chan.englishbaby.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chan on 16/6/21.
 * intellij 项目
 */
public abstract class AbstractBookParser {

    private InputStream m_inputStream;
    private ParseState m_parseState;

    public AbstractBookParser(InputStream inputStream) {
        m_inputStream = inputStream;
        setState(new UnitParseState());
    }

    protected void parse() throws IOException {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        String content = null;

        try {
            inputStreamReader = new InputStreamReader(m_inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            while ((content = bufferedReader.readLine()) != null) {
                m_parseState.parse(content);
            }
            parseFinished();
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    private void setState(ParseState state) {
        m_parseState = state;
    }

    private interface ParseState {
        void parse(String newLine);
    }

    private class UnitParseState implements ParseState {

        @Override
        public void parse(String newLine) {
            setState(new LessonParseState());

            //如果是lesson 那么 要回退单词
            if (newLine.toLowerCase().contains("lesson")) {
                m_parseState.parse(newLine);
                return;
            }

            parsedUnit(newLine.trim());
        }
    }

    private class LessonParseState implements ParseState {

        @Override
        public void parse(String newLine) {

            //System.out.println("课程: " + newLine);
            parsedLesson(newLine.trim());
            setState(new TitleParseState());
        }
    }

    private class TitleParseState implements ParseState {

        boolean isEnglishTitle = true;

        @Override
        public void parse(String newLine) {

            newLine = newLine.trim();

            if (TextUtils.isEmpty(newLine)) {
                setState(new ListenTitleParseState());
                return;
            } else if (newLine.contains("First listen and then answer the following")) {
                //37 47 会出现错误   47是typo
                setState(new ListenTitleParseState());
                m_parseState.parse(newLine);
                return;
            }

            int index = 0;
            int length = newLine.length();
            for (; index < length; ++index) {
                if (TextUtils.isChinese(newLine.charAt(index))) break;
            }

            //中文标题 和英文标题在同一行
            if (index != length && index != 0) {
                parsedEnglishTitle(newLine.substring(0, index).trim());
                parsedChineseTitle(newLine.substring(index).trim());
            } else {
                if (isEnglishTitle) {

                    //英文标题
                    parsedEnglishTitle(newLine.trim());
                    isEnglishTitle = false;
                } else {

                    //中文标题
                    parsedChineseTitle(newLine.trim());
                    isEnglishTitle = true;
                }
            }
        }
    }

    private class ListenTitleParseState implements ParseState {

        @Override
        public void parse(String newLine) {
            if (TextUtils.isEmpty(newLine)) {
                setState(new ListenQuestionParseState());
            } else if (newLine.contains("?")) {
                setState(new ListenQuestionParseState());
                m_parseState.parse(newLine);
            }
        }
    }

    private class ListenQuestionParseState implements ParseState {

        private String m_question;

        @Override
        public void parse(String newLine) {

            if (TextUtils.isEmpty(newLine)) {
                parsedListenQuestion(m_question);
                setState(new ContentParseState());
                return;
            } else if (newLine.contains("?")) {

                if (android.text.TextUtils.isEmpty(m_question)) {
                    m_question = newLine.trim();
                } else {
                    m_question = m_question + "\n" + newLine.trim();
                }

                parsedListenQuestion(m_question);
                setState(new ContentParseState());
                return;
            }

            m_question = newLine.trim();
        }
    }

    private class ContentParseState implements ParseState {

        @Override
        public void parse(String newLine) {

            //内容可能有多段 中间不是以换行符作为分隔的
            //所以只能检测关键字
            if (newLine.contains("生词和短语") || newLine.contains("New words and expression")) {
                setState(new WordsTitleParseState());
                m_parseState.parse(newLine);
                return;
            }

            parsedContent(newLine);
        }
    }

    private class WordsTitleParseState implements ParseState {

        @Override
        public void parse(String newLine) {

            //这里可能也不是有空格的
            if (TextUtils.isEmpty(newLine)) {
                setState(new WordsParseState());
            } else if (newLine.contains("生词和短语") || newLine.contains("New words and expression")) {
                //System.out.println("单词标题: " + newLine);
            } else {
                setState(new WordsParseState());
                m_parseState.parse(newLine);
            }
        }
    }

    private class WordsParseState implements ParseState {

        private boolean m_isWord = true;

        @Override
        public void parse(String newLine) {
            newLine = newLine.trim();
            if (TextUtils.isEmpty(newLine)) {
                setState(new TranslationTitleParseState());
                return;
            } else if (newLine.contains("参考译文")) {

                //有的时候 单词下面是没有空格的 那么就查询关键字
                setState(new TranslationTitleParseState());
                m_parseState.parse(newLine);
                return;
            }

            if (m_isWord) {
                parsedWord(newLine);
            } else {
                parsedWordAttribute(newLine);
            }

            m_isWord = !m_isWord;
        }
    }

    private class TranslationTitleParseState implements ParseState {

        boolean hasPickedTitle = false;

        @Override
        public void parse(String newLine) {
            newLine = newLine.trim();

            if (newLine.contains("参考译文")) {
                hasPickedTitle = true;
                return;
            }

            setState(new TranslationParseState());

            if (!TextUtils.isEmpty(newLine)) {
                m_parseState.parse(newLine);
            }
        }
    }

    private class TranslationParseState implements ParseState {

        @Override
        public void parse(String newLine) {

            if (TextUtils.isEmpty(newLine)) {
                setState(new UnitParseState());
                return;
            } else if (newLine.contains("Lesson") || newLine.contains("Unit")) {
                setState(new UnitParseState());
                m_parseState.parse(newLine);
                return;
            }

            parsedWordTranslation(newLine);
        }
    }

    protected abstract void parsedUnit(String unit);
    protected abstract void parsedLesson(String lesson);
    protected abstract void parsedEnglishTitle(String title);
    protected abstract void parsedChineseTitle(String title);
    protected abstract void parsedListenQuestion(String QuestionContent);
    protected abstract void parsedContent(String segment);
    protected abstract void parsedWord(String word);
    protected abstract void parsedWordAttribute(String attribute);
    protected abstract void parsedWordTranslation(String segment);
    protected abstract void parseFinished();
}
