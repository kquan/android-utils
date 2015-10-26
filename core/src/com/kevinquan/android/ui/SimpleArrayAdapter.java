package com.kevinquan.android.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.lang.reflect.Constructor;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Simple ArrayAdapter pattern similar to RecyclerView
 * @author Kevin Quan (kevin.quan@gmail.com
 */
public class SimpleArrayAdapter<T> extends ArrayAdapter<T> {

    @SuppressWarnings("Unused")
    private static final String TAG = SimpleArrayAdapter.class.getSimpleName();

    public static abstract class AdapterViewHolder<T> {

        @SuppressWarnings("Unused")
        private static final String TAG = AdapterViewHolder.class.getSimpleName();

        public AdapterViewHolder(View parent) {
            parent.setTag(this);
            ButterKnife.bind(this, parent);
        }

        public abstract void bind(T item);

    }

    protected Class<? extends AdapterViewHolder<T>> mViewHolderClass;
    protected int mItemLayoutId;

    public SimpleArrayAdapter(Context context, Class<? extends AdapterViewHolder<T>> clazz, int itemLayoutId, List<T> objects) {
        super(context, View.NO_ID, objects);
        mViewHolderClass = clazz;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mItemLayoutId, parent, false);
            instantiateViewHolder(convertView);
        }

        AdapterViewHolder<T> holder = (AdapterViewHolder<T>)convertView.getTag();
        T item = getItem(position);
        if (holder != null) {
            holder.bind(item);
        }
        updateView(holder, position, item);
        return convertView;
    }

    protected <R extends AdapterViewHolder<T>> R instantiateViewHolder(View convertView) {
        for (Constructor ctor : mViewHolderClass.getConstructors()) {
            Class<?>[] ctorParameters = ctor.getParameterTypes();
            if (ctorParameters.length == 1 && ctorParameters[0].isInstance(convertView)) {
                try {
                    return (R)ctor.newInstance(convertView);
                } catch (Exception e) {
                    Log.e(TAG, "Could not instantiate view holder of class " + mViewHolderClass.getName(), e);
                }
            }
        }
        Log.w(TAG, "Could not find expected view holder constructor");
        return null;
    }

    protected void updateView(AdapterViewHolder viewHolder, int position, T item) {

    }
}
