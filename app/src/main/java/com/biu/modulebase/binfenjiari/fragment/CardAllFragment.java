package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CardDetailActivity;
import com.biu.modulebase.binfenjiari.activity.CircleDetailActivity;
import com.biu.modulebase.binfenjiari.activity.CircleManageActivity;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ExitCircleDialogFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.NoticePopDialogFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ShareDialogFragment;
import com.biu.modulebase.binfenjiari.model.AnnounceVO;
import com.biu.modulebase.binfenjiari.model.CardVO;
import com.biu.modulebase.binfenjiari.model.CircleDetailVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Title: {全部帖子列表}
 * @Description:{}
 * @date 2016/4/13
 */
public class CardAllFragment extends BaseFragment {
    private static final String TAG = "CardAllFragment";

    public static final int TARGET_REQUESST_CODE_EXIT_CIRCLE = 1;

    public static final int REQUEST_CARD_ADD = 2;
    /**
     * 操作帖子
     **/
    public static final int REQUEST_OPERATE_POST = 3;

    public static final String KEY_HEADER = "header";

    private static final Object TAG_HEADER = "header";

    private static final Object TAG_CARD = "card";

    private RecyclerView mRecyclerView;

    private LSwipeRefreshLayout mRefreshLayout;

    private String mCircleId = "";

    private boolean loadHeader = false;

    private boolean loadCard = false;

    private long time;
    private int pageNum = 1;
    private int allPageNumber;
    private CircleDetailVO mCircleDetailVO;

    /**
     * F发布帖子回调刷新标识
     **/
    public static boolean refresh = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //标记下当前的登录状态，提供后期回到该界面是否需要刷新界面
        MyApplication.sRefreshMap.put(TAG, !OtherUtil.hasLogin(getActivity()));
        Bundle args = getArguments();
        if (args != null) {
            mCircleId = args.getString(CircleFragment.EXTRA_CIRCLE_ID);
        }
    }

    public static CardAllFragment newInstance(String circleId) {
        Bundle args = new Bundle();
        args.putString(CircleFragment.EXTRA_CIRCLE_ID, circleId);
        CardAllFragment cardAllFragment = new CardAllFragment();
        cardAllFragment.setArguments(args);
        return cardAllFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_recyclerview_swiperefresh, container, false);

        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG, "onRefresh******************");
                loadData();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG, "onLoadMore******************");
                pageNum++;
                loadCards(Constant.LIST_LOAD_MORE);
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter = new BaseAdapter(getActivity()) {
            private static final int TYPE_HEADER = 0X1;
            private static final int TYPE_ITEM = 0X2;

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, final int viewType) {
                BaseViewHolder baseViewHolder = null;
                if (viewType == TYPE_HEADER) {
                    LogUtil.LogE(TAG, "getViewHolder--------->" + "TYPE_HEADER");
                    baseViewHolder = new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.header_circle_detail, parent, false),
                            new BaseViewHolder.Callbacks2() {
                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    if (data == null) {
                                        return;
                                    }
                                    if (data instanceof CircleDetailVO) {
                                        CircleDetailVO circleDetail = (CircleDetailVO) data;
//                                        holder.setNetImage(Constant.IMG_COMPRESS,R.id.header_pic, circleDetail.getBack_img(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                        holder.setNetImage(Constant.IMG_COMPRESS, R.id.iv_head_portrait, circleDetail.getIntro_img(), ImageDisplayUtil.DISPLAY_ROUND_IMAGE);
                                        holder.setText(R.id.tv_name, circleDetail.getName());
                                        holder.setText(R.id.tv_subject, circleDetail.getIntro_content());
                                        holder.setText(R.id.tv_circle_number, String.format(getString(R.string.circle_join_num), circleDetail.getUser_n()));
                                        holder.setText(R.id.tv_card_number, String.format(getString(R.string.circle_post_num), circleDetail.getPost_n()));
                                        int idType = Utils.isInteger(circleDetail.getType());
                                        TextView btn_join = holder.getView(R.id.btn_join);
                                        if (idType == 4) {
                                            btn_join.setVisibility(View.GONE);
                                        } else {
                                            if (idType == 2 || idType == 3) {
                                                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_orange));
                                                btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                                                btn_join.setText("管理");
                                            } else if (idType == 1) {//已加入的普通会员
                                                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_gray));
                                                btn_join.setTextColor(getResources().getColor(R.color.colorTextGray));
                                                btn_join.setText("退出");
                                            } else {
                                                btn_join.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_outline_orange));
                                                btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                                                btn_join.setText("加入");
                                            }
                                            btn_join.setVisibility(View.VISIBLE);
                                        }
