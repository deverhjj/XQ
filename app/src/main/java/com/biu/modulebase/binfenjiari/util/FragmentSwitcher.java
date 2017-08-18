package com.biu.modulebase.binfenjiari.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by jhj_Plus on 2016/1/7.
 */
public abstract class FragmentSwitcher {

    private static final String TAG = "FragmentSwitcher";

    private FragmentManager mFragmentManager;

    private int mContainerId;

    private String mCurAttachedFragmentTag;

    public FragmentSwitcher(FragmentManager fm, int containerId) {
        mFragmentManager = fm;
        mContainerId = containerId;
    }

    private void attach(int attachPosition) {
        FragmentTransaction transaction=mFragmentManager.beginTransaction();
        final long itemId = getItemId(attachPosition);
        String fragmentName = makeFragmentName(mContainerId, itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(fragmentName);
        if (fragment == null) {
            fragment = getItem(attachPosition);
            transaction.add(mContainerId, fragment, fragmentName);

            Log.i(TAG,"add***********************>"+attachPosition);
        } else {
            transaction.attach(fragment);
            Log.i(TAG,"attach***********************>"+attachPosition);
        }
        mCurAttachedFragmentTag=fragmentName;
        //TODO Can not perform this action after onSaveInstanceState
        transaction.commit();
    }

    private void detach() {
        FragmentTransaction transaction=mFragmentManager.beginTransaction();

        Fragment fragment = mFragmentManager.findFragmentByTag(mCurAttachedFragmentTag);

        if (fragment!=null) {
            Log.i(TAG,"detach***********************>");
            transaction.detach(fragment).commit();
        }

        Log.i(TAG,"detach***********************>"+mCurAttachedFragmentTag);
    }

    public void toggle(int selectedPos) {
        detach();
        attach(selectedPos);
    }

    public abstract Fragment getItem(int position);

    private long getItemId(int position) {
        return position;
    }

    private String makeFragmentName(int containerId, long itemId) {
        return "fragment switcher" + containerId + itemId;
    }

}
