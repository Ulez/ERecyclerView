package comulez.github.erecyclerview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ulez on 2017/4/24.
 * Email：lcy1532110757@gmail.com
 */


public  class BaseHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mContentView;
    Context context;

    public BaseHolder(View itemView) {
        super(itemView);
        mContentView = itemView;
        mViews = new SparseArray<>();
        context = itemView.getContext();
    }

    public static BaseHolder get(ViewGroup parent, int layoutResId) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new BaseHolder(view);
    }


    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void setText(int id, String str) {
        TextView textView = getView(id);
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setImageResource(int id, @DrawableRes int resId) {
        ImageView imageView = getView(id);
        imageView.setImageResource(resId);
    }


    public void setImageByPicasso(int id, String url) {
        ImageView imageView = getView(id);
        Picasso.with(context)
                .load(url)
                .error(R.drawable.defaultmin)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    public BaseHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
    public BaseHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
