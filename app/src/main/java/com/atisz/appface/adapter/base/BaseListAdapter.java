package com.atisz.appface.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by LaoWu on 2018/6/19 0019.
 * ListView 和 GridView的适配器基类
 */

public abstract class BaseListAdapter extends BaseAdapter {
    protected Context context;
    protected List dataList;
    protected LayoutInflater inflater;

    public BaseListAdapter(Context context, List dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    protected abstract View createView(int i, View view, ViewGroup viewGroup);
    protected abstract ViewHolder createViewHolder(int position, View view);

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = createView(i, view, viewGroup);
            viewHolder = createViewHolder(i, view);
            viewHolder.setContext(context);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
    }
        viewHolder.refresh(context, dataList.get(i), view);

        return view;
    }

    public abstract class ViewHolder {
        private Context context;

        public abstract void refresh(Context context, Object item, View view);

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }
}
