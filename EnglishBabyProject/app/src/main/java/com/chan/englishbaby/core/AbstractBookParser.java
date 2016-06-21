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

    private static final int STATE_UNIT = 0x01;
    private static final int STATE_LESSON = 0x02;
    private static final int STATE_TITLE = 0x04;
    private static final int STATE_LISTEN_TITLE = 0x08;
    private static final int STATE_LISTEN_QUESTION = 0x10;
    private static final int STATE_CONTENT = 0x100;
    private static final int STATE_WORDS_TITLE = 0x1000;
    private static final int STATE_WORDS = 0x10000;
    private static final int STATE_TRANSLATION_TITLE = 0x100000;
    private static final int STATE_TRANSLATION = 0x1000000;

    private InputStream m_inputStream;
    private ParseState m_parseState;

    public AbstractBookParser(InputStream inputStream) {
        m_inputStream = inputStream;
        setState(STATE_UNIT);
    }

    protected void parse() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_inputStream));
        String content = null;

        while ((content = bufferedReader.readLine()) != null) {
            m_parseState.parse(content);
        }

        parseFinished();
    }

    private void setState(int state) {
        switch (state) {
            case STATE_UNIT:
                m_parseState = new UnitParseState();
                break;
            case STATE_LESSON:
                m_parseState = new LessonParseState();
                break;
            case STATE_TITLE:
                m_parseState = new TitleParseState();
                break;
            case STATE_LISTEN_TITLE:
                m_parseState = new ListenTitleParseState();
                break;
            case STATE_LISTEN_QUESTION:
                m_parseState = new ListenQuestionParseState();
                break;
            case STATE_CONTENT:
                m_parseState = new ContentParseState();
                break;
            case STATE_WORDS_TITLE:
                m_parseState = new WordsTitleParseState();
                break;
            case STATE_WORDS:
                m_parseState = new WordsParseState();
                break;
            case STATE_TRANSLATION_TITLE:
                m_parseState = new TranslationTitleParseState();
                break;
            case STATE_TRANSLATION:
                m_parseState = new TranslationParseState();
                break;
        }
    }

    private interface ParseState {
        void parse(String newLine);
    }

    private class UnitParseState implements ParseState {

        @Override
        public void parse(String newLine) {
            setState(STATE_LESSON);

            //如果是lesson 那么 要回退单词
            if (newLine.toLowerCase().contains("lesson")) {
                m_parseState.parse(newLine);
                return;
            }

            parsedUnit(newLine);
            ////System.out.println("单元: " + newLine);
        }
    }

    private class LessonParseState implements ParseState {

        @Override
        public void parse(String newLine) {
            //System.out.println("课程: " + newLine);
            parsedLesson(newLine);
            setState(STATE_TITLE);
        }
    }

    private class TitleParseState implements ParseState {

        boolean isEnglishTitle = true;

        @Override
        public void parse(String newLine) {

            newLine = newLine.trim();

            if (TextUtils.isEmpty(newLine)) {
                setState(STATE_LISTEN_TITLE);
                return;
            }

            int index = 0;
            int length = newLine.length();
            for (; index < length; ++index) {
                if (TextUtils.isChinese(newLine.charAt(index))) break;
            }

            //中文标题 和英文标题在同一行
            if (index != length && index != 0) {
                ////System.out.println("e: " + newLine.substring(0, index));
                ////System.out.println("标题: " + newLine.substring(index));
                parsedEnglishTitle(newLine.substring(0, index).trim());
                parsedChineseTitle(newLine.substring(index).trim());
            } else {
                if (isEnglishTitle) {

                    //英文标题
                    ////System.out.println("e: " + newLine);
                    parsedEnglishTitle(newLine.trim());
                    isEnglishTitle = false;
                } else {

                    //中文标题
                    ////System.out.println("z:" + newLine);
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
                setState(STATE_LISTEN_QUESTION);
            } else if (newLine.contains("?")) {
                setState(STATE_LISTEN_QUESTION);
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
                setState(STATE_CONTENT);
                return;
            } else if (newLine.contains("?")) {

                if (android.text.TextUtils.isEmpty(m_question)) {
                    m_question = newLine.trim();
                } else {
                    m_question = m_question + "\n" + newLine.trim();
                }

                parsedListenQuestion(m_question);
                setState(STATE_CONTENT);
                return;
            }

            m_question = newLine.trim();

            //听力题可能有多行哦
            //System.out.println("听力题: " + newLine);
        }
    }

    private class ContentParseState implements ParseState {

        @Override
        public void parse(String newLine) {

            //内容可能有多段 中间不是以换行符作为分隔的
            //所以只能检测关键字
            if (newLine.contains("生词和短语") || newLine.contains("New words and expression")) {
                setState(STATE_WORDS_TITLE);
                m_parseState.parse(newLine);
                return;
            }

            parsedContent(newLine);
            //System.out.println("内容: " + newLine);
        }
    }

    private class WordsTitleParseState implements ParseState {

        @Override
        public void parse(String newLine) {

            //这里可能也不是有空格的
            if (TextUtils.isEmpty(newLine)) {
                setState(STATE_WORDS);
            } else if (newLine.contains("生词和短语") || newLine.contains("New words and expression")) {
                //System.out.println("单词标题: " + newLine);
            } else {
                setState(STATE_WORDS);
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
                setState(STATE_TRANSLATION_TITLE);
                return;
            } else if (newLine.contains("参考译文")) {

                //有的时候 单词下面是没有空格的 那么就查询关键字
                setState(STATE_TRANSLATION_TITLE);
                m_parseState.parse(newLine);
                return;
            }

            //System.out.println("单词: " + newLine);
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

            setState(STATE_TRANSLATION);

            if (!TextUtils.isEmpty(newLine)) {
                m_parseState.parse(newLine);
            }
        }
    }

    private class TranslationParseState implements ParseState {

        @Override
        public void parse(String newLine) {

            if (TextUtils.isEmpty(newLine)) {
                setState(STATE_UNIT);
                return;
            } else if (newLine.contains("Lesson") || newLine.contains("Unit")) {
                setState(STATE_UNIT);
                m_parseState.parse(newLine);
                return;
            }

            parsedWordTranslation(newLine);
            //System.out.println("翻译内容: " + newLine);
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
