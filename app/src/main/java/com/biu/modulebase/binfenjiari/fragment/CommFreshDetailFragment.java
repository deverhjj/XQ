package com.biu.modulebase.binfenjiari.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.binfenjiari.activity.ReportActivity;
import com.biu.modulebase.binfenjiari.adapter.CommentAvailableAdapter;
import com.biu.modulebase.binfenjiari.adapter.CommentLoader;
import com.biu.modulebase.binfenjiari.adapter.CommonAdapter;
import com.biu.modulebase.binfenjiari.adapter.ViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.NewsHeaderParentItem;
import com.biu.modulebase.binfenjiari.model.ReplyItem;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.KickBackAnimator;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
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
 * Created by jhj_Plus on 2016/5/24.
 */
public class CommFreshDetailFragment extends BaseFragment {
    private static final String TAG = "CommFreshDetailFragment";

    private RecyclerView mRecyclerView;

    private LSwipeRefreshLayout mRefreshLayout;
    private CommentLoader mCommentLoader;

    private NewsHeaderParentItem eventHeader;
    private String mId="-1";
    private int mType;

    private CheckBox like;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt(Constant.KEY_TYPE);
        }
    }

    public static CommFreshDetailFragment newInstance(int type) {
        Bundle args=new Bundle();
        args.putInt(Constant.KEY_TYPE,type);
        CommFreshDetailFragment fragment=new CommFreshDetailFragment();
        fragment.setArguments(args);
        return fragment;
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
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_comm_fresh_detail,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
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

        args.put(Constant.KEY_MODEL, Constant.MODEL_NEWS);
        args.put(Constant.KEY_ACTION, Constant.ACTION_NEWS_COMMENT_REPLY_LIST);

        //回复主评论或回复 的 action 和 model
        args.put(Constant.KEY_MODEL_COMMENT_DETAIL,Constant.MODEL_NEWS);
        args.put(Constant.KEY_ACTION_COMMENT_DETAIL,Constant.ACTION_NEWS_COMMENT_REPLY);

        //删除评论 的 action 和 model
        args.put(Constant.KEY_MODEL_DELETE_COMMENT_COMMENT_DETAIL,Constant.MODEL_NEWS);
        args.put(Constant.KEY_ACTION_DELETE_COMMENT_COMMENT_DETAIL,Constant
                .ACTION_NEWS_COMMENT_DELETE);

        //删除回复 的 action 和 model
        args.put(Constant.KEY_MODEL_DELETE_REPLY_COMMENT_DETAIL,Constant.MODEL_NEWS);
        args.put(Constant.KEY_ACTION_DELETE_REPLY_COMMENT_DETAIL,Constant.ACTION_NEWS_COMMENT_REPLY_DELETE);


        args.put(Constant.KEY_NAME_ARGS, "newsId");
        args.put(Constant.KET_VALUE_ARGS, mId);

        //评论举报类型
        args.put(Constant.KEY_REPORT_TYPE,Constant.REPORT_NEWS_COMMENT);

        //评论基础参数设置
        JSONObject params = OtherUtil.getJSONObject(getActivity(), Constant.MODEL_NEWS,
                Constant.ACTION_NEWS_COMMENT_LIST, false);
        JSONUtil.put(params, "id", mId);
        //评论处理类(加载，回复，删除 评论)
        mCommentLoader = new CommentLoader(CommentLoader.TYPE_LIST,this , params,args);

        CommentAvailableAdapter adapter=new CommentAvailableAdapter(this,mCommentLoader ,args ,new
                ArrayList<ParentListItem>(),
                R.layout.header_card_detail2, new ViewHolderCallbacks() {
            @Override
            public int[] getNeedRegisterClickListenerChildViewIds() {
                return new int[0];
            }

            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder, View view, int position,
                    int adapterPosition, int parentPosition, int parentAdapterPosition)
            {

            }

            @Override
            public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {
                final NewsHeaderParentItem bean = (NewsHeaderParentItem) data;
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
                TextView tv_content = (TextView) holder.getView(R.id.tv_content);
                tv_nickname.setText(bean.getUsername());
                tv_date.setText(Utils.getReleaseTime(new Date(Utils.isLong(bean.getCreate_time())*1000)));
                tv_content.setText(bean.getContent());
                ImageDisplayUtil.displayImage(Constant.IMG_SOURCE,bean.getUser_pic(),iv_head_portrait,ImageDisplayUtil.DISPLAY_HEADER);
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
//                            helper.setNetImage(Constant.IMG_COMPRESS,R.id.imageView,item,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
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
                like.setText(bean.getLike_number());
                if(bean.getLike_status().equals("2")){
                    like.setChecked(false);
                }else{
                    like.setChecked(true);
                }

            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentLoader.setCommentAvailableAdapter(adapter);


        like = (CheckBox) rootView.findViewById(R.id.like);
        like.setOnClickListener(this);
        View comment = rootView.findViewById(R.id.tv_comment);
        comment.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        getNewsDetail();
        loadComments();
    }

    private void loadComments() {
        //加载评论处理丢给他处理
        mCommentLoader.loadComments();
    }

    private void getNewsDetail(){
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_NEWS);
        JSONUtil.put(params,"action",Constant.ACTION_NEWS_DETAIL);
        JSONUtil.put(params,"id",mId);
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, TAG, new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                inVisibleLoading();
                eventHeader = JSONUtil.fromJson(jsonString,
                        NewsHeaderParentItem.class);
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

    private void showShareDialog() {
        DialogFactory.showDialog(getActivity(), R.layout.dialog_share, R.style.CustomDialog,
                R.style.popwin_anim_style, Gravity.BOTTOM, 1, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        LinearLayout line1 = (LinearLayout) v.findViewById(R.id.line1);
                        LinearLayout line2 = (LinearLayout) v.findViewById(R.id.line2);
                        for (int i = 0; i < line1.getChildCount(); i++) {
                              final View child = line1.getChildAt(i);
                              new Handler().postDelayed(new Runnable() {
                                  @Override
                                  public void run() {
                                      ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
                                              "translationY", 200, 0);
                                      fadeAnim.setDuration(800);
                                      KickBackAnimator kickAnimator = new KickBackAnimator();
                                      kickAnimator.setDuration(500);
                                      fadeAnim.setEvaluator(kickAnimator);
                                      fadeAnim.start();
                                  }
                              },i*10);
                        }
                        for (int i = 0; i < line2.getChildCount(); i++) {
                            final View child = line2.getChildAt(i);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
                                            "translationY", 200, 0);
                                    fadeAnim.setDuration(800);
                                    KickBackAnimator kickAnimator = new KickBackAnimator();
                                    kickAnimator.setDuration(500);
                                    fadeAnim.setEvaluator(kickAnimator);
                                    fadeAnim.start();
                                }
                            },i*10);
                        }
                        v.findViewById(R.id.btn_cancel).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.report).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(getActivity(), ReportActivity.class);
                                getActivity().startActivity(intent);
                            }
                        });
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
            OtherUtil.showMoreOperate(this, mId, eventHeader.getContent(), eventHeader.getContent(), eventHeader.getUser_id(), Constant.SHARE_NEWS,
                    Constant.REPORT_NEWS, Constant.DELETE_NEWS, true, null, -1,
                    (ExpandableRecyclerViewAdapter) mRecyclerView.getAdapter());

        } else {
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_comment) {
            JSONObject params = OtherUtil.getJSONObject(getActivity(),
                    Constant.MODEL_NEWS, Constant
                            .ACTION_NEWS_COMMENT, true);
            JSONUtil.put(params, "id", mId);
            mCommentLoader.doComment(params, -1);

        } else if (i == R.id.like) {
            JSONObject object = new JSONObject();
            JSONUtil.put(object, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
            JSONUtil.put(object, "id", mId);
            JSONUtil.put(object, Constant.KEY_MODEL, Constant.MODEL_NEWS);
            JSONUtil.put(object, Constant.KEY_ACTION, Constant.ACTION_NEWS_LIKE);
            OtherUtil.like(CommFreshDetailFragment.this, like, object, null);

        } else {
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