//                                        changeBtnAppear(btn_join,status);

                                        LinearLayout layout_notice = holder.getView(R.id.layout_notice);
                                        layout_notice.removeAllViews();
                                        List<AnnounceVO> list = circleDetail.getAnnounce_list();
                                        if (list != null && list.size() > 0) {
                                            for (int i = 0; i < list.size(); i++) {
                                                AnnounceVO bean = list.get(i);
                                                View notice = View.inflate(getActivity(), R.layout.part_circle_detail_header_notice, null);
                                                TextView content = (TextView) notice.findViewById(R.id.tv_notice);
                                                content.setText(OtherUtil.filterSensitives(getActivity(), bean.getTitle()));
                                                layout_notice.addView(notice);
                                                notice.setOnClickListener(new NoticeClickListener(getBaseActivity(), bean));
                                            }


                                        }


                                    }
                                }

                                @Override
                                public void onItemClick(final BaseViewHolder holder, View view, final int position) {
                                    final CircleDetailVO bean = (CircleDetailVO) getData(0);
                                    int i = view.getId();
                                    if (i == R.id.btn_join) {
                                        int idType = Utils.isInteger(bean.getType());
                                        if (idType == 3 || idType == 2) {//管理
                                            Intent intent = new Intent(getActivity(), CircleManageActivity.class);
                                            intent.putExtra(CircleManageFragment.KEY_CIRCLE_ID, bean.getId());
                                            startActivity(intent);
                                        } else if (idType == 1) {//退出
                                            ExitCircleDialogFragment shareDialog = ExitCircleDialogFragment.newInstance(ShareDialogFragment.STYLE_NO_TITLE, position, bean.getId());
                                            shareDialog.setTargetFragment(CardAllFragment.this, TARGET_REQUESST_CODE_EXIT_CIRCLE);
                                            shareDialog.show(getActivity().getSupportFragmentManager(), "exit_circle");
                                        } else {//加入
                                            OtherUtil.joinCircle(CardAllFragment.this, bean.getId(), new OtherUtil.JoinCircleCallback() {
                                                @Override
                                                public void onFinish(int key) {
                                                    CircleItemFragment.refresh = true;
                                                    bean.setType("1");
                                                    bean.setUser_n(Utils.isInteger(bean.getUser_n()) + 1 + "");
                                                    BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                                                    adapter.changeData(position, bean);
//                                                        changeBtnAppear((TextView) view,idType);
                                                }
                                            });
                                        }

                                    }

//                                    final CircleDetailHeader header=((List<CircleDetail>)getData())
//                                            .get(position).getHeader();
//
//                                    if (view.getTag() == CardAllFragment.this) {
//                                        LogUtil.LogE(TAG, "click_layout_notice_item");
//
//                                        if (header != null) {
//                                            int index = ((ViewGroup) view.getParent()).indexOfChild(
//                                                    view);
//                                            CircleDetailAnnounce announce =
//                                                    header.getAnnounce_list()[index];
//                                            showNotice(getActivity(), announce.getTitle(),
//                                                    announce.getContent());
//                                        }
//                                        return;
//                                    }
//                                    int id = view.getId();
//                                    switch (id) {
//                                        case R.id.layout_circle_intro:
//                                            //TODO
//                                            Intent intent=new Intent(getActivity(),
//                                                    CircleIntroActivity.class);
//                                            intent.putExtra(KEY_HEADER,header);
//                                            startActivity(intent);
//
//                                            break;
//                                        default:
//                                            break;
//                                    }
                                }
                            });

                    baseViewHolder.itemView.setTag(TAG_HEADER);

                    //根据服务器的 Notice 数量动态创建并添加 Notice View
