package com.atisz.appface.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.atisz.appface.R;
import com.atisz.appface.adapter.ButtonContentsHolder;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.SystemUtil;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by wuwei on 2019/2/19.
 */

public class RecyclerViewActivity extends MyBaseActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar_recycler_view)
    Toolbar mToolbarRecyclerView;
    @BindView(R.id.recyclerView)
    EasyRecyclerView mRecyclerView;
    private ArrayList<String> mContents;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_recyclerview);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        initToolBar();
        initRecyclerView();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }

    private void initToolBar() {
        mToolbarRecyclerView.setTitle("RecyclerView");
        mToolbarRecyclerView.setNavigationIcon(R.mipmap.ic_back);
        mToolbarRecyclerView.setLogo(R.mipmap.ic_recyclerview);
        mToolbarRecyclerView.inflateMenu(R.menu.setting_menu);

        mToolbarRecyclerView.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbarRecyclerView.setOnMenuItemClickListener(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, SystemUtil.dip2px(this,0.2f), 0,0);
        itemDecoration.setDrawLastItem(true);
        mRecyclerView.addItemDecoration(itemDecoration);

        mContents = new ArrayList<>();
        mContents.add("Refresh&More");
        mContents.add("九宫格");

        final RecyclerArrayAdapter<String> adapter = new RecyclerArrayAdapter<String>(this,mContents) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ButtonContentsHolder(parent,mContents);
            }
        };
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                alert("点击事件");
                recyclerViewItemClick(position);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_collect:
                alert("收藏心心被点击");
                break;
            case R.id.item_setting:
                alert("设置被点击");
                break;
            case R.id.item_model:
                alert("模式被点击");
                break;
            default:
                break;
        }
        return false;
    }

    private void recyclerViewItemClick(int position){
        if (position == 0){
            Intent intent = new Intent(RecyclerViewActivity.this,RefreshAndMoreActivity.class);
            startActivity(intent);
            finish();
        } else if (position == 1){
            Intent intent = new Intent(RecyclerViewActivity.this,RecyclerGridActivity.class);
            startActivity(intent);
            finish();
        } else if (position == 2){

        } else if (position == 3){

        }
    }
}
