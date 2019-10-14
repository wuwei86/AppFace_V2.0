package com.atisz.appface.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.atisz.appface.R;
import com.atisz.appface.adapter.FaceEntityViewHolder;
import com.atisz.appface.entity.FaceEntity;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.SystemUtil;
import com.github.clans.fab.FloatingActionButton;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author wuwei
 * @date 2019/2/20
 */

public class RefreshAndMoreActivity extends MyBaseActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.tb_refresh_more)
    Toolbar mTbRefreshMore;
    @BindView(R.id.erv_refreshmore)
    EasyRecyclerView mErvRefreshmore;
    @BindView(R.id.fab_top)
    FloatingActionButton mFabTop;
    private RecyclerArrayAdapter<FaceEntity> mFaceEntityRecyclerArrayAdapter;
    private List<FaceEntity> mFaceEntities = new ArrayList<FaceEntity>();
    private int page = 0;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_refreshandmore);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        initToolBar();
        initRecyclerView();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mFabTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErvRefreshmore.scrollToPosition(0);
            }
        });
    }

    private void initToolBar() {
        mTbRefreshMore.setTitle("RefreshAndMore");
        mTbRefreshMore.setLogo(R.mipmap.ic_refreshmore);
        mTbRefreshMore.setNavigationIcon(R.mipmap.ic_back);
        mTbRefreshMore.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mErvRefreshmore.setLayoutManager(layoutManager);

        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, SystemUtil.dip2px(this,0.2f), 0,0);
        itemDecoration.setDrawLastItem(false);
        mErvRefreshmore.addItemDecoration(itemDecoration);

        mFaceEntityRecyclerArrayAdapter = new RecyclerArrayAdapter<FaceEntity>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new FaceEntityViewHolder(parent);
            }
        };
        mErvRefreshmore.setAdapterWithProgress(mFaceEntityRecyclerArrayAdapter);
        mFaceEntityRecyclerArrayAdapter.setMore(R.layout.view_more,this);
        mFaceEntityRecyclerArrayAdapter.setNoMore(R.layout.view_nomore);
        mFaceEntityRecyclerArrayAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                mFaceEntityRecyclerArrayAdapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                mFaceEntityRecyclerArrayAdapter.resumeMore();
            }
        });
        mFaceEntityRecyclerArrayAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                alert(" "+position+"被点击了");
            }
        });
        mErvRefreshmore.setRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onLoadMore() {
        if (page == 4){
            page = 0;
            mFaceEntityRecyclerArrayAdapter.stopMore();
        } else {
            mFaceEntityRecyclerArrayAdapter.addAll(mFaceEntities);
            page++;
        }
    }

    @Override
    public void onRefresh() {
        getBmobData();
    }

    private void getBmobData() {
        BmobQuery<FaceEntity> bmobQuery = new BmobQuery();
        bmobQuery.findObjects(new FindListener<FaceEntity>() {
            @Override
            public void done(List<FaceEntity> list, BmobException e) {
                if (e == null) {
                    mFaceEntities = list;
                    mFaceEntityRecyclerArrayAdapter.clear();
                    mFaceEntityRecyclerArrayAdapter.addAll(mFaceEntities);
                    page = 0;
                    alert("查询成功");
                } else {
                    alert("没有数据");
                    //没有数据的时候直接制空就行
                    mFaceEntityRecyclerArrayAdapter.clear();
                }
            }
        });
    }
}
