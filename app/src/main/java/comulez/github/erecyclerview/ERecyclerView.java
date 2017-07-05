package comulez.github.erecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Ulez on 2017/5/26.
 * Email：lcy1532110757@gmail.com
 */


public class ERecyclerView extends RecyclerView {

    private static final String TAG = "ERecyclerView";
    private SimpleAdapterWrapper wrapper;
    private Header headerView;
    private View footerView;
    private float mLastY = -1;
    //    private int contentHeight;
    private LAYOUT_MANAGER_TYPE layoutManagerType;

    private boolean isloading = false;
    private int[] lastPositions;
    private int size;
    private boolean noMore = false;
    private TextView textView;

    public void loadAllComplete(boolean noMore) {
        this.noMore = noMore;
        loadMoreComplete();
    }

    enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    public ERecyclerView(Context context) {
        super(context);
        init();
    }

    public ERecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ERecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        headerView = new MyHeaderView(getContext());
        footerView = new MyFootView(getContext());
        textView = (TextView) footerView.findViewById(R.id.tv_hint);
    }

    public void setRefreshHeader(Header header) {
        this.headerView = header;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof SimpleAdapter) {
            wrapper = new SimpleAdapterWrapper((SimpleAdapter) adapter);
            wrapper.setHeaderView((View) headerView);
            wrapper.setFooterView(footerView);
            super.setAdapter(wrapper);
        } else
            super.setAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        int lastVisibleItemPosition;

        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        //  int lastVisibleItemPosition = -1;
        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
        switch (layoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            default:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                break;
        }
        if (layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
            if (freshListener != null && !noMore && !isloading) {
                int count = layoutManager.getChildCount();
                size = layoutManager.getItemCount();
                isloading = true;
                freshListener.onLoadMore();
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        if (isOnTop() && !isloading) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = e.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float delta = e.getRawY() - mLastY;
                    mLastY = e.getRawY();
                    headerView.onMove(delta / 1.5f);
                    break;
                default:
                    if (headerView.releaseAction() && freshListener != null && !isloading) {
                        noMore = false;
                        isloading = true;
                        freshListener.onRefresh();
                    }
                    mLastY = -1;
                    break;
            }
        }
        return super.onTouchEvent(e);
    }

    public void loadMoreComplete() {
        isloading = false;
        wrapper.notifyItemRangeInserted(size + 1, getLayoutManager().getItemCount() - size);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onChildDetachedFromWindow(View child) {
        super.onChildDetachedFromWindow(child);
    }

    @Override
    public Adapter getAdapter() {
        if (wrapper != null)
            return wrapper.getAdapter();
        return null;
    }

    private void notifyDataSetChanged() {
        if (wrapper != null)
            wrapper.notifyDataSetChanged();
        else {
            getAdapter().notifyDataSetChanged();
        }
    }

    public void refreshComplete() {
        noMore = false;
        isloading = false;
        Log.d("lcy", "refreshComplete");
        headerView.refreshComplete();
        scrollToPosition(0);
        notifyDataSetChanged();
    }


    private boolean isOnTop() {
        if (((View) headerView).getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    private FreshListener freshListener;

    public void setFreshListener(FreshListener listener) {
        freshListener = listener;
    }


    interface FreshListener {
        void onRefresh();

        void onLoadMore();
    }


    public class SimpleAdapterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int HEADER = 7777;
        public static final int FOOTER = 7778;
        public static final int NORMAL = 7779;

        private SimpleAdapter mAdapter;
        private View mHeaderView;
        private View mFooterView;

        public SimpleAdapterWrapper(SimpleAdapter simpleAdapter) {
            this.mAdapter = simpleAdapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER) {
                return new BaseHolder(mHeaderView);
            } else if (viewType == FOOTER) {
                return new BaseHolder(mFooterView);
            } else {
                return mAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type == HEADER) {
                return;
            } else if (type == FOOTER) {
                if (noMore)
                    textView.setText(getContext().getString(R.string.listview_loaded_all));
                else
                    textView.setText(getContext().getString(R.string.listview_loading));
                return;
            } else {
                mAdapter.onBindViewHolder((BaseHolder) holder, position - 1);
            }
            if (position == 0) {
                return;
            } else if (position == mAdapter.getItemCount() + 1) {
                return;
            } else {
                mAdapter.onBindViewHolder((BaseHolder) holder, position - 1);
            }
        }

        @Override
        public int getItemCount() {
//            Log.e("lcy", "size==" + (mAdapter.getItemCount() + 2));
            return mAdapter.getItemCount() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER;
            } else if (position == mAdapter.getItemCount() + 1) {
                return FOOTER;
            } else {
                return NORMAL;
            }
        }

        public void setHeaderView(View mHeaderView) {
            this.mHeaderView = mHeaderView;
        }

        public void setFooterView(View mFooterView) {
            this.mFooterView = mFooterView;
        }

        public void setNewData(List<T> newDatas) {
            mAdapter.setNewData(newDatas);
            notifyDataSetChanged();
        }

        public void setAdapter(SimpleAdapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        public Adapter getAdapter() {
            return mAdapter;
        }
    }
}
