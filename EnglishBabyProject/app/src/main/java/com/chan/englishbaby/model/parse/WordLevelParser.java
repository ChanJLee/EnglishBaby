package com.chan.englishbaby.model.parse;

import android.util.Pair;

import com.chan.englishbaby.core.AbstractWordLevelParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 16/6/22.
 */
public class WordLevelParser extends AbstractWordLevelParser {

    List<Pair<String, Integer>> m_pairList = new ArrayList<>();

    public WordLevelParser(InputStream inputStream) {
        super(inputStream);
    }

    public List<Pair<String, Integer>> getWordsLevel() throws IOException {
        m_pairList.clear();
        parse();
        return m_pairList;
    }

    @Override
    protected void parsedWordLevel(Pair<String, Integer> pair) {
        m_pairList.add(pair);
    }
}
