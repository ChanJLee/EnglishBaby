package com.chan.englishbaby.core;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chan on 16/6/22.
 */
public abstract class AbstractWordLevelParser {
    private InputStream m_inputStream;

    public AbstractWordLevelParser(InputStream inputStream) {
        m_inputStream = inputStream;
    }

    protected void parse() throws IOException {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;

        boolean hasParsedTitle = true;
        try {
            inputStreamReader = new InputStreamReader(m_inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String content = null;

            while ((content = bufferedReader.readLine()) != null) {
                if (hasParsedTitle) {
                    hasParsedTitle = false;
                    continue;
                }

                String[] array = content.split("\t");
                parsedWordLevel(new Pair<String, Integer>(array[0], Integer.parseInt(array[1])));
            }
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    protected abstract void parsedWordLevel(Pair<String, Integer> pair);
}
