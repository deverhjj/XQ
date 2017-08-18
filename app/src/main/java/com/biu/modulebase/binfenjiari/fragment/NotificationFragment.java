package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AddFriendActivity;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * Created by jhj_Plus on 2016/4/20.
 */
public class NotificationFragment extends BaseFragment {
    private static final String TAG = "NotificationFragment";

    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notification, container,
                false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);
        getBaseActivity().setBackNavigationIcon();
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        RadioGroup group = (RadioGroup) rootView.findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position = group.indexOfChild(group.findViewById(checkedId));
                mViewPager.setCurrentItem(position, false);
            }
        });

    }

    @Override
    public void loadData() {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position){
                case 0:
                    return new MessageFragment();
                case 1:
                    return new FriendsFragment();

            }
            return  new MessageFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_add) {
            startActivity(new Intent(getActivity(), AddFriendActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }



}
