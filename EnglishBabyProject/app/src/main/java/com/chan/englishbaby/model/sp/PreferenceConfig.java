package com.chan.englishbaby.model.sp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by chan on 16/6/21.
 */
public class PreferenceConfig {
    private SharedPreferences m_sharedPreferences;
    private static final String NAME = "config";
    private static final String KEY_FIRST_USE = "first_use";

    public PreferenceConfig(@NonNull Context context) {
        m_sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstUse() {
        return m_sharedPreferences.getBoolean(KEY_FIRST_USE, true);
    }

    @SuppressLint("CommitPrefEdits")
    public void setHasUsed() {
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_USE, false);
        editor.commit();
    }
}
