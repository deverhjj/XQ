package com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentWrapper;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ChildViewHolder;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.viewholder.ParentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展 RecyclerView.Adapters 实现可展开和折叠列表项.
 */
public abstract class ExpandableRecyclerViewAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder>
        extends RecyclerView.Adapter
        implements ParentViewHolder.OnParentListItemExpandCollapseListener
{
    private static final String TAG = "ExpandableRVAdapter";
    /**
     * 父列表项标识
     */
    private static final int TYPE_PARENT = 0x0001;
    /**
     * 子列表项标识
     */
    private static final int TYPE_CHILD = 0x0002;
    /**
     * 默认的没有指定父列表项类型的标识
     */
    private static final int TYPE_PARENT_NO_TYPE = 0;
    /**
     * 默认的没有指定子列表项类型的标识
     */
    private static final int TYPE_CHILD_NO_TYPE = 0;

    /**
     * 父列表项点击展开或者折叠监听器
     */
    private OnParentExpandCollapseListener mParentExpandCollapseListener;
    /**
     * 父列表项集合
     */
    private List<? extends ParentListItem> mParentListItems;
    /**
     * 当前显示的列表项(父列表项和所有展开的子列表项)集合
     */
    private List<Object> mItemList;
    /**
     * 当前所有监听适配器的 RecyclerView 集合
     */
    private List<RecyclerView> mAttachedRecyclerViews;
    /**
     * 是否展开所有的父列表项
     */
    private boolean mExpandAllParent;

    /**
     * 父列表项是否可展开,如果为 true 所有父列表项会展开但之后不再会触发展开折叠事件
     */
    private boolean mExpandable=true;


    public ExpandableRecyclerViewAdapter(List<? extends ParentListItem> parentListItems) {
        init(parentListItems);
    }

    private void init(List<? extends ParentListItem> parentListItems) {
        if (parentListItems==null) {
           throw new NullPointerException("parentListItems can not be null");
        }
        mParentListItems = parentListItems;
        mItemList = ExpandableRecyclerViewAdapterHelper.generateParentChildItemList(
                parentListItems);
        mAttachedRecyclerViews = new ArrayList<>();
    }

    public void setParentListItems(List<? extends ParentListItem> parentListItems) {
        init(parentListItems);
    }

    /**
     * 父列表项展开或折叠状态监听接口
     */
    public interface OnParentExpandCollapseListener {

        /**
         * 父列表项展开后的回调
         * @param parentPosition 该父列表项在父列表里的位置
         * @param byUser 是否属于用户点击父列表项之后产生的展开事件，用于区分用户手动还是程序触发的
         */
        void onParentExpanded(int parentPosition, boolean byUser);

        /**
         * 父列表项折叠后的回调
         * @param parentPosition 该父列表项在父列表里的位置
         * @param byUser 是否属于用户点击父列表项之后产生的折叠事件，用于区分用户手动还是程序触发的
         */
        void onParentCollapsed(int parentPosition, boolean byUser);
    }

    /**
     * 获取当前监听父列表项展开或折叠状态改变的监听器
     * @return 父列表项展开或折叠状态改变的监听器
     */
    public OnParentExpandCollapseListener getParentExpandCollapseListener() {
        return mParentExpandCollapseListener;
    }

    /**
     * 注册一个监听父列表项展开或折叠状态改变的监听器
     * @param parentExpandCollapseListener 监听父列表项展开或折叠状态改变的监听器
     */
    public void setParentExpandCollapseListener(
            OnParentExpandCollapseListener parentExpandCollapseListener)
    {
        mParentExpandCollapseListener = parentExpandCollapseListener;
    }


    public boolean isExpandable() {
        return mExpandable;
    }

    public void setExpandable(boolean expandable) {
        mExpandable = expandable;
    }

    /**
     * @return
     */
    public boolean isExpandAllParent() {
        return mExpandAllParent;
    }

    /**
     * @param expandAllParent
     */
    public void setExpandAllParent(boolean expandAllParent) {
        mExpandAllParent = expandAllParent;
        if (expandAllParent) {
            expandAllParent();
        }
    }

    /**
     * <p>
     * 更具列表项类型判断并回调{@link #onCreateParentViewHolder(ViewGroup, int)}
     * 或者{@link #onCreateChildViewHolder(ViewGroup, int)}
     * </p>
     * 列表项类型包含 父列表项和子列表项类型,也可通过覆盖{@link #getParentType(int)}
     * 或者{@link #getChildType(int, int)}提供具体父列表项类型或子列表项类型
     *
     * @param parent   用于显示列表项视图{@link android.view.View}的 {@link ViewGroup}
     * @param viewType 列表项类型(父列表项类型或子列表项类型，包含具体的父列表项或子列表项类型)
     * @return 根据适配器 ExpandableRecycleViewAdapter 里的数据位置创建的该位置所代表的列表项视图
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //父或子类型和具体的不同父或子类型的类型组合
        //例如 11，第一个1代表父列表项，第二个1代表外部传进来的具体的父列表项类型
        String packedViewType = viewType + "";

        //获得列表项类型，父列表项或子列表项类型，用于预先判断该列表项是父列表项还是子列表项
        viewType = Integer.valueOf(String.valueOf(packedViewType.charAt(0)));

        //外部返回的指定的具体的列表项类型(具体的父或子列表项类型)
        int specifiedViewType = Integer.valueOf(String.valueOf(packedViewType.substring(1)));

        if (viewType == TYPE_PARENT) {
            //回调并返回父列表项视图 ParentViewHolder
            PVH pvh = onCreateParentViewHolder(parent, specifiedViewType);
            if (mExpandable) {
                //注册父列表项视图点击事件监听器,用于监听列表项视图的点击并根据列表项的展开状态触发列表项的展开或折叠回调
                pvh.setParentListItemOnClickListener();
                //注册父列表项展开收缩子视图事件监听器
                pvh.setParentListItemExpandCollapseListener(this);
            }

            return pvh;
        } else if (viewType == TYPE_CHILD) {
            ////回调并返回子列表项视图 ChildViewHolder
            return onCreateChildViewHolder(parent, specifiedViewType);
        } else {
            throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    /**
     * <p>
     *  根据指定的列表项位置在适配器{@link #ExpandableRecyclerViewAdapter}里对应的数据集位置绑定数据到该列表项
     * 视图
     *
     *</p>
     * 判断适配器位置的对应的列表项类型
     * (父或子列表项类型，或者具体的父或子列表项类型)
     * 并回调{@link #onBindParentViewHolder(ParentViewHolder, int, int, ParentListItem)}
     * 或者{@link #onBindChildViewHolder(ChildViewHolder, int, int, int, Object)}通知更新该列表项位置的视图内容
     * 
     *
     * @param holder 指定列表项位置的 ViewHolder，用于更新指定位置的列表项视图
     * @param position 该列表项在适配器数据集中代表的位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //
        Object listItem = getListItem(position);

        int parentPosition = getParentPosition(position);

        if (listItem instanceof ParentWrapper) {

            PVH pvh = (PVH) holder;

            ParentWrapper parentWrapper = (ParentWrapper) listItem;

            //初始化展开或收缩状态
            pvh.setExpanded(parentWrapper.isExpanded());

            onBindParentViewHolder(pvh, position, parentPosition,
                    parentWrapper.getParentListItem());
        } else if (listItem == null) {
            throw new IllegalStateException("Incorrect ViewHolder found");
        } else {
            CVH cvh = (CVH) holder;
            onBindChildViewHolder(cvh, position, parentPosition,
                    getChildPosition(position), listItem);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //获取指定列表项位置在适配器数据集合里所代表的列表项
        Object listItem = getListItem(position);

        //获取指定列表项位置(父或子视图项的位置)的在父列表项位置
        int parentPosition = getParentPosition(position);

        //如果是父列表项类型就回调查询具体的父类型
        //返回父类型和具体的父类型的组合后的列表项类型
        if (listItem instanceof ParentWrapper) {
            LogUtil.LogE(TAG,"getItemViewType----Parent---------->"+position);
            int parentType = getParentType(parentPosition);

            String specifiedParentType = TYPE_PARENT + "" + parentType;

            return Integer.valueOf(specifiedParentType);
        } else if (listItem == null) {
            throw new IllegalStateException("Null object added");
        } else {

            //回调获取具体的子列表项类型
            //返回子类型和具体子类型的组合后的列表项类型
            int childPosition=getChildPosition(position);
            int childType = getChildType(parentPosition, childPosition);
            String specifiedChildType = TYPE_CHILD + "" + childType;
            LogUtil.LogE(TAG,"getItemViewType----Child---------->"+position+"," +
                    "child"+childPosition);
            return Integer.valueOf(specifiedChildType);
        }
    }

    /**
     * 获取指定父列表位置的父列表类型
     * @param parentPosition 要查询的父列表类型的位置
     * @return 指定父列表位置的父列表类型
     */
    public int getParentType(int parentPosition) {
        return TYPE_PARENT_NO_TYPE;
    }

    /**
     * 获取指定的父列表项位置下从属该父列表的子列表项的位置对应的子列表项类型
     * @param parentPosition 该子列表项的从属父列表项位置
     * @param childPosition 子列表项的位置
     * @return 子列表项的类型
     */
    public int getChildType(int parentPosition, int childPosition) {
        return TYPE_CHILD_NO_TYPE;
    }

    /**
     * 返回当前所有要显示的列表项(父列表和子列表)数据数量
     * @return 初始化时显示的列表项数量
     */
    @Override
    public int getItemCount() {
        Log.e(TAG,"getItemCount--->"+mItemList.size());
        return mItemList.size();
    }

    /**
     * 来自{@link #onCreateViewHolder(ViewGroup, int)}的创建指定父列表项视图的回调
     * 创建父列表项用于显示的父列表项视图的 {@link ParentViewHolder}，通过回调返回客户端的所指定的父列表项视图
     * @param parent 用于显示列表项视图的{@link ViewGroup}
     * @param parentType 父列表项的类型，用于提供不同的父列表项视图
     * @return 父列表项所需显示视图的 {@link ParentViewHolder}
     */
    public abstract PVH onCreateParentViewHolder(ViewGroup parent, int parentType);

    /**
     * 来自{@link #onCreateViewHolder(ViewGroup, int)}的创建指定子列表项视图的回调
     * @param child 用于显示列表项视图的{@link ViewGroup}
     * @param childType 子列表项的类型
     * @return 子列表项所需显示视图的 {@link ChildViewHolder}
     */
    public abstract CVH onCreateChildViewHolder(ViewGroup child, int childType);

    /**
     * 来自 {@link #onBindViewHolder(RecyclerView.ViewHolder, int)}的用于绑定视图数据 到{@link PVH}
     * 的回调
     * @param parentViewHolder 用于绑定数据的父列表项的 parentViewHolder
     * @param adapterParentPosition 该父列表项在适配器数据集里所对应的位置
     * @param parentPosition 该父列表项所在父列表里的位置
     * @param parentListItem 和该父列表项绑定的数据源 {@link ParentListItem}
     */
    public abstract void onBindParentViewHolder(PVH parentViewHolder, int adapterParentPosition,
            int parentPosition, ParentListItem parentListItem);

    /**
     * 来自 {@link #onBindViewHolder(RecyclerView.ViewHolder, int)}的用于绑定数据到{@link CVH}的回调
     * @param childViewHolder 用于显示或更新绑定到 CVH 里的数据
     * @param adapterChildPosition 该子列表项在适配器数据集里对应的位置
     * @param parentPosition 该子列表项所从属的父列表项在父列表里的位置
     * @param childPosition 该子列表项在子列表里的位置
     * @param childListItem 用于绑定到 CVH 里的数据源
     */
    public abstract void onBindChildViewHolder(CVH childViewHolder, int adapterChildPosition,
            int parentPosition, int childPosition, Object childListItem);


    /**
     * {@link ParentViewHolder}里父列表项展开回调，用于监听父列表项展开事件并触发相关的展开操作
     * @param adapterParentPosition 该父列表项在适配器数据集里对应的位置
     */
    @Override
    public void onParentListItemExpanded(int adapterParentPosition) {
        Object listItem = getListItem(adapterParentPosition);
        if (listItem instanceof ParentWrapper) {
            expandParentListItem(adapterParentPosition, (ParentWrapper) listItem, true);
        }
    }

    /**
     * {@link ParentViewHolder}里父列表项折叠回调，用于监听父列表项折叠事件并触发相关的折叠操作
     * @param adapterParentPosition 该父列表项在适配器数据集里对应的位置
     */
    @Override
    public void onParentListItemCollapsed(int adapterParentPosition) {
        Object listItem = getListItem(adapterParentPosition);
        if (listItem instanceof ParentWrapper) {
            collapseParentListItem(adapterParentPosition, (ParentWrapper) listItem, true);
        }
    }
    
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachedRecyclerViews.add(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAttachedRecyclerViews.remove(recyclerView);
    }


    /**
     * 获取指定适配器位置数据所代表的列表项
     *
     * @param adapterPosition 该列表项在适配器数据集中对应的位置
     * @return 指定适配器位置数据所代表的列表项
     */
    private Object getListItem(int adapterPosition) {
        if (adapterPosition>=mItemList.size()) {
           return null;
        }
        return mItemList.get(adapterPosition);
    }

    public int getParentCount() {
        return mParentListItems.size();
    }

    public int getChildCount(int parentPosition) {
        int childCount = 0;
        Object parentItem = mItemList.get(getAdapterParentPosition(parentPosition));
        if (parentItem instanceof ParentWrapper) {
            ParentWrapper parentWrapper = (ParentWrapper) parentItem;
            List<?> childItems = parentWrapper.getChildItemList(parentPosition);
            childCount = childItems != null ? childItems.size() : 0;
        }
        return childCount;
    }


    public ParentListItem getParent(int parentPosition) {
        return mParentListItems.get(parentPosition);
    }

    public Object getChild(int parentPosition, int childPosition) {
        return mParentListItems.get(parentPosition).getChildItemList(parentPosition).get(
                childPosition);
    }

    /**
     * 根据指定的列表项在适配器数据集中所对应的位置返回该列表项在父列表里的位置
     * 如果该列表项为子列表项就返回该子列表项所属的父列表项在父列表里位置
     * @param adapterPosition 要查询的列表项在适配器里所对应的位置
     * @return 指定列表项在父列表里的位置
     */
    public int getParentPosition(int adapterPosition) {
        if (adapterPosition == 0) {
            return 0;
        } else {
            Object listItem = getListItem(adapterPosition);

            if (listItem instanceof ParentWrapper) {

                int adapterParentPosition = adapterPosition;

                int beforeExpandedChildItemCount = getBeforeExpandedChildCount(
                        adapterParentPosition);

                return adapterParentPosition - beforeExpandedChildItemCount;

            } else {

                int adapterChildPosition = adapterPosition;

                for (int i = adapterChildPosition - 1; i >= 0; i--) {
                    if (getListItem(i) instanceof ParentWrapper) {
                        int adapterParentPosition = i;
                        return adapterParentPosition - getBeforeExpandedChildCount(
                                adapterParentPosition);
                    }
                }
            }
        }
        return -1;
    }
    
    
    /**
     * 根据指定的子列表项在适配器数据集中所对应的位置返回该子列表项在子列表里的位置
     * @param adapterChildPosition 子列表项在适配器里对应的位置
     * @return 指定子列表项在子列表里的位置
     */
    public int getChildPosition(int adapterChildPosition) {
        int adapterChildPos = -1;
        LogUtil.LogE(TAG,"adapterChildPosition="+adapterChildPosition);
        ParentWrapper parentWrapper = getParentWrapper(adapterChildPosition);
        Object childVisibleItem = getListItem(adapterChildPosition);
        if (parentWrapper != null) {
            List<?> childItemList=parentWrapper.getChildItemList(getParentPosition
                    (adapterChildPosition));
            LogUtil.LogE(TAG,"ParentPosition="+getParentPosition
                    (adapterChildPosition));
            LogUtil.LogE(TAG,"childItemList!=null?"+(childItemList!=null));
            adapterChildPos = childItemList!=null?childItemList.indexOf
                    (childVisibleItem):-1;
        }
        return adapterChildPos;
    }


    //修复 BUG:当返回的父列表项可以展开但是父列表项没有字列表时，不能添加字列表到本地数据集中，否则会报错
    /**
     * @param parentWrapperPos
     * @param newParentListItem
     * @return
     */
    private int addParentWrapper(int parentWrapperPos, ParentListItem newParentListItem) {
        ParentWrapper newParentWrapper = new ParentWrapper(newParentListItem);
        mItemList.add(parentWrapperPos, newParentWrapper);
        int insertItemCount = 1;
        if (newParentWrapper.isInitiallyExpanded()) {
            newParentWrapper.setExpanded(true);
            List<?> childItemList = newParentWrapper.getChildItemList(getParentPosition(parentWrapperPos));
            if (childItemList!=null) {
                mItemList.addAll(parentWrapperPos + insertItemCount, childItemList);
                insertItemCount += childItemList.size();
            }
        }
        return insertItemCount;
    }

    /**
     * 根据指定的子列表项在适配器数据集中所对应的位置返回该子列表项所属的父列表项
     * @param adapterChildPosition 子列表项在适配器里对应的位置
     * @return 指定子列表项所属的父列表项
     */
    private ParentWrapper getParentWrapper(int adapterChildPosition) {
        for (int i = adapterChildPosition - 1; i >= 0; i--) {
            Object listItem = getListItem(i);
            if (listItem instanceof ParentWrapper) {
                Log.e(TAG,"i-------------------->"+i);
                return (ParentWrapper) listItem;
            }
        }
        return null;
    }

    public boolean isAdapterParentPosition(int adapterPosition) {
        return !(adapterPosition >= mItemList.size() || adapterPosition < 0) && mItemList.get(
                adapterPosition) instanceof ParentWrapper;
    }


    //    /**
//     * 根据 {@link ParentListItem}返回其在适配器里对应的位置
//     * @param parentListItem 要查询的 父列表项数据
//     * @return 该数据在适配器数据集合里对应的位置
//     */
//    private int getAdapterParentPosition(ParentListItem parentListItem) {
//        for (int i = 0; i < mItemList.size(); i++) {
//            Object listItem = mItemList.get(i);
//            if (listItem instanceof ParentWrapper) {
//                ParentWrapper parentWrapper = (ParentWrapper) listItem;
//                if (parentWrapper.getParentListItem() == parentListItem) {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }

    public int getAdapterParentPosition(int parentPosition) {
        int parentCount = 0;
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            Object listItem = mItemList.get(i);
            if (listItem instanceof ParentWrapper) {
                parentCount++;
                if (parentCount > parentPosition) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int getAdapterChildPosition(int parentPosition, int childPosition) {
        int adapterParentPosition=getAdapterParentPosition(parentPosition);
        return adapterParentPosition+childPosition+1;
    }

    /**
     * 获取父列表项之前展开的所有子列表项数量
     * @param adapterParentPosition 父列表项在适配器中所对应的位置
     * @return 父列表项之前展开的所有子列表项数量
     */
    private int getBeforeExpandedChildCount(int adapterParentPosition) {
        if (adapterParentPosition == 0) {
            return 0;
        }
        int beforeExpandedChildCount = 0;
        for (int i = 0; i < adapterParentPosition; i++) {
            if (!(getListItem(i) instanceof ParentWrapper)) {
                beforeExpandedChildCount++;
            }
        }
        return beforeExpandedChildCount;
    }


    /**
     * 展开指定适配器位置所对应的父列表项
     * @param adapterParentPosition 父列表项在适配器里所对应的位置
     * @param parentWrapper 和父列表项相关的 {@link ParentWrapper}，用于获取该父列表项里的子列表项的信息
     * @param byUser 是否是用户通过点击展开父列表项的
     */
    private void expandParentListItem(int adapterParentPosition, ParentWrapper parentWrapper,
            boolean byUser)
    {
        if (!parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(true);
            List<?> childItemList = parentWrapper.getChildItemList(getParentPosition(adapterParentPosition));
            if (childItemList != null) {
                int insertPosStart = adapterParentPosition + 1;
                int childItemCount = childItemList.size();
                //按照顺序依次将子列表项插入到该父列表项下
                mItemList.addAll(insertPosStart, childItemList);
                //通知 RecyclerView 指定位置有新的列表项插入，刷新界面
                notifyItemRangeInserted(insertPosStart, childItemCount);

                if (mParentExpandCollapseListener != null) {
                    int beforeExpandedChildCount = getBeforeExpandedChildCount(
                            adapterParentPosition);
                    mParentExpandCollapseListener.onParentExpanded(
                            adapterParentPosition - beforeExpandedChildCount, byUser);
                }
            }
        }
    }

    /**
     * 折叠指定适配器位置所对应的父列表项
     * @param adapterParentPosition 父列表项在适配器里所对应的位置
     * @param parentWrapper 和父列表项相关的 {@link ParentWrapper}，用于获取该父列表项里的子列表项的信息
     * @param byUser 是否是用户通过点击展开父列表项的
     */
    private void collapseParentListItem(int adapterParentPosition, ParentWrapper parentWrapper,
            boolean byUser)
    {
        //保存该父列表项当前为折叠状态
        if (parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(false);
            List<?> childItemList = parentWrapper.getChildItemList(getParentPosition(adapterParentPosition));
            if (childItemList != null) {
                int collapsePosStart = adapterParentPosition + 1;
                int childItemCount = childItemList.size();
                //按照顺序依次将该父列表项下的子列表项移除
                mItemList.removeAll(childItemList);
                //通知 RecyclerView 指定位置有列表项已移除，刷新界面
                notifyItemRangeRemoved(collapsePosStart, childItemCount);

                if (mParentExpandCollapseListener != null) {
                    int beforeExpandedChildCount = getBeforeExpandedChildCount(
                            adapterParentPosition);
                    mParentExpandCollapseListener.onParentCollapsed(
                            adapterParentPosition - beforeExpandedChildCount, byUser);
                }
            }
        }
    }

    /**
     * 展开指定适配器位置所对应的父列表项。
     * @param adapterParentPosition 父列表项在适配器里所对应的位置
     * @param parentWrapper 和父列表项相关的 {@link ParentWrapper}，用于获取该父列表项里的子列表项的信息
     */
    private void expandViews(int adapterParentPosition, ParentWrapper parentWrapper) {

        for (RecyclerView recyclerView : mAttachedRecyclerViews) {

            PVH pvh = (PVH) recyclerView.findViewHolderForAdapterPosition(adapterParentPosition);

            Log.e(TAG,"****************expandViews---->"+"adapterParentPosition" +
                    "="+adapterParentPosition+"pvh==null?--->"+(pvh==null));

            if (pvh != null && !pvh.isExpanded()) {
                pvh.setExpanded(true);
                Log.e(TAG, "****************expandViews***********************");
            }

            expandParentListItem(adapterParentPosition, parentWrapper, false);
        }
    }

    /**
     * 根据指定的父列表项在父列表里的位置展开该位置在适配器里对应的父列表项
     * @param parentPosition 该父列表项在父列表里的位置
     */
    public void expandParent(int parentPosition) {
        if (parentPosition >= mParentListItems.size()) {
            return;
        }
        int adapterParentPosition = getAdapterParentPosition(parentPosition);
        if (adapterParentPosition!=-1) {
            Object listItem = mItemList.get(adapterParentPosition);
            if (listItem instanceof ParentWrapper) {
                ParentWrapper parentWrapper = (ParentWrapper) listItem;
                expandViews(adapterParentPosition, parentWrapper);
            }
        }
    }

    /**
     * 展开与{@link ParentListItem}相关的父列表项
     * @param parentListItem 与父列表项相关的 ParentListItem
     */
    public void expandParent(ParentListItem parentListItem) {
        if (parentListItem != null) {
            int parentPosition=mParentListItems.indexOf(parentListItem);
            expandParent(parentPosition);
        }
    }

    /**
     * 展开所有的父列表项
     */
    public void expandAllParent() {
        for (int i = 0; i < mParentListItems.size(); i++) {
            expandParent(i);
        }
    }

    /**
     * 折叠指定适配器位置所对应的父列表项。
     * @param adapterParentPosition 父列表项在适配器里所对应的位置
     * @param parentWrapper 和父列表项相关的 {@link ParentWrapper}，用于获取该父列表项里的子列表项的信息
     */
    private void collapseViews(int adapterParentPosition, ParentWrapper parentWrapper) {
        for (RecyclerView recyclerView : mAttachedRecyclerViews) {
            PVH pvh = (PVH) recyclerView.findViewHolderForAdapterPosition(adapterParentPosition);
            if (pvh != null && pvh.isExpanded()) {
                pvh.setExpanded(false);
            }
            collapseParentListItem(adapterParentPosition, parentWrapper, false);
        }
    }

    /**
     * 根据指定的父列表项在父列表里的位置折叠该位置在适配器里对应的父列表项
     * @param parentPosition 该父列表项在父列表里的位置
     */
    public void collapseParent(int parentPosition) {
        if (parentPosition >= mParentListItems.size()) {
            return;
        }
        int adapterParentPosition = getAdapterParentPosition(parentPosition);

        if (adapterParentPosition != -1) {
            Object listItem = mItemList.get(adapterParentPosition);

            if (listItem instanceof ParentWrapper) {
                ParentWrapper parentWrapper = (ParentWrapper) listItem;
                collapseViews(adapterParentPosition, parentWrapper);
            }
        }
    }

    /**
     * 折叠与{@link ParentListItem}相关的父列表项
     * @param parentListItem 与父列表项相关的 ParentListItem
     */
    public void collapseParent(ParentListItem parentListItem) {
        if (parentListItem != null) {
            int parentPosition=mParentListItems.indexOf(parentListItem);
            collapseParent(parentPosition);
        }
    }

    /**
     * 折叠所有的父列表项
     */
    public void collapseAllParent() {
        for (int i = 0; i < mParentListItems.size(); i++) {
            collapseParent(i);
        }
    }

    /**
     *  通知任何注册的监视器当前在 {@code parentPosition} 位置有新的父列表项插入，
     * 之前在该位置存在的父列表项将被移动到 {@code parentPosition + 1} 位置。
     * <p>
     * 这是数据结构上的变化，尽管位置变化了，但是之前存在的所有列表项在数据集里的数据都会被认为最新，
     * 因此这些列表项不会被重新绑定数据
     * </p>
     * @param parentPosition
     *                      要插入新的父列表项的位置
     *
     * @throws IndexOutOfBoundsException
     *                      {@code parentPosition >= ParentItemList.size() || parentPosition < 0}
     * @see #notifyParentItemRangeInserted(int, int)
     */
    public final void notifyParentItemInserted(int parentPosition) {

        if (parentPosition >= mParentListItems.size() || parentPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPosition + ", size is " + mParentListItems.size());
        }

        ParentListItem newParentListItem = mParentListItems.get(parentPosition);

        int parentWrapperPos;

        if (parentPosition < mParentListItems.size() - 1) {
            parentWrapperPos = getAdapterParentPosition(parentPosition);
        } else {
            parentWrapperPos = mItemList.size();
        }
        int insertItemCount = addParentWrapper(parentWrapperPos,newParentListItem);
        notifyItemRangeInserted(parentWrapperPos, insertItemCount);
    }

    /**
     * 通知任何注册的监视器当前在 {@code parentPosition,childPosition} 位置有新的子列表项插入，
     * 之前在该位置存在的子列表项将被移动到 {@code childPosition+1} 位置。
     * <p>
     * 这是数据结构上的变化，尽管位置变化了，但是之前存在的所有列表项在数据集里的数据都会被认为最新，
     * 因此这些列表项不会被重新绑定数据
     * </p>
     * @param parentPosition 要插入的子列表项所属的父列表项位置
     *
     * @param childPosition 要插入的子列表项的位置
     * @throws IndexOutOfBoundsException
     *                      {@code parentPosition >= ParentItemList.size() || parentPosition < 0 ||
     *                       childPosition >= ChildItemList.size() || childPosition < 0}
     * @see #notifyChildItemRangeInserted(int, int, int)
     */
    public final void notifyChildItemInserted(int parentPosition, int childPosition) {
        if (parentPosition >= mParentListItems.size() || parentPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPosition + ", size is " + mParentListItems.size());
        }

        int adapterParentPosition=getAdapterParentPosition(parentPosition);

        ParentWrapper parentWrapper= (ParentWrapper) mItemList.get(adapterParentPosition);

        List<?> childItemList=parentWrapper.getChildItemList(parentPosition);

        if (childItemList == null) {
            throw new NullPointerException("childList is NullPointer");
        }

        if (childPosition >= childItemList.size() || childPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + childPosition + ", size is " + childItemList.size());
        }

        int adapterChildPos;

        if (childPosition < childItemList.size() - 1) {
           adapterChildPos=getAdapterChildPosition(parentPosition,childPosition);
        } else {
            int adapterParentPos = getAdapterParentPosition(parentPosition);
            adapterChildPos = adapterParentPos + childItemList.size();
        }

        if (parentWrapper.isExpanded()) {
            Object newChildListItem = childItemList.get(childPosition);
            mItemList.add(adapterChildPos, newChildListItem);
            notifyItemInserted(adapterChildPos);
        }
    }

    /**
     * 通知任何注册的监视器当前在 {@code parentPositionStart} 位置有 {@code parentItemCount} 个父列表项插入，
     * 之前在该位置存在的父列表项将被移动到 {@code parentPosition + parentItemCount} 位置。
     * <p>
     * 这是数据结构上的变化，尽管位置变化了，但是之前存在的所有列表项在数据集里的数据都会被认为最新，
     * 因此这些列表项不会被重新绑定数据
     * </p>
     * @param parentPositionStart 要插入多个新父列表项的位置
     *
     * @throws IndexOutOfBoundsException
     *                      {@code parentPosition > size() - parentItemCount  || parentPosition < 0}
     * @param parentItemCount 要插入新父列表项的个数
     * @see #notifyParentItemInserted(int)
     */
    public final void notifyParentItemRangeInserted(int parentPositionStart, int parentItemCount) {
        if (parentPositionStart > mParentListItems.size() - parentItemCount ||
                parentPositionStart < 0)
        {
            throw new IllegalStateException("parentPositionStart must <= size() - parentItemCount" +
                    " && parentPositionStart >= 0");
        }
        int parentWrappersPos;
        if (parentPositionStart < mParentListItems.size() - parentItemCount) {
            parentWrappersPos = getAdapterParentPosition(parentPositionStart);
        } else {
            parentWrappersPos = mItemList.size();
        }
        int insertedItemCount = 0;
        for (int i = 0; i < parentItemCount; i++) {
            ParentListItem newParentListItem = mParentListItems.get(parentPositionStart + i);
            insertedItemCount += addParentWrapper(parentWrappersPos +insertedItemCount,
                    newParentListItem);
        }
        notifyItemRangeInserted(parentWrappersPos,insertedItemCount);
    }

    /**
     * 通知任何注册的监视器当前在 {@code parentPosition,childPositionStart} 位置有 {@code childItemCount} 个子列表项插入，
     * 之前在该位置存在的子列表项将被移动到 {@code childPositionStart + childItemCount} 位置。
     * <p>
     * 这是数据结构上的变化，尽管位置变化了，但是之前存在的所有列表项在数据集里的数据都会被认为最新，
     * 因此这些列表项不会被重新绑定数据
     * </p>
     *
     * @param parentPosition 要插入多个新子列表项所属的父列表项位置
     * @param childPositionStart 要插入多个新子列表项的位置
     * @param childItemCount 要插入的子列表项的个数
     * @throws IndexOutOfBoundsException
     *                      {@code parentPosition >= ParentItemList.size() || parentPosition < 0 ||
     *                       childPosition >= ChildItemList.size() || childPosition < 0}
     * @see #notifyChildItemInserted(int, int)
     */
    public final void notifyChildItemRangeInserted(int parentPosition, int childPositionStart,
            int childItemCount)
    {
        if (parentPosition >= mParentListItems.size() || parentPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPosition + ", size is " + mParentListItems.size());
        }

        int adapterParentPos = getAdapterParentPosition(parentPosition);

        ParentWrapper parentWrapper = (ParentWrapper) getListItem(adapterParentPos);

        List<?> childItemList = parentWrapper.getChildItemList(parentPosition);

        if (childItemList!=null) {

            if (childPositionStart > childItemList.size() - childItemCount ||
                    childPositionStart < 0)
            {
                throw new IllegalStateException(
                        "childPositionStart must <= ChildItemList.size() - childItemCount" +
                                " && childPositionStart >= 0");
            }

            List<?> insertedChildItemList = childItemList.subList(childPositionStart,
                    childItemCount + childPositionStart);

            int adapterChildPos;
            if (childPositionStart <childItemList.size()-childItemCount) {
                adapterChildPos=getAdapterChildPosition(parentPosition,childPositionStart);
            } else {
                adapterChildPos=adapterParentPos+childItemList.size()
                        -childItemCount+1;
            }
            if (parentWrapper.isExpanded()) {

                mItemList.addAll(adapterChildPos,insertedChildItemList);
                notifyItemRangeInserted(adapterChildPos,childItemCount);
            }
        }
    }

    /**
     * 通知任何注册的监视器移除当前在 {@code parentPosition} 的父列表项，
     * 之前在该位置之下并存在的所有父列表项将被移动到 {@code oldPosition - 1} 位置。
     * <p>
     * 这是数据结构上的变化，尽管位置变化了，但是之前存在的所有列表项在数据集里的数据都会被认为最新，
     * 因此这些列表项不会被重新绑定数据
     * </p>
     * @param parentPosition 要移除的父列表项在父列表里的位置
     *  @throws IndexOutOfBoundsException
     *                      {@code parentPosition > size() || parentPosition < 0}
     *  @see #notifyParentItemRangeRemoved(int, int)
     */
    public final void notifyParentItemRemoved(int parentPosition) {
        if (parentPosition > mParentListItems.size() || parentPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPosition + ", size is " + mParentListItems.size());
        }

        int adapterParentPos = getAdapterParentPosition(parentPosition);
        if (adapterParentPos != -1) {
            ParentWrapper parentWrapper = (ParentWrapper) getListItem(adapterParentPos);
            LogUtil.LogE(TAG,"adapterParentPos--->"+adapterParentPos);
            if (parentWrapper != null) {
                int removeItemCount=1;
                if (parentWrapper.isExpanded()) {
                    List<?> childItemList = parentWrapper.getChildItemList(parentPosition);
                    LogUtil.LogE(TAG,"childItemList==null-------?"+(childItemList==null));
                    if (childItemList != null) {
                        int childItemCount = childItemList.size();
                        mItemList.removeAll(childItemList);
                        removeItemCount+=childItemCount;
                    }
                }
                mItemList.remove(parentWrapper);
                notifyItemRangeRemoved(adapterParentPos, removeItemCount);
            }
        }
    }

    /**
     * 通知任何注册的监视器移除当前在 {@code parentPosition,childPosition} 位置的子列表项，
     * 之前在该位置之下并存在的所有子列表项将被移动到 {@code oldPosition - 1} 位置。
     * <p>
     * 这是数据结构上的变化，尽管位置变化了，但是之前存在的所有列表项在数据集里的数据都会被认为最新，
     * 因此这些列表项不会被重新绑定数据
     * </p>
     * @param parentPosition 要移除的子列表项所属的父列表项的位置
     * @param childPosition 要移除的子列表项的位置
     * @throws IndexOutOfBoundsException
     *                      {@code parentPosition >= ParentItemList.size() || parentPosition < 0 ||
     *                       childPosition > ChildItemList.size() || childPosition < 0}
     * @see #notifyChildItemRangeRemoved(int, int, int)
     */
    public final void notifyChildItemRemoved(int parentPosition, int childPosition) {
        if (parentPosition >= mParentListItems.size() || parentPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPosition + ", size is " + mParentListItems.size());
        }
        int adapterParentPos = getAdapterParentPosition(parentPosition);
        if (adapterParentPos!=-1) {
           ParentWrapper parentWrapper= (ParentWrapper) getListItem(adapterParentPos);
            if (parentWrapper!=null) {
                List<?> childItemList=parentWrapper.getChildItemList(parentPosition);
                if (childItemList!=null) {
                    int childItemCount=childItemList.size();
                    if (childPosition > childItemCount || childPosition < 0) {
                        throw new IllegalStateException(
                                "childPosition must <= size()" +
                                        " && childPosition >= 0");
                    }
                    int adapterChildPos=getAdapterChildPosition(parentPosition,childPosition);
                    mItemList.remove(adapterChildPos);
                    notifyItemRemoved(adapterChildPos);
                }
            }
        }
    }

    /**
     * 通知任何注册的监视器移除当前从 {@code parentPositionStart} 位置开始 {@code parentItemCount} 个 的父列表项，
     * 之前在该位置之下并存在的所有父列表项将被移动到 {@code oldPosition - parentItemCount} 位置。
     * <p>
     * 这是数据结构上的变化，尽管位置变化了，但是之前存在的所有列表项在数据集里的数据都会被认为最新，
     * 因此这些列表项不会被重新绑定数据
     * </p>
     * @param parentPositionStart 移除多个父列表项开始的位置
     * @param parentItemCount 移除父列表项的个数
     * @throws IndexOutOfBoundsException
     *                      {@code parentPosition > size() || parentPosition < 0}
     * @see #notifyParentItemRemoved(int)
     */
    public final void notifyParentItemRangeRemoved(int parentPositionStart, int parentItemCount) {
        if (parentPositionStart > mParentListItems.size() || parentPositionStart < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPositionStart + ", size is " + mParentListItems.size());
        }

        int removedItemCount=0;

        int adapterParentPosStart = getAdapterParentPosition(parentPositionStart);

        if (adapterParentPosStart != -1) {
            for (int i = 0; i < parentItemCount; i++) {
                    ParentWrapper parentWrapper = (ParentWrapper) getListItem(adapterParentPosStart);
                    if (parentWrapper != null) {
                        if (parentWrapper.isExpanded()) {
                            List<?> childItemList = parentWrapper.getChildItemList
                                    (getParentPosition(adapterParentPosStart));
                            if (childItemList != null) {
                                int childItemCount = childItemList.size();
                                mItemList.removeAll(childItemList);
                                removedItemCount += childItemCount;
                            }
                        }
                        mItemList.remove(parentWrapper);
                        removedItemCount++;
                    }
            }
            notifyItemRangeRemoved(adapterParentPosStart, removedItemCount);
        }
    }

    /**
     * 通知任何注册的监视器移除当前从 {@code parentPosition,childPositionStart} 位置 {@code childItemCount} 个子列表项，
     * 之前在该位置之下并存在的所有子列表项将被移动到 {@code oldPosition - childItemCount} 位置。
     * <p>
     * 这是数据结构上的变化，尽管位置变化了，但是之前存在的所有列表项在数据集里的数据都会被认为最新，
     * 因此这些列表项不会被重新绑定数据
     * </p>
     * @param parentPosition 要移除的多个子列表项所属的父列表项的位置
     * @param childPositionStart 移除多个子列表项所开始的位置
     * @param childItemCount 移除的子列表项的个数
     * @throws IndexOutOfBoundsException
     *                      {@code parentPosition >= ParentItemList.size() || parentPosition < 0 ||
     *                       childPositionStart > ChildItemList.size() || childPositionStart < 0}
     * @see #notifyChildItemRemoved(int, int)
     */
    public final void notifyChildItemRangeRemoved(int parentPosition, int childPositionStart,
            int childItemCount)
    {
        if (parentPosition >= mParentListItems.size() || parentPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPosition + ", size is " + mParentListItems.size());
        }

        int adapterParentPos = getAdapterParentPosition(parentPosition);

        if (adapterParentPos != -1) {
            ParentWrapper parentWrapper = (ParentWrapper) getListItem(adapterParentPos);
            if (parentWrapper != null) {
                List<?> childItemList = parentWrapper.getChildItemList(parentPosition);
                if (childItemList != null) {

                    int childCount = childItemList.size();

                    if (childPositionStart > childCount || childPositionStart < 0) {
                        throw new IllegalStateException(
                                "childPositionStart must <= size()" +
                                        " && childPositionStart >= 0");
                    }
                    int adapterChildPosStart = getAdapterChildPosition(parentPosition,
                            childPositionStart);
                    for (int i = 0; i < childItemCount; i++) {
                        mItemList.remove(adapterChildPosStart);
                    }
                    Log.i(TAG,"adapterChildPosStart="+adapterChildPosStart+"," +
                            "childItemCount="+childItemCount);
                    notifyItemRangeRemoved(adapterChildPosStart,childItemCount);
                }
            }
        }
    }

    /**
     * @param parentPosition
     */
    public final void notifyParentItemChanged(int parentPosition) {
        if (parentPosition >= mParentListItems.size() || parentPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPosition + ", size is " + mParentListItems.size());
        }

        ParentListItem changedParentListItem=mParentListItems.get(parentPosition);
        if (changedParentListItem!=null) {
            int adapterParentPos=getAdapterParentPosition(parentPosition);
            if (adapterParentPos!=-1) {
               ParentWrapper parentWrapper= (ParentWrapper) getListItem(adapterParentPos);
                if (parentWrapper!=null) {

                    if (changedParentListItem.isInitiallyExpanded() &&
                            !parentWrapper.isExpanded())
                    {
                        expandViews(adapterParentPos,parentWrapper);
                    } else if (!changedParentListItem.isInitiallyExpanded() &&
                            parentWrapper.isExpanded())
                    {
                        collapseViews(adapterParentPos,parentWrapper);
                    }

                    parentWrapper.setParentListItem(changedParentListItem);
                    notifyItemChanged(adapterParentPos);
                }
            }
        }
    }

    public final void notifyChildItemChanged(int parentPosition, int childPosition) {
        if (parentPosition >= mParentListItems.size() || parentPosition < 0) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + parentPosition + ", size is " + mParentListItems.size());
        }
        int adapterParentPos=getAdapterParentPosition(parentPosition);
        if (adapterParentPos!=-1) {
           ParentWrapper parentWrapper= (ParentWrapper) getListItem(adapterParentPos);
            if (parentWrapper!=null) {
                if (parentWrapper.isInitiallyExpanded() &&
                        !parentWrapper.isExpanded())
                {
                    expandViews(adapterParentPos,parentWrapper);
                } else if (!parentWrapper.isInitiallyExpanded() &&
                        parentWrapper.isExpanded())
                {
                    collapseViews(adapterParentPos,parentWrapper);
                }

                List<?> childItemList=parentWrapper.getChildItemList(parentPosition);
                if (childItemList!=null) {
                    int childCount=childItemList.size();
                    if (childPosition >= childCount || childPosition < 0) {
                        throw new IndexOutOfBoundsException(
                                "Invalid index " + childPosition + ", size is " + childCount);
                    }
                    Object changedChildItem=childItemList.get(childPosition);
                    int adapterChildPos=getAdapterChildPosition(parentPosition,childPosition);
                    mItemList.set(adapterChildPos,changedChildItem);
                    notifyItemChanged(adapterChildPos);
                }
            }
        }
    }

    /**
     * 通知
     *
     * @param fromParentPosition
     * @param toParentPosition
     */
    public final void notifyParentItemMoved(int fromParentPosition, int toParentPosition)
    {
        int fromParentAdapterPos = getAdapterParentPosition(fromParentPosition);

        int parentAdapterPos = getAdapterParentPosition(toParentPosition);
        int toParentAdapterPos = parentAdapterPos == -1 ? mItemList.size() : parentAdapterPos;

        if (fromParentAdapterPos != -1) {

            boolean movedToBottom = fromParentPosition < toParentPosition;

            ParentWrapper fromParentWrapper = (ParentWrapper) getListItem(fromParentAdapterPos);

            if (fromParentWrapper != null) {

                mItemList.add(toParentAdapterPos, fromParentWrapper);

                mItemList.remove(movedToBottom ? fromParentAdapterPos : fromParentAdapterPos + 1);

                notifyItemMoved(fromParentAdapterPos, toParentAdapterPos);

                List<?> fromChildItemList = fromParentWrapper.getChildItemList(fromParentPosition);

                if (fromChildItemList != null && fromParentWrapper.isExpanded()) {
                    for (int i = 0; i < fromChildItemList.size(); i++) {

                        Object childListItem = fromChildItemList.get(i);

                        mItemList.add(
                                movedToBottom ? toParentAdapterPos : toParentAdapterPos + i + 1,
                                childListItem);

                        mItemList.remove(movedToBottom ? fromParentAdapterPos
                                : fromParentAdapterPos + i + 2);

                        notifyItemMoved(
                                movedToBottom ? fromParentAdapterPos : fromParentAdapterPos + i + 1,
                                movedToBottom ? toParentAdapterPos : toParentAdapterPos + i + 1);
                    }
                }
            }
        }
    }

}

