package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.MapActivity;
import com.biu.modulebase.binfenjiari.activity.SubmitOrderActivity;
import com.biu.modulebase.binfenjiari.adapter.CommentAvailableAdapter;
import com.biu.modulebase.binfenjiari.adapter.CommentLoader;
import com.biu.modulebase.binfenjiari.adapter.HeaderHandler;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.EventHeaderParentItem;
import com.biu.modulebase.binfenjiari.model.ReplyItem;
import com.biu.modulebase.binfenjiari.util.DoubleUtil;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.MyCheckBox;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ParentViewHolder;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ViewHolderCallbacks;
import com.biu.modulebase.common.base.BaseFragment;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jhj_Plus on 2016/4/25.
 */
public class EventDetailFragment extends BaseFragment {
    private static final String TAG = "EventDetailFragment";

    private static final int RESERVATION =111;

    private RecyclerView mRecyclerView;

    private HeaderHandler mHeaderHandler;
    private CommentLoader mCommentLoader;

    private String mId="-1";
    private int position =-1;

//    private MyCheckBox mLikeBox;
    private MyCheckBox mCollectBox;

    private  EventHeaderParentItem eventHeader;

    private String oldLikeStatus="";

    private double latitude;
    private double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHeaderHandler=new HeaderHandler(getActivity());