//                    addNoticeViewDynamically(baseViewHolder,((CircleDetail)getData(0)).getHeader());

                    //设置需要获得点击回调的 View
                    baseViewHolder.setItemChildViewClickListener(R.id.layout_circle_intro, R.id
                            .btn_join, R.id.layout_notice);

                } else if (viewType == TYPE_ITEM) {
                    baseViewHolder = new BaseViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_card, parent, false), new BaseViewHolder
                            .Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            final CardVO bean = (CardVO) data;
                            holder.setText(R.id.tv_nickname, bean.getUsername());
                            holder.setText(R.id.tv_date, Utils.getReleaseTime(new Date(Utils.isLong(bean.getCreate_time()) * 1000)));
                            ImageView header = holder.getView(R.id.iv_head_portrait);
                            header.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                                    intent.putExtra(Constant.KEY_ID, bean.getUser_id());
                                    startActivity(intent);
                                }
                            });
                            holder.setNetImage(Constant.IMG_SOURCE, R.id.iv_head_portrait, bean.getUser_pic(), ImageDisplayUtil.DISPLAY_HEADER);
//                            holder.setText(R.id.tv_title,bean.getTitle());
                            TextView title = holder.getView(R.id.tv_title);
                            holder.setText(R.id.tv_content, OtherUtil.filterSensitives(getActivity(), bean.getContent()));
                            String recomHtml = "<img src='" + R.mipmap.ic_recom + "'/>";
                            String esseHtml = "<img src='" + R.mipmap.ic_essence + "'/>";
                            String esseAndRecomHtml = recomHtml + "  " + esseHtml;
                            Html.ImageGetter imgGetter = new Html.ImageGetter() {

                                @Override
                                public Drawable getDrawable(String source) {
                                    // TODO Auto-generated method stub
                                    int id = Integer.parseInt(source);
                                    Drawable d = getResources().getDrawable(id);
                                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                                    return d;
                                }
                            };
                            CharSequence charSequence = null;
                            String isEssence = bean.getIs_essence();
                            String isRecom = bean.getIs_commend();
                            if (isEssence.equals("2") && isRecom.equals("2")) {
                                title.setText(bean.getContent());
                            } else if (isEssence.equals("1") && isRecom.equals("2")) {
                                charSequence = Html.fromHtml(esseHtml, imgGetter, null);
                            } else if (isEssence.equals("2") && isRecom.equals("1")) {
                                charSequence = Html.fromHtml(recomHtml, imgGetter, null);
                            } else if (isEssence.equals("1") && isRecom.equals("1")) {
                                charSequence = Html.fromHtml(esseAndRecomHtml, imgGetter, null);
                            }
                            title.setText(charSequence);
                            title.append("  " + OtherUtil.filterSensitives(getActivity(), bean.getTitle()));
                            LinearLayout img_layout = holder.getView(R.id.img_layout);
                            String pic = bean.getPic();
                            if (!Utils.isEmpty(pic)) {
                                String imgs[] = pic.split(",");
                                if (imgs != null && imgs.length > 0) {
                                    img_layout.setVisibility(View.VISIBLE);
                                    ImageView img1 = holder.getView(R.id.img1);
                                    ImageView img2 = holder.getView(R.id.img2);
                                    ImageView img3 = holder.getView(R.id.img3);
                                    int width = (Utils.getScreenWidth(getActivity()) - getResources().getDimensionPixelSize(R.dimen.view_margin_24dp)) / 3;
                                    ViewGroup.LayoutParams layoutParams = img1.getLayoutParams();
                                    layoutParams.height = width;
                                    layoutParams.width = width;
                                    img1.setLayoutParams(layoutParams);
                                    ViewGroup.LayoutParams layoutParams2 = img2.getLayoutParams();
                                    layoutParams2.height = width;
                                    layoutParams2.width = width;
                                    img2.setLayoutParams(layoutParams2);
                                    ViewGroup.LayoutParams layoutParams3 = img3.getLayoutParams();
                                    layoutParams3.height = width;
                                    layoutParams3.width = width;
                                    img3.setLayoutParams(layoutParams3);
                                    switch (imgs.length) {
                                        case 1:
                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, imgs[0], img1, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            holder.getView(R.id.img1).setVisibility(View.VISIBLE);
                                            holder.getView(R.id.img2).setVisibility(View.INVISIBLE);
                                            holder.getView(R.id.img3).setVisibility(View.INVISIBLE);
                                            break;
                                        case 2:
                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, imgs[0], img1, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, imgs[1], img2, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            holder.getView(R.id.img1).setVisibility(View.VISIBLE);
                                            holder.getView(R.id.img2).setVisibility(View.VISIBLE);
                                            holder.getView(R.id.img3).setVisibility(View.INVISIBLE);
                                            break;
                                        default:
                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, imgs[0], img1, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, imgs[1], img2, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, imgs[2], img3, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            holder.getView(R.id.img1).setVisibility(View.VISIBLE);
                                            holder.getView(R.id.img2).setVisibility(View.VISIBLE);
                                            holder.getView(R.id.img3).setVisibility(View.VISIBLE);
                                            break;
                                    }
                                }
                            } else {
                                img_layout.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            CardVO bean = (CardVO) getData(position);
                            int i = view.getId();
                            if (i == R.id.ib_more) {
                                OtherUtil.showPostMoreOperate(CardAllFragment.this, mCircleId, bean.getId(), bean.getTitle(), bean.getContent(),
                                        bean.getUser_id(), Constant.SHARE_POST,
                                        Constant.REPORT_POST, Constant.DELETE_POST,
                                        true, (BaseAdapter) mRecyclerView.getAdapter(),
                                        position, null);

                            } else {
                                Intent intent = new Intent(getActivity(), CardDetailActivity.class);
                                intent.putExtra(Constant.KEY_ID, bean.getId());
                                intent.putExtra(CardDetailFragment.KEY_CIRCLE_ID, mCircleId);
                                intent.putExtra(Constant.KEY_POSITION, position);
                                startActivityForResult(intent, REQUEST_OPERATE_POST);

                            }
//                            List<CircleDetail> circleDetails=getData();
//                            if (circleDetails!=null) {
//                                CircleDetail circleDetail=circleDetails.get(position);
//                                int postId=circleDetail.getCardItem().getId();
//                                Intent intent=new Intent(getActivity(), CardDetailActivity.class);
//                                intent.putExtra(CardDetailFragment.KEY_POST_ID,postId);
//                                intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID,mCircleId);
//                                startActivity(intent);
//                            }
                        }
                    });
                    baseViewHolder.setItemChildViewClickListener(R.id.ib_more);
                    baseViewHolder.itemView.setTag(TAG_CARD);
                }
                return baseViewHolder;
            }

            @Override
            public int getItemViewType(int position) {
                return TYPE_ITEM;//position == 0 ? TYPE_HEADER :
            }

        };

