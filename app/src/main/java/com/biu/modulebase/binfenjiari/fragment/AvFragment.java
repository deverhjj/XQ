package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.MainActivity;
import com.biu.modulebase.binfenjiari.activity.NotificationActivity;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;


public class AvFragment extends BaseFragment implements MainActivity.Callbacks {
    private static final String TAG = "AvFragment";

    private static final int CIRCLE_PAGE_COUNT = 3;

    private static final String[] TABNAMES={"视频","音频","图文"};//"全部",

    private ViewPager mVp;

    private ArrayList<Callbacks> mCallbacks=new ArrayList<>();

    private TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_av, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {
        mVp= (ViewPager) rootView.findViewById(R.id.viewPager);
        mVp.setAdapter(new AvPagerAdapter(getChildFragmentManager()));
        mVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {

                notifyOnChannelChanged(position == 1 || position == 0, position);
            }
        });
    }

    @Override
    public void onPageSelected(int position, View header) {
        if (header instanceof TabLayout) {
            mTabLayout = (TabLayout) header;
            mTabLayout.setupWithViewPager(mVp);
        } else {
            notifyOnChannelChanged(false,-1);
        }
    }


    private void notifyOnChannelChanged(boolean inVideoChannel,int selectedPos) {
        Callbacks selectedChannel =
                selectedPos != -1 && selectedPos >= 0 && selectedPos < mCallbacks.size()
                        ? mCallbacks.get(selectedPos) : null;
        for (int i = 0; i < mCallbacks.size(); i++) {
            Callbacks callbacks = mCallbacks.get(i);
            Log.e(TAG, "selectedCallbacks===>" + selectedChannel);
            callbacks.onChannelChanged(inVideoChannel, selectedChannel);
        }
    }


    private class AvPagerAdapter extends FragmentPagerAdapter {

        public AvPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e(TAG,"getItem==>"+position);
            switch (position) {
                case 0: {
//                    AvAllFragment avAllFragment=new AvAllFragment();
                    HashMap<String, Object> args = new HashMap<>();
                    args.put("type", 1+"");
                    AvHomeAllFragment fragment = AvHomeAllFragment.newInstance(args);
//                    mCallbacks.add(fragment);
                    return fragment;
                }
                case 1:{
                    HashMap<String, Object> args = new HashMap<>();
                    args.put("type", 3+"");
                    AvHomeAllFragment fragment = AvHomeAllFragment.newInstance(args);
                    return fragment;
                }
//                    AvVideoFragment videoFragment = new AvVideoFragment();
//                    mCallbacks.add(videoFragment);
//                    return videoFragment;
//                case 3:
//                    AvAudioFragment audioFragment = new AvAudioFragment();
//                    mCallbacks.add(audioFragment);
//                    return audioFragment;
                case 2:
                    return new AvImageTextFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return CIRCLE_PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TABNAMES[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e(TAG,"destroyItem==>"+position);
            //            if (object instanceof AvFragment.Callbacks) {
            //                mCallbacks.remove(object);
            //            }
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public void loadData() {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        //        final MenuItem item = menu.findItem(R.id.action_search);
        //        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        //        mSearchView.setQueryHint(Html.fromHtml(
        //                "<font color = #999999 style=\"font-size: 10sp\">" + "搜索..." + "</font>"));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        if(hasMessage()){
//            menu.findItem(R.id.action_notification).setIcon(R.mipmap.message_hint);
//        }else{
//            menu.findItem(R.id.action_notification).setIcon(R.mipmap.message);
//        }
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            startSearchIntent(Constant.SEARCH_AUDIO);

        } else if (i == R.id.action_notification) {
            if (checkIsLogin()) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public interface Callbacks {
        void onChannelChanged(boolean inVideoChannel, Callbacks selectedChannel);
    }

    @Override
    public void onResume() {
        getActivity().invalidateOptionsMenu();
        super.onResume();
    }
}
