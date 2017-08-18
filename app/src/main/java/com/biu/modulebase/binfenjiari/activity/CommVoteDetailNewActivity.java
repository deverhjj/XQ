package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.eventbus.VoteNextEvent;
import com.biu.modulebase.binfenjiari.eventbus.VoteSuccessEvent;
import com.biu.modulebase.binfenjiari.fragment.CommVoteDetailNewImageFragment;
import com.biu.modulebase.binfenjiari.fragment.CommVoteDetailNewTextFragment;
import com.biu.modulebase.binfenjiari.model.NewVoteBeanVO;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhj_Plus on 2016/4/18.
 *
 * CommVoteDetailNewActivity.sNewVoteBeanVOList
 * CommVoteDetailNewActivity.project_title
 *
 * Intent intent = new Intent(getContext(), CommVoteDetailNewActivity.class);
 * intent.putExtra("project_title",bannerVO.getTitle());
 * intent.putExtra(Constant.KEY_POSITION, 0);
 * startActivity(intent);
 *
 */
public class CommVoteDetailNewActivity extends BaseActivity {
    private static final String TAG = "CommVoteDetailNewActivity";

    public static final int TYPE_VOTE_TEXT=2;

    public static final int TYPE_VOTE_IMAGE=1;

    public static List<NewVoteBeanVO> sNewVoteBeanVOList;

    public static Map<String,String> sVoteIds;

    public static String projectId;

    public static String project_title="";

    public int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
//        Log.e("ID",CommVoteDetailNewActivity.projectId);
        EventBus.getDefault().register(this);
    }

    @Override
    protected Fragment getFragment() {
        if(sNewVoteBeanVOList==null)
            return null;

        //CommVoteDetailNewImageFragment  CommVoteDetailNewTextFragment
        Intent intent=getIntent();
        position=intent.getIntExtra(Constant.KEY_POSITION, 0);
//        int voteType=intent.getIntExtra(Constant.KEY_TYPE, TYPE_VOTE_TEXT);

        int type = sNewVoteBeanVOList.get(position).getType();
        int voteType = sNewVoteBeanVOList.get(position).getShow_type();
        String id = sNewVoteBeanVOList.get(position).getId();

//        voteType = 2;
        //投票类型 1普通投票 2带图片投票
        if(type==1){
            return CommVoteDetailNewTextFragment.newInstance(position,id);
        }

        //投票项展示形式 1一行三个 2一行一个
        if(voteType==TYPE_VOTE_TEXT){
//            String id =intent.getStringExtra(Constant.KEY_ID);
            return CommVoteDetailNewTextFragment.newInstance(position,id);
        }else if(voteType==TYPE_VOTE_IMAGE){
//            String id =intent.getStringExtra(Constant.KEY_ID);
            return CommVoteDetailNewImageFragment.newInstance(position,id);
        }else{
//            String id =intent.getStringExtra(Constant.KEY_ID);
            return CommVoteDetailNewTextFragment.newInstance(position,id);
        }

    }

    @Override
    protected String getToolbarTitle() {
        return "新版投票详情";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftKeybord();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 投票成功完成接收消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING) //默认方式, 在发送线程执行
    public void onVoteSuccessEvent(VoteSuccessEvent event) {
        sNewVoteBeanVOList=null;
        sVoteIds=null;
//        CommVoteDetailNewActivity.projectId = null;
//        CommVoteDetailNewActivity.project_title=null;
        finish();
    }

    /**
     * 投票下一步接收消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onVoteNextEvent(VoteNextEvent event){
        if (Utils.isEmpty(PreferencesUtils.getString(CommVoteDetailNewActivity.this, PreferencesUtils.KEY_TOKEN)) || MyApplication.getUserInfo(CommVoteDetailNewActivity.this) == null) {
            showUnLoginSnackbar();
            return;
        }
        if(CommVoteDetailNewActivity.sNewVoteBeanVOList==null){
            return;
        }

        int posi = event.getPosition();
        if(position!=posi)
            return;
        String ids = event.getIds();
        int size = CommVoteDetailNewActivity.sNewVoteBeanVOList.size();

        addVoteID(posi,ids);
        if(posi==size-1){
            Intent intent=new Intent(this, AddMyInfoActivity.class);
            if(CommVoteDetailNewActivity.sNewVoteBeanVOList.get(0).getIs_realname()==1){
                intent.putExtra("needCardNumber",true);
            }else{
                intent.putExtra("needCardNumber",false);
            }
            intent.putExtra("voteIds", getVoteIDS());
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, CommVoteDetailNewActivity.class);
            intent.putExtra(Constant.KEY_POSITION, posi+1);
            startActivity(intent);
        }
    }

    public void addVoteID(int position,String ids){
        if(sVoteIds==null){
            sVoteIds = new LinkedHashMap<>();
        }
        sVoteIds.put(position+"",ids);
    }

    /**
     * （选中的投票项用,隔开；投票id和选中的投票项目用@隔开；主副投票用#隔开；实例：1@1,2,3,4,5#2@3,4,5,6,7）
     * 获取voteIds  1@1,2,3,4,5#2@3,4,5,6,7
     * @return
     */
    public String getVoteIDS(){
        if(sVoteIds==null || sVoteIds.size()==0){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(String id:sVoteIds.values()){
            sb.append(id).append("#");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
