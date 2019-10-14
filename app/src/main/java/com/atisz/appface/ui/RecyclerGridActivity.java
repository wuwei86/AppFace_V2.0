package com.atisz.appface.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.adapter.FaceEntityViewHolder;
import com.atisz.appface.adapter.GridEntityViewHolder;
import com.atisz.appface.entity.FaceEntity;
import com.atisz.appface.entity.GridEntity;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.SystemUtil;
import com.bumptech.glide.util.Util;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

/**
 * @author wuwei
 * @date 2019/2/20
 */

public class RecyclerGridActivity extends MyBaseActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.tv_tbtitle)
    TextView mTvTbtitle;
    @BindView(R.id.tb_recyclerviewgrid)
    Toolbar mTbRecyclerviewgrid;
    @BindView(R.id.erv_recyclerviewgrid)
    EasyRecyclerView mErvRecyclerviewgrid;
    private RecyclerArrayAdapter<GridEntity> mGridEntityRecyclerArrayAdapter;
    private ArrayList<GridEntity> mContents;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_recyclerviewgrid);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        initToolBar();
        initRecyclerView();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        mErvRecyclerviewgrid.setLayoutManager(staggeredGridLayoutManager);

        setLocalData();
        mGridEntityRecyclerArrayAdapter = new RecyclerArrayAdapter<GridEntity>(this,mContents) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new GridEntityViewHolder(parent,mContents);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        gridLayoutManager.setSpanSizeLookup(mGridEntityRecyclerArrayAdapter.obtainGridSpanSizeLookUp(4));
        mErvRecyclerviewgrid.setLayoutManager(gridLayoutManager);
        mErvRecyclerviewgrid.setAdapter(mGridEntityRecyclerArrayAdapter);

        SpaceDecoration itemDecoration = new SpaceDecoration((int) SystemUtil.dip2px(this,5));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(false);
        mErvRecyclerviewgrid.addItemDecoration(itemDecoration);

        mGridEntityRecyclerArrayAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                alert("点击事件");
            }
        });

    }

    private void setLocalData() {

        final GridEntity gridEntity1 = new GridEntity();
        gridEntity1.setName("腾讯");
        gridEntity1.setId(R.mipmap.ic_qqchat);
        final GridEntity gridEntity2 = new GridEntity();
        gridEntity2.setName("微信");
        gridEntity2.setId(R.mipmap.ic_wechat);
        final GridEntity gridEntity3 = new GridEntity();
        gridEntity3.setName("支付宝");
        gridEntity3.setId(R.mipmap.ic_refreshmore);
        final GridEntity gridEntity4 = new GridEntity();
        gridEntity4.setName("花呗");
        gridEntity4.setId(R.mipmap.ic_huabai);
        final GridEntity gridEntity5 = new GridEntity();
        gridEntity5.setName("滴滴出行");
        gridEntity5.setId(R.mipmap.ic_driptrip);
        final GridEntity gridEntity6 = new GridEntity();
        gridEntity6.setName("芝麻信用");
        gridEntity6.setId(R.mipmap.ic_sesamecredit);
        final GridEntity gridEntity7 = new GridEntity();
        gridEntity7.setName("城市服务");
        gridEntity7.setId(R.mipmap.ic_cityservices);
        final GridEntity gridEntity8 = new GridEntity();
        gridEntity8.setName("支付中心");
        gridEntity8.setId(R.mipmap.ic_vouchercenter);
        final GridEntity gridEntity9 = new GridEntity();
        gridEntity9.setName("微博");
        gridEntity9.setId(R.mipmap.ic_micblog);
        final GridEntity gridEntity10 = new GridEntity();
        gridEntity10.setName("地球");
        gridEntity10.setId(R.mipmap.ic_recyclerview);
        final GridEntity gridEntity11 = new GridEntity();
        gridEntity11.setName("口碑");
        gridEntity11.setId(R.mipmap.ic_wordmouth);

        final GridEntity gridEntity12 = new GridEntity();
        gridEntity12.setName("淘宝");
        gridEntity12.setId(R.mipmap.ic_taobao);

        final GridEntity gridEntity13 = new GridEntity();
        gridEntity13.setName("火车");
        gridEntity13.setId(R.mipmap.ic_train);

        final GridEntity gridEntity14 = new GridEntity();
        gridEntity14.setName("飞机");
        gridEntity14.setId(R.mipmap.ic_aircraft);


        mContents = new ArrayList<GridEntity>();
        mContents.add(gridEntity1);
        mContents.add(gridEntity2);
        mContents.add(gridEntity3);
        mContents.add(gridEntity4);
        mContents.add(gridEntity5);
        mContents.add(gridEntity6);
        mContents.add(gridEntity7);
        mContents.add(gridEntity8);
        mContents.add(gridEntity9);
        mContents.add(gridEntity10);
        mContents.add(gridEntity11);
        mContents.add(gridEntity12);
        mContents.add(gridEntity13);
        mContents.add(gridEntity14);
    }

    private void initToolBar() {
        mTbRecyclerviewgrid.setTitle("");
        mTvTbtitle.setText("九宫格布局");
        mTbRecyclerviewgrid.setNavigationIcon(R.mipmap.ic_back);
        mTbRecyclerviewgrid.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTbRecyclerviewgrid.inflateMenu(R.menu.setting_add_menu);
        mTbRecyclerviewgrid.setOnMenuItemClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_collect:
                alert("收藏心心被点击");
                break;
            case R.id.item_add:
                alert("点击");
                break;
            default:
                break;
        }
        return true;
    }
}
