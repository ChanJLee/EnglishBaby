package com.chan.englishbaby.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 16/6/10.
 */
public class ActivityStack {
    private static List<Activity> s_activityList = new ArrayList<>();

    public static void push(Activity activity) {

        synchronized (ActivityStack.class) {
            if (s_activityList == null) {
                s_activityList = new ArrayList<>();
            }
            s_activityList.add(activity);
        }
    }

    public static void pop(Activity activity) {

        synchronized (ActivityStack.class) {
            if (s_activityList == null) return;
            s_activityList.remove(activity);
        }
    }

    public static void forEach(Predicate predicate) {
        synchronized (ActivityStack.class) {
            if (s_activityList == null || predicate == null) {
                return;
            }

            final int size = s_activityList.size();
            for (int i = size - 1; i > 0; --i){
                Activity activity = s_activityList.get(i);
                predicate.func(activity);
            }
        }
    }

    public interface Predicate {
        void func(Activity activity);
    }
}
