package com.biu.modulebase.binfenjiari.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.CommentDeleteFragment;
import com.biu.modulebase.binfenjiari.model.BaseHeaderParentItem;
import com.biu.modulebase.binfenjiari.model.Comment;
import com.biu.modulebase.binfenjiari.model.CommentItem;
import com.biu.modulebase.binfenjiari.model.Reply;
import com.biu.modulebase.binfenjiari.model.ReplyDetailItem;
import com.biu.modulebase.binfenjiari.model.ReplyItem;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.adapter.BaseAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/2.
 */
public class CommentLoader {

    private static final String TAG = "CommentLoader";

    public static final int TYPE_LIST = 0;
    public static final int TYPE_DETAIL = 1;

    public static final int TYPE_DELETE = 2;
    public static final int TYPE_ADD = 3;

    private BaseFragment mBaseFragment;
    private LSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private CommentAvailableAdapter mCommentAvailableAdapter;
    private BaseAdapter mBaseAdapter;

    private int mType=-1;
    private JSONObject mInitParams;
    private HashMap<String,Object> mArgs;

    private int mPageNum = 1;
    private int mAddTotalCount = 0;
    private boolean mFirstLoad=true;
    private boolean mRefreshData = true;
    private boolean mHeaderLoaded=false;
    private boolean mCommentsLoaded=false;

    private Comment mComments=null;
    private Reply mReply=null;

    /**
     * @deprecated
     */
    public CommentLoader(BaseFragment baseFragment, LSwipeRefreshLayout refreshLayout,
            RecyclerView recyclerView, JSONObject initParams)
    {
        mType=TYPE_LIST;

        mBaseFragment = baseFragment;

        mRefreshLayout = refreshLayout;
        mRecyclerView = recyclerView;
        mCommentAvailableAdapter = (CommentAvailableAdapter) recyclerView.getAdapter();

        mInitParams=initParams;

        init();
    }

    /**
     * @param type 评论页类型 评论列表 或 评论详情
     * @param baseFragment 使用基于 BaseFragment 里的方法，请求数据等等
     * @param initParams 评论数据请求时的基本参数
     * @param args 本地需要的一些 参数
     */
    public CommentLoader(int type,BaseFragment baseFragment, JSONObject initParams,
            HashMap<String, Object> args)
    {

        mType = type;

        mBaseFragment = baseFragment;

        View rootView=baseFragment.getView();
        if (rootView==null) return;

        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (type == TYPE_LIST && adapter instanceof CommentAvailableAdapter) {
            mCommentAvailableAdapter = (CommentAvailableAdapter) adapter;
        } else if (type == TYPE_DETAIL && adapter instanceof BaseAdapter) {
            mBaseAdapter = (BaseAdapter) adapter;
        }

        mInitParams = initParams;

        mArgs = args;

        init();
    }

    public void setCommentAvailableAdapter(CommentAvailableAdapter commentAvailableAdapter)
    {
        mCommentAvailableAdapter = commentAvailableAdapter;
    }

    public void setBaseAdapter(BaseAdapter baseAdapter) {
        mBaseAdapter = baseAdapter;
    }

    public boolean isCommentsLoaded() {
        return mCommentsLoaded;
    }

