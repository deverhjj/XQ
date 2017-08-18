package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.VoteProjectVO;
import com.biu.modulebase.binfenjiari.model.VoteVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Lee
 * @Title: {投票搜索结果界面}
 * @Description:{描述}
 * @date 2016/6/15
 */
public class SearchResultVoteFragment<E> extends BaseFragment {

    private LSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private long time;
    private int allPageNumber =1;
    private int pageNum;
    /**搜索关键词**/
    private String title;

    private VoteVO voteVO;
    private String id;

    private HashMap<Integer,String> voteMap =new HashMap<>();

    private TextView fab_vote;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.header_vote_detail_image,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void getIntentData() {
        Intent intent =getActivity().getIntent();
        voteVO = (VoteVO) intent.getSerializableExtra("voteVO");
        id = intent.getStringExtra(Constant.KEY_ID);
        title = intent.getStringExtra("title");
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        fab_vote =(TextView)rootView.findViewById(R.id.fab_vote);
        fab_vote.setOnClickListener(this);
        if(voteVO!=null){
            fab_vote.setText(String.format(getString(R.string.voted_num),voteVO.getAll_poll()));
        }
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getProjects(Constant.LIST_LOAD_MORE);
            }
        });
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter = new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = null;
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.part_image_vote_detail, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            VoteProjectVO bean = (VoteProjectVO) data;
                            holder.setNetImage(Constant.IMG_COMPRESS,R.id.vote_pic,bean.getPic(),ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                            holder.setText(R.id.name,bean.getTitle());//bean.getCreate_number()+" "+
                            int number =bean.getNumber();
                            holder.setText(R.id.vote_num,String.format(getString(R.string.voted_num2),number));
                            ProgressBar progress =holder.getView(R.id.progress);
                            progress.setMax(Utils.isInteger(voteVO.getAll_poll()));
                            progress.setProgress(number);
                            holder.setText(R.id.percent, Utils.getPencent(Utils.isInteger(number),Utils.isInteger(voteVO.getAll_poll()))+"%");
                            CheckBox voteCheckBox =holder.getView(R.id.voteCheckBox);
                            voteCheckBox.setOnCheckedChangeListener(new OnVoteCheckChangeListener(holder.getAdapterPosition(),bean.getId()));
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                        }
                    });
                return viewHolder;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state)
            {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                int itemOffset = getResources().getDimensionPixelSize(
                        R.dimen.item_divider_height_4dp);
                int itemOffsetTop = getResources().getDimensionPixelSize(
                        R.dimen.item_divider_height_8dp);
                outRect.set(childAdapterPosition % 2 != 0 ? itemOffset : itemOffsetTop, childAdapterPosition - 2 < 0 ? itemOffsetTop : itemOffset,
                        childAdapterPosition % 2 == 0 ? itemOffset : itemOffsetTop, parent.getChildCount() - childAdapterPosition <= 2 ? itemOffsetTop : itemOffset);

            }
        };
        GridLayoutManager manager =new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void loadData() {
        time =new Date().getTime()/1000;
        pageNum =1;
        getProjects(Constant.LIST_REFRESH);
    }

    private void getProjects(final int tag){
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_VOTE);
        JSONUtil.put(params,"action",Constant.ACTION_VOTE_SEARCH);
        JSONUtil.put(params,"id",id);
        JSONUtil.put(params,"time",time);
        JSONUtil.put(params,"pageNum",pageNum);
        JSONUtil.put(params,"title",title);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    time = JSONUtil.getLong(result, "time");
                    allPageNumber = JSONUtil.getInt(result, "allPageNumber");
                    JSONObject object = new JSONObject(jsonString);
                    JSONArray array =JSONUtil.getJSONArray(object,"projectList");
                    List<VoteProjectVO> projectVOList = JSONUtil.fromJson(array.toString(),new TypeToken<List<VoteProjectVO>>(){}.getType());
                    ArrayList<E> datas = new ArrayList<>();
                    datas.addAll((Collection<? extends E>) projectVOList);
                    refreshList(tag, datas);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if(key ==3) {
                    visibleNoData();
                }else{
                    showTost(message,1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });

    }

    private <T> void refreshList(int tag,ArrayList<T> datas){
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case Constant.LIST_REFRESH:
                if(datas.size()==0) {
                    showTost("没有最新数据...",1);
                    return;
                }
                adapter.setData(datas);
                mRefreshLayout.setRefreshing(false);
                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(datas);
                mRefreshLayout.setLoading(false);
                break;
        }
        if(pageNum<allPageNumber){
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
        }else{
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
        }
    }

    private void doVote(){
        showProgress(getClass().getSimpleName());
        StringBuilder projectIdBuilder =new StringBuilder();
        Collection values=voteMap.values();
        Iterator it =values.iterator();
        while (it.hasNext()){
            projectIdBuilder.append(it.next()).append(",");
        }
        String projectId =projectIdBuilder.substring(0,projectIdBuilder.length()-1);
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_VOTE);
        JSONUtil.put(params,"action",Constant.ACTION_DO_VOTE);
        JSONUtil.put(params,"projectId",projectId);
        JSONUtil.put(params,"id",id);
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                int size =voteMap.size();
                voteVO.setAll_poll(voteVO.getAll_poll()+size);
                voteVO.setSurplus_number(voteVO.getSurplus_number()-size);
                fab_vote.setText(String.format(getString(R.string.voted_num),voteVO.getAll_poll()));
                //更新adapter数据源
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                Set keys =voteMap.keySet();
                Iterator it = keys.iterator();
                while (it.hasNext()){
                    int position = Utils.isInteger(it.next().toString());
                    VoteProjectVO  voteProjectVO = (VoteProjectVO) adapter.getData(position);
                    voteProjectVO.setNumber(voteProjectVO.getNumber()+1);
                    adapter.changeData(position,voteProjectVO);
                }
                voteMap.clear();
                showTost("投票成功",1);
                getActivity().setResult(Activity.RESULT_OK);

            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                if(key ==10){//投票次数已完，不能进行投票
                    showTost(message,1);
                }else if(key==11){//当天投票次数已完，不能进行投票
                    showTost(message,1);
                }else{
                    showTost(message,1);
                }

            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
                visibleNoNetWork();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.fab_vote) {
            if (Utils.isEmpty(PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN)) || MyApplication.getUserInfo(getActivity()) == null) {
                showUnLoginSnackbar();
                return;
            }
            if (voteVO.getIsopen().equals("1")) {//未结束
                if (voteVO.getSurplus_number() > 0) {
                    int voteSize = voteMap.size();
                    if (voteSize == 0) {
                        showTost("请先选择投票项...", 0);
                    } else if (voteSize > voteVO.getSurplus_number()) {
                        showTost("已超过最大投票额...", 0);
                    } else {
                        doVote();
                    }
                } else {
                    showTost("投票次数已用完...", 0);
                }
            } else {
                showTost("投票已结束...", 0);
            }


        }
    }

    class OnVoteCheckChangeListener implements CompoundButton.OnCheckedChangeListener {

        private int position;
        /**投票项id**/
        private String id;
        public OnVoteCheckChangeListener(int position,String id){
            this.position =position;
            this.id =id;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(Utils.isEmpty(PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(getActivity())==null) {
                showUnLoginSnackbar();
                buttonView.setChecked(!isChecked);
                return;
            }
            if(isChecked){
                if(voteMap.size()<voteVO.getSurplus_number()){
                    voteMap.put(position,id);
                }else{
                    showTost("已达最大投票数...",0);
                    buttonView.setChecked(false);
                }
            }else{
                voteMap.remove(id);
            }
        }
    }


    @Override
    public void onDestroyView() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroyView();
    }
}
