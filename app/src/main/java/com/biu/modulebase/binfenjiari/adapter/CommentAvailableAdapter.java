package com.biu.modulebase.binfenjiari.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommentDetailActivity;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.CommentDeleteFragment;
import com.biu.modulebase.binfenjiari.model.CommentItem;
import com.biu.modulebase.binfenjiari.model.ReplyItem;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.adapter.ExpandableRecyclerViewAdapter;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ChildViewHolder;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ParentViewHolder;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ViewHolderCallbacks;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/5/23.
 */
public class CommentAvailableAdapter extends ExpandableRecyclerViewAdapter {
    private static final String TAG = "CommentAvailableAdapter";

    private CommentLoader mCommentLoader;

    private ViewHolderCallbacks mHeaderCallback;

    private static final int TYPE_PARENT_HEADER = 0;
    private static final int TYPE_PARENT_COMMENT = 1;
    private static final int TYPE_CHILD_COMMENT = 2;
    private static final int TYPE_CHILD_COMMENT_LAST = 3;

    private List<ParentListItem> mItems;
    private BaseFragment mBaseFragment;
    private int mHeaderResId;

    private HashMap<String,Object> mArgs;

    public CommentAvailableAdapter(BaseFragment baseFragment,CommentLoader commentLoader,HashMap<String,
            Object> args,
            List<ParentListItem> list, int headerResId, ViewHolderCallbacks headerCallback)
    {
        super(list);
        mItems = list;
        mCommentLoader=commentLoader;
        mBaseFragment = baseFragment;
        mHeaderResId = headerResId;
        mHeaderCallback = headerCallback;

        mArgs=args;

        setExpandable(false);
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(final ViewGroup parent, int parentType) {
        ParentViewHolder parentViewHolder = null;
        if (parentType==TYPE_PARENT_HEADER) {
            parentViewHolder=new ParentViewHolder(LayoutInflater.from(mBaseFragment.getActivity()).inflate
                    (mHeaderResId, parent, false), this, mHeaderCallback);
        } else if (parentType==TYPE_PARENT_COMMENT) {
            parentViewHolder=new ParentViewHolder(LayoutInflater.from(mBaseFragment.getActivity()).inflate(
                    R.layout.item_card_detail_comment_parent, parent, false), this, new
                    ViewHolderCallbacks() {
                        @Override
                        public int[] getNeedRegisterClickListenerChildViewIds() {
                            return new int[]{R.id.layout_comment_parent,R.id.ib_comment,R.id.tv_delete_parent,R.id.iv_head_portrait};
                        }

                        @Override
                        public void onClick(RecyclerView.ViewHolder viewHolder, View view,
                                int position, int adapterPosition, int parentPosition,
                                int parentAdapterPosition)
                        {

                            if (position==RecyclerView.NO_POSITION) return;

                            CommentItem commentItem= (CommentItem) getParent(position);

                            int id=view.getId();
                            if(id==R.id.iv_head_portrait){
                                Intent intent =new Intent(mBaseFragment.getActivity(),PersonalInfoActivity.class);
                                intent.putExtra(Constant.KEY_ID,commentItem.getUser_id());
                                mBaseFragment.startActivity(intent);
                                return;
                            }

                            if (id==R.id.tv_delete_parent) {

                                final JSONObject params= OtherUtil.getJSONObject(mBaseFragment
                                        .getActivity(),mArgs.get(Constant.KEY_MODEL_DELETE_COMMENT_COMMENT_DETAIL),
                                        mArgs.get(Constant.KEY_ACTION_DELETE_COMMENT_COMMENT_DETAIL),true);
                                JSONUtil.put(params,Constant.KEY_PARENT_POSITION, position);
                                JSONUtil.put(params, "id", commentItem.getId());
                                JSONUtil.put(params, (String) mArgs.get(Constant.KEY_NAME_ARGS),
                                        mArgs.get(Constant.KET_VALUE_ARGS));

                                mCommentLoader.doDeleteComment(new CommentDeleteFragment.Callbacks() {
                                    @Override
                                    public void onSelectionFinished(boolean isOk) {
                                        if (isOk) {
                                            mCommentLoader.deleteComment(CommentLoader.TYPE_LIST,
                                                    params,null);
                                        }
                                    }
                                });

                                return;
                            }

                            Intent intent = new Intent(mBaseFragment.getActivity(),
                                    CommentDetailActivity.class);

                            mArgs.put(Constant.KEY_PARENT_POSITION,position);
                            mArgs.put(Constant.KEY_CHILD_POSITION,-1);
                            mArgs.put(Constant.KEY_TO_NAME,null);
                            mArgs.put(Constant.KEY_ID,commentItem.getId());
                            //TODO 传递当前主评论的回复数量
                            mArgs.put(Constant.KEY_REPLY_COUNT,commentItem.getReplyNum());


                            intent.putExtra(Constant.KEY_ARGS,mArgs);
                            intent.putExtra(Constant.KEY_COMMENT,commentItem);

                             mBaseFragment.startActivityForResult(intent,Constant.REQUEST_COMMENT_CHANGED_STATUS);
                        }

                        @Override
                        public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {

                            if (data==null||!(data instanceof CommentItem)) return;

                            CommentItem commentItem = (CommentItem) data;
                            ParentViewHolder holder = (ParentViewHolder) viewHolder;

                            ImageView iv_head_portrait=
                                    (ImageView) holder.getView(R.id.iv_head_portrait);
                            ImageDisplayUtil.displayImage(Constant.IMG_SOURCE,commentItem.getUser_pic(),
                                    iv_head_portrait,ImageDisplayUtil.DISPLAY_HEADER);

                            TextView tv_nickname = (TextView) holder.getView(R.id.tv_nickname);
                            tv_nickname.setText(commentItem.getUsername());

                            TextView tv_date = (TextView) holder.getView(R.id.tv_date);
                            tv_date.setText(Utils.getReleaseTime(new Date(commentItem
                                    .getCreate_time()*1000)));
                            tv_date.setText(Utils.getReleaseTime(new Date(commentItem.getCreate_time()*1000)));

                            TextView tv_comment_parent = (TextView) holder.getView(R.id.tv_comment_parent);
//                            LinearLayout.LayoutParams lp=
//                                    (LinearLayout.LayoutParams) tv_comment_parent.getLayoutParams();
                            tv_comment_parent.setPadding(0,0,0,commentItem.isCanDelete()
                                    ?0:mBaseFragment.getResources().getDimensionPixelSize(R.dimen
                                    .padding_bottom_8dp));
                            tv_comment_parent.setText(commentItem.getContent());


                            View delete=holder.getView(R.id.tv_delete_parent);
                            delete.setVisibility(commentItem.isCanDelete()?View.VISIBLE:View.GONE);


                            View divider=holder.getView(R.id.divider);
                            divider.setVisibility(commentItem.isHasChildComment() ? View.GONE : View
                                    .VISIBLE);
                        }
                    });
        }
        return parentViewHolder;
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup child, final int childType) {
        final boolean commentLast=childType==TYPE_CHILD_COMMENT_LAST;
        ChildViewHolder childViewHolder = new ChildViewHolder(LayoutInflater.from(mBaseFragment.getActivity())
                .inflate(
                childType == TYPE_CHILD_COMMENT ? R.layout.item_card_detail_comment_child
                        : R.layout.item_card_detail_comment_child_last, child, false), this,
                new ViewHolderCallbacks() {
                    @Override
                    public int[] getNeedRegisterClickListenerChildViewIds() {
                        return commentLast ? new int[]{R.id.tv_load_more, R.id.layout_comment_child}
                                : new int[]{R.id.layout_comment_child};
                    }

                    @Override
                    public void onClick(RecyclerView.ViewHolder viewHolder, View view, int position,
                            int adapterPosition, int parentPosition, int parentAdapterPosition)
                    {
                        if (position==RecyclerView.NO_POSITION) return;

                        CommentItem commentItem= (CommentItem) getParent(parentPosition);
                        ReplyItem replyItem= (ReplyItem) getChild(parentPosition,position);

                        final int viewId=view.getId();

                        //TODO 跳转到二级评论页面
                        Intent intent = new Intent(mBaseFragment.getActivity(),
                                CommentDetailActivity.class);

                        mArgs.put(Constant.KEY_PARENT_POSITION,parentPosition);
                        mArgs.put(Constant.KEY_CHILD_POSITION,
                                viewId == R.id.tv_load_more ? -1 : position);
                        mArgs.put(Constant.KEY_TO_NAME,
                                viewId==R.id.tv_load_more ? null : replyItem.getUsername());
                        mArgs.put(Constant.KEY_ID,commentItem.getId());
                        //TODO 传递当前主评论的回复数量
                        mArgs.put(Constant.KEY_REPLY_COUNT,commentItem.getReplyNum());

                        intent.putExtra(Constant.KEY_ARGS,mArgs);
                        intent.putExtra(Constant.KEY_COMMENT,commentItem);
                        mBaseFragment.startActivityForResult(intent,Constant.REQUEST_COMMENT_CHANGED_STATUS);

                    }

                    @Override
                    public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {
                        if (data==null||!(data instanceof ReplyItem)) return;

                        ReplyItem replyItem= (ReplyItem) data;

                        ChildViewHolder holder= (ChildViewHolder) viewHolder;

                        TextView tv_comment_child = (TextView) holder.getView(R.id.tv_comment_child);
                        tv_comment_child.setText(replyItem.getContent());

                        //根据被回复者的姓名是否为 null 判断子回复者是回复主评论还是子评论
                        //回复主评论格式：回复者名：回复内容
                        //回复子评论格式：回复者名 回复 被回复者：回复内容
                        String toUserId=replyItem.getTo_user_id();//TODO 据此判断是回复格式
                        String toUserName=replyItem.getTo_name();

//                        String replyForm=mBaseFragment.getString(R.string.form_reply);
//                        String replyForm2=mBaseFragment.getString(R.string.form_reply2);
                        String commentFrom=replyItem.getUsername();
                        String commentTo=replyItem.getTo_name();
                        String comment=replyItem.getContent();

                        CharSequence reply1=Html.fromHtml(mBaseFragment.getString(R.string
                                .form_reply_new,commentFrom,commentTo,comment));
                        CharSequence reply2=Html.fromHtml(mBaseFragment.getString(R.string
                                .form_reply_new_2,commentFrom,comment));

                        tv_comment_child.setText(!TextUtils.isEmpty(toUserId) ? reply1 : reply2);

                        if (commentLast) {
                            int parentPosition = getParentPosition(viewHolder.getAdapterPosition());
                            final CommentItem commentItem = (CommentItem) getParent(parentPosition);
                            TextView tv_load_more = (TextView) holder.getView(R.id.tv_load_more);
                            tv_load_more.setVisibility(
                                    commentItem.isHasMoreChildComment() ? View.VISIBLE : View.GONE);
                        }

                        final int childPos=getChildPosition(viewHolder.getAdapterPosition());
                        View indicator=holder.getView(R.id.indicator);
                        if (indicator!=null) {
                            indicator.setVisibility(childPos == 0 ? View.VISIBLE : View.GONE);
                        }

                    }
                });

        return childViewHolder;
    }

    @Override
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int adapterParentPosition,
            int parentPosition, ParentListItem parentListItem)
    {
        parentViewHolder.bindData(parentListItem);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int adapterChildPosition,
            int parentPosition, int childPosition, Object childListItem)
    {
        childViewHolder.bindData(childListItem);
    }