//        adapter.testLayoutData(8);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.item_divider_height_8dp));
            }
        });
    }

    private void changeBtnAppear(TextView btn_join, int status) {
        boolean notJoin = status == 3 || status == 2;
        btn_join.setBackgroundDrawable(getResources().getDrawable(
                notJoin ? R.drawable.selector_btn_outline_orange
                        : R.drawable.selector_btn_outline_gray));
        btn_join.setTextColor(
                getResources().getColor(notJoin ? R.color.colorAccent : R.color.colorTextGray));
        btn_join.setText(notJoin ? "加入" : "退出");
//        Drawable add= getResources().getDrawable(R.drawable.add);
//        add.setBounds(0,0,add.getIntrinsicWidth(),add.getIntrinsicHeight());
//        btn_join.setCompoundDrawables(notJoin ? add : null,
//                null, null, null);
    }

//    private void refreshData() {
//        reset();
//        //重新加载数据
//        loadData();
//    }
//
//    private void reset() {
//        //初始化一些判断数据
//        mHeadLoaded=false;
//        mPageNum=1;
//        mCard=null;
//    }

//    /**
//     * 动态添加 Notice View（服务器返回的 Notice 数量可变）
//     * @param holder header ViewHolder
//     */
//    private void addNoticeViewDynamically(BaseViewHolder holder,CircleDetailHeader header) {
//        if (header != null) {
//            CircleDetailAnnounce[] announces = header.getAnnounce_list();
//            ViewGroup layout_notice = holder.getView(R.id.layout_notice);
//
//            //移除 Notice Layout 容器里所有旧的 Notice View
//            layout_notice.removeAllViews();
//
//            if (announces != null) {
//                for (int i = 0; i < announces.length; i++) {
//                    LogUtil.LogE(TAG,"addNoticeViewDynamically------->"+i);
//                    ViewGroup noticeLayout = (ViewGroup) getActivity().getLayoutInflater().inflate(
//                            R.layout.part_circle_detail_header_notice, layout_notice,false);
//                    int noticeLayoutId=0x100 + i;
//                    noticeLayout.setId(noticeLayoutId);
//                    noticeLayout.setTag(this);
//
//                    TextView noticeView= (TextView) noticeLayout.findViewById(R.id.tv_notice);
//                    int noticeViewId=0x200+i;
//                    noticeView.setId(noticeViewId);
//
//                    layout_notice.addView(noticeLayout);
//                    holder.setItemChildViewClickListener(noticeLayoutId);
//
//                    holder.setText(noticeViewId, announces[i].getTitle());
//
//                    LogUtil.LogE(TAG,"addNoticeViewDynamically---Title---->"+announces[i].getTitle());
//
//                    LogUtil.LogE(TAG,"addNoticeViewDynamically---noticeView---->"+(noticeView).getText());
//
//                    if (i < announces.length - 1) {
//                        View divider = getActivity().getLayoutInflater().inflate(
//                                R.layout.item_divider_gray, noticeLayout, false);
//                        noticeLayout.addView(divider);
//                    }
//                }
//            }
//        }
//    }


