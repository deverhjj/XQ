package com.biu.modulebase.binfenjiari.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommentDetailActivity;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.binfenjiari.activity.ReportActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.adapter.CommentLoader;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.CommentDeleteFragment;
import com.biu.modulebase.binfenjiari.model.CommentItem;
import com.biu.modulebase.binfenjiari.model.ReplyDetailItem;
import com.biu.modulebase.binfenjiari.model.ReplyItem;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.binfenjiari.widget.QuickReturnFooterBehavior;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by jhj_Plus on 2016/5/27.
 */
public class CommentDetailFragment2 extends BaseFragment implements CommentDetailActivity.Callback {
    private static final String TAG = "CommentDetailFragment";

    private CommentLoader mCommentLoader;

    private RecyclerView mRecyclerView;
    private ViewGroup mLayout_reply;
    private EditText mReplyEditText;

    private HashMap<String,Object> mArgs;
    private CommentItem mCommentItem;

    private boolean mCommentsChanged=false;
    private boolean mReplyParent = true;

    /**
     * 这里的初始数量因为 服务器那边传过来的 回复总数
     */
    private int mOrigCommentCount=0;
    /**
     * 这里是当前最新的评论数
     */
    private int mNewCommentCount = mOrigCommentCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        if (args != null) {
            mArgs= (HashMap<String, Object>) args.getSerializable(Constant.KEY_ARGS);
            mOrigCommentCount= mNewCommentCount=(int) mArgs.get(Constant.KEY_REPLY_COUNT);
            mCommentItem= (CommentItem) args.getSerializable(Constant.KEY_COMMENT);
        }
    }

    public static CommentDetailFragment2 newInstance(HashMap<String,Object> arguments,CommentItem
            commentItem)
    {
        Bundle args = new Bundle();

        args.putSerializable(Constant.KEY_ARGS,arguments);

        args.putSerializable(Constant.KEY_COMMENT,commentItem);

        CommentDetailFragment2 fragment = new CommentDetailFragment2();

        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_comment_detail,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter=new BaseAdapter(getActivity()) {

            public static final int TYPE_COMMENT_PARENT = 0;
            public static final int TYPE_COMMENT_CHILD = 1;

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, final int viewType) {
                final boolean parentComment=viewType == TYPE_COMMENT_PARENT;
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(viewType == TYPE_COMMENT_PARENT ? R.layout.item_card_detail_comment_detail_parent
                                : R.layout.item_card_detail_comment_detail_child, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {

                                if (parentComment && mCommentItem != null) {

                                    holder.setNetImage(Constant.IMG_SOURCE,R.id.iv_head_portrait,mCommentItem
                                            .getUser_pic(), ImageDisplayUtil.DISPLAY_HEADER);

                                    holder.setText(R.id.tv_nickname,mCommentItem.getUsername());

                                    holder.setText(R.id.tv_date, Utils.getReleaseTime(new Date
                                            (mCommentItem.getCreate_time()*1000)));

                                    holder.setText(R.id.tv_comment_parent,mCommentItem.getContent
                                            ());

                                } else if (data != null && data instanceof ReplyDetailItem) {

                                    ReplyDetailItem replyItem= (ReplyDetailItem) data;

                                    holder.setNetImage(Constant.IMG_SOURCE,R.id.iv_head_portrait,replyItem
                                            .getUser_pic(),ImageDisplayUtil.DISPLAY_HEADER);

                                    holder.setText(R.id.tv_nickname,replyItem.getUsername());

                                    holder.setText(R.id.tv_date, Utils.getReleaseTime(new Date
                                            (replyItem.getCreate_time()*1000)));

                                    final String toUserName=replyItem.getTo_name();

                                    LogUtil.LogE(TAG,"toUserName="+toUserName+",replyItem" +
                                            ".getTo_user_id()="+replyItem.getTo_user_id()+"," +
                                            "mCommentItem.getUser_id()="+mCommentItem.getUser_id());

                                    CharSequence content =
                                            !TextUtils.isEmpty(replyItem.getTo_user_id())
                                            ? Html.fromHtml(getString(R.string.form_reply_new_3,
                                            replyItem.getTo_name(), replyItem.getContent()))
                                            : replyItem.getContent();

                                    holder.setText(R.id.tv_comment_child, content);

                                    final boolean canDelete =replyItem.isCanDelete();
                                    View delete=holder.getView(R.id.tv_delete_child);
                                    delete.setVisibility(canDelete?View.VISIBLE:View.GONE);

                                    TextView tv_comment_child = (TextView) holder.getView(
                                            R.id.tv_comment_child);

                                    tv_comment_child.setPadding(0, 0, 0, canDelete ? 0
                                            : getResources().getDimensionPixelSize(
                                                    R.dimen.padding_bottom_8dp));
                                }
                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view,
                                    final int position)
                            {
                                if (position==RecyclerView.NO_POSITION) return;

                                final int id=view.getId();

                                BaseAdapter adapter= (BaseAdapter) mRecyclerView.getAdapter();

                                ReplyDetailItem replyItem= (ReplyDetailItem) adapter.getData
                                        (position);

                                if(id ==R.id.iv_head_portrait){
                                    if(parentComment){
                                        Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
                                        intent.putExtra(Constant.KEY_ID,mCommentItem.getUser_id());
                                        startActivity(intent);
                                    }else{
                                        Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
                                        intent.putExtra(Constant.KEY_ID,replyItem.getUser_id());
                                        startActivity(intent);
                                    }
                                }

                                if (id==R.id.ib_more) {

                                    showDialog(mCommentItem.getId());

                                } else if (id == R.id.ib_comment ||
                                        id == R.id.layout_comment_detail_child)
                                {

                                    //这里先检查是否登录再是否显示评论窗口，而不是评论之后再检查
                                    if (!OtherUtil.hasLogin(getActivity())) {
                                        showUnLoginSnackbar();
                                        return;
                                    }

                                    CoordinatorLayout.LayoutParams layoutParams =
                                            (CoordinatorLayout.LayoutParams) mLayout_reply
                                                    .getLayoutParams();
                                    QuickReturnFooterBehavior behavior =
                                            (QuickReturnFooterBehavior) layoutParams.getBehavior();
                                    behavior.quickShow(mLayout_reply);

                                    mReplyEditText.requestFocus();

                                    mReplyEditText.setText("");

                                    mReplyEditText.setHint(getString(R.string.hint_reply,
                                            replyItem.getUsername()));

                                    mReplyEditText.setTag(position);

                                    showSoftKeyboard2(mReplyEditText);

                                    mReplyParent=false;

                                } else if (id==R.id.layout_comment_detail_parent) {

                                    //这里先检查是否登录再是否显示评论窗口，而不是评论之后再检查
                                    if (!OtherUtil.hasLogin(getActivity())) {
                                        showUnLoginSnackbar();
                                        return;
                                    }

                                    mReplyEditText.setText("");
                                    mReplyEditText.setHint(getString(R.string.hint_comment));

                                    CoordinatorLayout.LayoutParams layoutParams =
                                            (CoordinatorLayout.LayoutParams) mLayout_reply
                                                    .getLayoutParams();
                                    QuickReturnFooterBehavior behavior =
                                            (QuickReturnFooterBehavior) layoutParams.getBehavior();
                                    behavior.quickShow(mLayout_reply);
                                    mReplyEditText.requestFocus();
                                    showSoftKeyboard2(mReplyEditText);

                                    mReplyParent=true;
                                } else if (id==R.id.tv_delete_child) {

                                    final JSONObject params = OtherUtil.getJSONObject(
                                            CommentDetailFragment2.this.getActivity(),
                                            mArgs.get(Constant.KEY_MODEL_DELETE_REPLY_COMMENT_DETAIL),
                                            mArgs.get(Constant.KEY_ACTION_DELETE_REPLY_COMMENT_DETAIL), true);
                                    JSONUtil.put(params, Constant.KEY_CHILD_POSITION, position);
                                    JSONUtil.put(params, "id",replyItem.getId());
                                    JSONUtil.put(params, (String) mArgs.get(Constant.KEY_NAME_ARGS),
                                            mArgs.get(Constant.KET_VALUE_ARGS));
                                    mCommentLoader.doDeleteComment(new CommentDeleteFragment.Callbacks() {
                                        @Override
                                        public void onSelectionFinished(boolean isOk) {
                                            if (isOk)
                                            mCommentLoader.deleteComment(CommentLoader.TYPE_DETAIL,
                                                    params, new CommentLoader.Callback() {
                                                        @Override
                                                        public void onRequestFinished(
                                                                boolean success)
                                                        {
                                                            if (success) {
                                                                mNewCommentCount--;
                                                                if (!mCommentsChanged) {
                                                                    mCommentsChanged=true;
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                }
                            }
                        });

                if (parentComment) {
                    holder.setItemChildViewClickListener(R.id.iv_head_portrait,R.id.ib_more);
                } else {
                    holder.setItemChildViewClickListener(R.id.iv_head_portrait,R.id.ib_comment, R.id.tv_delete_child);
                }

                return holder;

            }

            @Override
            public int getItemViewType(int position) {
                return position == 0 ? TYPE_COMMENT_PARENT : TYPE_COMMENT_CHILD;
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //获取回复数据基础参数设置
        JSONObject params = OtherUtil.getJSONObject(getActivity(), mArgs.get(Constant.KEY_MODEL),
                mArgs.get(Constant.KEY_ACTION), false);
        JSONUtil.put(params, "id", mCommentItem.getId());
        mCommentLoader = new CommentLoader(CommentLoader.TYPE_DETAIL, this, params, mArgs);
        mCommentLoader.addHeaderData(BaseAdapter.AddType.FIRST, new ReplyDetailItem());



        final Button btn_send= (Button) rootView.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        final Object childPos=mArgs.get(Constant.KEY_CHILD_POSITION);
        mReplyParent = childPos == null || (Integer) childPos == -1;
        mReplyEditText= (EditText) rootView.findViewById(R.id.et_comment);
        mReplyEditText.setTag(childPos != null ? (Integer) childPos + 1 : null);
        mReplyEditText.setHint(!mReplyParent && mArgs.get(Constant.KEY_TO_NAME) != null ? getString(
                R.string.hint_reply, mArgs.get(Constant.KEY_TO_NAME))
                : getString(R.string.hint_comment));
        mReplyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_send.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLayout_reply= (ViewGroup) rootView.findViewById(R.id.layout_reply);

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) mLayout_reply
                        .getLayoutParams();
        QuickReturnFooterBehavior behavior =
                (QuickReturnFooterBehavior) layoutParams.getBehavior();
        behavior.setOnFooterViewVisibilityChangeListener(mListener);

    }

    private void showDialog(final String id) {
        if (!mCommentLoader.isCommentsLoaded()) return;
        DialogFactory.showDialog(getActivity(), R.layout.dialog_comment_detail, R.style.CustomDialog,
                R.style.popwin_anim_style, Gravity.BOTTOM, 1, 0, new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {

                      final boolean isAuthor=OtherUtil.isAuthor(getActivity(),mCommentItem
                              .getUser_id());

                        final View delete = v.findViewById(R.id.btn_delete);
                        View report = v.findViewById(R.id.btn_report);

                        delete.setVisibility(isAuthor ? View.VISIBLE : View.GONE);
                        report.setVisibility(isAuthor ? View.GONE : View.VISIBLE);

                        if (delete.getVisibility()==View.VISIBLE) {
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteMyComment();
                                    dialog.dismiss();
                                }
                            });
                        }

                        if (report.getVisibility()==View.VISIBLE) {
                            report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), ReportActivity.class);
                                    intent.putExtra("project_id", id);
                                    intent.putExtra("type",
                                            (Integer) mArgs.get(Constant.KEY_REPORT_TYPE));
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                        }

                        v.findViewById(R.id.btn_cancel).setOnClickListener(new View
                                .OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
    }

    private void deleteMyComment() {

        final JSONObject params = OtherUtil.getJSONObject(CommentDetailFragment2.this.getActivity(),
                mArgs.get(Constant.KEY_MODEL_DELETE_COMMENT_COMMENT_DETAIL), mArgs.get(Constant
                        .KEY_ACTION_DELETE_COMMENT_COMMENT_DETAIL), true);

        JSONUtil.put(params, Constant.KET_IS_DELETE_ALL, true);

        JSONUtil.put(params, "id", mArgs.get(Constant.KEY_ID));
        JSONUtil.put(params, (String) mArgs.get(Constant.KEY_NAME_ARGS),
                mArgs.get(Constant.KET_VALUE_ARGS));

        mCommentLoader.doDeleteComment(new CommentDeleteFragment.Callbacks() {
            @Override
            public void onSelectionFinished(boolean isOk) {
                if (isOk)
                mCommentLoader.deleteComment(CommentLoader.TYPE_DETAIL, params, new CommentLoader
                        .Callback() {
                    @Override
                    public void onRequestFinished(boolean success) {
                        if (success) {
                            mCommentLoader.notifyCommentReplyDataChanged(CommentLoader.TYPE_DETAIL,
                                    CommentLoader.TYPE_DELETE, mOrigCommentCount + 1,
                                    (Integer) mArgs.get(Constant.KEY_PARENT_POSITION), null);
                            if (!mCommentsChanged) {
                                mCommentsChanged=true;
                            }
                        }
                    }
                });
            }
        });
    }


    @Override
    public void loadData() {
        mCommentLoader.loadReplies();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_send) {
            reply();

        } else if (i == R.id.et_comment) {
        }
    }

    private void reply() {

        JSONObject params = OtherUtil.getJSONObject(getActivity(), mArgs.get(Constant.KEY_MODEL_COMMENT_DETAIL),
                mArgs.get(Constant.KEY_ACTION_COMMENT_DETAIL), true);

        //主评论Id
        JSONUtil.put(params, "comment_id", mCommentItem.getId());
        //回复内容
        JSONUtil.put(params, "content", mReplyEditText.getText().toString());

        mCommentLoader.doReply(mReplyParent, mReplyParent ? -1 : (Integer) mReplyEditText.getTag(),
                params, new CommentLoader.Callback() {
                    @Override
                    public void onRequestFinished(
                            boolean success)
                    {
                        if (success) {
                            mNewCommentCount++;
                            if (!mCommentsChanged) {
                                mCommentsChanged=true;
                            }
                        }
                    }
                });

        if (!mReplyParent) {
            mReplyParent = true;
        }
        mReplyEditText.setText("");
        mReplyEditText.setHint(getString(R.string.hint_comment));
        hideSoftKeyboard();

    }

    @Override
    public void onDestroyView() {
        LogUtil.LogE(TAG,"onDestroyView");
        mCommentLoader.cancleRequest();
        cancelRequest(TAG);
        ImageDisplayUtil.stopTask();
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        LogUtil.LogE(TAG,"onConfigurationChanged");

        switch (newConfig.keyboardHidden) {
            case Configuration.KEYBOARDHIDDEN_YES:
                LogUtil.LogE(TAG,"KEYBOARDHIDDEN_YES");
                break;
            case Configuration.KEYBOARDHIDDEN_NO:
                LogUtil.LogE(TAG,"KEYBOARDHIDDEN_NO");
                break;
            case Configuration.KEYBOARDHIDDEN_UNDEFINED:
                LogUtil.LogE(TAG,"KEYBOARDHIDDEN_UNDEFINED");
                break;
            default:
                break;
        }

    }


    private QuickReturnFooterBehavior.SimpleOnFooterViewVisibilityChangeListener mListener =
            new QuickReturnFooterBehavior.SimpleOnFooterViewVisibilityChangeListener() {
                @Override
                public void onShowAfter() {
                    if (mReplyEditText != null) {
                        mReplyEditText.requestFocus();
                    }
                }

                @Override
                public void onHideBefore() {
                    hideSoftKeyboard();
                }

                @Override
                public void onHideAfter() {
                    if (!mReplyParent) {
                        mReplyParent = true;
                    }

                    if (mReplyEditText != null) {
                        mReplyEditText.setText("");
                        mReplyEditText.setHint(getString(R.string.hint_comment));

                    }
                }

            };

    @SuppressWarnings("unchecked")
    @Override
    public void onBackPressed() {
        //如果回复数据第一次还没有加载成功，不通知刷新详情里的主评论回复数据
        if (!mCommentLoader.isCommentsLoaded() || !mCommentsChanged) return;
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        adapter.getData().remove(0);
        ArrayList<ReplyItem> replyDetailItems = (ArrayList<ReplyItem>) adapter.getData();
        LogUtil.LogE(TAG,"changed count=======>"+ (mNewCommentCount > mOrigCommentCount ?
                mNewCommentCount - mOrigCommentCount
                : mOrigCommentCount - mNewCommentCount));
        LogUtil.LogE(TAG,"replyDetailItems=="+replyDetailItems.toString());
        mCommentLoader.notifyCommentReplyDataChanged(CommentLoader.TYPE_DETAIL,
                mNewCommentCount > mOrigCommentCount ? CommentLoader.TYPE_ADD
                        : CommentLoader.TYPE_DELETE,
                mNewCommentCount > mOrigCommentCount ? mNewCommentCount - mOrigCommentCount
                        : mOrigCommentCount - mNewCommentCount,
                (Integer) mArgs.get(Constant.KEY_PARENT_POSITION),
                replyDetailItems!=null?replyDetailItems:new ArrayList<ReplyItem>());
    }
}
