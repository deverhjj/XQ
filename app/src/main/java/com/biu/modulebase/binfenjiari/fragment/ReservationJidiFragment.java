package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.common.base.BaseFragment;

import java.util.ArrayList;

/**
 * @author Lee
 * @Title: {Y预约的基地}
 * @Description:{描述}
 * @date 2016/4/15
 */
public class ReservationJidiFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recyclerview_swiperefresh, container, false);
        return layout;
    }

    @Override
    protected void initView(View rootView) {


        ArrayList<String> datas=new ArrayList<>();
        datas.add(""); datas.add("");
        datas.add("");datas.add("");
        datas.add(""); datas.add("");
        datas.add("");datas.add("");
        datas.add(""); datas.add("");
        datas.add("");datas.add("");
        datas.add(""); datas.add("");
        datas.add("");datas.add("");
        datas.add(""); datas.add("");
        datas.add("");datas.add("");
        datas.add(""); datas.add("");
        datas.add("");datas.add("");

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter =  new BaseAdapter(getActivity(),datas) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_exercise_unpay, parent, false), new BaseViewHolder.Callbacks() {
                    @Override
                    public void bind(BaseViewHolder holder, Object data) {
//                        Palette.generateAsync(bitmap,
//                                new Palette.PaletteAsyncListener() {
//                                    @Override
//                                    public void onGenerated(Palette palette) {
//                                        Palette.Swatch vibrant =
//                                                palette.getVibrantSwatch();
//                                        if (vibrant != null) {
//
//                                            int rgb = vibrant.getRgb();
//                                            int titleTextColor = vibrant.getTitleTextColor();
//                                            int bodyTextColor = vibrant.getBodyTextColor();
//                                        }
//                                    }
//                                });
                    }

                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
            }
        };
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        //设置网格布局管理器
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);



    }


    @Override
    public void loadData() {

    }
}
