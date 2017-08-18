//package com.biu.xq.adapter;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.biu.xq.R;
//import com.biu.xq.communication.Communications;
//import com.biu.xq.fragment.BaseFragment;
//import com.biu.xq.model.CardDetailChildItem;
//import com.biu.xq.model.CardDetailParentItem;
//import com.biu.xq.util.LogUtil;
//import com.biu.xq.widget.expandablerecyclerview.adapter.ExpandableRecyclerViewAdapter;
//import com.biu.xq.widget.expandablerecyclerview.model.ParentListItem;
//import com.biu.xq.widget.expandablerecyclerview.viewholder.ChildViewHolder;
//import com.biu.xq.widget.expandablerecyclerview.viewholder.ParentViewHolder;
//import com.biu.xq.widget.expandablerecyclerview.viewholder.ViewHolderCallbacks;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by jhj_Plus on 2016/1/13.
// */
//public class CardDetailAdapter extends ExpandableRecyclerViewAdapter {
//
//    private static final String TAG = "CardDetailAdapter";
//
//    private static final int NO_FROM_ID=-1;
//
//    private static final int NO_USER_ID=-1;
//
//    private static final int TYPE_PARENT_CARD=0x10;
//
//    private static final int TYPE_PARENT_COMMENT=0x20;
//
//    private static final int TYPE_CHILD_CARD=0x10;
//
//    private static final int TYPE_CHILD_COMMENT=0x20;
//
//    private static final int TYPE_CHILD_COMMENT_LAST=0x30;
//
//    private BaseFragment mBaseFragment;
//    private Context mContext;
//
//    private LayoutInflater mLayoutInflater;
//
//    private List<CardDetailParentItem> mItems;
//
//    public CardDetailAdapter(BaseFragment baseFragment,List<CardDetailParentItem> parentListItems) {
//        super(parentListItems);
//        mItems= parentListItems;
//        mBaseFragment=baseFragment;
//        mContext = baseFragment.getActivity();
//        mLayoutInflater = LayoutInflater.from(mContext);
//    }
//
//    public int getCount() {
//        return mItems.size();
//    }
//
//    public void setData(List<CardDetailParentItem> data) {
//        super.setParentListItems(data);
//        mItems=  data;
//        notifyDataSetChanged();
//    }
//
//
//    public List<? extends ParentListItem> getData(){
//        return mItems;
//    }
//
//    public void addParent(int position, CardDetailParentItem parentItem) {
//        if (parentItem != null) {
//            mItems.add(position, parentItem);
//            notifyParentItemInserted(position);
//        }
//    }
//
//    public void addParentLast(CardDetailParentItem parentItem) {
//        if (parentItem != null) {
//            int insertPos = mItems.size();
//            mItems.add(insertPos, parentItem);
//            notifyParentItemInserted(insertPos);
//        }
//    }
//
//    public void addChild(int parentPosition, int childPosition, CardDetailChildItem child) {
//        CardDetailParentItem parentItem=mItems.get(parentPosition);
//        List<CardDetailChildItem> childItemList = parentItem.getChildItemList(
//                parentPosition);
//        if (childItemList == null) {
//            childItemList = new ArrayList<>();
//            parentItem.setCardDetailChildItems(childItemList);
//            //为了影藏父评论的分割线
//            parentItem.setHasChildComment(true);
//            notifyParentItemChanged(parentPosition);
//        }
//        childItemList.add(childPosition, child);
//        notifyChildItemInserted(parentPosition, childPosition);
//    }
//
//    public void addChildLast(int parentPosition, CardDetailChildItem child) {
//        CardDetailParentItem parentItem=mItems.get(parentPosition);
//        List<CardDetailChildItem> childItemList = parentItem.getChildItemList(
//                parentPosition);
//        if (childItemList == null) {
//            childItemList = new ArrayList<>();
//            parentItem.setCardDetailChildItems(childItemList);
//            //为了影藏父评论的分割线
//            parentItem.setHasChildComment(true);
//            notifyParentItemChanged(parentPosition);
//        }
//        int insertPos = childItemList.size();
//        childItemList.add(insertPos, child);
//        notifyChildItemInserted(parentPosition, insertPos);
//    }
//
//    public void addAllData(List<CardDetailParentItem> data) {
//        if (data != null) {
//            int parentPositionStart = mItems.size();
//            int parentItemCount = data.size();
//            mItems.addAll(data);
//            notifyParentItemRangeInserted(parentPositionStart, parentItemCount);
//        }
//    }
//
//    public void removeData(int positionStart, int itemCount) {
//        List<CardDetailParentItem> parentItems = mItems.subList(positionStart,positionStart+itemCount);
//        mItems.removeAll(parentItems);
//        notifyParentItemRangeRemoved(positionStart,itemCount);
//    }
//
//
//    /**
//     * 注意：必须先通知清除所有的列表项再清除本地列表里所有数据，否则本地会报错，应为RecyclerView
//     * 在清除所有 ItemView 时还会最后调用一次 getItemType 导致本地从本地数据集中获取数据时
//     * 本地数据集List会因size是0而报错
//     */
//    public void removeAllData() {
//        notifyParentItemRangeRemoved(0, mItems.size());
//        mItems.clear();
//    }
//
//
//    @Override
//    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
//        ParentViewHolder parentViewHolder=null;
//        if (parentType==TYPE_PARENT_CARD) {
//            parentViewHolder = new ParentViewHolder(
//                    mLayoutInflater.inflate(R.layout.item_card_detail_card_parent, parent, false),
//                    this, new ViewHolderCallbacks() {
//
//              @Override
//              public int[] getNeedRegisterClickListenerChildViewIds() {
//                  return new int[]{R.id.iv_head_portrait};
//              }
//
//              @Override
//              public void onClick(RecyclerView.ViewHolder viewHolder, View view, int position,
//                      int adapterPosition, int parentPosition, int parentAdapterPosition)
//              {
////                  final CardDetailParentItem parentItem= mItems.get(position);
////
////                  int id=view.getId();
////
////                  if (id==R.id.iv_head_portrait) {
////                      final  int userId=parentItem.getUser_id();
////                      final  int myId= MyApplication.getUserInfo(mContext).getId();
////                      if (userId==myId) {
////                          Intent intent=new Intent(mContext, MainActivity.class);
////                          intent.putExtra(MainActivity.EXTRA_FRAGMENT_POSITION,4);
////                          mContext.startActivity(intent);
////                      } else {
////                          Intent intent=new Intent(mContext, PersonalCenterOthersActivity.class);
////                          intent.putExtra(PersonalCenterOthersActivity.EXTRA_USER_ID,userId);
////                          mContext.startActivity(intent);
////                      }
////                  }
//              }
//
//                @Override
//                public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {
////                    if (data == null) {
////                        return;
////                    }
////                    if (data instanceof CardDetailParentItem &&
////                            viewHolder instanceof ParentViewHolder)
////                    {
////                        CardDetailParentItem parentItem = (CardDetailParentItem) data;
////                        ParentViewHolder holder = (ParentViewHolder) viewHolder;
////                        ImageView iv_head_portrait = (ImageView) holder.getView(
////                                R.id.iv_head_portrait);
////                        TextView tv_nickname = (TextView) holder.getView(R.id.tv_nickname);
////                        TextView tv_date = (TextView) holder.getView(R.id.tv_date);
////                        TextView tv_title = (TextView) holder.getView(R.id.tv_title);
////                        TextView tv_content = (TextView) holder.getView(R.id.tv_content);
////
////
////                        Communications.setNetImage(parentItem.getHeadUrl(), iv_head_portrait, 0, 0);
////                        tv_nickname.setText(parentItem.getName());
////                        tv_date.setText(Utils.getReleaseTime(new Date(parentItem.getTime())));
////                        tv_title.setText(parentItem.getTitle());
////                        tv_content.setText(parentItem.getContent());
////                    }
//                }
//          });
//        } else if (parentType==TYPE_PARENT_COMMENT) {
//          parentViewHolder=new ParentViewHolder(
//                  mLayoutInflater.inflate(R.layout.item_card_detail_comment_parent, parent, false),
//                  this, new ViewHolderCallbacks() {
//              @Override
//              public int[] getNeedRegisterClickListenerChildViewIds() {
//                  return new int[]{R.id.iv_head_portrait,R.id.ib_comment};
//              }
//
//              @Override
//              public void onClick(RecyclerView.ViewHolder viewHolder, View view, final int position,
//                      int adapterPosition, int parentPosition, int parentAdapterPosition)
//              {
//
////                  final CardDetailParentItem parentItem= mItems.get(position);
////
////                  int id=view.getId();
////
////                  if (id==R.id.iv_head_portrait) {
////                      final  int userId=parentItem.getUser_id();
////                      final  int myId=MyApplication.getUserInfo(mContext).getId();
////                      if (userId==myId) {
////                         Intent intent=new Intent(mContext, MainActivity.class);
////                          intent.putExtra(MainActivity.EXTRA_FRAGMENT_POSITION,4);
////                          mContext.startActivity(intent);
////                      } else {
////                          Intent intent=new Intent(mContext, PersonalCenterOthersActivity.class);
////                          intent.putExtra(PersonalCenterOthersActivity.EXTRA_USER_ID,userId);
////                          mContext.startActivity(intent);
////                      }
////                  } else if (id == R.id.ib_comment) {
////                    //  OtherUtil.showToast(mContext, "Comment");
////                      OtherUtil.showReplyWindow(mContext, new DialogFactory.DialogListener() {
////                          @Override
////                          public void OnInitViewListener(View v, final Dialog dialog) {
////                              final Button btn_send= (Button) v.findViewById(R.id.send);
////                              final EditText et_reply= (EditText) v.findViewById(R.id.reply_content);
////                              et_reply.addTextChangedListener(new TextWatcher() {
////                                  @Override
////                                  public void beforeTextChanged(CharSequence s, int start,
////                                          int count, int after)
////                                  {
////
////                                  }
////
////                                  @Override
////                                  public void onTextChanged(CharSequence s, int start, int before,
////                                          int count)
////                                  {
////                                       btn_send.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
////                                  }
////
////                                  @Override
////                                  public void afterTextChanged(Editable s) {
////
////                                  }
////                              });
////
////                              btn_send.setOnClickListener(new View.OnClickListener() {
////                                  @Override
////                                  public void onClick(View v) {
////                                      UserInfoBean userInfoVO= MyApplication.getUserInfo(mContext);
////                                      String reply = et_reply.getText().toString();
////                                      String fromName=userInfoVO!=null?userInfoVO.getUsername()
////                                              :"error";
//////                                      String toName =
//////                                              parentItem != null ? parentItem.getName() : "";
////                                      int toId = parentItem != null ? parentItem.getId() : -1;
////                                      int myId=userInfoVO!=null?userInfoVO.getId():-1;
////                                      uploadCommentData(position,reply, fromName,null,
////                                              NO_FROM_ID, toId,NO_USER_ID,myId);
////
////                                      dialog.dismiss();
////                                  }
////                              });
////                          }
////                      });
////                  }
//              }
//
//              @Override
//              public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {
////                  if (data == null) {
////                      return;
////                  }
////                  if (data instanceof CardDetailParentItem &&
////                          viewHolder instanceof ParentViewHolder)
////                  {
//                      CardDetailParentItem commentItem = (CardDetailParentItem) data;
//                      ParentViewHolder holder = (ParentViewHolder) viewHolder;
////                      ImageView iv_head_portrait = (ImageView) holder.getView(R.id
////                              .iv_head_portrait);
////                      TextView tv_nickname = (TextView) holder.getView(R.id.tv_nickname);
////                      TextView tv_date = (TextView) holder.getView(R.id.tv_date);
////                      TextView tv_comment_parent = (TextView) holder.getView(R.id
////                              .tv_comment_parent);
////
////                      Communications.setNetImage(commentItem.getHeadUrl(), iv_head_portrait,
////                              0,0);
////                      tv_nickname.setText(commentItem.getName());
////                      tv_date.setText(Utils.getReleaseTime(new Date(commentItem.getTime())));
////                      tv_comment_parent.setText(commentItem.getContent());
////
//                      View divider=holder.getView(R.id.divider);
//                      divider.setVisibility(commentItem.isHasChildComment() ? View.GONE : View
//                           .VISIBLE);
////
////                  }
//              }
//          });
//        }
//        return parentViewHolder;
//    }
//
//    /**
//     * 上传评论数据
//     * @param parentPosition
//     * @param reply
//     * @param fromName
//     * @param toName
//     * @param fromId
//     * @param toId
//     * @param toUserId
//     * @param myId
//     */
//    private void uploadCommentData(final int parentPosition, final String reply,
//            final String fromName, final String toName, int fromId, int toId, int toUserId,
//            final int myId)
//    {
//
////        mBaseFragment.showProgress(TAG);
////
////        String token = PreferencesUtils.getString(mContext, PreferencesUtils.KEY_TOKEN);
////        final Map<String, Object> params = new HashMap<>();
////        if (fromId!=NO_FROM_ID) {
////          params.put("comment_id",fromId+"");
////        }
////        if (toUserId!=NO_USER_ID) {
////            params.put("to_user_id",toUserId+"");
////        }
////        params.put("comment", reply);
////        params.put("evaluate_id",toId+"");
////
////
////        Communications.stringRequestData(true, false, token, params, Constant.URL_COMMENT_CHILD, Request.Method.POST, TAG,
////                new RequestCallBack() {
////                    @Override
////                    public void onResponse(JSONObject response) {
////                        LogUtil.LogE(TAG,"response---->"+ JSONUtil.prettyPrintJsonString
////                                (1,response));
////                        int statusCode= JSONUtil.getInt(response,"key");
////                        if (statusCode==1) {
////                            JSONObject map=JSONUtil.getJSONObject(response,"map");
////                            int comment_id=JSONUtil.getInt(map,"id");
////                            ReplyItem replyItem = new ReplyItem();
////                            replyItem.setUsername(fromName);
////                            replyItem.setTo_name(toName);
////                            replyItem.setComment(reply);
////                            replyItem.setId(comment_id);
////
////                            //TODO 待检查
////                            replyItem.setUser_id(myId);
////                            //添加一个子评论
////                            CardDetailChildItem childItem=new CardDetailChildItem();
////                            childItem.setReplyItem(replyItem);
////                            addChildLast(parentPosition,childItem);
////                        }
////                        OtherUtil.showToast(mContext,statusCode==1?"子评论添加成功":"子评论添加失败");
////                        mBaseFragment.dismissProgress();
////                    }
////
////                    @Override
////                    public void onErrorResponse(String errorInfo) {
////                        LogUtil.LogE(TAG,"onErrorResponse---->"+errorInfo);
////                        OtherUtil.showToast(mContext,"子评论添加失败");
////                        mBaseFragment.dismissProgress();
////                    }
////
////                    @Override
////                    public void onUnLogin() {
////                        LogUtil.LogE(TAG,"onUnLogin---->");
////                        mBaseFragment.dismissProgress();
////                        mBaseFragment.showUnloginSnackbar();
////                    }
////                });
//
//    }
//
//    @Override
//    public ChildViewHolder onCreateChildViewHolder(ViewGroup child, final int childType) {
//        ChildViewHolder childViewHolder=null;
//        if (childType==TYPE_CHILD_CARD) {
//            childViewHolder=new ChildViewHolder(
//                    mLayoutInflater.inflate(R.layout.item_card_detail_card_child, child, false),
//                    this, new ViewHolderCallbacks() {
//                @Override
//                public int[] getNeedRegisterClickListenerChildViewIds() {
//                    return null;
//                }
//
//                @Override
//                public void onClick(RecyclerView.ViewHolder viewHolder, View view, int position,
//                        int adapterPosition, int parentPosition, int parentAdapterPosition)
//                {
////                    CardDetailParentItem parentItem=mItems.get(parentPosition);
////
////                    List<CardDetailChildItem> childItems=parentItem.getCardDetailChildItems();
////
////                    String[] imageUrls=new String[childItems.size()];
////
////                    for (int i = 0; i < childItems.size(); i++) {
////                        imageUrls[i]=childItems.get(i).getImageItem().getCompress_img();
////                    }
////
////                    //TODO
////                    Intent intent=new Intent(mContext, ImageBrowseActivity.class);
////                    intent.putExtra(ImageBrowseActivity.EXTRA_IMAGES,imageUrls);
////                    intent.putExtra(ImageBrowseActivity.EXTRA_CURR_IMAGE_POSITION,position);
////                    mContext.startActivity(intent);
////                    OtherUtil.showToast(mContext,"Image Clicked"+"\n"+",position="+position+"," +
////                            "adapterPosition"
////                    +adapterPosition+",parentPosition="+parentPosition+",parentAdapterPosition"+parentAdapterPosition);
//                }
//
//                @Override
//                public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {
////                    if (data==null) {
////                      return;
////                    }
////                    if (data instanceof CardDetailChildItem && viewHolder instanceof ChildViewHolder) {
////                        ImageItem imageItem=((CardDetailChildItem) data).getImageItem();
////                        ChildViewHolder holder= (ChildViewHolder) viewHolder;
////                        ImageView imageView = (ImageView) holder.getView(R.id.iv_card_image);
////                        Communications.setNetImage(imageItem.getCompress_img(),
////                                imageView,
////                                0,0);
////                    }
//                }
//
//            });
//            //设置展示图片的ItemView可点击
//            childViewHolder.setRegisterClickListener(true);
//
//        } else if (childType==TYPE_CHILD_COMMENT||childType==TYPE_CHILD_COMMENT_LAST) {
//             final boolean commentLast=childType==TYPE_CHILD_COMMENT_LAST;
//             childViewHolder=new ChildViewHolder(
//                     mLayoutInflater.inflate(childType==TYPE_CHILD_COMMENT?R.layout
//                             .item_card_detail_comment_child:R
//                             .layout.item_card_detail_comment_child_last, child, false),
//                     this, new ViewHolderCallbacks() {
//                 @Override
//                 public int[] getNeedRegisterClickListenerChildViewIds() {
//                     return commentLast ? new int[]{R.id.tv_load_more} : null;
//                 }
//
//                 @Override
//                 public void onClick(RecyclerView.ViewHolder viewHolder, View view, int position,
//                         int adapterPosition, final int parentPosition, int parentAdapterPosition)
//                 {
////                     OtherUtil.showToast(mContext,childType==TYPE_CHILD_COMMENT?"Comment " +
////                             "Clicked"+"\n"+",position="+position+"," +
////                             "adapterPosition"
////                             +adapterPosition+",parentPosition="+parentPosition+",parentAdapterPosition"+parentAdapterPosition
////                     :view.getId()==R.id.tv_load_more?"Load More Child":"Comment Last Clicked");
//
////                     final CardDetailParentItem parentItem= mItems.get(parentPosition);
////
////                     int viewId=view.getId();
////                     if (viewId==R.id.tv_load_more) {
////                         //TODO 跳转到二级评论页面
////                         Intent intent=new Intent(mContext, CommentChildListActivity.class);
////                         intent.putExtra(CommentChildListActivity.EXTRA_COMMENT_PARENT,parentItem);
////                         mContext.startActivity(intent);
////                         return;
////                     }
////
////
////                     final  List<CardDetailChildItem> childItems=parentItem!=null?parentItem
////                             .getCardDetailChildItems():null;
////                     final ReplyItem replyItem = childItems != null ? childItems.get(position)
////                             .getReplyItem() : null;
////
////                     OtherUtil.showReplyWindow(mContext, new DialogFactory.DialogListener() {
////                         @Override
////                         public void OnInitViewListener(View v, final Dialog dialog) {
////                             final Button btn_send= (Button) v.findViewById(R.id.send);
////                             final EditText et_reply= (EditText) v.findViewById(R.id.reply_content);
////                             et_reply.addTextChangedListener(new TextWatcher() {
////                                 @Override
////                                 public void beforeTextChanged(CharSequence s, int start,
////                                         int count, int after)
////                                 {
////
////                                 }
////
////                                 @Override
////                                 public void onTextChanged(CharSequence s, int start, int before,
////                                         int count)
////                                 {
////                                     btn_send.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
////                                 }
////
////                                 @Override
////                                 public void afterTextChanged(Editable s) {
////
////                                 }
////                             });
////
////                             btn_send.setOnClickListener(new View.OnClickListener() {
////                                 @Override
////                                 public void onClick(View v) {
////                                     UserInfoBean userInfoVO= MyApplication.getUserInfo(mContext);
////                                     String reply = et_reply.getText().toString();
////                                     String fromName=userInfoVO!=null?userInfoVO.getUsername()
////                                             :"error";
////                                     String toName =
////                                             replyItem != null ? replyItem.getUsername() : "";
////                                     int fromId = replyItem != null ? replyItem.getId() : -1;
////                                     int toId = parentItem != null ? parentItem.getId() : -1;
////                                     int toUserId=replyItem!=null?replyItem.getUser_id():-1;
////                                     int myId=userInfoVO!=null?userInfoVO.getId():-1;
////
////                                     LogUtil.LogE(TAG,"toUserId====>"+toUserId);
////
////                                     uploadCommentData(parentPosition, reply, fromName, toName,
////                                             fromId,
////                                             toId,toUserId,myId);
////                                     dialog.dismiss();
////                                 }
////                             });
////                         }
////                     });
//                 }
//
//                 @Override
//                 public void bindData(RecyclerView.ViewHolder viewHolder, Object data) {
////                     if (data==null) {
////                         return;
////                     }
////                     if (data instanceof CardDetailChildItem && viewHolder instanceof ChildViewHolder) {
////                         ReplyItem replyItem=((CardDetailChildItem) data).getReplyItem();
//                         ChildViewHolder holder= (ChildViewHolder) viewHolder;
////
//                         if (commentLast) {
//                             int parentPosition=getParentPosition(viewHolder.getAdapterPosition());
//                             final CardDetailParentItem parentItem= mItems.get(parentPosition);
//                             TextView tv_load_more= (TextView) holder.getView(R.id.tv_load_more);
//                             tv_load_more.setVisibility(
//                                     parentItem.isHasMoreChildComment() ? View.VISIBLE : View.GONE);
//                         }
////
////                         TextView tv_comment_child = (TextView) holder.getView(R.id
////                                 .tv_comment_child);
////
////                         //根据被回复者的姓名是否为 null 判断子回复者是回复主评论还是子评论
////                         //回复主评论格式：回复者名：回复内容
////                         //回复子评论格式：回复者名 回复 被回复者：回复内容
////                         int toUserId=replyItem.getTo_user_id();//TODO 据此判断是回复格式
////                         String toUserName=replyItem.getTo_name();
////
////                         String replyForm=mContext.getString(R.string.form_reply);
////                         String replyForm2=mContext.getString(R.string.form_reply2);
////                         String commentFrom=replyItem.getUsername();
////                         String commentTo=replyItem.getTo_name();
////                         String comment=replyItem.getComment();
////                         tv_comment_child.setText(toUserName!=null?String.format(replyForm,
////                                 commentFrom,
////                                 commentTo, comment):String.format(replyForm2,commentFrom,comment));
////                     }
//                 }
//             });
//            childViewHolder.setRegisterClickListener(true);
//
//        }
//        return childViewHolder;
//    }
//
//    @Override
//    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int adapterParentPosition,
//            int parentPosition, ParentListItem parentListItem)
//    {
//        parentViewHolder.bindData(parentListItem);
//    }
//
//    @Override
//    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int adapterChildPosition,
//            int parentPosition, int childPosition, Object childListItem)
//    {
//        childViewHolder.bindData(childListItem);
//    }
//
//    @Override
//    public int getParentType(int parentPosition) {
//        LogUtil.LogE(TAG,"parentPosition="+parentPosition);
//        CardDetailParentItem parentItem=mItems.get(parentPosition);
//        return parentPosition == 0 && parentItem.isCard() ? TYPE_PARENT_CARD : TYPE_PARENT_COMMENT;
//    }
//
//    @Override
//    public int getChildType(int parentPosition, int childPosition) {
//        LogUtil.LogE(TAG,"getChildType---->"+parentPosition+","+childPosition);
//
//        CardDetailParentItem parentItem=mItems.get(parentPosition);
//        return parentPosition == 0 && parentItem.isCard() ? TYPE_CHILD_CARD
//                : childPosition == parentItem.getChildItemList(parentPosition)
//                        .size() - 1 ? TYPE_CHILD_COMMENT_LAST : TYPE_CHILD_COMMENT;
//    }
//
//    public ItemDecoration getItemDecoration() {
//        return new ItemDecoration();
//    }
//
//    public  class ItemDecoration extends RecyclerView.ItemDecoration {
//
//        private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        public ItemDecoration() {
//            mPaint.setColor(mContext.getResources().getColor(R.color.grey_light));
//        }
//
//        @Override
//        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//
//        }
//
//        @Override
//        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
//                RecyclerView.State state)
//        {
//            int childAdapterPosition = parent.getChildAdapterPosition(view);
//
//            if (childAdapterPosition!= RecyclerView.NO_POSITION) {
//                int parentPosition=getParentPosition(childAdapterPosition);
//
//                if (parentPosition == 0) {
//                    CardDetailParentItem parentItem=mItems.get(parentPosition);
//                    if (parentItem.isCard()) {
//                        List<CardDetailChildItem> mImages = mItems.get(0)
//                                .getChildItemList(0);
//                        boolean hasImage=mImages!=null&&mImages.size()>0;
//
//                        int childPosition = getChildPosition(childAdapterPosition);
//
//                        outRect.set(0, 0, 0,
//                                (hasImage && childPosition == mImages.size() - 1) || (!hasImage)
//                                        ? mContext.getResources()
//                                        .getDimensionPixelSize(R.dimen.item_divider_height_8dp) : 0);
//                    }
//                }
//            }
//
//        }
//    }
//
//    public void cancelRequest() {
//       Communications.cancelRequest(TAG);
//    }
//
//}
