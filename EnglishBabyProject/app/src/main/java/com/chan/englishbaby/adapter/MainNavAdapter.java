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
public class MainNavAdapter extends RecyclerView.Adapter<MainNavAdapter.MainNavHolder> {

    private OnUnitItemClick m_onUnitItemClick;

    private List<EnglishBookProto.Unit> m_unitList;
    private LayoutInflater m_layoutInflater;

    public MainNavAdapter(List<EnglishBookProto.Unit> unitList, LayoutInflater layoutInflater) {
        m_unitList = unitList;
        m_layoutInflater = layoutInflater;
    }

    public void setOnUnitItemClick(OnUnitItemClick onUnitItemClick) {
        m_onUnitItemClick = onUnitItemClick;
    }

    @Override
    public MainNavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainNavHolder(m_layoutInflater.inflate(R.layout.item_nav, parent, false));
    }

    @Override
    public void onBindViewHolder(MainNavHolder holder, int position) {
        final EnglishBookProto.Unit unit = m_unitList.get(position);
        holder.m_title.setText(unit.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_onUnitItemClick != null) {
                    m_onUnitItemClick.onUnitItemClick(unit);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_unitList.size();
    }

    public class MainNavHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_title)
        TextView m_title;

        public MainNavHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnUnitItemClick {
        void onUnitItemClick(EnglishBookProto.Unit unit);
    }
}
