package com.biu.modulebase.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.VoteProjectVO;
import com.biu.modulebase.binfenjiari.model.VoteVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
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

/**
 * Created by jhj_Plus on 2016/4/18.
 */
public class CommVoteDetailFragment extends BaseFragment {
    private static final String TAG = "CommVoteDetailFragment";

    private LSwipeRefreshLayout mRefreshLayout;

    private String id;

    private VoteVO voteVO;

    private long time;

    private int pageNum;

    private TextView vote_rule;
    private TextView extra_day;

    private LinearLayout layout_vote;
    private List<VoteProjectVO> projectVOList = new ArrayList<>();

    private boolean doDetail =false;

    private boolean doProject =false;
    private TextView fab_vote;

    private HashMap<String,String> voteMap =new HashMap<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            id = args.getString(Constant.KEY_ID);
        }
    }

    public static CommVoteDetailFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(Constant.KEY_ID,id);
        CommVoteDetailFragment fragment = new CommVoteDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.header_vote_detail_text, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
               loadData();
            }

            @Override
            public void onLoadMore() {
            }
        });
        vote_rule =(TextView) rootView.findViewById(R.id.rule);
        extra_day = (TextView) rootView.findViewById(R.id.extra_day);
        layout_vote =(LinearLayout) getView().findViewById(R.id.layout_vote);
        fab_vote =(TextView)getView().findViewById(R.id.fab_vote);
        fab_vote.setOnClickListener(this);
    }


    @Override
    public void loadData() {
        time =new Date().getTime()/1000;
        pageNum =1;
        getVoteDetail();
        getProjects();
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
                voteMap.clear();
                for(int i=0;i<layout_vote.getChildCount();i++){
                    CheckBox checkBox = (CheckBox) layout_vote.getChildAt(i);
                    checkBox.setChecked(false);
                }

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

    private void getVoteDetail(){
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_VOTE);
        JSONUtil.put(params,"action",Constant.ACTION_VOTE_DETAIL);
        JSONUtil.put(params,"id",getArguments().getString(Constant.KEY_ID));
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                doDetail =true;
                voteVO = JSONUtil.fromJson(jsonString, VoteVO.class);
                setViewData();
                if(doDetail&&doProject){
                    inVisibleLoading();
                    mRefreshLayout.stopRefresh();
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

    private void getProjects(){
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
                    doProject =true;
                    JSONObject object = new JSONObject(jsonString);
                    JSONArray array =JSONUtil.getJSONArray(object,"projectList");
                    projectVOList = JSONUtil.fromJson(array.toString(),new TypeToken<List<VoteProjectVO>>(){}.getType());
                    showProjects();
                    if(doDetail&&doProject){
                        inVisibleLoading();
                        mRefreshLayout.stopRefresh();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                mRefreshLayout.stopRefresh();
                if(key ==3) {
                    showTost("投票项为空",0);
                }else{
                    showTost(message,1);
                }
            }

            @Override
            public void onConnectError(String message) {
                mRefreshLayout.stopRefresh();
                visibleNoNetWork();
            }
        });

    }

    private void setViewData(){
        TextView tv_nickname = (TextView) getView().findViewById(R.id.tv_nickname);
        tv_nickname.setText(voteVO.getUsername());
        ImageView iv_head_portrait= (ImageView) getView().findViewById(R.id.iv_head_portrait);
//        iv_head_portrait.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
//                intent.putExtra(Constant.KEY_ID,voteVO.getUser_id());
//                startActivity(intent);
//            }
//        });
        ImageDisplayUtil.displayImage(Constant.IMG_SOURCE,voteVO.getUser_pic(), iv_head_portrait,ImageDisplayUtil.DISPLAY_HEADER);
        TextView tv_date = (TextView) getView().findViewById(R.id.tv_date);
        tv_date.setText( Utils.getReleaseTime(new Date(Utils.isLong(voteVO.getCreate_time())*1000)));
        TextView tv_content = (TextView) getView().findViewById(R.id.tv_content);
        tv_content.setText(voteVO.getTitle());
        int day_type=voteVO.getDay_type();
        if(day_type ==1){
            vote_rule.setText(String.format(getString(R.string.vote_rule1),voteVO.getCheck_number()));
        }else{
            vote_rule.setText(String.format(getString(R.string.vote_rule2),voteVO.getCheck_number()));
        }

        long extra =Utils.isLong(voteVO.getEnd_time())*1000-new Date().getTime();
        String timeStr ="";
        try {
            timeStr=  Utils.getDistanceTimes(Utils.getCurrentDate2(),Utils.sec2Date(voteVO.getEnd_time(),"yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(extra>0){
            extra_day.setText(String.format(getString(R.string.extra_day),timeStr));
        }else{
            extra_day.setText("投票已结束");
        }

        //设置总票数
        fab_vote.setText(String.format(getString(R.string.voted_num),voteVO.getAll_poll()+""));
    }

    private void showProjects(){
        layout_vote.removeAllViews();
        for(int i=0;i<projectVOList.size();i++){
            VoteProjectVO projectVO = projectVOList.get(i);
            CheckBox radio =(CheckBox)LayoutInflater.from(getContext()).inflate(R.layout.part_vote_select_item_detail_text, null);
            radio.setTag(projectVO.getId());
            radio.setText(Utils.getString(projectVO.getTitle()));
            radio.setOnCheckedChangeListener(new OnVoteCheckChangeListener(projectVO.getId()));

            layout_vote.addView(radio, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate( R.menu.share , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_share) {
            OtherUtil.showMoreOperate(CommVoteDetailFragment.this, id, voteVO.getTitle(), voteVO.getTitle(), null,
                    Constant.SHARE_VOTE, -1, "-1",
                    false, null, -1, null);

        } else {
        }
        return super.onOptionsItemSelected(item);
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
        /**投票项id**/
        private String id;
        public OnVoteCheckChangeListener(String id){
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
                    voteMap.put(id,id);
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
