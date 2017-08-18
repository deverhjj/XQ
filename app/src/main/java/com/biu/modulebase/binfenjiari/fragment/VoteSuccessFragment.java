package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommVoteDetailNewActivity;
import com.biu.modulebase.binfenjiari.activity.VoteRankActivity;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * Created by hasee on 2016/11/3.
 */

public class VoteSuccessFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_vote_success,container,false);
        return  super.onCreateView(inflater,rootView,savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        View showBtn = rootView.findViewById(R.id.vote);
        if(TextUtils.isEmpty(CommVoteDetailNewActivity.projectId)){
            showBtn.setVisibility(View.GONE);
            return;
        }
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getActivity(), VoteRankActivity.class);
                intent1.putExtra("project_id", CommVoteDetailNewActivity.projectId);
                intent1.putExtra("title",CommVoteDetailNewActivity.project_title);
                startActivity(intent1);
                getActivity().finish();
            }
        });
    }

    @Override
    public void loadData() {

    }
}
