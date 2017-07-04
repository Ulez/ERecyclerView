package comulez.github.erecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Ulez on 2017/4/18.
 * Email：lcy1532110757@gmail.com
 */


public abstract class SimpleAdapter<T, K extends BaseHolder> extends RecyclerView.Adapter<K> {
    public List<T> mDatas;
    private int layoutResId;
    protected Context mContext;

    public SimpleAdapter(List<T> mDatas, int layoutResId) {
        this.mDatas = mDatas;
        this.layoutResId = layoutResId;
    }

    public SimpleAdapter(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    public SimpleAdapter(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    protected abstract void convert(K holder, T item, int position);

    protected abstract int getLayoutId(int viewType);

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        if (layoutResId != 0) {
            return (K) BaseHolder.get(parent, layoutResId);
        }
        return (K) BaseHolder.get(parent, getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    public void setNewData(List<T> newDatas) {
        this.mDatas = newDatas;
        notifyDataSetChanged();
    }
}
