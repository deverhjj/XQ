package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.adapter.AvEpisodeAdapter;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.utils.Views;
import com.github.huajianjiang.expandablerecyclerview.widget.PatchedRecyclerView;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AvVideoIntroFragment extends AppFragment {
    private TextView mIntroView;

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_intro, container, false);
    }

    @Override
    public void onInitView(View root) {
        mIntroView = Views.find(root, R.id.intro);
    }

}
