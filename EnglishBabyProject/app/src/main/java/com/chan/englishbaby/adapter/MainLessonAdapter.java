package com.chan.englishbaby.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chan.englishbaby.proto.EnglishBookProto;
import com.chan.englishboby.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chan on 16/6/22.
 */
public class MainLessonAdapter extends RecyclerView.Adapter<MainLessonAdapter.MainLessonHolder> {
    private List<EnglishBookProto.Lesson> m_lessons;
    private LayoutInflater m_layoutInflater;
    private OnLessonClickListener m_onLessonClickListener;

    public void setOnLessonClickListener(OnLessonClickListener onLessonClickListener) {
        m_onLessonClickListener = onLessonClickListener;
    }

    public MainLessonAdapter(List<EnglishBookProto.Lesson> lessons, LayoutInflater layoutInflater) {
        m_lessons = lessons;
        m_layoutInflater = layoutInflater;
    }

    @Override
    public MainLessonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainLessonHolder(m_layoutInflater.inflate(R.layout.item_lesson, parent, false));
    }

    @Override
    public void onBindViewHolder(MainLessonHolder holder, int position) {
        final EnglishBookProto.Lesson lesson = m_lessons.get(position);

        holder.m_lessName.setText(lesson.getName());
        holder.m_titleChinese.setText(lesson.getTitleChinese());
        holder.m_titleEnglish.setText(lesson.getTitleEnglish());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_onLessonClickListener != null) {
                    m_onLessonClickListener.onLessonClick(lesson);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_lessons.size();
    }

    public static class MainLessonHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_lessonName)
        TextView m_lessName;
        @Bind(R.id.id_titleEnglish)
        TextView m_titleEnglish;
        @Bind(R.id.id_titleChinese)
        TextView m_titleChinese;

        public MainLessonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnLessonClickListener {
        void onLessonClick(EnglishBookProto.Lesson lesson);
    }
}
