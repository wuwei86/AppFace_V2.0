package com.atisz.appface.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.adapter.CardManagerAdapter;
import com.atisz.appface.entity.CardListEntity;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.DbUtil;
import com.atisz.appface.utils.SystemUtil;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.helper.ErrorCode;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * @author wuwei
 * @date 2018/10/11
 */

public class CardListActivity extends MyBaseActivity implements View.OnClickListener {
    private static final int START_ADDCARD_CODE = 0x0001;

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.view_stub_err)
    ViewStub mViewStubErr;
    @BindView(R.id.lv_card)
    SwipeMenuListView mLvCard;
    @BindView(R.id.fab_add)
    FloatingActionButton mFabAdd;
    @BindView(R.id.ly_text)
    LinearLayout mLyText;

    private TextView errTextView;
    private LinearLayout errLayout;

    protected CardManagerAdapter adapter;
    private List<CardListEntity> mCardList = new ArrayList<>();
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void initView(Bundle saveInstanceState) {
        isAnimation = true;
        setContentView(R.layout.activity_cardlist);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mTvTitleText.setText("门禁卡");
        mIvMenu.setVisibility(View.INVISIBLE);

        createSwipeMenu();
//        SetCardList();

        if (adapter == null) {
            adapter = new CardManagerAdapter(mActivity, mCardList);
            mLvCard.setAdapter(adapter);
        }
        if (mLvCard.getAdapter() == null) {
            mLvCard.setAdapter(adapter);
        }
        mFabAdd.setVisibility(View.GONE);
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mIvTitleBack.setOnClickListener(this);
        mFabAdd.setVisibility(View.VISIBLE);
        mFabAdd.setOnClickListener(this);
        getCardList(true);
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(SystemUtil.dp2px(mActivity, 90));
                deleteItem.setIcon(R.mipmap.ic_action_del);

                menu.addMenuItem(deleteItem);
            }
        };
        mLvCard.setMenuCreator(creator);
        mLvCard.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        delCardDialog(position);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void delCardDialog(final int position) {
        new QMUIDialog.MessageDialogBuilder(mActivity)
                .setMessage("确定要删除吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        delCard(position);
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void delCard(final int position) {
        final CardListEntity cardListEntity = mCardList.get(position);
        final String objectId = cardListEntity.getObjectId();
        mCardList.remove(position);
        adapter.notifyDataSetChanged();

        cardListEntity.setObjectId(objectId);
        cardListEntity.delete(new UpdateListener() {
        @Override
        public void done(BmobException e) {
            if (e == null){
                DbUtil.cardDbDel(cardListEntity);
            } else {
                alert("删除数据错误");
            }
        }
    });
}

    private void AddCard() {
        Intent intent = new Intent(CardListActivity.this, AddCardActivity.class);
//        intent.putExtra(IntentFlag.type_id,typeId);
        startActivityForResult(intent, START_ADDCARD_CODE);
    }

    /**
     * 获取卡列表，通过网络请求获取，保存本地数据库
     * isUpdate用来判断是否更新整个数据库,在初始化的时候需要更新整个数据库
     * 在添加卡的时候不需要更新整个数据库
     */
    private void getCardList(final boolean isUpdate) {
        //2018/10/19 先通过数据库获取，后续改成网络获取，写本地数据库
//        final List<CardListEntity> cardListEntities = DbUtil.cardDbGetAll();
        BmobQuery<CardListEntity> query = new BmobQuery<>();
        query.findObjects(new FindListener<CardListEntity>() {
            @Override
            public void done(List<CardListEntity> list, BmobException e) {
                if (e == null) {
                    mCardList.clear();
                    mCardList.addAll(list);
                    adapter.notifyDataSetChanged();
                    /*if (isUpdate == true) {
                        DbUtil.cardDbSave(list);
                    }*/
                } else {
                    if (e.getErrorCode() == ErrorCode.E9016){
                        showErrView(ErrorCode.E9016S);
                    }
                }
            }
        });
    }

    private void showErrView(String msg) {
        if (findViewById(R.id.view_stub_err) != null) {
            mViewStubErr.inflate();
            errTextView = findViewById(R.id.tv_err);
            errLayout = findViewById(R.id.err_layout);
        }
        errTextView.setText(msg);
        showCardListView(false);
    }

    private void showCardListView(boolean isShow) {
        if (findViewById(R.id.view_stub_err) == null) {
            if (isShow) {
                errLayout.setVisibility(View.GONE);
                mLvCard.setVisibility(View.VISIBLE);
                mLyText.setVisibility(View.VISIBLE);
            } else {
                errLayout.setVisibility(View.VISIBLE);
                mLvCard.setVisibility(View.GONE);
                mLyText.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                AddCard();
                break;
            case R.id.iv_title_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_ADDCARD_CODE && resultCode == RESULT_OK) {
            getCardList(false);
        }
    }
}