    @Override
    public int getParentType(int parentPosition) {
        ParentListItem parentItem = getParent(parentPosition);
        return parentPosition == 0 && !(parentItem instanceof CommentItem) ? TYPE_PARENT_HEADER
                : TYPE_PARENT_COMMENT;
    }

    @Override
    public int getChildType(int parentPosition, int childPosition) {
        int childCount = getChildCount(parentPosition);
        return childPosition == childCount - 1 ? TYPE_CHILD_COMMENT_LAST : TYPE_CHILD_COMMENT;
    }

    public void addHeader(ParentListItem parentItem) {
        if (parentItem != null && mItems.size()!=0) {
            mItems.set(0, parentItem);
            notifyParentItemChanged(0);
        }else{
            mItems.add(0, parentItem);
            notifyParentItemInserted(0);
        }
    }

    public void addParent(int position, ParentListItem parentItem) {
        if (parentItem != null) {
            mItems.add(position, parentItem);
            notifyParentItemInserted(position);
        }
    }

    public void addParent(ParentListItem parentItem) {
        if (parentItem != null) {
            int insertPos = mItems.size();
            mItems.add(insertPos, parentItem);
            notifyParentItemInserted(insertPos);
        }
    }

    public ParentListItem getHeader(int position) {
        return getParent(position);
    }

