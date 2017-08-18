package com.biu.modulebase.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.SearchFragment;
import com.biu.modulebase.common.base.BaseActivity;

public class SearchActivity extends BaseActivity {

	@Override
	protected Fragment getFragment() {
		setBackNavigationIcon();
		return new SearchFragment();
	}

	@Override
	protected String getToolbarTitle() {
		return "";
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.fade_out_quick);
	}

	/**
	 * 跳轉搜索界面的Intent
	 * @param tag
	 *      Constant.SEARCH_EVENT or Constant.SEARCH_JIDI or Constant.SEARCH_AUDIO or Constant.SEARCH_VOTE
	 *
	 */
	public static void beginActivity(Context context, String tag){
		Intent intent =new Intent(context, SearchActivity.class);
		intent.putExtra(Constant.SEARCH_TAG,tag);
		context.startActivity(intent);
//		getActivity().overridePendingTransition(0, R.anim.fade_in);
	}
}