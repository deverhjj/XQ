package com.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.CommonSearchResultActivity;
import com.binfenjiari.adapter.SearchHisAdapter;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.base.BaseActivity;
import com.binfenjiari.base.BaseFragment;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Msgs;
import com.binfenjiari.utils.Uis;
import com.binfenjiari.utils.Views;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.github.huajianjiang.alphaslidebar.SimpleSearchView;
import com.github.huajianjiang.alphaslidebar.SoftKeyboardHelper;
import com.github.huajianjiang.expandablerecyclerview.util.Logger;
import com.github.huajianjiang.expandablerecyclerview.widget.PatchedRecyclerView;

import java.lang.annotation.Retention;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/15
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class CommonSearchFragment extends AppFragment
        implements SoftKeyboardHelper.OnSoftKeyboardStateChangeListener,
        BaseActivity.OnPreFinishListener
{
    private static final String TAG = CommonSearchFragment.class.getSimpleName();
    private PatchedRecyclerView mSearchHisList;
    private SearchHisAdapter mAdapter;
    private SimpleSearchView mSearchView;

    private SoftKeyboardHelper mKeyboardHelper;

    private Bundle mArgs;
    private String mKeyword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mArgs = getArguments();
        mKeyword = mArgs != null ? mArgs.getString(Constants.KEY_SEARCH_KEY_WORDS) : "";
        getAssociateActivity().setOnPreFinishListener(this);
    }

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_common_search, container, false);
    }

    @Override
    public void onInitView(View root) {
        mSearchHisList = Views.find(root, R.id.searchHisList);
        mAdapter = new SearchHisAdapter(getContext());
        mSearchHisList.setAdapter(mAdapter);
        mSearchHisList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mKeyboardHelper = new SoftKeyboardHelper(root);
        mKeyboardHelper.setListener(this);

        mSearchView = (SimpleSearchView) ((BaseActivity) getActivity()).getCustomViewForToolbar();
        mSearchView.setText(mKeyword);
        mSearchView.setSelection(mKeyword.length());
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                doSearch();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doSearch() {
        final String kw = mSearchView.getText().toString();
        if (TextUtils.isEmpty(kw)) {
            return;
        }

        Uis.hideSoftInput(mSearchView);

        Intent result = new Intent(getContext(), CommonSearchResultActivity.class);
        result.putExtra(Constants.KEY_SEARCH_KEY_WORDS, kw);
        startActivity(result);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mKeyboardHelper.detach();
    }

    @Override
    public void onKeyboardShow() {

    }

    @Override
    public void onKeyboardHide() {

    }


    @Override
    public boolean onPreFinish() {
        Uis.hideSoftInput(mSearchView);
        return true;
    }
}
