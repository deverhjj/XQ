package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.SearchResultVoteActivity;
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
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.SearchEditText;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by jhj_Plus on 2016/4/18.
 */
public class CommVoteDetail2Fragment<E> extends BaseFragment {
    private static final String TAG = "CommVoteDetailFragment";

    private static final int SEARCH_VOTE_AND_DO_VOTE =111;

    private LSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private SearchEditText searchEditText;
    private String id;

    private VoteVO voteVO;
    private long time;
    private int allPageNumber =1;
    private int pageNum;
    /**搜索关键词**/
    private String title;

    private TextView fab_vote;

    private HashMap<Integer,String> voteMap =new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            id = args.getString(Constant.KEY_ID);
        }
    }

    public static CommVoteDetail2Fragment newInstance( String id) {
        Bundle args = new Bundle();
        args.putString(Constant.KEY_ID,id);
        CommVoteDetail2Fragment fragment = new CommVoteDetail2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.header_vote_detail_image, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        searchEditText = (SearchEditText) getBaseActivity().setToolBarCustomView(R.layout.search_editview);
        searchEditText.setTextHint("搜索投票项...");
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    title =searchEditText.getText().toString();
                    if(Utils.isEmpty(title)){
                        showTost("请输入搜索关键词",0);
                    }else{
                        /* 隐藏软键盘 */
                        showSoftKeyboard();
                        Intent intent = new Intent(getActivity(), SearchResultVoteActivity.class);
                        intent.putExtra(Constant.KEY_ID,id);
                        intent.putExtra("title",title);
                        intent.putExtra("voteVO",voteVO);
                        startActivityForResult(intent ,SEARCH_VOTE_AND_DO_VOTE);
                    }
                    return true;
                }
                return false;
            }
        });
        fab_vote =(TextView)rootView.findViewById(R.id.fab_vote);
        fab_vote.setOnClickListener(this);
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.requestFocus();
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
            private static final int TYPE_HEADER = 0X0001;//头部
            private static final int TYPE_BODY = 0X0002;
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = null;
                if(viewType ==TYPE_HEADER){
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_vote_img_header, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                            p.setFullSpan(true);
                            final VoteVO bean = (VoteVO) data;
                            holder.setText(R.id.tv_nickname,bean.getUsername());
                            holder.setNetImage(Constant.IMG_SOURCE,R.id.iv_head_portrait,bean.getUser_pic(), ImageDisplayUtil.DISPLAY_HEADER);
                            ImageView header = holder.getView(R.id.iv_head_portrait);
//                            header.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
//                                    intent.putExtra(Constant.KEY_ID,bean.getUser_id());
//                                    startActivity(intent);
//                                }
//                            });
                            int day_type=bean.getDay_type();
                            if(day_type ==1){
                                holder.setText(R.id.rule,String.format(getString(R.string.vote_rule1),bean.getCheck_number()));
                            }else{
                                holder.setText(R.id.rule,String.format(getString(R.string.vote_rule2),bean.getCheck_number()));
                            }
                            long extra =Utils.isLong(voteVO.getEnd_time())*1000-new Date().getTime();
                            String timeStr ="";
                            try {
                                timeStr=  Utils.getDistanceTimes(Utils.getCurrentDate2(),Utils.sec2Date(voteVO.getEnd_time(),"yyyy-MM-dd HH:mm:ss"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(extra>0){
                                holder.setText(R.id.extra_day,String.format(getString(R.string.extra_day),timeStr));
                            }else{
                                holder.setText(R.id.extra_day,"投票已结束");
                            }
                            holder.setText(R.id.tv_date, Utils.getReleaseTime(new Date(Utils.isLong(bean.getCreate_time())*1000)));
                            holder.setText(R.id.tv_content,bean.getTitle());
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                        }
                    });
                }else if(viewType ==TYPE_BODY){
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.part_image_vote_detail, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            VoteProjectVO bean = (VoteProjectVO) data;
                            holder.setNetImage(Constant.IMG_COMPRESS,R.id.vote_pic,bean.getPic(),ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                            holder.setText(R.id.name,bean.getTitle());
                            int number =bean.getNumber();
                            holder.setText(R.id.vote_num,String.format(getString(R.string.voted_num2),number+""));
                            ProgressBar progress =holder.getView(R.id.progress);
                            progress.setMax(Utils.isInteger(voteVO.getAll_poll()));
                            progress.setProgress(number);
                            holder.setText(R.id.percent, Utils.getPencent(Utils.isInteger(number),Utils.isInteger(voteVO.getAll_poll()))+"%");
                            CheckBox voteCheckBox =holder.getView(R.id.voteCheckBox);
                            if(bean.isChecked()){
                                voteCheckBox.setChecked(true);
                            }else{
                                voteCheckBox.setChecked(false);
                            }
                            voteCheckBox.setOnCheckedChangeListener(new OnVoteCheckChangeListener(holder.getAdapterPosition(),bean.getId()));

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                        }
                    });
                }
                return viewHolder;
            }

            @Override
            public int getItemViewType(int position) {
                if(position == 0){
                    return TYPE_HEADER;
                }else{
                    return TYPE_BODY;
                }
            }
            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state)
            {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if(childAdapterPosition==0){
                    outRect.set(0,0,0,0);
                }else{
                    outRect.set(0,getResources().getDimensionPixelSize(R.dimen.item_divider_height_4dp), 0,
                            0);
                }

            }
        };
        StaggeredGridLayoutManager manager =new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if(position==0){
//                    return 2;
//                }else{
//                    return 1;
//                }
//            }
//        });
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void loadData() {
        time =new Date().getTime()/1000;
        pageNum =1;
        getVoteDetail();
    }

    private void getVoteDetail(){
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_VOTE);
        JSONUtil.put(params,"action",Constant.ACTION_VOTE_DETAIL);
        JSONUtil.put(params,"id",getArguments().getString(Constant.KEY_ID));
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                inVisibleLoading();
                setHasOptionsMenu(true);
                voteVO = JSONUtil.fromJson(jsonString, VoteVO.class);
                ArrayList<E> datas = new ArrayList<>();
                datas.add((E) voteVO);
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                adapter.setData(datas);
                //设置总票数
                fab_vote.setText(String.format(getString(R.string.voted_num),voteVO.getAll_poll()+""));
                getProjects(Constant.LIST_REFRESH);
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

    private void getProjects(final int tag){
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_VOTE);
        JSONUtil.put(params,"action",Constant.ACTION_VOTE_SEARCH);
        JSONUtil.put(params,"id",id);
        JSONUtil.put(params,"time",time);
        JSONUtil.put(params,"pageNum",pageNum);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    time = JSONUtil.getLong(result, "time");
                    allPageNumber = JSONUtil.getInt(result, "allPageNumber");
                    JSONObject object = new JSONObject(jsonString);
                    JSONArray array =JSONUtil.getJSONArray(object,"projectList");
                    List<VoteProjectVO>projectVOList = JSONUtil.fromJson(array.toString(),new TypeToken<List<VoteProjectVO>>(){}.getType());
                    List<E> datas = new ArrayList<>();
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

    private <T> void refreshList(int tag,List<T> datas){
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case Constant.LIST_REFRESH:
                if(datas.size()==0) {
                    showTost("没有最新数据...",1);
                    return;
                }
                adapter.addData(1,datas);
//                adapter.setData(datas);
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
        while (it.hasNext()) {
            projectIdBuilder.append(it.next()).append(",");
        }
        String projectId =projectIdBuilder.substring(0,projectIdBuilder.length()-1);
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_VOTE);
        JSONUtil.put(params,"action",Constant.ACTION_DO_VOTE);
        JSONUtil.put(params,"projectId",projectId);
        JSONUtil.put(params,"id",getArguments().getString(Constant.KEY_ID));
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                int size =voteMap.size();
                voteVO.setAll_poll(voteVO.getAll_poll()+size);
                if(voteVO.getDay_type() ==1){
                    voteVO.setSurplus_number(0);
                }else{
                    voteVO.setSurplus_number(voteVO.getSurplus_number()-size);
                }
                fab_vote.setText(String.format(getString(R.string.voted_num),voteVO.getAll_poll()+""));
                //更新adapter数据源
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                Set keys =voteMap.keySet();
                Iterator it = keys.iterator();
                while (it.hasNext()){
                    int position = (int) it.next();
                    VoteProjectVO  voteProjectVO = (VoteProjectVO) adapter.getData(position);
                    voteProjectVO.setNumber(voteProjectVO.getNumber()+1);
                    voteProjectVO.setChecked(false);
                    adapter.changeData(position,voteProjectVO);
                }

                voteMap.clear();
                showTost("投票成功",1);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate( R.menu.share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_share) {
            OtherUtil.showMoreOperate(CommVoteDetail2Fragment.this, id, voteVO.getTitle(), voteVO.getTitle(), null,
                    Constant.SHARE_VOTE, -1, "-1",
                    false, null, -1, null);

        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case SEARCH_VOTE_AND_DO_VOTE://更改本地可投票数
                    loadData();
                    LogUtil.LogD("SEARCH_VOTE_AND_DO_VOTE");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onDestroyView() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroyView();
    }

    class OnVoteCheckChangeListener implements OnCheckedChangeListener {

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
}
