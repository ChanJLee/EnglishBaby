package com.chan.englishbaby.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chan.englishbaby.proto.EnglishBookProto;

/**
 * Created by chan on 16/6/22.
 */
public class LessonBean implements Parcelable {
    private EnglishBookProto.Lesson m_lesson;

    public LessonBean(EnglishBookProto.Lesson lesson) {
        m_lesson = lesson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.m_lesson);
    }

    protected LessonBean(Parcel in) {
        this.m_lesson = (EnglishBookProto.Lesson) in.readSerializable();
    }

    public static final Creator<LessonBean> CREATOR = new Creator<LessonBean>() {
        @Override
        public LessonBean createFromParcel(Parcel source) {
            return new LessonBean(source);
        }

        @Override
        public LessonBean[] newArray(int size) {
            return new LessonBean[size];
        }
    };
}
