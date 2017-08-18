package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.binfenjiari.adapter.CommentAvailableAdapter;
import com.biu.modulebase.binfenjiari.adapter.CommentLoader;
import com.biu.modulebase.binfenjiari.adapter.CommonAdapter;
import com.biu.modulebase.binfenjiari.adapter.ViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.CommentItem;
import com.biu.modulebase.binfenjiari.model.QuestionHeaderParentItem;
import com.biu.modulebase.binfenjiari.model.ReplyItem;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.adapter.ExpandableRecyclerViewAdapter;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ParentViewHolder;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ViewHolderCallbacks;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by jhj_Plus on 2016/4/19.
 */
public class CommQADetailFragment extends BaseFragment {
    private static final String TAG = "CommQADetailFragment";

    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;
    private CommentLoader mCommentLoader;
    private QuestionHeaderParentItem eventHeader;

    private String mId="-1";
    private int mType;


    private EditText et_comment;

    private Button btn_send;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt(Constant.KEY_TYPE);
        }
    }

    @Override
    protected void getIntentData() {
        mId =getActivity().getIntent().getStringExtra(Constant.KEY_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_comm_qa_detail,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        et_comment =(EditText)rootView.findViewById(R.id.et_comment);
        btn_send =(Button)rootView.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
                btn_send.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG,"onRefresh******************");
                //refreshData();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG,"onLoadMore******************");
                //loadCards();
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);


        HashMap<String, Object> args = new HashMap<>();

        args.put(Constant.KEY_MODEL, Constant.MODEL_QUESTION);
        args.put(Constant.KEY_ACTION, Constant.ACTION_QUESTION_COMMENT_REPLY_LIST);

        //回复主评论或回复 的 action 和 model
        args.put(Constant.KEY_MODEL_COMMENT_DETAIL,Constant.MODEL_QUESTION);
        args.put(Constant.KEY_ACTION_COMMENT_DETAIL,Constant.ACTION_QUESTION_COMMENT_REPLY);

        //删除评论 的 action 和 model
        args.put(Constant.KEY_MODEL_DELETE_COMMENT_COMMENT_DETAIL,Constant.MODEL_QUESTION);
        args.put(Constant.KEY_ACTION_DELETE_COMMENT_COMMENT_DETAIL,Constant
                .ACTION_QUESTION_COMMENT_DELETE);

        //删除回复 的 action 和 model
        args.put(Constant.KEY_MODEL_DELETE_REPLY_COMMENT_DETAIL,Constant.MODEL_QUESTION);
        args.put(Constant.KEY_ACTION_DELETE_REPLY_COMMENT_DETAIL,Constant.ACTION_QUESTION_COMMENT_REPLY_DELETE);

        args.put(Constant.KEY_NAME_ARGS, "questionsId");
        args.put(Constant.KET_VALUE_ARGS, mId);

        //评论举报类型
        args.put(Constant.KEY_REPORT_TYPE,Constant.REPORT_QUESTION_COMMENT);

        //评论基础参数设
        JSONObject params = OtherUtil.getJSONObject(getActivity(), Constant.MODEL_QUESTION,
                Constant.ACTION_QUESTION_COMMENT_LIST, false);
        JSONUtil.put(params, "id", mId);
        //评论处理类(加载，回复，删除 评论)
        mCommentLoader = new CommentLoader(CommentLoader.TYPE_LIST,this, params,args);

        CommentAvailableAdapter adapter=new CommentAvailableAdapter(this,mCommentLoader,args, new
                ArrayList<ParentListItem>(),
                R.layout.header_card_detail3, new ViewHolderCallbacks() {
            @Override
            public int[] getNeedRegisterClickListenerChildViewIds() {
                return new int[]{R.id.toggle};
            }

            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder, View view, int position,
                    int adapterPosition, int parentPosition, int parentAdapterPosition)
            {
                ParentViewHolder holder = (ParentViewHolder) viewHolder;
                final int id = view.getId();
                if (id == R.id.toggle) {
                    toggleContent(view, holder.getView(R.id.admin_content));

                }
            }

            @Override
            public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {
                final QuestionHeaderParentItem bean = (QuestionHeaderParentItem) data;
                ParentViewHolder holder = (ParentViewHolder) viewHolder;
                TextView tv_nickname = (TextView) holder.getView(R.id.tv_nickname);
                TextView tv_date = (TextView) holder.getView(R.id.tv_date);
                ImageView iv_head_portrait = (ImageView) holder.getView(R.id.iv_head_portrait);
                iv_head_portrait.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
                        intent.putExtra(Constant.KEY_ID,bean.getUser_id());
                        startActivity(intent);
                    }
                });
                TextView tv_title = (TextView) holder.getView(R.id.tv_title);
                TextView tv_content = (TextView) holder.getView(R.id.tv_content);
                tv_nickname.setText(bean.getUsername());
                tv_date.setText(Utils.getReleaseTime(new Date(Utils.isLong(bean.getCreate_time())*1000)));
                tv_title.setText(bean.getTitle());
                tv_content.setText(bean.getContent());
                ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS,bean.getUser_pic(),iv_head_portrait,ImageDisplayUtil.DISPLAY_HEADER);

                TextView tv_comment_number = (TextView) holder.getView(R.id.tv_comment_number);
                tv_comment_number.setText(getString(R.string.comment,bean.getComment_number()));
                //图片
                GridView gridView = (GridView) holder.getView(R.id.gridView);
                String pic = bean.getPic();
                if(!Utils.isEmpty(pic)){
                    gridView.setVisibility(View.VISIBLE);
                    final ArrayList<String> imgList =new ArrayList<>();
                    String imgs[]= pic.split(",");
                    for(int i=0;i<imgs.length;i++){
                        imgList.add(imgs[i]);
                    }
                    gridView.setAdapter(new CommonAdapter<String>(getActivity(),imgList,R.layout.item_img) {
                        @Override
                        public void convert(ViewHolder helper, String item) {
                            int width =(Utils.getScreenWidth(getActivity())-getResources().getDimensionPixelSize(R.dimen.view_margin_24dp))/3;
                            ImageView imgView =helper.getView(R.id.imageView);
                            ViewGroup.LayoutParams layoutParams =imgView.getLayoutParams();
                            layoutParams.height = width;
                            layoutParams.width = width;
                            imgView.setLayoutParams(layoutParams);
                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, item, imgView, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                        }
                    });
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startPhotoViewIntent(position,imgList);
                        }
                    });

                } else{
                    gridView.setVisibility(View.GONE);
                }

                //官方回复，根据admin_content判断显示
                String adminContent = bean.getAdmin_content();
                FrameLayout admin_layout = (FrameLayout) holder.getView(R.id.admin_layout);
                if(!Utils.isEmpty(adminContent)){
                    admin_layout.setVisibility(View.VISIBLE);
                    ImageView admin_img =(ImageView) holder.getView(R.id.admin_img);
                    TextView admin_name = (TextView) holder.getView(R.id.admin_name);
                    TextView admin_date = (TextView) holder.getView(R.id.admin_date);
                    TextView admin_content = (TextView) holder.getView(R.id.admin_content);

                    ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, bean.getAdmin_pic(), admin_img, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                    admin_date.setText(Utils.getReleaseTime(new Date(Utils.isLong(bean.getAdmin_time())*1000)));
                    admin_name.setText(bean.getAdmin_name());
                    admin_content.setText(adminContent);


                }else{
                    admin_layout.setVisibility(View.GONE);
                }
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentLoader.setCommentAvailableAdapter(adapter);


