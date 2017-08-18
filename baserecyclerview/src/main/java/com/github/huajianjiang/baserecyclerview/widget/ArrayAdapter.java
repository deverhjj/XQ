/*
 * Copyright (c) 2017 HuaJian Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.huajianjiang.baserecyclerview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Filter;
import android.widget.Filterable;

import com.github.huajianjiang.baserecyclerview.util.Predications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ArrayAdapter<BVH extends BaseViewHolder, T> extends BaseAdapter<BVH>
        implements Filterable
{
    private static final String TAG = ArrayAdapter.class.getSimpleName();
    private List<T> mItems;

    private boolean mAutoNotify = true;

    private BaseFilter mFilter;
    private List<T> mOriginalItems;

    public ArrayAdapter(Context ctxt) {
        this(ctxt, null);
    }

    public ArrayAdapter(Context ctxt, List<T> items) {
        super(ctxt);
        mItems = items == null ? new ArrayList<T>() : items;
    }

    public boolean isAutoNotify() {
        return mAutoNotify;
    }

    public void setAutoNotify(boolean autoNotify) {
        mAutoNotify = autoNotify;
    }

    //-----------------------

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<T> getItems() {
        return mItems;
    }

    public void invalidate(List<T> newItems) {
        mItems.clear();
        if (mOriginalItems != null) {
            mOriginalItems.clear();
        }
        if (newItems != null) {
            mItems = newItems;
            if (mOriginalItems != null) {
                mOriginalItems.addAll(newItems);
            }
        }
        notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mItems, comparator);
        if (mOriginalItems != null) {
            Collections.sort(mOriginalItems, comparator);
        }
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return getItemCount() <= 0;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    public int getPosition(@Nullable T item) {
        return mItems.indexOf(item);
    }

    public void insert(T item) {
        insert(item, false);
    }

    public void insert(T item, boolean reverse) {
        insert(reverse ? 0 : mItems.size(), item);
    }

    public void insert(int position, T item) {
        mItems.add(position, item);
        if (mOriginalItems != null) {
            mOriginalItems.add(position, item);
        }
        if (mAutoNotify) notifyItemInserted(position);
    }

    public void insertAll(List<T> items) {
        insertAll(items, false);
    }

    public void insertAll(List<T> items, boolean reverse) {
        insertAll(reverse ? 0 : mItems.size(), items);
    }

    public void insertAll(int position, List<T> items) {
        mItems.addAll(position, items);
        if (mOriginalItems != null) {
            mOriginalItems.addAll(position, items);
        }
        if (mAutoNotify) notifyItemRangeInserted(position, items.size());
    }

    public T remove(int position) {
        T removedItem = mItems.remove(position);
        if (mOriginalItems != null) {
            mOriginalItems.remove(position);
        }
        if (mAutoNotify) notifyItemRemoved(position);
        return removedItem;
    }

    public void remove(T item) {
        final int removePos = mItems.indexOf(item);
        remove(removePos);
    }

    public void removeAll(List<T> items) {
        final int removedPosStart = mItems.indexOf(items.get(0));
        removeAll(removedPosStart, items);
    }

    public void removeAll(int positionStart, List<T> items) {
        final int removedCount = items.size();
        mItems.removeAll(items);
        if (mOriginalItems != null) {
            mOriginalItems.removeAll(items);
        }
        if (mAutoNotify) notifyItemRangeRemoved(positionStart, removedCount);
    }

    public void change(int position, T newItem) {
        mItems.set(position, newItem);
        if (mOriginalItems != null) {
            mOriginalItems.set(position, newItem);
        }
        if (mAutoNotify) notifyItemChanged(position);
    }

    public void move(int fromPosition, int toPosition) {
        T moveItem = mItems.remove(fromPosition);
        mItems.add(toPosition, moveItem);
        if (mOriginalItems != null) {
            T origMoveItem = mOriginalItems.remove(fromPosition);
            mOriginalItems.add(toPosition, origMoveItem);
        }
        if (mAutoNotify) notifyItemMoved(fromPosition, toPosition);
    }



    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new BaseFilter();
        }
        return mFilter;
    }

    /**
     *
     */
    private class BaseFilter extends Filter {

        BaseFilter() {
            if (mOriginalItems == null) {
                mOriginalItems = new ArrayList<>(mItems);
            }
        }

        public void restore() {
            mItems = mOriginalItems;
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return super.convertResultToString(resultValue);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 返回的查询结果
            FilterResults results = new FilterResults();

            if (Predications.isNullOrEmpty(constraint)) {
                results.values = null;
                results.count = 0;
            } else {
                final String constraintStr = constraint.toString().toLowerCase();
                List<T> originalCopy = new ArrayList<>(mOriginalItems);
                //TODO
                List<T> filteredItems = new ArrayList<>(mOriginalItems.size());

                for (int i = 0, c = originalCopy.size(); i < c; i++) {
                    final T item = originalCopy.get(i);
                    final String itemStr = item.toString();
                    if (itemStr.startsWith(constraintStr)) {
                        filteredItems.add(item);
                    } else {

                    }
                }
            }
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<T> filteredItems = (List<T>) results.values;
            if (filteredItems != null) {
                mItems = filteredItems;
            } else {
                mItems.clear();
            }
            notifyDataSetChanged();
        }
    }

}