        Bundle args=getArguments();
        if (args!=null) {
            mId=args.getString(Constant.KEY_ID);
            position =args.getInt("position");
        }
    }

    public static EventDetailFragment newInstance(String id,int position) {
        Bundle args = new Bundle();
        args.putString(Constant.KEY_ID, id);
        args.putInt("position", position);
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_event_detail,container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        HashMap<String, Object> args = new HashMap<>();

        //获取主评论 的 action 和 model
        args.put(Constant.KEY_MODEL, Constant.MODEL_ACTIVITY_COMMENT_DETAIL);
        args.put(Constant.KEY_ACTION, Constant.ACTION_ACTIVITY_COMMENT_DETAIL);

        //回复主评论或回复 的 action 和 model
        args.put(Constant.KEY_MODEL_COMMENT_DETAIL,Constant.MODEL_ACTIVITY_COMMENT_DETAIL);
        args.put(Constant.KEY_ACTION_COMMENT_DETAIL,Constant.ACTION_ACTIVITY_COMMENT_REPLY);

        //删除评论 的 action 和 model
        args.put(Constant.KEY_MODEL_DELETE_COMMENT_COMMENT_DETAIL,Constant.MODEL_ACTIVITY_COMMENT);
        args.put(Constant.KEY_ACTION_DELETE_COMMENT_COMMENT_DETAIL,Constant
                .ACTION_ACTIVITY_COMMENT_DELETE);

        //删除回复 的 action 和 model
        args.put(Constant.KEY_MODEL_DELETE_REPLY_COMMENT_DETAIL,Constant.MODEL_ACTIVITY_COMMENT_DETAIL);
        args.put(Constant.KEY_ACTION_DELETE_REPLY_COMMENT_DETAIL,Constant.ACTION_ACTIVITY_COMMENT_REPLY_DELETE);

        //删除回复或评论的 的字段 名
        args.put(Constant.KEY_NAME_ARGS, "activityId");
        //删除回复或评论的 的字段 值
        args.put(Constant.KET_VALUE_ARGS, mId);

        //评论举报类型
        args.put(Constant.KEY_REPORT_TYPE,Constant.REPORT_ACTIVITY_COMMENT);

        //评论基础参数设置
        JSONObject params = OtherUtil.getJSONObject(getActivity(), Constant.MODEL_ACTIVITY_COMMENT,
                Constant.ACTION_ACTIVITY_COMMENT_LIST, false);
        JSONUtil.put(params, "id", mId);
        //评论处理类(加载，回复，删除 评论)
        mCommentLoader = new CommentLoader(CommentLoader.TYPE_LIST,this, params,args);

        CommentAvailableAdapter adapter=new CommentAvailableAdapter(this,mCommentLoader,args ,new
                ArrayList<ParentListItem>(),
                R.layout.header_event_detail, new ViewHolderCallbacks() {
            @Override
            public int[] getNeedRegisterClickListenerChildViewIds() {
                return new int[]{R.id.tv_location};
            }

            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder, View view, int position,
                    int adapterPosition, int parentPosition, int parentAdapterPosition)
            {


                int i = view.getId();
                if (i == R.id.tv_location) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra(Constant.KEY_LOCATION_LATITUDE, eventHeader.getLatitude());
                    intent.putExtra(Constant.KEY_LOCATION_LONGITUDE, eventHeader.getLongitude());
                    intent.putExtra(Constant.KEY_MAP_TARGET, eventHeader.getAddress());
                    startActivity(intent);

                } else {
                }
            }

            @Override
            public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {

                if (data==null||!(data instanceof EventHeaderParentItem)) return;

                EventHeaderParentItem item= (EventHeaderParentItem) data;

                ParentViewHolder holder = (ParentViewHolder) viewHolder;

                TextView tv_name= (TextView) holder.getView(R.id.tv_name);
                tv_name.setText(item.getName());

                TextView tv_location= (TextView) holder.getView(R.id.tv_location);
                tv_location.setText(getString(R.string.address3,item.getAddress(), DoubleUtil.round(item.getDistance(),2)+""));

                TextView tv_date= (TextView) holder.getView(R.id.tv_date);
                tv_date.setText(getString(R.string.data, Utils.sec2Date(item.getOpen_time(),"yyyy/MM/dd"),Utils.sec2Date(item.getEnd_time(),"yyyy/MM/dd")));

                TextView tv_name_jidi= (TextView) holder.getView(R.id.tv_name_jidi);
                tv_name_jidi.setText(getString(R.string.address4,item.getBase_name()));

                TextView tv_remark = (TextView) holder.getView(R.id.tv_remark);
                tv_remark.setText(getString(R.string.remark,item.getRemark()));

                TextView tv_limit_number = (TextView) holder.getView(R.id.tv_limit_number);
//                tv_limit_number.setText(Utils.isInteger(item.getLimit_number())!=0?String.format(getString(R.string.event_detail_people_num),item.getApply_number(),item.getLimit_number()):"无限制");
                tv_limit_number.setText(Utils.isInteger(item.getLimit_number())!=0?String.format("%1$s/%2$s",item.getApply_number(),item.getLimit_number()):"无限制");

                TextView tv_money = (TextView) holder.getView(R.id.tv_money);
                final double money = item.getMoney();
//                tv_money.setText(money == 0.f ? "所需金额：免费" : getString(R.string.money, item.getMoney()));
                tv_money.setText(money == 0.f ? "免费" : "￥"+item.getMoney()+"");

                TextView tv_comment_number = (TextView) holder.getView(R.id.tv_comment_number);
                tv_comment_number.setText(getString(R.string.comment,item.getComment_number()));

                TextView tv_score_composite = (TextView) holder.getView(R.id.tv_score_composite);
                tv_score_composite.setText(String.format("%.1f", Double.valueOf(item.getAverage_collect())));

//                if (mLikeBox != null) {
//                    mLikeBox.setChecked(item.getLike_status().equals("1"));
//                    mLikeBox.setText(item.getLike_number());
//                }

                if (mCollectBox != null) {
                    mCollectBox.setChecked(item.getCollect_status().equals("1"));
                }

                if (mHeaderHandler!=null) {
                    mHeaderHandler.createBindImageViews((LinearLayout) holder.getView(R.id.layout_it)
                            ,item.getPicList());
                    mHeaderHandler.createBindRatingViews((LinearLayout) holder.getView(R.id
                            .layout_ratings),item.getCollectList());
                }

            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentLoader.setCommentAvailableAdapter(adapter);


//        mLikeBox = (MyCheckBox) rootView.findViewById(R.id.like);
        mCollectBox =(MyCheckBox) rootView.findViewById(R.id.collect);

        TextView comment = (TextView) rootView.findViewById(R.id.tv_comment);
        comment.setOnClickListener(this);

        rootView.findViewById(R.id.sign_up).setOnClickListener(this);
        rootView.findViewById(R.id.tv_call).setOnClickListener(this);

//        mLikeBox.setOnClickListener(this);
        mCollectBox.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        visibleLoading();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void loadData() {
        startLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                stopLocation();
                getEventDetail();
            }
        });

        loadComments();
    }

    private void getEventDetail(){

        JSONObject params= OtherUtil.getJSONObject(getActivity(),Constant.MODEL_ACTIVITY,Constant
                .ACTION_ACTIVITY_DETAIL,false);
        Double[] location=new Double[2];
//        OtherUtil.getLocation(getActivity(),location);

        JSONUtil.put(params,"id",mId);
        JSONUtil.put(params,"longitude",longitude);
        JSONUtil.put(params,"latitude",latitude);

        dataRequest(false, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
//                visibleLoading();
            }

            @Override
            public void onSuccess(String mainJsonString,JSONObject mainJsonObject,JSONObject rootJsonObject) {
                setHasOptionsMenu(true);
                eventHeader = JSONUtil.fromJson(mainJsonString,
                        EventHeaderParentItem.class);
                oldLikeStatus =eventHeader.getLike_status();

                LogUtil.LogE(TAG,"onSuccess=>"+eventHeader.toString());

                //头部数据加载完成设置给评论类
                mCommentLoader.addHeaderData(eventHeader);
            }

            @Override
            public void onFail(int key,String message) {
                if (key == RequestCallBack2.KEY_FAIL) {
                    visibleNoNetWork();
                } else if(key ==3) {
                    visibleNoData();
                }else{
                    showTost(message,1);
                }
                LogUtil.LogE(TAG,"onFail*");
            }

            @Override
            public void requestAfter() {
                inVisibleLoading();
            }
        });
    }


    private void loadComments() {
        //加载评论处理丢给他处理
        mCommentLoader.loadComments();
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.LogE(TAG,"onActivityResult====================");
        if(resultCode ==Activity.RESULT_OK){
            switch (requestCode){
                case RESERVATION:
                    int num =data.getIntExtra("num",0);
                    eventHeader.setApply_number(Utils.isInteger(eventHeader.getApply_number())+num+"");
                    eventHeader.setAppointment_status("1");
                    CommentAvailableAdapter adapter = (CommentAvailableAdapter) mRecyclerView.getAdapter();
                    adapter.addParent(0,eventHeader);
                    break;
                case Constant.REQUEST_COMMENT_CHANGED_STATUS:
                    ArrayList<ReplyItem> replies = (ArrayList<ReplyItem>) data.getSerializableExtra(
                            Constant.KEY_DATA);
                    final int parentPosition=data.getIntExtra(Constant.KEY_PARENT_POSITION,-1);
                    //删除评论列表数据，如果返回的回复数据为null表明直接删除整个评论，否则先删除该评论下的所有回复后再
                    //添加新的回复数据
                    if (replies==null) {
                        mCommentLoader.deleteLocaleComment(CommentLoader.TYPE_LIST,parentPosition);
                    } else {
                        mCommentLoader.deleteLocaleReplies(CommentLoader.TYPE_LIST,parentPosition);
                        mCommentLoader.addLocalReplies(CommentLoader.TYPE_LIST,parentPosition,replies);
                    }
                    //通知刷新评论数
                    mCommentLoader.notifyCommentReplyDataChanged(CommentLoader.TYPE_LIST,
                            data.getIntExtra(Constant.KEY_COMMENT_OPREATE_TYPE,-1),data.getIntExtra
                                    (Constant.KEY_COMMENT_CHANGED_COUNT,0),parentPosition,null);
                    break;
            }

        }
        UMShareAPI.get(getActivity()).onActivityResult( requestCode, resultCode, data);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.more, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_more) {
            OtherUtil.showMoreOperate(EventDetailFragment.this, mId, eventHeader.getName(), eventHeader.getRemark(), null, Constant.SHARE_ACTIVITY,
                    Constant.REPORT_ACTIVITY, null, false, null, -1, null);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        stopLocation();
        mCommentLoader.cancleRequest();
        cancelRequest(TAG);
        ImageDisplayUtil.stopTask();
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(eventHeader==null){
            showTost("未获取到活动信息",0);
            return;
        }
        int i = v.getId();
        if (i == R.id.tv_comment) {
            JSONObject params = OtherUtil.getJSONObject(getActivity(),
                    Constant.MODEL_ACTIVITY_COMMENT, Constant
                            .ACTION_ACTIVITY_COMMENT, true);
            JSONUtil.put(params, "id", mId);
            mCommentLoader.doComment(params, -1);

        } else if (i == R.id.like) {
            JSONObject params2 = OtherUtil.getJSONObject(getActivity(), Constant.MODEL_ACTIVITY,
                    Constant.ACTION_ACTIVITY_LIKE, true);
            JSONUtil.put(params2, "id", mId);
//            OtherUtil.like(this, mLikeBox, params2, new OtherUtil.LikeCallback() {
//                @Override
//                public void onFinished(int backKey) {
//                    Intent intent = new Intent();
//                    intent.putExtra("position", position);
//                    intent.putExtra("backKey", backKey);
//                    if ((backKey == 1 && oldLikeStatus.equals("2")) || (backKey == 2 && oldLikeStatus.equals("1"))) {//点赞 或取消赞成功
//                        getActivity().setResult(Activity.RESULT_OK, intent);
//                    } else {
//                        getActivity().setResult(Activity.RESULT_CANCELED);
//                    }
//                }
//            });

        } else if (i == R.id.collect) {
            OtherUtil.collect(this, mCollectBox, mId, Constant.MODEL_ACTIVITY, Constant
                    .ACTION_ACTIVITY_COLLECT);

        } else if (i == R.id.sign_up) {
            if (Utils.isEmpty(PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN))) {
                showUnLoginSnackbar();
                return;
            }
            if (Utils.getString(eventHeader.getAppointment_status()).equals("1")) {
                showTost("您已预约过该活动...", 0);
                return;
            }
            if (eventHeader.getIsopen() != 1) {
                showTost("该活动已过期", 0);
                return;
            }
            int apply_num = Utils.isInteger(eventHeader.getApply_number());
            int limit_number = Utils.isInteger(eventHeader.getLimit_number());
            if (limit_number == 0 || (limit_number > 0 && apply_num < limit_number)) {
                Intent intent = new Intent(getActivity(), SubmitOrderActivity.class);
                intent.putExtra("event", eventHeader);
                startActivityForResult(intent, RESERVATION);
            } else {
                showTost("预约人数已满...", 0);
            }


        } else if (i == R.id.tv_call) {
            Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:88066511"));
            startActivity(call);

        } else {
        }
    }



}