//        mLikeBox = (MyCheckBox) rootView.findViewById(R.id.like);
//        mLikeBox.setOnClickListener(this);
//        View comment = rootView.findViewById(R.id.tv_comment);
//        comment.setOnClickListener(this);
    }

    private void toggleContent(View title, View content) {
        content.setVisibility(content.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        title.setSelected(content.getVisibility() == View.VISIBLE);
    }


    @Override
    public void loadData() {
        getQuestionDetail();
        loadComments();
    }
    private void loadComments() {
        //加载评论处理丢给他处理
        mCommentLoader.loadComments();
    }

    private void getQuestionDetail(){
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_QUESTION);
        JSONUtil.put(params,"action",Constant.ACTION_QUESTION_DETAIL);
        JSONUtil.put(params,"id",mId);
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, TAG, new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                eventHeader = JSONUtil.fromJson(jsonString,
                        QuestionHeaderParentItem.class);
                LogUtil.LogE(TAG,"onSuccess=>"+eventHeader.toString());
                //头部数据加载完成设置给评论类
                mCommentLoader.addHeaderData(eventHeader);
                setHasOptionsMenu(true);
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

    public void commentCard(final String comment) {
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_QUESTION);
        JSONUtil.put(params,"action",Constant.ACTION_QUESTION_COMMENT);
        JSONUtil.put(params,"id",mId);
        JSONUtil.put(params,"content",comment);
        JSONUtil.put(params,"token",PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        dataRequest(true, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
                showProgress(TAG);
            }

            @Override
            public void onSuccess(String mainJsonString, JSONObject mainJsonObject, JSONObject rootJsonObject) {
                CommentItem commentItem=new CommentItem();
                final String id=String.valueOf(JSONUtil.getInt(mainJsonObject,"id"));
                LogUtil.LogE(TAG,"id===========>"+id);
                commentItem.setContent(comment);
                commentItem.setId(id);
                UserInfoBean userInfoVO = MyApplication.getUserInfo(getActivity());
                commentItem.setUser_pic(userInfoVO.getUser_pic());
                commentItem.setUsername(userInfoVO.getUsername());
                commentItem.setCreate_time(OtherUtil.getTimeSecs());
                commentItem.setCanDelete(true);
                mCommentLoader.getAdapter().addParent(1,commentItem);

            }

            @Override
            public void onFail(int key,String message) {
               showTost("评论失败，请重试",1);
            }

            @Override
            public void requestAfter() {
                dismissProgress();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.more,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_more) {
            OtherUtil.showMoreOperate(CommQADetailFragment.this, mId, eventHeader.getTitle(), eventHeader.getContent(), eventHeader.getUser_id(),
                    Constant.SHARE_QUESTION, Constant.REPORT_QUESTION, Constant.DELETE_QUESTION,
                    true, null, -1, (ExpandableRecyclerViewAdapter) mRecyclerView.getAdapter());

        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_send) {
            String comment = et_comment.getText().toString();
            if (Utils.isEmpty(comment)) {
                showTost("请输入回复内容", 1);
            } else {
//                    commentCard(comment);
                JSONObject params = OtherUtil.getJSONObject(getActivity(),
                        Constant.MODEL_QUESTION, Constant
                                .ACTION_QUESTION_COMMENT, true);
                JSONUtil.put(params, "id", mId);
                JSONUtil.put(params, "content", comment);
                mCommentLoader.comment(params);
                et_comment.setText("");
            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
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
        super.onActivityResult(requestCode, resultCode, data);
    }
}
