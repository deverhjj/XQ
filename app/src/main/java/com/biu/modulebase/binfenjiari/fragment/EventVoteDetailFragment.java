package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommentActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.widget.MyRecyclerView;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * Created by jhj_Plus on 2016/5/4.
 */
public class EventVoteDetailFragment extends BaseFragment {
    private static final String TAG = "EventVoteDetailFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_event_detail_vote,
                container,
                false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }
    NestedScrollView scrollView;
    @Override
    protected void initView(View rootView) {

        final MyRecyclerView recyclerView= (MyRecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        BaseAdapter adapter = new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.part_image_vote_detail_event, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position)
                            {
                                OtherUtil.showToast(getActivity(),"pos="+position);
                                recyclerView.setSelectedPos(position);
                            }
                        });
                return viewHolder;
            }
        };

        adapter.testLayoutData(5);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(null);

        View vote = rootView.findViewById(R.id.fab_vote);
        vote.setOnClickListener(this);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        ViewGroup layout_comment= (ViewGroup) rootView.findViewById(R.id.layout_comment);
        layout_comment.setOnClickListener(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.layout_comment) {
            Intent intent = new Intent(getActivity(), CommentActivity.class);
            startActivity(intent);

        } else if (i == R.id.fab_vote) {
            scrollView.fullScroll(View.FOCUS_UP);

        } else {
        }
    }


}
