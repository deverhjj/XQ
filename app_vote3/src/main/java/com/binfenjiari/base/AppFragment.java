package com.binfenjiari.base;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.binfenjiari.R;
import com.binfenjiari.utils.Views;


/**
 * Title:
 * <p>Description:
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/3/22
 * <br>Email: developer.huajianjiang@gmail.com
 */
public abstract class AppFragment<P extends BaseContract.BaseIPresenter> extends MvpFragment<P>
        implements PreIView, PostIView, PostLoadingDialog.OnBackPressedListener,
        View.OnClickListener
{
    private static final String TAG = AppFragment.class.getSimpleName();
    private AnimationDrawable mLoadingAnim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoadingView(R.layout.include_loading);
        setEmptyView(R.layout.part_error);
        getEmptyView().setOnClickListener(this);
        mLoadingAnim = (AnimationDrawable) ((ImageView) Views.find(getLoadingView(),
                                                                   R.id.loading)).getDrawable();
    }

    @Override
    protected void onFirstShow() {
        start();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }

    @Override
    protected void hideLoadingView() {
        mLoadingAnim.stop();
        super.hideLoadingView();
    }

    @Override
    protected void showLoadingView() {
        super.showLoadingView();
        mLoadingAnim.start();
    }

    //-------------pre ui logic -----------------\\
    @Override
    public void showPrePrepareUi() {
        showLoadingView();
    }

    @Override
    public void showPreSuccessUi() {
    }

    @Override
    public void showPreFailureUi(AppExp exp) {
        showEmptyView();
    }

    @Override
    public void clearPreUi() {
        hideAll();
    }

    //-------------post ui logic -----------------\\

    protected void showPostLoading() {
        PostLoadingDialog postLoading =
                (PostLoadingDialog) getChildFragmentManager().findFragmentByTag("POST_LOADING");
        if (postLoading != null && postLoading.isAdded()) {
            return;
        }
        postLoading = new PostLoadingDialog();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(postLoading, "POST_LOADING").commitAllowingStateLoss();
        //postLoading.show(getChildFragmentManager(), "POST_LOADING");
    }

    protected void dismissPostLoading() {
        DialogFragment post_loading =
                (DialogFragment) getChildFragmentManager().findFragmentByTag("POST_LOADING");
        if (post_loading != null && post_loading.isAdded()) {
            post_loading.dismiss();
        }
    }

    @Override
    public void showPostPrepareUi() {
        showPostLoading();
    }

    @Override
    public void showPostSuccessUi() {
    }

    @Override
    public void showPostFailureUi(AppExp exp) {
    }

    @Override
    public void clearPostUi() {
        dismissPostLoading();
    }

    @Override
    public void onBackPressed() {
        dismissPostLoading();
        stop();
    }

    protected void onRetry() {
        start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.error) {
            showLoadingView();
            onRetry();
        }
    }

}
