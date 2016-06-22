package com.chan.englishbaby;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.chan.babytextviewedit.BabyTextView;
import com.chan.englishbaby.adapter.MainLessonAdapter;
import com.chan.englishbaby.adapter.MainNavAdapter;
import com.chan.englishbaby.base.BaseActivity;
import com.chan.englishbaby.injector.component.ActivityComponent;
import com.chan.englishbaby.presenter.impls.MainActivityPresenter;
import com.chan.englishbaby.presenter.interfaces.IPresenter;
import com.chan.englishbaby.proto.EnglishBookProto;
import com.chan.englishbaby.view.RecycleViewDivider;
import com.chan.englishbaby.views.impls.LessonActivity;
import com.chan.englishbaby.views.interfaces.IMainView;
import com.chan.englishbaby.views.interfaces.IView;
import com.chan.englishboby.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements IMainView {

    @Inject
    MainActivityPresenter m_mainActivityPresenter;
    @Bind(R.id.toolbar)
    Toolbar m_toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout m_drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView m_navigationView;
    @Bind(R.id.id_lessonContainer)
    RecyclerView m_lessonContainer;

    private RecyclerView m_recyclerView;
    private MainNavAdapter m_mainNavAdapter;
    private MainLessonAdapter m_mainLessonAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initActionBar();
        initDrawer();
        initRecycler();

        m_mainActivityPresenter.load();
    }

    private void initView() {
        ButterKnife.bind(this);

        m_recyclerView = (RecyclerView) m_navigationView.getHeaderView(0).findViewById(R.id.id_recycler);
    }

    private void initActionBar() {
        setSupportActionBar(m_toolbar);
    }

    @SuppressWarnings("deprecation")
    private void initDrawer() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, m_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }

        toggle.syncState();
    }

    private void initRecycler() {
        m_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        m_recyclerView.addItemDecoration(new RecycleViewDivider(this, RecyclerView.VERTICAL, 10, Color.GRAY));

        m_lessonContainer.setLayoutManager(new LinearLayoutManager(this));
        m_lessonContainer.addItemDecoration(new RecycleViewDivider(this, RecyclerView.VERTICAL, 10, Color.GRAY));
    }

    @OnClick(R.id.fab)
    void onRefresh() {
        //这里都是只读 所以没必要重新读数据
        if (m_mainNavAdapter != null) {
            m_mainNavAdapter.notifyDataSetChanged();
        }

        Toast.makeText(this, R.string.refresh_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected IView createView() {
        return this;
    }

    @Override
    protected IPresenter createPresenter() {
        return m_mainActivityPresenter;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void showUnits(List<EnglishBookProto.Unit> units) {
        m_mainNavAdapter = new MainNavAdapter(units, LayoutInflater.from(this));
        m_mainNavAdapter.setOnUnitItemClick(new MainNavAdapter.OnUnitItemClick() {
            @Override
            public void onUnitItemClick(EnglishBookProto.Unit unit) {
                showLessons(unit.getLessonsList());
                m_drawerLayout.closeDrawers();
            }
        });
        m_recyclerView.setAdapter(m_mainNavAdapter);
    }

    @Override
    public void showError(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showLessons(List<EnglishBookProto.Lesson> lessons) {
        m_mainLessonAdapter = new MainLessonAdapter(lessons, LayoutInflater.from(this));
        m_mainLessonAdapter.setOnLessonClickListener(new MainLessonAdapter.OnLessonClickListener() {
            @Override
            public void onLessonClick(EnglishBookProto.Lesson lesson) {
                LessonActivity.invoke(MainActivity.this, lesson);
            }
        });
        m_lessonContainer.setAdapter(m_mainLessonAdapter);
    }
}