    private void init() {
        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG, "onRefresh******************");
                refreshData();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG, "onLoadMore******************");
                loadMoreData();
            }
        });
    }


    public void reset() {
        mRefreshData = true;
        mHeaderLoaded =false;
        mCommentsLoaded=false;
        mComments = null;
        mReply=null;
        mPageNum = 1;
        mAddTotalCount = 0;
        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
    }

    private void refreshData() {
        reset();
        if (mType == TYPE_LIST) {
            mBaseFragment.loadData();
//            loadComments();
        } else if (mType == TYPE_DETAIL) {
            loadReplies();
        }
    }

    private void loadMoreData() {
        if (mRefreshData) {
            mRefreshData = false;
        }
        mPageNum++;
        if (mType == TYPE_LIST) {
            loadComments();
        } else if (mType == TYPE_DETAIL) {
            loadReplies();
        }
    }


    //------------------------------------------处理头部数据 Start-------------------------------------

    public void addHeaderData(ParentListItem headerData) {
//        mCommentAvailableAdapter.addParent(0, headerData);
        mCommentAvailableAdapter.addHeader(headerData);
        mHeaderLoaded = true;
        addCommentsData();
    }

    public ParentListItem getHeaderData() {
        return mCommentAvailableAdapter.getParent(0);
    }

    public void addHeaderData(BaseAdapter.AddType addType, Object headerData) {
        mBaseAdapter.addData(addType, headerData);
    }

    //------------------------------------------处理头部数据 End-------------------------------------



    //------------------------------------------加载评论数据 Start-------------------------------------

    /** 请求服务器评论数据
     * @return 是否加载成功
     */
    public boolean loadComments()
    {
        final boolean[] result=new boolean[1];

        final long time =
                mPageNum == 1 || mComments == null ? OtherUtil.getTimeSecs() : mComments.getTime();

        final JSONObject params = mInitParams;
        JSONUtil.putInt(params, "pageNum", mPageNum);
        JSONUtil.putlong(params, "time", time);
        mBaseFragment.dataRequest(false, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {

            }

            @Override
            public void onSuccess(String mainJsonString,JSONObject mainJsonObject,JSONObject rootJsonObject) {

                mComments = JSONUtil.fromJson(mainJsonString, Comment.class);

                if (mComments==null) { result[0]=false; return; }

                final int allPageNumber = mComments.getAllPageNumber();

                final boolean canLoadMore = mPageNum < allPageNumber;

                result[0] = true;

                mAddTotalCount += addCommentsData();

                //添加评论数据
                final int addTotalCount=mAddTotalCount;

                //如果当前评论加载页能显示的数据量小于5个并且当前可以上拉加载更多，那么程序自动加载下一页数据
                //如果加载下一页数据失败，那么停止自动加载更多操作，用户手动加载更多
                if (addTotalCount < 5 && canLoadMore) {
                    loadMoreData();
                } else {
                    mAddTotalCount = 0;
                }

                if (mRefreshLayout != null) {
                    //判断是否下次还可以上拉加载更多
                    if (!canLoadMore) {
                        LogUtil.LogE(TAG, "stop load more");
                        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
                    }
                }

                if (!mCommentsLoaded) {
                   mCommentsLoaded=true;
                }

            }

            @Override
            public void onFail(int key,String message) {

                result[0] = false;

                mAddTotalCount = 0;

                if (key != RequestCallBack2.KEY_FAIL) {
                    OtherUtil.showToast(mBaseFragment.getActivity(), message);
                }else {
                    OtherUtil.showToast(mBaseFragment.getActivity(),"没有更多评论了");
                }

                if (!mRefreshData) {
                    mPageNum--;
                }
            }

            @Override
            public void requestAfter() {
                mRefreshLayout.setLoading(false);
                mRefreshLayout.setRefreshing(false);
            }
        });

        return result[0];
    }

    /** 添加评论数据并通知刷新界面
     * @return 添加了多少条数据
     */
    private int addCommentsData() {

        if (!mHeaderLoaded || mComments == null) {
            return 0;
        }

        //移除旧的评论
        if (mRefreshData) {
            mCommentAvailableAdapter.removeData(1, mCommentAvailableAdapter.getParentCount() - 1);
        }

        List<CommentItem> comments=mComments.getCommentList();

        if (comments==null) {
            return 0;
        }

        final int commentCount=comments.size();

        for (int i = 0; i < commentCount; i++) {

            CommentItem comment=comments.get(i);

            if (comment == null) continue;

            // 1.添加，2.剔除
            final int status=comment.getStatus();

            if (status==2) {
                comments.remove(i);continue;
            }

            //是否可以删除该条评论
            comment.setCanDelete(
                    OtherUtil.isAuthor(mBaseFragment.getActivity(), comment.getUser_id()));

            List<ReplyItem> replies = comment.getReplyList();
            if (replies != null) {
                final int replyCount = replies.size();
                final boolean hasMore = comment.getIs_more() == 1;
                if (replyCount > 0) {
                    comment.setHasChildComment(true);
                    if (replyCount > 5 || hasMore) {
                        comment.setHasMoreChildComment(true);
                    }
                }
            }
        }

        //添加新的评论
        mCommentAvailableAdapter.addAllData(comments);
        mRecyclerView.scrollToPosition(0);
        return comments.size();

    }

    //------------------------------------------加载评论数据 End-----------------------------------------



    //------------------------------------------加载回复数据 Start---------------------------------------
    /** 请求服务器回复数据
     */
    public void loadReplies() {

        long time = mReply == null || mPageNum == 1 ? OtherUtil.getTimeSecs() : mReply.getTime();

//        JSONObject params = OtherUtil.getJSONObject(mBaseFragment.getActivity(), mArgs.get
//                (Constant.KEY_MODEL),
//                mArgs.get(Constant.KEY_ACTION), false);
        JSONUtil.put(mInitParams,"id",mArgs.get(Constant.KEY_ID));
        JSONUtil.put(mInitParams,"time",time);
        JSONUtil.putInt(mInitParams,"pageNum",mPageNum);

        mBaseFragment.dataRequest(false, mInitParams, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
                if (mFirstLoad) {
                    mBaseFragment.visibleLoading();
                }
            }

            @Override
            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
                    JSONObject rootJsonObject)
            {
                mReply=JSONUtil.fromJson(mainJsonString,Reply.class);

                if (mReply==null) return;

                final int allPageNumber = mReply.getAllPageNumber();

                final boolean canLoadMore = mPageNum < allPageNumber;

                mAddTotalCount += addRepliesData();

                //添加评论数据
                final int addTotalCount=mAddTotalCount;

                //如果当前评论加载页能显示的数据量小于5个并且当前可以上拉加载更多，那么程序自动加载下一页数据
                //如果加载下一页数据失败，那么停止自动加载更多操作，用户手动加载更多
                if (addTotalCount < 5 && canLoadMore) {
                    loadMoreData();
                } else {
                    mAddTotalCount = 0;
                }

                if (mRefreshLayout != null && mReply != null) {
                    //判断是否下次还可以上拉加载更多
                    if (!canLoadMore) {
                        LogUtil.LogE(TAG, "stop load more");
                        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
                    }
                }

                if (!mCommentsLoaded) {
                    mCommentsLoaded=true;
                }

            }

            @Override
            public void onFail(int key, String message) {

                mAddTotalCount = 0;

                if (key == RequestCallBack2.KEY_FAIL) {
                    mBaseFragment.visibleNoNetWork();
                } else {
                    OtherUtil.showToast(mBaseFragment.getActivity(),"没有更多回复了");
                }

                LogUtil.LogE(TAG, "onFail*");

                //如果上拉时出现错误还原请求时页号
                if (!mRefreshData) {
                    mPageNum--;
                }
            }

            @Override
            public void requestAfter() {
                if (mFirstLoad) {
                    mBaseFragment.inVisibleLoading();
                    mFirstLoad=false;
                }

                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);
            }
        });
    }

    /** 添加回复数据并通知刷新界面
     * @return 添加了多少条数据
     */
    private int addRepliesData() {

        if (mReply==null) return 0;

        final BaseAdapter adapter = mBaseAdapter;

        if (mRefreshData) {
            adapter.removeData(1, adapter.getItemCount() - 1);
        }

        List<ReplyDetailItem> replyList=mReply.getList();

        if (replyList==null) return 0;

        final int count=replyList.size();

        for (int i = 0; i < count; i++) {

            ReplyDetailItem replyItem = replyList.get(i);

            if (replyItem == null) continue;

            // 1.添加，2.剔除
            final int status=replyItem.getStatus();

            if (status==2) {
                replyList.remove(i);continue;
            }

            replyItem.setCanDelete(OtherUtil.isAuthor(mBaseFragment.getActivity(), replyItem
                    .getUser_id()));

        }

        adapter.addData(BaseAdapter.AddType.LASE, replyList);

        return replyList.size();
    }

    //------------------------------------------加载回复数据 End-----------------------------------------


    public void doComment(final JSONObject initParams, int parentPosition)
    {

        //这里先检查是否登录再是否显示评论窗口，而不是评论之后再检查
        if (!OtherUtil.hasLogin(mBaseFragment.getActivity())) {
            mBaseFragment.showUnLoginSnackbar();
            return;
        }

        DialogFactory.showDialog(mBaseFragment.getActivity(), R.layout.part_action_footer_5, R.style
                        .Theme_Dialog_Reply,
                -1, Gravity.BOTTOM, 1.0f, 0, new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                mBaseFragment.showSoftKeyboard();
                            }
                        });

                        final EditText et_comment= (EditText) v.findViewById(R.id.et_comment);
                        final Button btn_send= (Button) v.findViewById(R.id.btn_send);
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

                        btn_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String comment = et_comment.getText().toString();
                                JSONUtil.put(initParams,"content",comment);
                                comment(initParams);
                                dialog.dismiss();
                            }
                        });

                    }
                });
    }


    public void doReply(final boolean replyParent,int position,final JSONObject initParams,
            final Callback callback)
    {
        //这里先检查是否登录再是否显示评论窗口，而不是评论之后再检查
        if (!OtherUtil.hasLogin(mBaseFragment.getActivity())) {
            mBaseFragment.showUnLoginSnackbar();
            return;
        }

        ReplyDetailItem replyItem=null;

        if (!replyParent) {

            replyItem= (ReplyDetailItem) mBaseAdapter.getData(position);

            //回复Id
            JSONUtil.put(initParams, "reply_id", replyItem.getId());

            LogUtil.LogE(TAG, "to_user_id===========>" +  replyItem.getUser_id());

            //回复项的人的Id
            JSONUtil.put(initParams, "to_user_id", replyItem.getUser_id());

        }

        final ReplyDetailItem finalReplyItem = replyItem;
        mBaseFragment.dataRequest(true, initParams, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
                mBaseFragment.showProgress(TAG);
            }

            @Override
            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
                    JSONObject rootJsonObject)
            {
                final UserInfoBean userInfo= MyApplication.getUserInfo(mBaseFragment.getActivity());

                if (userInfo==null) { OtherUtil.showToast(mBaseFragment.getActivity(),"评论失败，无法获取用户信息");
                    return;}

                //回复内容
                final String comment = JSONUtil.getString(initParams,"content");

                ReplyDetailItem reply = new ReplyDetailItem();
                final String id = JSONUtil.getString(mainJsonObject, "id");
                LogUtil.LogE(TAG, "id===========>" + id);

                reply.setId(id);
                reply.setCreate_time(OtherUtil.getTimeSecs());
                reply.setUser_pic(userInfo.getUser_pic());
                reply.setUsername(userInfo.getUsername());
                reply.setUser_id(userInfo.getId());
                reply.setTo_user_id(null);
                reply.setTo_name(null);
                reply.setContent(comment);
                reply.setCanDelete(true);

                if (!replyParent) {
                    LogUtil.LogE(TAG,
                            "finalReplyItem.getUsername()==>" + finalReplyItem.getUsername() +
                                    "finalReplyItem.getTo_user_id()" +
                                    finalReplyItem.getTo_user_id());
                    reply.setTo_name(finalReplyItem.getUsername());
                    reply.setTo_user_id(finalReplyItem.getUser_id());
                }

                final int allPageNum=mReply.getAllPageNumber();
                LogUtil.LogE(TAG,"doReply Success===>"+"allPageNum="+allPageNum+",,," +
                        "currentPageNum==>"+mPageNum);
                if (mPageNum >= allPageNum) {
                    mBaseAdapter.addData(BaseAdapter.AddType.LASE,reply);
                }

                if (callback!=null) {
                   callback.onRequestFinished(true);
                }
            }

            @Override
            public void onFail(int key, String msg) {
                if (key != RequestCallBack2.KEY_FAIL) {
                    OtherUtil.showToast(mBaseFragment.getActivity(), msg);
                }
                if (callback!=null) {
                    callback.onRequestFinished(false);
                }
            }

            @Override
            public void requestAfter() {
                mBaseFragment.dismissProgress();
            }
        });
    }



    /**
     *  评论
     * @param params 服务器请求参数
     */
    public void comment(final JSONObject params) {

        mBaseFragment.dataRequest(true, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
                mBaseFragment.showProgress(TAG);
            }

            @Override
            public void onSuccess(String mainJsonString,JSONObject mainJsonObject,JSONObject rootJsonObject) {
                mBaseFragment.showTost("评论成功",0);
                CommentItem commentItem = new CommentItem();
                final String comment = JSONUtil.getString(params, "content");
                final String id = JSONUtil.getString(mainJsonObject, "id");
                final UserInfoBean userInfoVO = MyApplication.getUserInfo(
                        mBaseFragment.getActivity());
                commentItem.setContent(comment);
                commentItem.setId(id);
                commentItem.setUser_id(userInfoVO.getId());
                commentItem.setUser_pic(userInfoVO.getUser_pic());
                commentItem.setUsername(userInfoVO.getUsername());
                commentItem.setCreate_time(OtherUtil.getTimeSecs());
                commentItem.setCanDelete(true);
                mCommentAvailableAdapter.addParent(1, commentItem);

                notifyCommentReplyDataChanged(TYPE_LIST,TYPE_ADD ,1,-1, null);

            }

            @Override
            public void onFail(int key, String message) {
                if (key != RequestCallBack2.KEY_FAIL) {
                    OtherUtil.showToast(mBaseFragment.getActivity(), message);
                }
            }

            @Override
            public void requestAfter() {
                mBaseFragment.dismissProgress();
            }
        });
    }



    /**
     * 回复主评论或子评论
     * @param params
     */
    public void reply(final JSONObject params) {

        mBaseFragment.dataRequest(true, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
                mBaseFragment.showProgress(TAG);
            }

            @Override
            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
                    JSONObject rootJsonObject)
            {
                final UserInfoBean userInfo=MyApplication.getUserInfo(mBaseFragment.getActivity());
                final String comment = JSONUtil.getString(params, "content");

                ReplyDetailItem replyItem = new ReplyDetailItem();
                final String id = JSONUtil.getString(mainJsonObject, "id");
                LogUtil.LogE(TAG, "id===========>" + id);

                replyItem.setId(id);
                replyItem.setCreate_time(OtherUtil.getTimeSecs());
                replyItem.setUser_pic(userInfo.getUser_pic());
                replyItem.setUsername(userInfo.getUsername());
                replyItem.setContent(comment);


                //              mCommentAvailableAdapter.addChild(,replyItem);
            }

            @Override
            public void onFail(int key, String msg) {
                if (key != RequestCallBack2.KEY_FAIL) {
                    OtherUtil.showToast(mBaseFragment.getActivity(), msg);
                }
            }

            @Override
            public void requestAfter() {
                mBaseFragment.dismissProgress();
            }
        });
    }


    public void deleteComment(final int type, final JSONObject params, final Callback callback)
    {
        mBaseFragment.dataRequest(true, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
            @Override
            public void requestBefore() {
                mBaseFragment.showProgress(TAG);
            }

            @Override
            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
                    JSONObject rootJsonObject)
            {
                if (callback!=null) {
                    callback.onRequestFinished(true);
                }

                if (type == TYPE_LIST && mCommentAvailableAdapter != null) {
                   int deletedCount= mCommentAvailableAdapter.removeParent(
                            JSONUtil.getInt(params, Constant.KEY_PARENT_POSITION));
                    notifyCommentReplyDataChanged(TYPE_LIST, TYPE_DELETE, deletedCount,-1,null);
                } else if (type == TYPE_DETAIL && mBaseAdapter != null) {
                    boolean isDeleteAll = JSONUtil.getBoolean(params, Constant.KET_IS_DELETE_ALL);
                    if (isDeleteAll) {
                        //删除自己的主评论后自动退出当前界面
                        mBaseAdapter.removeAllData();
                        mBaseFragment.getActivity().finish();
                    } else {
                        mBaseAdapter.removeData(
                                JSONUtil.getInt(params, Constant.KEY_CHILD_POSITION));
                    }

                }

                OtherUtil.showToast(mBaseFragment.getActivity(), "内容已删除");
            }

            @Override
            public void onFail(int key, String message) {
                if (key != RequestCallBack2.KEY_FAIL) {
                    OtherUtil.showToast(mBaseFragment.getActivity(), message);
                }
                if (callback!=null) {
                    callback.onRequestFinished(false);
                }
            }

            @Override
            public void requestAfter() {
                mBaseFragment.dismissProgress();
            }
        });
    }

    /**
     * 删除评论或回复
     * @param callbacks
     */
    public void doDeleteComment(CommentDeleteFragment.Callbacks callbacks)
    {
        //这里先检查是否登录再是否显示评论窗口，而不是评论之后再检查
        if (!OtherUtil.hasLogin(mBaseFragment.getActivity())) {
            mBaseFragment.showUnLoginSnackbar();
            return;
        }

        CommentDeleteFragment fragment =
                (CommentDeleteFragment) mBaseFragment.getActivity().getSupportFragmentManager()
                        .findFragmentByTag(CommentDeleteFragment.TAG);
        if (fragment == null) {
            fragment = new CommentDeleteFragment();
        }

        fragment.setCallbacks(callbacks);
        fragment.show(mBaseFragment.getActivity().getSupportFragmentManager(),
                CommentDeleteFragment.TAG);
    }




    /**
     *  由评论详情返回到 主评论页面时调用通知刷新主评论页面的评论数据，如果有删除的回复的话
     *  如果 childPosition 数组为 null 或 空 表明删除整个主评论+回复，否则删除所属主评论的指定的那些回复
     *  @param  type 该操作所在的界面的类型 ，主评论列表 或者 主评论详情 类型
     *  @param operateType 该操作的类型 删除 还是 添加
     *  @param changedCount 添加/删除 评论/回复 的数量
     *  @param parentPosition
     * @param replyDetailItems
     */
    public void notifyCommentReplyDataChanged(int type, int operateType, int changedCount,
            int parentPosition,ArrayList<ReplyItem> replyDetailItems)
    {
        if (type == TYPE_LIST) {
            ParentListItem parentListItem=getHeaderData();
            if (parentListItem instanceof BaseHeaderParentItem) {
                BaseHeaderParentItem baseHeaderParentItem= (BaseHeaderParentItem) parentListItem;
                final int currentNum=Integer.valueOf(baseHeaderParentItem.getComment_number());
                int newNum=Integer.valueOf(baseHeaderParentItem.getComment_number());
                if (operateType==TYPE_ADD) {
                    newNum = currentNum + changedCount;
                } else if (operateType==TYPE_DELETE) {
                    newNum = currentNum - changedCount;
                }
                if (newNum==currentNum) return;
                baseHeaderParentItem.setComment_number(String.valueOf(newNum));
                mCommentAvailableAdapter.notifyParentItemChanged(0);
            }

        } else if (type == TYPE_DETAIL) {
            Intent result = new Intent();
            result.putExtra(Constant.KEY_PARENT_POSITION,parentPosition);
            result.putExtra(Constant.KEY_DATA, replyDetailItems);
            result.putExtra(Constant.KEY_COMMENT_OPREATE_TYPE,operateType);
            result.putExtra(Constant.KEY_COMMENT_CHANGED_COUNT,changedCount);

            mBaseFragment.getActivity().setResult(Activity.RESULT_OK, result);
        }
    }


    public CommentAvailableAdapter getAdapter() {
        return mCommentAvailableAdapter;
    }


    public void cancleRequest() {
        if (mBaseFragment != null) {
            mBaseFragment.cancelRequest(TAG);
        }
        reset();
    }


    /**
     * 删除，评论，回复 请求结束回调
     */
    public interface Callback {

        /**
         * @param success 是否请求成功
         */
        void onRequestFinished(boolean success);
    }


    public void deleteLocaleComment(int type, int commentPosition){
        if (type == TYPE_LIST) {
            mCommentAvailableAdapter.removeParent(commentPosition);
        }
    }

    public void deleteLocaleReplies(int type, int commentPosition) {
        if (type == TYPE_LIST) {
            mCommentAvailableAdapter.removeChildren(commentPosition);
        }
    }

    public void addLocalReplies(int type,int commentPosition ,List<ReplyItem> replies) {
        if (type == TYPE_LIST) {
            ParentListItem parentListItem=mCommentAvailableAdapter.getParent(commentPosition);
            CommentItem commentItem = null;
            if (parentListItem instanceof CommentItem) {
                commentItem= (CommentItem) parentListItem;
            }

            if (commentItem==null) return;

            boolean hasReplies=replies != null && replies.size() > 0;
            commentItem.setHasChildComment(hasReplies);
            mCommentAvailableAdapter.changeParent(commentPosition);
            if (!hasReplies) return;

            commentItem.setHasMoreChildComment(replies.size() > 5);

            List<ReplyItem> resultReplies =
                    replies.size() <= 5 ? replies : replies.subList(0, 5);
            mCommentAvailableAdapter.addChildren(commentPosition,resultReplies);
            //设置新的回复数据
            commentItem.setReplyNum(replies.size());
        }
    }

}