//    private void showNotice(Context context, final String title, final String content) {
//        DialogFactory.showDialog(context, R.layout.pop_notice, R.style.CustomDialog, 0,
//                Gravity.CENTER, 1f, 1f, new DialogFactory.DialogListener() {
//                    @Override
//                    public void OnInitViewListener(View v, final Dialog dialog) {
//                        ((TextView) v.findViewById(R.id.notice_title)).setText(title);
//                        ((TextView) v.findViewById(R.id.notice_content)).setText(content);
//                        v.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//                });
//    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        visibleLoading();
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void loadData() {
        refresh = false;
        time = new Date().getTime() / 1000;
        pageNum = 1;
        loadHeaderData();
    }

    private void loadHeaderData() {
        int flag = 1;
        if (flag == 1) {
            loadCards(Constant.LIST_REFRESH);
            return;
        }

        JSONObject params = new JSONObject();
        try {
            params.put("token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
            params.put("circle_id", mCircleId);
            params.put("model", Constant.MODEL_CIRCLE);
            params.put("action", Constant.ACTION_GET_CIRCLE_DETAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                CircleDetailVO bean = JSONUtil.fromJson(jsonString, CircleDetailVO.class);
                mCircleDetailVO = bean;
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                adapter.removeAllData();
                adapter.addData(BaseAdapter.AddType.FIRST, Collections.singletonList(bean));
                loadCards(Constant.LIST_REFRESH);
                inVisibleLoading();

            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                } else {
                    showTost(message, 1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });

//            JSONObject map = JSONUtil.getJSONObject(response, "map");
//            CircleDetailHeader header = JSONUtil.getGson().fromJson(map != null ? map.toString
//                            () : null,
//                    CircleDetailHeader.class);
//
//            BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
//
//            adapter.removeAllData();
//
//            CircleDetail circleDetail=new CircleDetail();
//            circleDetail.setHeader(header);
//            adapter.addData(BaseAdapter.AddType.FIRST, Collections.singletonList(circleDetail));
//
//            //帖子加载完成，可以显示评论列表了
//            mHeadLoaded = true;
//            addCards();
//            mRefreshLayout.setRefreshing(false);
//            //      }


    }

    private void loadCards(final int tag) {
        JSONObject params = new JSONObject();
        try {
            params.put("token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
            params.put("model", Constant.MODEL_POST);
            params.put("action", Constant.ACTION_GET_POST_LIST);
            params.put("time", time + "");
            params.put("pageNum", pageNum + "");
            params.put("circle_id", mCircleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    time = JSONUtil.getLong(result, "time");
                    allPageNumber = JSONUtil.getInt(result, "allPageNumber");
                    JSONArray array = JSONUtil.getJSONArray(result, "postList");
                    ArrayList<CardVO> list = JSONUtil.fromJson(array.toString(), new TypeToken<List<CardVO>>() {
                    }.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                    refreshList(tag, new ArrayList<CardVO>());
                } else {
                    showTost(message, 1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    private <T> void refreshList(int tag, List<T> datas) {
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag) {
            case Constant.LIST_REFRESH:
                mRefreshLayout.setRefreshing(false);
                if (datas.size() == 0) {
                    showTost("没有最新数据...", 1);
                    return;
                }
//                adapter.addData(BaseAdapter.AddType.LASE, datas);
                adapter.setData(datas);
                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(datas);
                mRefreshLayout.setLoading(false);
                break;
        }
        if (pageNum < allPageNumber) {
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
        } else {
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
        }
    }

    private void addCard() {
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();

        // adapter.addData(BaseAdapter.AddType.FIRST, circleDetails);
    }

//    private void addCards() {
//        if (mCard != null && mHeadLoaded) {
//            LogUtil.LogE(TAG, "time------------->" + mCard.getTime());
//            LogUtil.LogE(TAG, "list------------->" + (mCard.getList() == null));
//            List<CardItem> cardItems=mCard.getList();
//            if (cardItems != null && cardItems.size() > 0) {
//                List<CircleDetail> circleDetails = new ArrayList<>();
//                int cardCount = cardItems.size();
//                for (int i = 0; i < cardCount; i++) {
//                    CardItem cardItem = cardItems.get(i);
//                    CircleDetail circleDetail = new CircleDetail();
//                    circleDetail.setCardItem(cardItem);
//                    circleDetails.add(circleDetail);
//                }
//                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
//                adapter.addData(BaseAdapter.AddType.LASE, circleDetails);
//
//                if (mRefreshLayout != null && mCard != null) {
//                    //判断是否下次还可以上拉加载更多
//                    int allPageNumber = mCard.getAllPageNumber();
//                    if (mPageNum > allPageNumber) {
//                        LogUtil.LogE(TAG, "stop load more");
//                        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
//                    } else {
//                        mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
//                    }
//                }
//            }
//        }
//    }

    //
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (MyApplication.sRefreshMap.get(TAG) && OtherUtil.hasLogin(getActivity())) {
//            reset();
//            loadData();
//            MyApplication.sRefreshMap.put(TAG, false);
//        }
//    }
//
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Communications.cancelRequest(TAG);
    }


    @Override
    public void onResume() {
        if (refresh) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 500);

        }
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TARGET_REQUESST_CODE_EXIT_CIRCLE://退出圈子
                    int position = data.getIntExtra("position", -1);
                    if (position != -1) {
//                        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
//                        CircleDetailVO circleDetailVO = (CircleDetailVO) adapter.getData(position);
//                        circleDetailVO.setType("0");
//                        circleDetailVO.setUser_n(Utils.isInteger(circleDetailVO.getUser_n()) - 1 + "");
//                        adapter.changeData(position, circleDetailVO);

                        CircleDetailVO circleDetailVO = mCircleDetailVO;
                        circleDetailVO.setType("0");
                        circleDetailVO.setUser_n(Utils.isInteger(circleDetailVO.getUser_n()) - 1 + "");
                        ((CircleDetailActivity)getActivity()).updateBtn(circleDetailVO);
                    }
                    break;
                case REQUEST_CARD_ADD://发帖
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                        }
                    }, 500);


                    break;
                case REQUEST_OPERATE_POST:
                    String type = data.getStringExtra(Constant.KEY_TYPE);
                    int pos = data.getIntExtra(Constant.KEY_POSITION, -1);
                    if (pos != -1 && type.equals(Constant.DELETE)) {
//                        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
//                        adapter.removeData(pos);
//                        CircleDetailVO detailVO = (CircleDetailVO) adapter.getData(0);
//                        detailVO.setPost_n(Utils.isInteger(detailVO.getPost_n()) - 1 + "");
//                        adapter.changeData(0, detailVO);

                        CircleDetailVO detailVO = mCircleDetailVO;
                        detailVO.setPost_n(Utils.isInteger(detailVO.getPost_n()) - 1 + "");
                        ((CircleDetailActivity)getActivity()).updateBtn(detailVO);

                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class NoticeClickListener implements View.OnClickListener {

        private AnnounceVO announceVO;
        private AppCompatActivity activity;

        public NoticeClickListener(AppCompatActivity act, AnnounceVO announceVO) {
            this.announceVO = announceVO;
            this.activity = act;
        }


        @Override
        public void onClick(View v) {
            NoticePopDialogFragment shareDialog = NoticePopDialogFragment.newInstance(NoticePopDialogFragment.STYLE_NO_TITLE, announceVO);
            shareDialog.show(activity.getSupportFragmentManager(), "notice_pop");
        }
    }

    public void btnClick(final CircleDetailVO bean) {
//        if(this.mCircleDetailVO ==null)
//            return;
//        final CircleDetailVO bean = this.mCircleDetailVO;
        this.mCircleDetailVO = bean;
        final int position = 0;
        int idType = Utils.isInteger(bean.getType());
        if (idType == 3 || idType == 2) {//管理
            Intent intent = new Intent(getActivity(), CircleManageActivity.class);
            intent.putExtra(CircleManageFragment.KEY_CIRCLE_ID, bean.getId());
            startActivity(intent);
        } else if (idType == 1) {//退出
            ExitCircleDialogFragment shareDialog = ExitCircleDialogFragment.newInstance(ShareDialogFragment.STYLE_NO_TITLE, position, bean.getId());
            shareDialog.setTargetFragment(CardAllFragment.this, TARGET_REQUESST_CODE_EXIT_CIRCLE);
            shareDialog.show(getActivity().getSupportFragmentManager(), "exit_circle");
        } else {//加入
            OtherUtil.joinCircle(CardAllFragment.this, bean.getId(), new OtherUtil.JoinCircleCallback() {
                @Override
                public void onFinish(int key) {
                    CircleItemFragment.refresh = true;
                    bean.setType("1");
                    bean.setUser_n(Utils.isInteger(bean.getUser_n()) + 1 + "");

                    ((CircleDetailActivity)getActivity()).updateBtn(bean);

//                    BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
//                    adapter.changeData(position, bean);
//                                                        changeBtnAppear((TextView) view,idType);
                }
            });
        }

    }

}
