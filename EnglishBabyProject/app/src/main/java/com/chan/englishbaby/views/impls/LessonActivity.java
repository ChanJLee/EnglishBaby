package com.chan.englishbaby.views.impls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.chan.englishbaby.base.BaseActivity;
import com.chan.englishbaby.injector.component.ActivityComponent;
import com.chan.englishbaby.presenter.impls.LessonActivityPresenter;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.proto.EnglishBookProto;
import com.chan.englishbaby.view.LessonDialog;
import com.chan.englishbaby.views.interfaces.ILessonView;
import com.chan.englishbaby.views.interfaces.IView;
import com.chan.englishboby.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chan on 16/6/22.
 */
public class LessonActivity extends BaseActivity implements ILessonView {

    @Bind(R.id.id_toolbar_layout)
    CollapsingToolbarLayout m_collapsingToolbarLayout;

    @Bind(R.id.id_titleChinese)
    TextView m_titleChinese;
    @Bind(R.id.id_titleEnglish)
    TextView m_titleEnglish;

    @Bind(R.id.id_question)
    TextView m_listenQuestion;

    @Bind(R.id.id_content)
    TextView m_content;

    @Bind(R.id.id_newWordsContent)
    TextView m_words;

    @Bind(R.id.id_transContent)
    TextView m_transContent;

    @Inject
    LessonDialog m_lessonDialog;

    private ProgressDialog m_progressDialog;

    private static final String EXTRA_LESSON = "lesson";
    private EnglishBookProto.Lesson m_lesson;

    @Bind(R.id.toolbar)
    Toolbar m_toolbar;

    @Inject
    LessonActivityPresenter m_lessonActivityPresenter;

    @Override
    protected IView createView() {
        return this;
    }

    @Override
    protected IPresenter createPresenter() {
        return m_lessonActivityPresenter;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public static void invoke(Activity activity, EnglishBookProto.Lesson lesson) {
        Intent intent = new Intent(activity, LessonActivity.class);
        intent.putExtra(EXTRA_LESSON, lesson);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_lesson;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        initProgressDialog();
        setSupportActionBar(m_toolbar);
        setContentViews();
        initDialog();

        m_lessonActivityPresenter.load();
    }

    private void initProgressDialog() {
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(false);
        m_progressDialog.setTitle(getString(R.string.lesson_progress));
    }

    private void setContentViews() {
        Intent intent = getIntent();
        if (intent != null) {
            m_lesson = (EnglishBookProto.Lesson) intent.getSerializableExtra(EXTRA_LESSON);
            m_listenQuestion.setText(m_lesson.getListenQuestion());
            m_content.setText(m_lesson.getContent());
            setWordsContent();
            m_transContent.setText(m_lesson.getTranslation());
            m_titleChinese.setText(m_lesson.getTitleChinese());
            m_titleEnglish.setText(m_lesson.getTitleEnglish());
            m_collapsingToolbarLayout.setTitle(m_lesson.getName());
        }
    }


    private void initDialog() {
        m_lessonDialog.setOnLevelSelected(new LessonDialog.OnLevelSelected() {
            @Override
            public void onLevelSelected(short level) {
                m_lessonActivityPresenter.setHighLight(m_content.getEditableText(), level);
            }
        });
    }

    private void setWordsContent() {
        List<EnglishBookProto.Word> words = m_lesson.getWordsList();
        StringBuilder stringBuilder = new StringBuilder();

        for (EnglishBookProto.Word word : words) {
            stringBuilder.append(word.getContent()).append("\n")
                    .append(word.getAttribute()).append("\n");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        m_words.setText(stringBuilder.toString());
    }

    @OnClick(R.id.id_more)
    void showDialog() {
        m_lessonDialog.show();
    }

    @Override
    public void showLoading(String message) {
        m_progressDialog.setMessage(message);
        m_progressDialog.show();
    }

    @Override
    public void dismissLoading() {
        m_progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
