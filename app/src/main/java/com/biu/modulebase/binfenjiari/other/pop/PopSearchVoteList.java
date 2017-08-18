package com.biu.modulebase.binfenjiari.other.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.WebViewVoteDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.binfenjiari.fragment.WebViewVoteDetailFragment;
import com.biu.modulebase.binfenjiari.model.NewVoteBeanVO;
import com.biu.modulebase.binfenjiari.model.VoteNewProjectVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PopSearchVoteList {

    private String TAG = "PopSearchVoteList";

    private Context context;

    private BaseFragment mBaseFragment;

    private View view;

    private PopupWindow popwindow;

    private View popview;

    private OnPopOperatorListener mOnPopOperatorListener;

    private View mPopBackview;

    private LayoutInflater mflater;

    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;
    private BaseAdapter adapter;
    private long time;
    String id1;
    private int pageNum =1;
    private int allPageNumber;
    private String mid;

    private HashMap<String,String> voteMap =new HashMap<>();
    private NewVoteBeanVO mNewVoteBeanVO;
    private String isCheckIds="",msearchTitle="";

    /**
     * 投票类别 1普通投票 2带图片
     */
    private int type=1;

    public PopSearchVoteList(BaseFragment fragment, final View view,String searchTitle,String id,NewVoteBeanVO newVoteBeanVO,HashMap<String,String> idsMap, String tag) {
        super();
        this.context = fragment.getContext();
        this.mBaseFragment=fragment;
        this.view = view;
        this.TAG = tag;
        this.mid = id;
        this.mNewVoteBeanVO = newVoteBeanVO;
        this.voteMap = idsMap;
        this.msearchTitle = searchTitle;

        mflater = LayoutInflater.from(this.context);
        popview = mflater.inflate(R.layout.pop_search_vote_list, null);
        popwindow = new PopupWindow(popview, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

//		popwindow.setAnimationStyle(0);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(true);
        popwindow.setFocusable(true);
        popwindow.setBackgroundDrawable(new BitmapDrawable(context
                .getResources()));
        popwindow.setOnDismissListener(new OnDismissListener() {

            public void onDismiss() {
                if(mPopBackview!=null){
                    mPopBackview.setVisibility(View.GONE);
                }
                if (mOnPopOperatorListener != null) {
                    mOnPopOperatorListener.popDismiss();
                }
            }
        });

        StringBuffer sb = new StringBuffer();
        for(String idvote:voteMap.values()){
            sb.append(idvote).append(",");
        }
        isCheckIds=sb.toString();

        initView(popview);
        loadData();
        mBaseFragment.showProgress(getClass().getSimpleName());

    }

    protected void initView(View rootView) {
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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        adapter=new BaseAdapter(context) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, final int viewType) {
                BaseViewHolder viewHolder = new BaseViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.part_pop_image_text_vote_select, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                VoteNewProjectVO bean = (VoteNewProjectVO) data;
                                id1=bean.getId();
                                View imgView = holder.getView(R.id.vote_pic);
                                if(TextUtils.isEmpty(bean.getPic())){
                                    imgView.setVisibility(View.GONE);
                                }else{
                                    imgView.setVisibility(View.VISIBLE);
                                    holder.setNetImage(Constant.IMG_COMPRESS,R.id.vote_pic,bean.getPic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                }

                                holder.setText(R.id.name,bean.getTitle());
                                CheckBox checkBox = holder.getView(R.id.voteCheckBox);

                                checkBox.setOnCheckedChangeListener(null);
                                if(isCheckIds.contains(bean.getId())){
                                    checkBox.setChecked(true);
                                }else{
                                    checkBox.setChecked(false);
                                }
                                checkBox.setOnCheckedChangeListener(new PopSearchVoteList.OnVoteCheckChangeListener(bean.getId()));

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {

                                VoteNewProjectVO bean = (VoteNewProjectVO)(getData().get(position));
                                int i = view.getId();
                                if (i == R.id.ll_part_pop_vote) {
                                    CheckBox checkBox = holder.getView(R.id.voteCheckBox);
                                    if (checkBox.isChecked()) {
                                        checkBox.setChecked(false);
                                    } else {
                                        checkBox.setChecked(true);
                                    }

                                } else {
                                    Intent intent = new Intent(context, WebViewVoteDetailActivity.class);
                                    intent.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                    intent.putExtra("type1", 2);
                                    intent.putExtra("id", id1);
                                    intent.putExtra("title1", mNewVoteBeanVO.getTitle());
                                    context.startActivity(intent);


                                }

                            }
                        });
//                viewHolder.setItemChildViewClickListener(R.id.ll_part_pop_vote);
                return viewHolder;

            }



        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,0,0,1);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void loadData() {
        time = new Date().getTime()/1000;
        pageNum =1;
        getProjects(Constant.LIST_REFRESH);
    }

    private void refreshList(int tag,ArrayList<VoteNewProjectVO> datas){
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case Constant.LIST_REFRESH:
                if(datas.size()==0) {
//                    visibleNoData();
                    mBaseFragment.dismissProgress();
                    return;
                }
                mBaseFragment.dismissProgress();
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

    private void getProjects(final int tag){
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model","NewVote");
        JSONUtil.put(params,"action","app_findVoteProjectList");
        JSONUtil.put(params,"title",msearchTitle);
        JSONUtil.put(params,"id",mid);
        JSONUtil.put(params,"time",time);
        JSONUtil.put(params,"pageNum",pageNum);
        mBaseFragment.jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    time = JSONUtil.getLong(result, "time");
                    allPageNumber = JSONUtil.getInt(result, "allPageNumber");
                    type = JSONUtil.getInt(result, "type");
                    JSONObject object = new JSONObject(jsonString);
                    JSONArray array =JSONUtil.getJSONArray(object,"projectList");
                    ArrayList<VoteNewProjectVO> projectVOList = JSONUtil.fromJson(array.toString(),new TypeToken<List<VoteNewProjectVO>>(){}.getType());
                    refreshList(tag, projectVOList);

                    if(allPageNumber==0){
                        mBaseFragment.dismissProgress();
                        mBaseFragment.showTost("未搜索到任何结果",1);
                        hide();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if(allPageNumber==0){
                    hide();
                }
                if(key ==3) {
                    mBaseFragment.dismissProgress();
                }else{
                    mBaseFragment.showTost(message,1);
                }
            }

            @Override
            public void onConnectError(String message) {
                if(allPageNumber==0){
                    hide();
                }
                mBaseFragment.dismissProgress();
            }
        });

    }

    class OnVoteCheckChangeListener implements CompoundButton.OnCheckedChangeListener {

        private String id;
        public OnVoteCheckChangeListener(String id){
            this.id =id;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            showAreaFilter();

            if(Utils.isEmpty(PreferencesUtils.getString(context, PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(context)==null) {
                mBaseFragment.showUnLoginSnackbar();
//                buttonView.setChecked(!isChecked);
                return;
            }
            if(isChecked){
                if(voteMap.size()<mNewVoteBeanVO.getMax_number()){
                    voteMap.put(id,id);
                    if(mOnPopOperatorListener!=null){
                        mOnPopOperatorListener.popSelect(true,id);
                    }
                }else{
                    if(mNewVoteBeanVO.getMax_number()>1) {
                        buttonView.setChecked(false);
                        mBaseFragment.showTost("已达最大投票数...",0);
                    }else{
                        if(mOnPopOperatorListener!=null){
                            mOnPopOperatorListener.popSelectFull(id);
                        }
                    }
                }
            }else{
                voteMap.remove(id);

                if(mOnPopOperatorListener!=null){
                    mOnPopOperatorListener.popSelect(false,id);
                }
            }
            hide();
        }
    }


    public PopupWindow getPopwindow() {
        return popwindow;
    }

    public void setBackgroundView(View view){
        mPopBackview=view;
        mPopBackview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }



    public OnPopOperatorListener getOnPopSelectListener() {
        return mOnPopOperatorListener;
    }

    public void setOnPopSelectListener(OnPopOperatorListener mListener) {
        this.mOnPopOperatorListener = mListener;
    }

    public void setType(int type) {

    }

    public void show() {
        if(mPopBackview!=null){
            mPopBackview.setVisibility(View.VISIBLE);
        }
        popwindow.showAsDropDown(view);
    }

    public void hide() {
        popwindow.dismiss();
    }

    public boolean isShow() {
        return popwindow.isShowing();
    }

    public static interface OnPopOperatorListener {
        void popSelect(boolean isCheck, String str);

        void popDismiss();

        void popSelectFull(String id);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


}
