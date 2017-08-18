package com.binfenjiari.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.Parent;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class BaseExpandableAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder, P extends Parent, C>
        extends ExpandableAdapter<PVH, CVH, P, C>
{
    private Context mCtxt;
    private LayoutInflater mInflater;

    private List<P> mParents;

    public BaseExpandableAdapter(Context ctxt) {
        this(ctxt, null);
    }

    public BaseExpandableAdapter(Context ctxt, @Nullable List<P> parents) {
        super(parents == null ? new ArrayList<P>() : parents);
        mCtxt = ctxt.getApplicationContext();
        mInflater = LayoutInflater.from(ctxt);
        mParents = getParents();
    }

    public Context getContext() {
        return mCtxt;
    }

    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }
}