    public void addChild(int parentPosition, int childPosition, ReplyItem child) {
        CommentItem parentItem = (CommentItem) mItems.get(parentPosition);
        List<ReplyItem> childItemList = parentItem.getChildItemList(parentPosition);
        if (childItemList == null) {
            childItemList = new ArrayList<>();
            parentItem.setReplyList(childItemList);
            //为了影藏父评论的分割线
            parentItem.setHasChildComment(true);
            notifyParentItemChanged(parentPosition);
        }
        childItemList.add(childPosition, child);
        notifyChildItemInserted(parentPosition, childPosition);
    }

    public void addChild(int parentPosition, ReplyItem child) {
        CommentItem parentItem = (CommentItem) mItems.get(parentPosition);
        List<ReplyItem> childItemList = parentItem.getChildItemList(parentPosition);
        if (childItemList == null) {
            childItemList = new ArrayList<>();
            parentItem.setReplyList(childItemList);
            //为了影藏父评论的分割线
            parentItem.setHasChildComment(true);
            notifyParentItemChanged(parentPosition);
        }
        int insertPos = childItemList.size();
        childItemList.add(insertPos, child);
        notifyChildItemInserted(parentPosition, insertPos);
    }

    public void addChildren(int parentPosition, List<ReplyItem> children) {
        if (children==null||children.size()==0) return;
        CommentItem parentItem = (CommentItem) mItems.get(parentPosition);
        List<ReplyItem> childItemList = parentItem.getChildItemList(parentPosition);
        if (childItemList == null) {
            childItemList = new ArrayList<>();
            parentItem.setReplyList(childItemList);
            //为了影藏父评论的分割线
            parentItem.setHasChildComment(true);
            notifyParentItemChanged(parentPosition);
        }
        int insertPos = childItemList.size();
        childItemList.addAll(insertPos, children);
        notifyChildItemRangeInserted(parentPosition, insertPos, children.size());
    }


