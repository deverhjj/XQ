package com.biu.modulebase.binfenjiari.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommentDetailActivity;
import com.biu.modulebase.binfenjiari.model.CardDetailParentItem;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.adapter.ExpandableRecyclerViewAdapter;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ChildViewHolder;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ParentViewHolder;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ViewHolderCallbacks;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/4/25.
 */
public class CommentAdapter extends ExpandableRecyclerViewAdapter {
    private static final String TAG = "CommentAdapter";
    private static final int TYPE_PARENT_COMMENT = 1;
    private static final int TYPE_CHILD_COMMENT = 2;
    private static final int TYPE_CHILD_COMMENT_LAST = 3;
    private Context mContext;

    public CommentAdapter(Context context,List list) {
        super(list);
        mContext=context;
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
        ParentViewHolder parentViewHolder = null;
        if (parentType==TYPE_PARENT_COMMENT) {
            parentViewHolder=new ParentViewHolder(LayoutInflater.from(mContext).inflate(
                    R.layout.item_card_detail_comment_parent, parent, false), this, new
                    ViewHolderCallbacks() {
                        @Override
                        public int[] getNeedRegisterClickListenerChildViewIds() {
                            return new int[0];
                        }

                        @Override
                        public void onClick(RecyclerView.ViewHolder viewHolder, View view,
                                int position, int adapterPosition, int parentPosition,
                                int parentAdapterPosition)
                        {

                        }

                        @Override
                        public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {
                            CardDetailParentItem commentItem = (CardDetailParentItem) data;
                            ParentViewHolder holder = (ParentViewHolder) viewHolder;
                            View divider=holder.getView(R.id.divider);
                            divider.setVisibility(commentItem.isHasChildComment() ? View.GONE : View
                                    .VISIBLE);
                        }
                    });
        }
        return parentViewHolder;
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup child, int childType) {
        final boolean commentLast=childType==TYPE_CHILD_COMMENT_LAST;
        ChildViewHolder childViewHolder = new ChildViewHolder(LayoutInflater.from(mContext).inflate(
                childType == TYPE_CHILD_COMMENT ? R.layout.item_card_detail_comment_child
                        : R.layout.item_card_detail_comment_child_last, child, false), this,
                new ViewHolderCallbacks() {
                    @Override
                    public int[] getNeedRegisterClickListenerChildViewIds() {
                        return commentLast ? new int[]{R.id.tv_load_more} : null;
                    }

                    @Override
                    public void onClick(RecyclerView.ViewHolder viewHolder, View view, int position,
                            int adapterPosition, int parentPosition, int parentAdapterPosition)
                    {
                        int viewId=view.getId();
                        if (viewId==R.id.tv_load_more) {
                            //TODO 跳转到二级评论页面
                            Intent intent=new Intent(mContext, CommentDetailActivity.class);
                            //                            intent.putExtra(CommentChildListActivity.EXTRA_COMMENT_PARENT,parentItem);
                            mContext.startActivity(intent);
                            return;
                        }
                    }

                    @Override
                    public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {

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
        return TYPE_PARENT_COMMENT;
    }

    @Override
    public int getChildType(int parentPosition, int childPosition) {
        int childCount = getChildCount(parentPosition);
        return childPosition == childCount - 1 ? TYPE_CHILD_COMMENT_LAST : TYPE_CHILD_COMMENT;
    }
}
