package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.AvItDetailActivity;
import com.biu.modulebase.binfenjiari.activity.AvVideoDetailActivity;
import com.biu.modulebase.binfenjiari.activity.CommActivity;
import com.biu.modulebase.binfenjiari.activity.CommDetailActivity;
import com.biu.modulebase.binfenjiari.activity.EventActivity;
import com.biu.modulebase.binfenjiari.activity.EventDetailActivity;
import com.biu.modulebase.binfenjiari.activity.MainActivity;
import com.biu.modulebase.binfenjiari.activity.MapActivity;
import com.biu.modulebase.binfenjiari.activity.NotificationActivity;
import com.biu.modulebase.binfenjiari.activity.RankingActivity;
import com.biu.modulebase.binfenjiari.activity.WebViewActivity;
import com.biu.modulebase.binfenjiari.model.HomeRecommendAct;
import com.biu.modulebase.binfenjiari.widget.FixedRecycleHomeScrollView;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.BannerVO;
import com.biu.modulebase.binfenjiari.model.EventVO;
import com.biu.modulebase.binfenjiari.model.HomeHotVO;
import com.biu.modulebase.binfenjiari.model.VoteProjectVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.MyCheckBox;
import com.biu.modulebase.binfenjiari.widget.jazzviewpager.CirclePageIndicator;
import com.biu.modulebase.binfenjiari.widget.jazzviewpager.JazzyViewPager;
import com.biu.modulebase.binfenjiari.widget.jazzviewpager.OutlineContainer;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class HomeFragment<E> extends BaseFragment {

    public static final int HOME_LIKE_REQUEST = 111;

    private MainActivity context;

    private static final String TAG = "HomeFragment";

    private RecyclerView mRecyclerView;

    private LSwipeRefreshLayout mRefreshLayout;

    private long time;
    private int pageNum = 1;
    private int allPageNumber = 1;

    private MyHandler mHandler;
    private int currentPage = 0;
    String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {
        if (isAdded())
            context = (MainActivity) getActivity();
        visibleLoading();
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getMainList(Constant.LIST_LOAD_MORE);
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter = new BaseAdapter(context) {
            private static final int TYPE_HEADER = 0X0001;//头部
            private static final int TYPE_HEADER_RECOMMEND = 0X00012;//推荐活动
            private static final int TYPE_ACTIVITY = 0X0002;//活动
            private static final int TYPE_VOTE_COMMON = 0X0003;//普通投票
            private static final int TYPE_VOTE_PIC = 0X0004;//带图片的投票
            private static final int TYPE_AUDIO_AV = 0X0005;//视频、语音视听
            private static final int TYPE_AUDIO_TEXT_AND_PIC = 0X0006;//图文视听

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = null;
                if (viewType == TYPE_HEADER) {
                    LogUtil.LogE(TAG, "getViewHolder--------->" + "TYPE_HEADER");
                    viewHolder = new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_header, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            List<BannerVO> bannerList = (List<BannerVO>) data;
                            holder.setItemChildViewClickListener(R.id.layout_event, R.id.layout_comm, R.id.layout_ranking);
                            JazzyViewPager guidePager = holder.getView(R.id.mViewPager);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) guidePager.getLayoutParams();
                            layoutParams.height = Utils.getScreenWidth(context) / 5 * 3;
                            guidePager.setLayoutParams(layoutParams);
                            CirclePageIndicator mIndicator = holder.getView(R.id.main_indicator);
                            mIndicator.setPageColor(getResources().getColor(R.color.white));
                            mIndicator.setFillColor(getResources().getColor(R.color.colorAccent));
                            mIndicator.setStrokeWidth(0);
                            List<View> mPageViews = new ArrayList<>();
                            guidePager.setTransitionEffect(JazzyViewPager.TransitionEffect.FlipHorizontal);
                            guidePager.setAdapter(new ViewPagerAdapter(guidePager, mPageViews));
                            guidePager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);// 设置效果模式
                            mIndicator.setCentered(true);
                            mIndicator.setRadius(8);
                            mIndicator.setViewPager(guidePager);
                            mPageViews.clear();
                            for (int i = 0; i < bannerList.size(); i++) {
                                BannerVO banner = bannerList.get(i);
                                View view = View.inflate(context, R.layout.item_banner, null);
                                ImageView imgView = (ImageView) view.findViewById(R.id.iv_banner);
                                TextView title = (TextView) view.findViewById(R.id.title);
                                title.setText(banner.getTitle());
                                ImageDisplayUtil.displayImage(Constant.IMG_SOURCE, banner.getPath(), imgView, ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                mPageViews.add(view);
                                view.setOnClickListener(new MyBannerOnClickListener(banner));
                            }
                            guidePager.setAdapter(new ViewPagerAdapter(guidePager, mPageViews));
                            mIndicator.setViewPager(guidePager);
                            if (mHandler != null)
                                mHandler.removeCallbacksAndMessages(null);
                            mHandler = new MyHandler(guidePager, mPageViews);
                            startBanner();

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            int i1 = view.getId();
                            if (i1 == R.id.layout_event) {
                                Intent i = new Intent(context, EventActivity.class);
                                startActivity(i);

                            } else if (i1 == R.id.layout_comm) {
                                Intent i2 = new Intent(context, CommActivity.class);
                                startActivity(i2);

                            } else if (i1 == R.id.layout_ranking) {
                                Intent intent = new Intent(context, RankingActivity.class);
                                startActivity(intent);

                            } else {
                            }

                        }
                    });
                } else if (viewType == TYPE_HEADER_RECOMMEND) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_fixedrecyclescrollview, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            if (data instanceof HomeRecommendAct) {
                                HomeRecommendAct homeRecommendVO = (HomeRecommendAct) data;

                                FixedRecycleHomeScrollView view = (FixedRecycleHomeScrollView) holder.itemView;
                                view.setRecommendData(HomeFragment.this,homeRecommendVO);
                            }
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                        }


                    });

                } else if (viewType == TYPE_ACTIVITY) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event_home, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            String banner_pic, name, address, open_time, end_time, base_name, apply_num, limit_num, like_num, like_status, isopen;
                            if (data instanceof HomeHotVO) {
                                HomeHotVO homeHotVO = (HomeHotVO) data;
                                banner_pic = homeHotVO.getBanner_pic();
                                name = homeHotVO.getName();
                                address = homeHotVO.getAddress();
                                open_time = homeHotVO.getOpen_time();
                                end_time = homeHotVO.getEnd_time();
                                base_name = homeHotVO.getBase_name();
                                apply_num = homeHotVO.getApply_number();
                                limit_num = homeHotVO.getLimit_number();
                                like_num = homeHotVO.getLike_number();
                                like_status = homeHotVO.getLike_status();
                                isopen = homeHotVO.getIsopen();
                            } else {
                                EventVO eventVO = (EventVO) data;
                                banner_pic = eventVO.getBanner_pic();
                                name = eventVO.getName();
                                address = eventVO.getAddress();
                                open_time = eventVO.getOpen_time();
                                end_time = eventVO.getEnd_time();
                                base_name = eventVO.getBase_name();
                                apply_num = eventVO.getApply_number();
                                limit_num = eventVO.getLimit_number();
                                like_num = eventVO.getLike_number();
                                like_status = eventVO.getLike_status();
                                isopen = eventVO.getIsopen();
                            }
                            holder.setNetImage(Constant.IMG_COMPRESS, R.id.banner, banner_pic, ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                            holder.setText(R.id.title, name);
                            holder.setText(R.id.address, address.length() > 16 ? address.substring(0, 16) + "..." : address);
                            holder.setText(R.id.time, String.format(getString(R.string.time),
                                    Utils.sec2Date(open_time, "yyyy/MM/dd"), Utils.sec2Date(end_time, "yyyy/MM/dd")));
//                                    holder.setText(R.id.base,String.format(getString(R.string.home_activity_address),base_name));
                            holder.setText(R.id.base, base_name);

                            if (Utils.isInteger(limit_num) > 0) {
                                holder.setText(R.id.join_num, String.format(getString(R.string.home_activity_num), apply_num, limit_num));
                            } else {
                                holder.setText(R.id.join_num, getString(R.string.home_activity_num2));
                            }

                            MyCheckBox like = holder.getView(R.id.like);
                            like.setText(like_num);
                            if (like_status.equals("1")) {
                                like.setChecked(true);
                            } else {
                                like.setChecked(false);
                            }
                            ImageView over = holder.getView(R.id.over);
//                                    if(isopen.equals("2")){
//                                        over.setVisibility(View.VISIBLE);
//                                    }else{
//                                        over.setVisibility(View.GONE);
//                                    }
                            holder.setItemChildViewClickListener(R.id.like, R.id.share);
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view,
                                                int position) {
                            final Object object = getData(position);
                            String id;
                            String name;
                            String latitude = null;
                            String longitude = null;
                            String address = null;
                            if (getData(position) instanceof HomeHotVO) {//上首页的活动
                                HomeHotVO homeHotVO = (HomeHotVO) object;
                                id = homeHotVO.getId();
                                name = homeHotVO.getName();
                            } else {
                                EventVO event = (EventVO) object;
                                id = event.getId();
                                name = event.getName();
                                latitude = event.getLatitude();
                                longitude = event.getLongitude();
                                address = event.getAddress();
                            }

                            int i = view.getId();
                            if (i == R.id.like) {
                                JSONObject params = OtherUtil.getJSONObject(context, Constant.MODEL_ACTIVITY,
                                        Constant.ACTION_ACTIVITY_LIKE, true);
                                JSONUtil.put(params, "id", id);
                                OtherUtil.like(HomeFragment.this, (CheckBox) view, params,
                                        new OtherUtil.LikeCallback() {
                                            @Override
                                            public void onFinished(int backKey) {
                                                if (backKey != -1) {

                                                    if (object instanceof HomeHotVO) {//上首页的活动
                                                        HomeHotVO homeHotVO = (HomeHotVO) object;
                                                        homeHotVO.setLike_status(backKey + "");
                                                        if (backKey == 1) {
                                                            homeHotVO.setLike_number(Utils.isInteger(homeHotVO.getLike_number()) + 1 + "");
                                                        } else if (backKey == 2) {
                                                            homeHotVO.setLike_number(Utils.isInteger(homeHotVO.getLike_number()) - 1 + "");
                                                        }
                                                    } else {
                                                        EventVO event = (EventVO) object;
                                                        event.setLike_status(backKey + "");
                                                        if (backKey == 1) {
                                                            event.setLike_number(Utils.isInteger(event.getLike_number()) + 1 + "");
                                                        } else if (backKey == 2) {
                                                            event.setLike_number(Utils.isInteger(event.getLike_number()) - 1 + "");
                                                        }
                                                    }

                                                }
                                            }
                                        });

                            } else if (i == R.id.share) {
                                OtherUtil.showShareFragment(HomeFragment.this, id, name, name, Constant.SHARE_ACTIVITY);

                            } else if (i == R.id.address) {
                                Intent mapIntent = new Intent(context, MapActivity
                                        .class);
                                mapIntent.putExtra(Constant.KEY_LOCATION_LATITUDE, latitude);
                                mapIntent.putExtra(Constant.KEY_LOCATION_LONGITUDE, longitude);
                                mapIntent.putExtra(Constant.KEY_MAP_TARGET, address);
                                startActivity(mapIntent);

                            } else {
                                Intent intent = new Intent(context, EventDetailActivity.class);
                                intent.putExtra(Constant.KEY_ID, id);
                                intent.putExtra("position", position);
                                startActivityForResult(intent, HOME_LIKE_REQUEST);

                            }
                        }
                    });
                    viewHolder.setItemChildViewClickListener(R.id.address);
                } else if (viewType == TYPE_VOTE_COMMON) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(context)
                            .inflate(R.layout.item_home_vote, parent, false),
                            new BaseViewHolder.Callbacks2() {

                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    HomeHotVO bean = (HomeHotVO) data;
                                    id = bean.getId();
                                    holder.setText(R.id.title, bean.getName());
                                    LinearLayout vote_layout = holder.getView(R.id.vote_layout);
                                    holder.setNetImage(Constant.IMG_COMPRESS, R.id.banner, bean.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                    vote_layout.removeAllViews();
                                    holder.setText(R.id.tv_vote_info, String.format(getString(R.string.all_vote_num), bean.getAll_number()));
                                    holder.setText(R.id.time, String.format(getString(R.string.finish_time), Utils.sec2Date(bean.getEnd_time(), "yyyy/MM/dd HH:mm")));
                                    ImageView over = holder.getView(R.id.over);
                                    if (bean.getIsopen().equals("2")) {
                                        over.setVisibility(View.VISIBLE);
                                    } else {
                                        over.setVisibility(View.GONE);
                                    }
                                    holder.getView(R.id.tv_more).setVisibility(View.INVISIBLE);
                                    List<VoteProjectVO> projectList = bean.getProjectList();
                                    if (projectList.size() == 0 || projectList == null) {
                                        return;

                                    }
                                    int length = projectList.size() > 3 ? 3 : projectList.size();
                                    for (int i = 0; i < length; i++) {
                                        View vote_item = View.inflate(context, R.layout.item_organiztion, null);
                                        VoteProjectVO voteProjectVO = projectList.get(i);
                                        ImageView img = (ImageView) vote_item.findViewById(R.id.img);
                                        if (i == 0) {
                                            img.setImageResource(R.mipmap.vote_1);
                                        } else if (i == 1) {
                                            img.setImageResource(R.mipmap.vote_2);
                                        } else if (i == 2) {
                                            img.setImageResource(R.mipmap.vote_3);
                                        }
                                        TextView project_name = (TextView) vote_item.findViewById(R.id.project_name);
                                        project_name.setMaxLines(1);
                                        //普通投票显示长标题
                                        project_name.setText(voteProjectVO.getTitle());//voteProjectVO.getCreate_number()+"  "+
                                        ProgressBar progressBar = (ProgressBar) vote_item.findViewById(R.id.progress);
                                        progressBar.setMax(Utils.isInteger(bean.getAll_poll()));
                                        progressBar.setProgress(Utils.isInteger(voteProjectVO.getNumber()));
                                        TextView num = (TextView) vote_item.findViewById(R.id.num);
                                        num.setText(voteProjectVO.getNumber() + "");
                                        vote_layout.addView(vote_item);
                                    }


                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view,
                                                        int position) {
                                    Intent intent = new Intent(context, CommDetailActivity.class);
                                    HomeHotVO vo = (HomeHotVO) getData().get(position);
                                    intent.putExtra("id", vo.getId());
                                    startActivity(intent);

                                }
                            });
                } else if (viewType == TYPE_VOTE_PIC) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(context)
                            .inflate(R.layout.item_home_vote_image, parent, false),
                            new BaseViewHolder.Callbacks2() {

                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    HomeHotVO bean = (HomeHotVO) data;
                                    id = bean.getId();
                                    holder.setText(R.id.title, bean.getName());
                                    holder.setNetImage(Constant.IMG_COMPRESS, R.id.banner, bean.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                    holder.setText(R.id.time, "截止时间：" + Utils.sec2Date(bean.getEnd_time(), "yyyy/MM/dd HH:mm"));

                                    LinearLayout vote_layout = holder.getView(R.id.vote_layout);
                                    vote_layout.removeAllViews();
                                    holder.setText(R.id.tv_vote_info, String.format(getString(R.string.all_vote_num), bean.getAll_number()));

                                    ImageView over = holder.getView(R.id.over);
                                    if (bean.getIsopen().equals("2")) {
                                        over.setVisibility(View.VISIBLE);
                                    } else {
                                        over.setVisibility(View.GONE);
                                    }
                                    List<VoteProjectVO> projectList = bean.getProjectList();
                                    if (projectList.size() == 0 || projectList == null) {
                                        return;
                                    }
//                                    int width =(Utils.getScreenWidth(context)-getResources().getDimensionPixelSize(R.dimen.view_margin_24dp))/3;
                                    int width = (Utils.getScreenWidth(getActivity()) - getResources().getDimensionPixelSize(R.dimen.view_margin_12dp) * 2) / 3;
                                    if (projectList.size() > 3) {
                                        holder.getView(R.id.tv_more).setVisibility(View.INVISIBLE);
                                    } else {
                                        holder.getView(R.id.tv_more).setVisibility(View.INVISIBLE);
                                    }

                                    for (int i = 0; i < (projectList.size() > 3 ? 3 : projectList.size()); i++) {
                                        View vote_item = LayoutInflater.from(context).inflate(R.layout.part_image_vote, null);
                                        VoteProjectVO voteProjectVO = projectList.get(i);
                                        ImageView img = (ImageView) vote_item.findViewById(R.id.img);
                                        ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, voteProjectVO.getPic(), img, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                        ImageView rank_img = (ImageView) vote_item.findViewById(R.id.rank_img);
                                        if (i == 0) {
                                            rank_img.setImageResource(R.mipmap.vote_11);
                                        } else if (i == 1) {
                                            rank_img.setImageResource(R.mipmap.vote_22);
                                        } else if (i == 2) {
                                            rank_img.setImageResource(R.mipmap.vote_33);
                                        }
                                        TextView name = (TextView) vote_item.findViewById(R.id.name);
                                        name.setText(voteProjectVO.getSmall_title());//voteProjectVO.getCreate_number()+" "+
                                        Utils.setSexIconState(getContext(), name, voteProjectVO.getSex());
                                        ProgressBar progressBar = (ProgressBar) vote_item.findViewById(R.id.progress);
                                        progressBar.setMax(Utils.isInteger(bean.getAll_poll()));
                                        progressBar.setProgress(Utils.isInteger(voteProjectVO.getNumber()));

                                        TextView num = (TextView) vote_item.findViewById(R.id.num);
                                        num.setText(voteProjectVO.getNumber() + "");
                                        vote_layout.addView(vote_item);
                                        ViewGroup.LayoutParams vlayoutParams = vote_item.getLayoutParams();
                                        //layoutParams.height = width;
                                        vlayoutParams.width = width;
                                        vote_item.setLayoutParams(vlayoutParams);
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) img.getLayoutParams();
                                        layoutParams.width = width;
                                        layoutParams.height = width;
                                        img.setLayoutParams(layoutParams);

                                        if (i == 1) {
                                            LinearLayout.LayoutParams layoutParam = (LinearLayout.LayoutParams) vote_item.getLayoutParams();
                                            layoutParam.setMargins(4, 0, 4, 0);
                                            vote_item.setLayoutParams(layoutParam);
                                        }
                                    }


                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view,
                                                        int position) {
                                    Intent intent = new Intent(context, CommDetailActivity.class);
                                    HomeHotVO vo = (HomeHotVO) getData().get(position);
                                    intent.putExtra("id", vo.getId());
                                    String id = vo.getId();
                                    startActivity(intent);
                                }
                            });
                } else if (viewType == TYPE_AUDIO_AV) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_av_video_show_only, parent, false), new BaseViewHolder.Callbacks2() {

                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            HomeHotVO bean = (HomeHotVO) data;
                            holder.setNetImage(Constant.IMG_COMPRESS, R.id.iv_cover, bean.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                            holder.setText(R.id.tv_title, bean.getName());
                            holder.setText(R.id.tv_time_total,
                                    OtherUtil.getVideoTime(bean.getVedio_time()));
                            CheckBox like = holder.getView(R.id.like);
                            like.setText(bean.getLike_number());
                            if (bean.getLike_status().equals("1")) {
                                like.setChecked(true);
                            } else {
                                like.setChecked(false);
                            }
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            final HomeHotVO bean = (HomeHotVO) getData(position);
                            int i = view.getId();
                            if (i == R.id.share) {
                                OtherUtil.showShareFragment(HomeFragment.this, bean.getId(), bean.getName(), bean.getName(), Constant.SHARE_AUDIO);

                            } else if (i == R.id.like) {
                                if (bean != null) {
                                    JSONObject params = OtherUtil.getJSONObject(context, Constant.MODEL_AV,
                                            Constant.ACTION_AV_LIKE, true);
                                    JSONUtil.put(params, "id", bean.getId());
                                    OtherUtil.like(HomeFragment.this, (CheckBox) view, params,
                                            new OtherUtil.LikeCallback() {
                                                @Override
                                                public void onFinished(int backKey) {
                                                    if (backKey != -1) {
                                                        bean.setLike_status(backKey + "");
                                                        if (backKey == 1) {
                                                            bean.setLike_number(Utils.isInteger(bean.getLike_number()) + 1 + "");
                                                        } else if (backKey == 2) {
                                                            bean.setLike_number(Utils.isInteger(bean.getLike_number()) - 1 + "");
                                                        }
                                                    }
                                                }
                                            });
                                }

                            } else {
                                Intent intent = new Intent(context, AvVideoDetailActivity.class);
                                intent.putExtra(Constant.KEY_ID, bean.getId());
                                intent.putExtra(Constant.KEY_POSITION, position);
                                intent.putExtra(Constant.KEY_VIDEO_URL, bean.getVedio_url());
                                startActivityForResult(intent, HOME_LIKE_REQUEST);

                            }

                        }

                    });
                    viewHolder.setItemChildViewClickListener(R.id.like, R.id.share);
                } else if (viewType == TYPE_AUDIO_TEXT_AND_PIC) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(context)
                            .inflate(R.layout.item_av_image_text, parent, false),
                            new BaseViewHolder.Callbacks2() {

                                @Override
                                public void bind(BaseViewHolder holder, Object data) {
                                    HomeHotVO bean = (HomeHotVO) data;
                                    holder.setNetImage(Constant.IMG_COMPRESS, R.id.iv_cover, bean.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                    holder.setText(R.id.tv_title, bean.getName());
                                    CheckBox like = holder.getView(R.id.like);
                                    like.setText(bean.getLike_number());
                                    if (bean.getLike_status().equals("1")) {
                                        like.setChecked(true);
                                    } else {
                                        like.setChecked(false);
                                    }

                                }

                                @Override
                                public void onItemClick(BaseViewHolder holder, View view, int position) {
                                    final HomeHotVO bean = (HomeHotVO) getData(position);
                                    int i = view.getId();
                                    if (i == R.id.share) {
                                        OtherUtil.showShareFragment(HomeFragment.this, bean.getId(), bean.getName(), bean.getName(), Constant.SHARE_PIC_AND_TEXT);

                                    } else if (i == R.id.like) {
                                        if (bean != null) {
                                            JSONObject params = OtherUtil.getJSONObject(context, Constant.MODEL_AV,
                                                    Constant.ACTION_AV_LIKE, true);
                                            JSONUtil.put(params, "id", bean.getId());
                                            OtherUtil.like(HomeFragment.this, (CheckBox) view, params,
                                                    new OtherUtil.LikeCallback() {
                                                        @Override
                                                        public void onFinished(int backKey) {
                                                            if (backKey != -1) {
                                                                bean.setLike_status(backKey + "");
                                                                if (backKey == 1) {
                                                                    bean.setLike_number(Utils.isInteger(bean.getLike_number()) + 1 + "");
                                                                } else if (backKey == 2) {
                                                                    bean.setLike_number(Utils.isInteger(bean.getLike_number()) - 1 + "");
                                                                }
                                                            }
                                                        }
                                                    });
                                        }

                                    } else {
                                        Intent intent = new Intent(context, AvItDetailActivity.class);
                                        intent.putExtra(Constant.KEY_ID, bean.getId());
                                        intent.putExtra(Constant.KEY_POSITION, position);
                                        startActivityForResult(intent, HOME_LIKE_REQUEST);

                                    }

                                }
                            });
                    viewHolder.setItemChildViewClickListener(R.id.like, R.id.share);
                }
                return viewHolder;
            }

            @Override
            public int getItemViewType(int position) {
                Object bean = getData(position);
                if (bean instanceof ArrayList) {
                    LogUtil.LogD("banner");
                    return TYPE_HEADER;
                } else if (bean instanceof HomeRecommendAct) {
                    return TYPE_HEADER_RECOMMEND;
                } else if (bean instanceof HomeHotVO) {
                    HomeHotVO homeHotVO = (HomeHotVO) bean;
                    int type = Utils.isInteger(homeHotVO.getType());
                    if (type == 1) {//活动
                        return TYPE_ACTIVITY;
                    } else if (type == 2) {//普通投票
                        return TYPE_VOTE_COMMON;
                    } else if (type == 3) {//带图投票
                        return TYPE_VOTE_PIC;
                    } else if (type == 4) {//视频、语音视听
                        return TYPE_AUDIO_AV;
                    } else {//type ==5图文视听
                        return TYPE_AUDIO_TEXT_AND_PIC;
                    }
                } else {
                    return TYPE_ACTIVITY;
                }
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
//                outRect.set(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.item_divider_height_8dp));
                outRect.set(0, 0, 0, 0);

//                int childAdapterPosition = parent.getChildAdapterPosition(view);
////                if (childAdapterPosition > 0) {
//                    outRect.set(0, childAdapterPosition == 0 ? getResources()
//                                    .getDimensionPixelSize(R.dimen.toolbar_size)
//                                    : getResources().getDimensionPixelSize(R.dimen
//                                            .item_divider_height_8dp),
//                            0, 0);
//               // }
            }
        };
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
//                        swipeToLoadLayout.setLoadingMore(true);
                        mRefreshLayout.startLoad();