    public void addAllData(List<? extends ParentListItem> data) {
        if (data != null) {
            int parentPositionStart = mItems.size();
            int parentItemCount = data.size();
            mItems.addAll(data);
            notifyParentItemRangeInserted(parentPositionStart, parentItemCount);

        }
    }

        /**
         * 注意：必须先通知清除所有的列表项再清除本地列表里所有数据，否则本地会报错，应为RecyclerView
         * 在清除所有 ItemView 时还会最后调用一次 getItemType 导致本地从本地数据集中获取数据时
         * 本地数据集List会因size是0而报错
         */
        public void removeAllData() {
            if (mItems.size() > 0) {
                notifyParentItemRangeRemoved(0, mItems.size());
                mItems.clear();
            }
        }

    public void removeData(int positionStart, int itemCount) {
        if (positionStart >= 0 && itemCount > 0 && positionStart < positionStart + itemCount &&
                mItems.size() >= positionStart + itemCount && positionStart < mItems.size())
        {
            List<ParentListItem> parentItems = mItems.subList(positionStart,
                    positionStart + itemCount);
            LogUtil.LogE(TAG, "removeData=>" + parentItems.size());
            Iterator<ParentListItem> iterator = parentItems.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
            notifyParentItemRangeRemoved(positionStart, itemCount);
        }
    }

    public int removeParent(int position) {
        ParentListItem parentListItem = getParent(position);
        List<?> replies = parentListItem.getChildItemList(position);
        mItems.remove(position);
        notifyParentItemRemoved(position);
        return replies != null ? replies.size() + 1 : 1;
    }

    public void removeChildren(int parentPosition) {
        ParentListItem parentListItem = getParent(parentPosition);
        List<?> replies = parentListItem.getChildItemList(parentPosition);
        if (replies==null || replies.size()==0) return;

        LogUtil.LogE(TAG,"removeChildren==>"+parentPosition+",,,,replies"+replies);

        notifyChildItemRangeRemoved(parentPosition, 0, replies.size());

        replies.clear();
    }

//    public void changeChildren(int parentPosition){
//        ParentListItem parentListItem = getParent(parentPosition);
//        List<?> replies = parentListItem.getChildItemList(parentPosition);
//        if (replies==null) return;
//        notifyChil
//    }

    public void changeParent(int parentPosition){
        notifyParentItemChanged(parentPosition);
    }

//    public ItemDecoration getItemDecoration() {
//        return new ItemDecoration();
//    }
//
//    public class ItemDecoration extends RecyclerView.ItemDecoration {
//
//        @Override
//        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            LogUtil.LogE(TAG,"*******onDraw************");
//            c.drawARGB(100,23,134,45);
//        }
//
//        @Override
//        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
//                RecyclerView.State state)
//        {
//
////            BaseAdapter.this.getItemOffsets(outRect, view, parent, state);
//        }
//    }

}
