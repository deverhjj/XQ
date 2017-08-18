package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.adapter.ViewPageAdapter;
import com.biu.modulebase.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lee
 * @Title: {我的活动}
 * @Description:{描述}
 * @date 2016/4/15
 */
public class MyExerciseFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_activity, container, false);
        return layout;
    }

    @Override
    protected void initView(View rootView) {
        TabLayout tabLayout =(TabLayout)getView().findViewById(R.id.tabLayout);
        ViewPager viewPager =(ViewPager)getView().findViewById(R.id.viewPager);
        String titils[] = getResources().getStringArray(R.array.my_activity);
        List<String> tabTitles= new ArrayList<>();
        for (int i=0;i<titils.length;i++){
            tabTitles.add(titils[i]);
            tabLayout.addTab(tabLayout.newTab().setText(titils[i]));
        }
        List<Fragment>  fragments = new ArrayList<>();
        fragments.add(new ExerciseUnpayFragment());
        fragments.add(new ExercisePaiedFragment());
        viewPager.setAdapter(new ViewPageAdapter(getActivity().getSupportFragmentManager(),fragments,tabTitles));
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void loadData() {

    }
}