//                        showTost("加载更多",0);
                    }
                }
            }

        });
    }


    @Override
    public void loadData() {
//        datas.clear();
        currentPage = 0;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        time = new Date().getTime() / 1000;
        pageNum = 1;
        getBanners();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private void getBanners() {
        JSONObject params = new JSONObject();
        try {
            params.put("model", Constant.MODEL_BANNER);
            params.put("action", Constant.ACTION_BANNER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                LogUtil.LogD(TAG, "response:" + jsonString);
                List<BannerVO> bannerList = JSONUtil.fromJson(jsonString, new TypeToken<List<BannerVO>>() {
                }.getType());
                ArrayList<E> datas = new ArrayList<>();
                datas.add((E) bannerList);
                BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                adapter.setData(datas);
//                refreshList(Constant.LIST_REFRESH,datas);
                getMainList(Constant.LIST_REFRESH);

            }

            @Override
            public void onCodeError(int key, String message) {
                showTost("获取banner失败", 0);
                mRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    private <T> void getMainList(final int tag) {
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", Constant.MODEL_HOME);
        JSONUtil.put(params, "action", Constant.ACTION_HOME_INFO);
        JSONUtil.put(params, "time", time);
        Log.e("time", time + "");
        JSONUtil.put(params, "pageNum", pageNum);
        JSONUtil.put(params, "token", PreferencesUtils.getString(context, PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    time = JSONUtil.getLong(result, "time");
                    allPageNumber = JSONUtil.getInt(result, "allPageNumber");
                    JSONArray hotArray = JSONUtil.getJSONArray(result, "indexList");
                    JSONArray activityArray = JSONUtil.getJSONArray(result, "activityList");
                    List<HomeHotVO> hotList = JSONUtil.fromJson(hotArray.toString(), new TypeToken<List<HomeHotVO>>() {
                    }.getType());
                    List<EventVO> activityList = JSONUtil.fromJson(activityArray.toString(), new TypeToken<List<EventVO>>() {
                    }.getType());
                    List<E> datas = new ArrayList<>();
                    datas.addAll((Collection<? extends E>) hotList);
                    if(tag==Constant.LIST_REFRESH){
                        if(activityList.size()>5){
                            //推荐活动
                            HomeRecommendAct homeRecommendAct = new HomeRecommendAct();
                            List<EventVO> eventVOs = new ArrayList<>();
                            eventVOs.add(activityList.get(0));
                            eventVOs.add(activityList.get(1));
                            eventVOs.add(activityList.get(2));
                            eventVOs.add(activityList.get(3));
                            homeRecommendAct.setDatas(eventVOs);
                            datas.add((E) homeRecommendAct);
                            datas.addAll((Collection<? extends E>)activityList.subList(4,activityList.size()));
                        }else{
                            datas.addAll((Collection<? extends E>) activityList);
                        }
                    }else {
                        datas.addAll((Collection<? extends E>) activityList);
                    }
                    refreshList(tag, datas);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                    refreshList(tag, new ArrayList<EventVO>());
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

                adapter.addData(1, datas);
//                adapter.setData(datas);
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

    private void startBanner() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(AUTO_CHANGE_VIEWPAGER, 4000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case HOME_LIKE_REQUEST:
                    BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
                    int position = data.getIntExtra("position", -1);
                    int backKey = data.getIntExtra("backKey", -1);
                    Object object = adapter.getData(position);
                    if (object instanceof HomeHotVO) {
                        HomeHotVO bean = (HomeHotVO) object;
                        bean.setLike_status(backKey == 1 ? "1" : "2");
                        int likeNum = Utils.isInteger(bean.getLike_number());
                        bean.setLike_number(backKey == 1 ? ++likeNum + "" : --likeNum + "");
                        adapter.changeData(position, bean);
                    } else if (object instanceof EventVO) {
                        EventVO bean = (EventVO) object;
                        bean.setLike_status(backKey == 1 ? "1" : "2");
                        int likeNum = Utils.isInteger(bean.getLike_number());
                        bean.setLike_number(backKey == 1 ? ++likeNum + "" : --likeNum + "");
                        adapter.changeData(position, bean);
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (hasMessage()) {
            menu.findItem(R.id.action_notification).setIcon(R.mipmap.message_hint);
        } else {
            menu.findItem(R.id.action_notification).setIcon(R.mipmap.message);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_notification) {
            if (checkIsLogin()) {
                startActivity(new Intent(context, NotificationActivity.class));
            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        context.invalidateOptionsMenu();
        if (mHandler != null) {
            mHandler.getmViewPager().setCurrentItem(currentPage);
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(AUTO_CHANGE_VIEWPAGER, 3000);
        }

    }

    @Override
    public void onPause() {
        if (mHandler != null) {
            currentPage = mHandler.getmViewPager().getCurrentItem();
            // 停止viewPager中图片的自动切换
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onPause();
        Communications.cancelRequest(TAG);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.LogE(TAG, "onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.LogE(TAG, "onDestroy");
    }

    class MyBannerOnClickListener implements View.OnClickListener {
        private BannerVO bannerVO;

        public MyBannerOnClickListener(BannerVO bannerVO) {
            this.bannerVO = bannerVO;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("id", bannerVO.getId());
            intent.putExtra("title", bannerVO.getTitle());
            intent.putExtra("loadType", WebViewFragment.LOAD_HTML_CODE);
            startActivity(intent);
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private JazzyViewPager guidePager;
        private List<View> mPageViews;

        public ViewPagerAdapter(JazzyViewPager guidePager, List<View> mPageViews) {
            this.guidePager = guidePager;
            this.mPageViews = mPageViews;

        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(mPageViews.get(position), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            guidePager.setObjectForPosition(mPageViews.get(position), position);
            return mPageViews.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView(guidePager.findViewFromObject(position));
        }

        @Override
        public int getCount() {
            return mPageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }

    }

    /**
     * 自动轮播viewpager
     **/
    private static final int AUTO_CHANGE_VIEWPAGER = 100;

    class MyHandler extends Handler {
        private List<View> mPageViews;
        private ViewPager mViewPager;

        public MyHandler(ViewPager mViewPager, List<View> mPageViews) {
            this.mPageViews = mPageViews;
            this.mViewPager = mViewPager;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_CHANGE_VIEWPAGER:
                    int totalcount = mPageViews.size();
                    int position = mViewPager.getCurrentItem();

                    int toItem = position + 1 == totalcount ? 0
                            : position + 1;

                    mViewPager.setCurrentItem(toItem, true);
                    currentPage = position;
                    // 每4秒钟发送一个message，用于切换viewPager中的图片
                    this.sendEmptyMessageDelayed(AUTO_CHANGE_VIEWPAGER, 4000);
                    break;
            }
            super.handleMessage(msg);
        }

        public ViewPager getmViewPager() {
            return mViewPager;
        }

    }

}
