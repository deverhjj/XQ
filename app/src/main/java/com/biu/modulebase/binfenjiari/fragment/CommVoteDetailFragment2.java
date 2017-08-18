package com.biu.modulebase.binfenjiari.fragment;

import android.content.Context;
import android.content.Intent;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommVoteDetailNewActivity;
import com.biu.modulebase.binfenjiari.activity.VoteMoreActivity;
import com.biu.modulebase.binfenjiari.activity.VoteRankActivity;
import com.biu.modulebase.binfenjiari.activity.WebViewActivity;
import com.biu.modulebase.binfenjiari.activity.WebViewVoteDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.adapter.CommonAdapter;
import com.biu.modulebase.binfenjiari.adapter.ViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.InfoVO;
import com.biu.modulebase.binfenjiari.model.NewVoteBeanVO;
import com.biu.modulebase.binfenjiari.model.ProjectVO;
import com.biu.modulebase.binfenjiari.model.ShowListVO;
import com.biu.modulebase.binfenjiari.model.VoteArrVO;
import com.biu.modulebase.binfenjiari.model.VoteInfoVO;
import com.biu.modulebase.binfenjiari.model.VoteMoreVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/11/3.
 */

public class CommVoteDetailFragment2<E> extends BaseFragment {
    static String project_title;

    private RecyclerView mRecyclerView;
    //标题
    String project_id;
    String title2;
    //主键id
    String id1;
    String id;
    //封面图片
    String pic;
    //1普通投票 2带图片的投票
    int type;
    //总票数
    String all_poll;
    //是否更多 1是 2没有  注：移动端根据该字段来判断是否显示更多；后台只需要将总投票项all_number和5进行比较来决定该
    int is_more;
    //是否结束 1未结束 2已结束
    int isopen;
    //本人是否投票 1已投 2未投 注：移动端根据该字段来显示投票或已投票按钮
    int is_vote;
    //资讯是否更多 1是 2没有
    int has_more;
    //是否主投票 1是  2不是
    int is_main;
//    String titler;

    //副投票列表（is_main=1时有，根据副投票的关联顺序来升序排列）
    List<VoteMoreVO> mVoteMoreVOList;

    List<VoteInfoVO> mVoteInfoVOList;

    /**
     * 参赛人数  累计投票  参与人数
     */
    VoteArrVO mVoteArrVO;
    //咨询
    List<InfoVO> mInfoVOs;
    //投票项列表（排名前五个）
    List<ProjectVO> mProjectVOs;
    private LSwipeRefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
//        titler=getActivity().getIntent().getStringExtra("titler");
        project_id = getActivity().getIntent().getStringExtra("id");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        project_title = title2;
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        if ((mInfoVOs == null || mInfoVOs.size() == 0) && (mProjectVOs == null || mProjectVOs.size() == 0) && (mVoteMoreVOList == null || mVoteMoreVOList.size() == 0))
            inVisibleNoData();


        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                getMainList(Constant.LIST_REFRESH);
                mRefreshLayout.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                mRefreshLayout.setLoading(false);
            }
        });
        final Intent intent = getActivity().getIntent();
//        project_id=id = intent.getStringExtra("id");
        getBaseActivity().setBackNavigationIcon();
        BaseAdapter adapter = new BaseAdapter(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(final ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = null;
                if (viewType == 0) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_vote_show_banner, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            holder.setNetImage(Constant.IMG_COMPRESS, R.id.banner, pic, ImageDisplayUtil.DISPLAY_BIG_IMAGE);

                            if (mVoteArrVO != null) {
                                holder.setText(R.id.tv_joiners, "参赛人数\n" + mVoteArrVO.getJoiners() + "");
                                holder.setText(R.id.tv_number, "累计投票\n" + mVoteArrVO.getNumber() + "");
                                holder.setText(R.id.tv_votepeson, "参与人数\n" + mVoteArrVO.getVotePeson() + "");
                            }

                            if (mVoteInfoVOList != null) {
                                if(mVoteInfoVOList.size() == 1){
                                    if (mVoteInfoVOList.get(0).getType() == 3) {
                                        holder.setText(R.id.tv_activity_detail_info, mVoteInfoVOList.get(0).getTitle() + "");
                                    }
                                }else if (mVoteInfoVOList.size() >= 2) {
                                    if (mVoteInfoVOList.get(0).getType() == 3) {
                                        holder.setText(R.id.tv_activity_detail_info, mVoteInfoVOList.get(0).getTitle() + "");
                                    } else {
                                        holder.setText(R.id.tv_activity_detail_info, mVoteInfoVOList.get(1).getTitle() + "");
                                    }
                                }else{
                                    holder.setText(R.id.tv_activity_detail_info, "");
                                }
                            }

                            if (mVoteInfoVOList == null || mVoteInfoVOList.size() == 0) {
                                holder.getView(R.id.view_banner_divide).setVisibility(View.GONE);
                                holder.getView(R.id.ll_activity_detail).setVisibility(View.GONE);
                            } else {
                                holder.getView(R.id.view_banner_divide).setVisibility(View.VISIBLE);
                                holder.getView(R.id.ll_activity_detail).setVisibility(View.VISIBLE);

                                if (mVoteInfoVOList.size() == 1) {
                                    if (mVoteInfoVOList.get(0).getType() == 3) {
                                        //活动详情
                                        holder.getView(R.id.fl_activity_detail).setVisibility(View.VISIBLE);
                                        holder.getView(R.id.ll_vote_rule).setVisibility(View.GONE);
                                    } else {
                                        //活动规则
                                        holder.getView(R.id.fl_activity_detail).setVisibility(View.GONE);
                                        holder.getView(R.id.ll_vote_rule).setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    holder.getView(R.id.fl_activity_detail).setVisibility(View.VISIBLE);
                                    holder.getView(R.id.ll_vote_rule).setVisibility(View.VISIBLE);
                                }
                            }

//                            LinearLayout ll = holder.getView(R.id.rootView);
//                            ll.removeAllViews();
//                            TextView zx = holder.getView(R.id.zx);
//                            TextView m = holder.getView(R.id.more);
//                            if (has_more == 2)
//                                m.setVisibility(View.GONE);
//
//                            if (mInfoVOs == null || mInfoVOs.size() == 0) {
//                                zx.setVisibility(View.GONE);
//                                m.setVisibility(View.GONE);
//                                return;
//                            }
//
//                            m.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent1 = new Intent(getActivity(), AdMoreActivity.class);
//                                    //TODO
//
//                                    intent1.putExtra("id", project_id);
//                                    startActivity(intent1);
//                                }
//                            });
//                            for (int i = 0; i < (mInfoVOs.size() < 3 ? mInfoVOs.size() - 1 : 2); i++) {
//                                InfoVO vo = mInfoVOs.get(i);
//                                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_ad, null);
//                                TextView title = (TextView) view.findViewById(R.id.title);
//                                TextView content = (TextView) view.findViewById(R.id.content);
//                                final Intent intent = new Intent(getActivity(), WebViewVoteDetailActivity.class);
//                                intent.putExtra("id", vo.getId());
//                                intent.putExtra("project_id", project_id);
//                                intent.putExtra("title1", vo.getTitle());
//                                intent.putExtra("project_title", title2);
//                                intent.putExtra("content", vo.getTitle());
//                                intent.putExtra("title", vo.getTitle());
//                                intent.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDINFOMATIONINFO);
//                                view.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        startActivity(intent);
//                                    }
//                                });
//                                if (vo.getType() == 1) {
//                                    title.setText("公告");
//
//                                } else {
//                                    title.setText("活动");
//                                }
//                                content.setText(vo.getTitle());
//                                ll.addView(view);
//                            }
//                            InfoVO vo = mInfoVOs.get((mInfoVOs.size() < 3 ? mInfoVOs.size() - 1 : 2));
//                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.lay_out_ad1, null);
//                            final Intent intent1 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
//                            intent1.putExtra("id", vo.getId());
//                            intent1.putExtra("project_id", project_id);
//                            intent1.putExtra("project_title", title2);
//                            intent1.putExtra("content", vo.getTitle());
//
//                            intent1.putExtra("title1", vo.getTitle());
//                            Log.e("title", vo.getTitle());
//                            intent1.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDINFOMATIONINFO);
//                            view.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    startActivity(intent1);
//                                }
//                            });
//                            TextView title = (TextView) view.findViewById(R.id.title);
//                            TextView content = (TextView) view.findViewById(R.id.content);
//                            content.setText(vo.getTitle());
//
//                            if (vo.getType() == 1) {
//                                title.setText("公告");
//                            } else {
//                                title.setText("活动");
//                            }
//
//                            ll.addView(view);

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            int i = view.getId();
                            if (i == R.id.fl_activity_detail) {
                                if (mVoteInfoVOList != null && mVoteInfoVOList.size() == 1) {
                                    if (mVoteInfoVOList.get(0).getType() == 3) {
                                        Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
                                        policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_CODE);
                                        policyIntent.putExtra("title", "活动公告");
                                        policyIntent.putExtra("href", mVoteInfoVOList.get(0).getHref());
                                        policyIntent.putExtra("id", mVoteInfoVOList.get(0).getId());
                                        policyIntent.putExtra("shareType", Constant.SHARE_VOTE_PROJECT);
                                        startActivity(policyIntent);
                                    }
                                } else if (mVoteInfoVOList != null && mVoteInfoVOList.size() >= 2) {
                                    if (mVoteInfoVOList.get(0).getType() == 3) {
                                        Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
                                        policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_CODE);
                                        policyIntent.putExtra("title", "活动公告");
                                        policyIntent.putExtra("href", mVoteInfoVOList.get(0).getHref());
                                        policyIntent.putExtra("id", mVoteInfoVOList.get(0).getId());
                                        policyIntent.putExtra("shareType", Constant.SHARE_VOTE_PROJECT);
                                        startActivity(policyIntent);
                                    } else {
                                        Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
                                        policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_CODE);
                                        policyIntent.putExtra("title", "活动公告");
                                        policyIntent.putExtra("href", mVoteInfoVOList.get(1).getHref());
                                        policyIntent.putExtra("id", mVoteInfoVOList.get(1).getId());
                                        policyIntent.putExtra("shareType", Constant.SHARE_VOTE_PROJECT);
                                        startActivity(policyIntent);
                                    }
                                }

                            } else if (i == R.id.fl_vote_rule) {
                                if (mVoteInfoVOList != null && mVoteInfoVOList.size() == 1) {
                                    if (mVoteInfoVOList.get(0).getType() == 4) {
                                        Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
                                        policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_CODE);
                                        policyIntent.putExtra("title", "投票规则");
                                        policyIntent.putExtra("href", mVoteInfoVOList.get(0).getHref());
                                        startActivity(policyIntent);
                                    }
                                } else if (mVoteInfoVOList != null && mVoteInfoVOList.size() >= 2) {
                                    if (mVoteInfoVOList.get(0).getType() == 4) {
                                        Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
                                        policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_CODE);
                                        policyIntent.putExtra("title", "投票规则");
                                        policyIntent.putExtra("href", mVoteInfoVOList.get(0).getHref());
                                        startActivity(policyIntent);
                                    } else {
                                        Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
                                        policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_CODE);
                                        policyIntent.putExtra("title", "投票规则");
                                        policyIntent.putExtra("href", mVoteInfoVOList.get(1).getHref());
                                        startActivity(policyIntent);
                                    }
                                }

                            }
                        }


                    });
                    viewHolder.setItemChildViewClickListener(R.id.fl_activity_detail, R.id.fl_vote_rule);

                } else if (viewType == 1) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_vote_model_gridview, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {

                            TextView m = holder.getView(R.id.more);
                            if (is_more == 2)
                                m.setVisibility(View.GONE);
                            TextView ti = holder.getView(R.id.title);
                            ti.setText(title2);
                            LinearLayout voteView = holder.getView(R.id.view_group);
                            voteView.removeAllViews();
                            voteView.setVisibility(View.GONE);
                            holder.getView(R.id.gv_imageview_projects).setVisibility(View.GONE);

                            LinearLayout l1 = holder.getView(R.id.ll);
                            if (mProjectVOs == null || mProjectVOs.size() == 0) {

                                ti.setVisibility(View.GONE);
                                m.setVisibility(View.GONE);
                                l1.setVisibility(View.GONE);
                                return;
                            }

                            if (type == 2) {
                                holder.getView(R.id.gv_imageview_projects).setVisibility(View.VISIBLE);
                            } else {
                                holder.getView(R.id.view_group).setVisibility(View.VISIBLE);
                            }

                            GridView gridView = holder.getView(R.id.gv_imageview_projects);

//                            gv_imageview_projects
//                            item_projects_image_vote

                            final int width = (Utils.getScreenWidth(getActivity()) - 2 * getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) - getResources().getDimensionPixelSize(R.dimen.view_margin_12dp) * 2) / 3;

                            if (type == 2) {
                                gridView.setAdapter(new CommonAdapter<ProjectVO>(getActivity(), mProjectVOs, R.layout.item_projects_image_vote) {
                                    @Override
                                    public void convert(ViewHolder helper, ProjectVO item) {
//                            helper.setNetImage(R.id.item_img, item.toString(), Communications.TAG_IMG_DEFAULT);
                                        FrameLayout fl = helper.getView(R.id.fl_vote_image);
                                        LinearLayout.LayoutParams layoutParams = null;
                                        layoutParams = (LinearLayout.LayoutParams) fl.getLayoutParams();
                                        layoutParams.width = width;
                                        layoutParams.height = width;
                                        fl.setLayoutParams(layoutParams);
//                                    ImageDisplayUtil.displayImage(ImageDisplayUtil.IMG_THUMBNAIL, item.toString(), imgview, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
//                                        Glide.with(getContext()).load(Constant.IMAGE_URL + item).into(imgview);
//                                        ImageDisplayUtil.LoadNetImage(imgview, getContext(), item);
//                                        helper.setNetImage(Constant.IMG_COMPRESS, R.id.img, item.getPic(), ImageDisplayUtil.DISPLAY_ROUND_IMAGE);
                                        ImageView imageview = helper.getView(R.id.img);
                                        TextView num = helper.getView(R.id.num);
                                        num.setText(item.getNumber() + "");
                                        TextView name = helper.getView(R.id.name);
                                        Utils.setSexIconState(getContext(), name, item.getSex());
                                        name.setText(item.getSmall_title() + "");//item.getCreate_number() + " " +
                                        ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, item.getPic(), imageview);
                                    }
                                });
                                gridView.setOnItemClickListener(new MGridviewOnItemClickListener(mProjectVOs));

                            } else {
                                int allsize = (mProjectVOs.size() < 5 ? mProjectVOs.size() : 5);
                                for (int i = 0; i < allsize; i++) {
                                    View view = null;
                                    ProjectVO vo = mProjectVOs.get(i);
                                    if (type == 2) {


//                                    view = LayoutInflater.from(getActivity()).inflate(R.layout.item_small_man, null);
//
//                                    TextView title = (TextView) view.findViewById(R.id.title);
//                                    TextView num = (TextView) view.findViewById(R.id.num);
//                                    num.setText(vo.getNumber());
//                                    title.setText(vo.getCreate_number() + "  " + vo.getTitle());
//                                    final Intent intent1 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
//                                    intent1.putExtra("id", vo.getId());
//                                    intent1.putExtra("project_id", project_id);
//                                    intent1.putExtra("project_title", title2);
//                                    intent1.putExtra("title1", title2);
//                                    intent1.putExtra("title", vo.getSmall_title());
//                                    intent1.putExtra("content", vo.getTitle());
//                                    intent1.putExtra("type1", 1);
//                                    intent1.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
//                                    view.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            startActivity(intent1);
//                                        }
//                                    });
//                                    ImageView img1 = (ImageView) view.findViewById(R.id.img1);
//                                    ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, vo.getPic(), img1, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
//                                    ImageView img3 = (ImageView) view.findViewById(R.id.img3);
//                                    ImageView img2 = (ImageView) view.findViewById(R.id.img2);
//                                    ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
//                                    progressBar.setMax(Utils.isInteger(all_poll));
//                                    progressBar.setProgress(Utils.isInteger(vo.getNumber()));
//                                    if (i == 0) {
//                                        img3.setImageResource(R.mipmap.img111);
//                                    } else if (i == 1) {
//                                        img3.setImageResource(R.mipmap.vote_volunteers_number2);
//
//                                    } else if (i == 2) {
//                                        img3.setImageResource(R.mipmap.vote_volunteers_number3);
//                                    } else {
//                                        img2.setVisibility(View.GONE);
//                                        img3.setVisibility(View.GONE);
//
//                                    }


                                    } else {
                                        view = LayoutInflater.from(getActivity()).inflate(R.layout.item_organiztion, null);
//                                    TextView num= (TextView) view.findViewById(R.id.num);
//                                    num.setText(vo.getNumber());
                                        final Intent intent1 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                        intent1.putExtra("id", vo.getId());
                                        intent1.putExtra("title", vo.getSmall_title());
                                        intent1.putExtra("content", vo.getTitle());
                                        intent1.putExtra("project_id", project_id);
                                        intent1.putExtra("project_title", title2);
                                        intent1.putExtra("title1", title2);
                                        intent1.putExtra("type1", 1);


                                        intent1.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                        view.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(intent1);
                                            }
                                        });
                                        TextView title = (TextView) view.findViewById(R.id.project_name);
                                        title.setMaxLines(2);
                                        title.setText(vo.getTitle());//vo.getCreate_number() + "  " +
                                        TextView num = (TextView) view.findViewById(R.id.num);
                                        num.setText(vo.getNumber());
                                        ImageView img3 = (ImageView) view.findViewById(R.id.img);
                                        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
                                        progressBar.setMax(Utils.isInteger(all_poll));
                                        progressBar.setProgress(Utils.isInteger(vo.getNumber()));
                                        if (i == 0) {
                                            img3.setImageResource(R.mipmap.vote_1);
                                        } else if (i == 1) {
                                            img3.setImageResource(R.mipmap.vote_2);

                                        } else if (i == 2) {
                                            img3.setImageResource(R.mipmap.vote_3);
                                        } else {
                                            img3.setVisibility(View.INVISIBLE);

                                        }

                                        voteView.addView(view);
                                        View f = LayoutInflater.from(getActivity()).inflate(R.layout.divider_mine, null);
                                        if (i < allsize - 1) {
                                            voteView.addView(f);
                                        }

                                    }

                                }
                            }


                        }

                        class MGridviewOnItemClickListener implements AdapterView.OnItemClickListener {

                            private List<ProjectVO> vos;

                            public MGridviewOnItemClickListener(List<ProjectVO> vos) {
                                this.vos = vos;
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int arg2, long id) {
                                if (vos == null || vos.size() == 0)
                                    return;
                                ProjectVO vo = vos.get(arg2);
                                if (vo == null)
                                    return;
                                final Intent intent1 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                intent1.putExtra("id", vo.getId());
                                intent1.putExtra("project_id", project_id);
                                intent1.putExtra("project_title", title2);
                                intent1.putExtra("title1", title2);
                                intent1.putExtra("title", vo.getSmall_title());
                                intent1.putExtra("content", vo.getTitle());
                                intent1.putExtra("type1", 1);
                                intent1.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                startActivity(intent1);

                            }
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            int i = view.getId();
                            if (i == R.id.more) {
                                Intent intent = new Intent(getActivity(), VoteMoreActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("project_id", project_id);
                                intent.putExtra("project_title", title2);
                                intent.putExtra("title", title2);

                                startActivity(intent);

                            }
                        }
                    });
                    viewHolder.setItemChildViewClickListener(R.id.more);

                } else if (viewType == 2) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.lay_out, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            LinearLayout ll = holder.getView(R.id.ll1);
                            ll.removeAllViews();
                            if (mVoteMoreVOList == null || mVoteMoreVOList.size() == 0) {
                                return;
                            }
                            for (int i = 0; i < mVoteMoreVOList.size(); i++) {
                                final VoteMoreVO vo = mVoteMoreVOList.get(i);

                                View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_vote_model_gridview, null);//原有布局  item_vote_model
//                                final Intent intent1 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
//                                intent1.putExtra("id",vo.getId());
//                                intent1.putExtra("title",vo.getTitle());
//                                intent1.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
//                                v.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        startActivity(intent1);
//                                    }
//                                });
                                LinearLayout voteView = (LinearLayout) v.findViewById(R.id.view_group);
                                voteView.removeAllViews();
                                voteView.setVisibility(View.GONE);
                                v.findViewById(R.id.gv_imageview_projects).setVisibility(View.GONE);

                                TextView t = (TextView) v.findViewById(R.id.title);
                                t.setText(vo.getTitle());
                                TextView m = (TextView) v.findViewById(R.id.more);
                                if (Utils.isInteger(vo.getIs_more()) == 2)
                                    m.setVisibility(View.GONE);

                                m.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String id2 = vo.getId();
                                        Intent intent1 = new Intent(getActivity(), VoteMoreActivity.class);
                                        intent1.putExtra("id", id2);
                                        intent1.putExtra("project_id", project_id);
                                        intent1.putExtra("project_title", title2);

                                        intent1.putExtra("title", vo.getTitle());
                                        startActivity(intent1);
                                    }
                                });


                                List<ShowListVO> l = vo.getList();
                                if (l == null || l.size() == 0) {
                                    v.findViewById(R.id.ll).setVisibility(View.GONE);
                                    return;
                                }

                                if (vo.getType().equals("2")) {
                                    v.findViewById(R.id.gv_imageview_projects).setVisibility(View.VISIBLE);
                                } else {
                                    v.findViewById(R.id.view_group).setVisibility(View.VISIBLE);
                                }

                                if (vo.getType().equals("2")) {
                                    final int width = (Utils.getScreenWidth(getActivity()) - 2 * getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) - getResources().getDimensionPixelSize(R.dimen.view_margin_12dp) * 2) / 3;
                                    GridView gridView = (GridView) v.findViewById(R.id.gv_imageview_projects);
                                    gridView.setAdapter(new CommonAdapter<ShowListVO>(getActivity(), l, R.layout.item_projects_image_vote) {
                                        @Override
                                        public void convert(ViewHolder helper, ShowListVO item) {
//                            helper.setNetImage(R.id.item_img, item.toString(), Communications.TAG_IMG_DEFAULT);
                                            FrameLayout fl = helper.getView(R.id.fl_vote_image);
                                            LinearLayout.LayoutParams layoutParams = null;
                                            layoutParams = (LinearLayout.LayoutParams) fl.getLayoutParams();
                                            layoutParams.width = width;
                                            layoutParams.height = width;
                                            fl.setLayoutParams(layoutParams);
//                                    ImageDisplayUtil.displayImage(ImageDisplayUtil.IMG_THUMBNAIL, item.toString(), imgview, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
//                                        Glide.with(getContext()).load(Constant.IMAGE_URL + item).into(imgview);
//                                        ImageDisplayUtil.LoadNetImage(imgview, getContext(), item);
//                                        helper.setNetImage(Constant.IMG_COMPRESS, R.id.img, item.getPic(), ImageDisplayUtil.DISPLAY_ROUND_IMAGE);
                                            ImageView imageview = helper.getView(R.id.img);
                                            TextView num = helper.getView(R.id.num);
                                            num.setText(item.getNumber() + "");
                                            TextView name = helper.getView(R.id.name);
                                            name.setText(item.getSmall_title());//item.getCreate_number() + " " +
                                            Utils.setSexIconState(getContext(), name, item.getSex());

                                            ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, item.getPic(), imageview);
                                        }
                                    });
                                    gridView.setOnItemClickListener(new MGridviewOnItemClickListener(vo, l));
                                }

                                if (vo.getType().equals("1")) {
                                    ViewGroup ll2 = (ViewGroup) v.findViewById(R.id.view_group);
                                    int allsize = (l.size() < 3 ? l.size() : 3);
                                    for (int j = 0; j < allsize; j++) {
                                        View v1;
                                        ShowListVO sh = l.get(j);
                                        if (vo.getType().equals("1")) {
                                            v1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_organiztion, null);
                                            final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                            intent2.putExtra("id", sh.getId());
                                            intent2.putExtra("project_id", project_id);
                                            intent2.putExtra("project_title", title2);

                                            intent2.putExtra("title1", vo.getTitle());
                                            intent2.putExtra("title", vo.getTitle());
                                            intent2.putExtra("content", sh.getTitle());
                                            intent2.putExtra("type1", 1);

                                            intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                            v1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(intent2);
                                                }
                                            });
                                            TextView title = (TextView) v1.findViewById(R.id.project_name);
                                            title.setMaxLines(2);
                                            title.setText(sh.getTitle());//sh.getCreate_number() + "  " +
                                            TextView num = (TextView) v1.findViewById(R.id.num);
                                            num.setText(sh.getNumber());
                                            ImageView img3 = (ImageView) v1.findViewById(R.id.img);
                                            ProgressBar progressBar = (ProgressBar) v1.findViewById(R.id.progress);
                                            progressBar.setMax(Utils.isInteger(vo.getAll_poll()));
                                            progressBar.setProgress(Utils.isInteger(sh.getNumber()));
                                            if (j == 0) {
                                                img3.setImageResource(R.mipmap.vote_1);
                                            } else if (j == 1) {
                                                img3.setImageResource(R.mipmap.vote_2);

                                            } else if (j == 2) {
                                                img3.setImageResource(R.mipmap.vote_3);
                                            }


                                        } else {
                                            v1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_small_man_twostyle, null);
                                            final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                            intent2.putExtra("id", sh.getId());
                                            intent2.putExtra("title1", vo.getTitle());
                                            intent2.putExtra("title", vo.getTitle());
                                            intent2.putExtra("content", sh.getTitle());
                                            intent2.putExtra("project_id", project_id);
                                            intent2.putExtra("project_title", title2);
                                            intent2.putExtra("type1", 1);

                                            intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                            v1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(intent2);
                                                }
                                            });
                                            TextView title = (TextView) v1.findViewById(R.id.title);
                                            title.setText(sh.getTitle());//sh.getCreate_number() + "  " +
                                            TextView tv_small_title = (TextView) v1.findViewById(R.id.tv_small_title);
                                            tv_small_title.setText(sh.getSmall_title());//sh.getCreate_number() + " " +
                                            Utils.setSexIconState(getContext(), tv_small_title, sh.getSex());

                                            ImageView img1 = (ImageView) v1.findViewById(R.id.img1);
//                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, sh.getPic(), img1, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, sh.getPic(), (ImageView) holder.getView(R.id.img1));
                                            TextView num = (TextView) v1.findViewById(R.id.num);
                                            num.setText(sh.getNumber());
                                            ImageView img3 = (ImageView) v1.findViewById(R.id.img3);
                                            ProgressBar progressBar = (ProgressBar) v1.findViewById(R.id.progress);
                                            progressBar.setMax(Utils.isInteger(vo.getAll_poll()));
                                            progressBar.setProgress(Utils.isInteger(sh.getNumber()));
                                            if (j == 0) {
                                                img3.setImageResource(R.mipmap.img111);
                                            } else if (j == 1) {
                                                img3.setImageResource(R.mipmap.vote_volunteers_number2);

                                            } else if (j == 2) {
                                                img3.setImageResource(R.mipmap.vote_volunteers_number3);
                                            }


                                        }
                                        View f = LayoutInflater.from(getActivity()).inflate(R.layout.divider_mine, null);


                                        ll2.addView(v1);

                                        if (j < allsize - 1) {
                                            ll2.addView(f);
                                        }

                                    }
                                }
//                                View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_diveder, null);

                                ll.addView(v);
//                                ll.addView(v1);

                            }


                        }

                        class MGridviewOnItemClickListener implements AdapterView.OnItemClickListener {

                            private List<ShowListVO> vos;

                            private VoteMoreVO voteMoreVO;

                            public MGridviewOnItemClickListener(VoteMoreVO voteMoreVO, List<ShowListVO> vos) {
                                this.vos = vos;
                                this.voteMoreVO = voteMoreVO;
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int arg2, long id) {
                                if (vos == null || vos.size() == 0)
                                    return;
                                ShowListVO vo = vos.get(arg2);
                                if (vo == null)
                                    return;
                                final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                intent2.putExtra("id", vo.getId());
                                intent2.putExtra("title1", voteMoreVO.getTitle());
                                intent2.putExtra("title", voteMoreVO.getTitle());
                                intent2.putExtra("content", vo.getTitle());
                                intent2.putExtra("project_id", project_id);
                                intent2.putExtra("project_title", title2);
                                intent2.putExtra("type1", 1);

                                intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                startActivity(intent2);

                            }
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                        }
                    });

                } else if (viewType == 3) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_last, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            TextView vote = holder.getView(R.id.vote);

                            if (Utils.isInteger(isopen) == 2) {
                                vote.setText("投票已结束");
//                                vote.setBackgroundColor(Color.GRAY);
                                vote.setBackgroundResource(R.drawable.background_ripple_button_violet);

//                                vote.setTextAppearance(R.style.Ripple_Button_Violet);
                                vote.setClickable(true);
                            } else if (Utils.isInteger(is_vote) == 1) {
                                vote.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showTost("您已投过票了", 1);
                                    }
                                });

                                vote.setBackgroundResource(R.drawable.background_ripple_button_violet);

                                vote.setText("已投票");
//                                vote.setTextAppearance(R.style.Ripple_Button_Violet);

//                                vote.setBackgroundColor(getActivity().getColor(R.color.grey));
                            } else {
                                return;
                            }

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                            int i = view.getId();
                            if (i == R.id.vote) {
                                doVote();

                            } else if (i == R.id.watch) {
                                Intent intent1 = new Intent(getActivity(), VoteRankActivity.class);
                                intent1.putExtra("project_id", id);

//                                    intent1.putExtra("titler",titler);
                                intent1.putExtra("title", title2);
//                                    intent1.putExtra("id",id);
                                startActivity(intent1);

                            }
                        }
                    });
                    viewHolder.setItemChildViewClickListener(R.id.vote, R.id.watch);
                }

                return viewHolder;
            }

            @Override
            public int getItemViewType(int position) {
                return (Integer) (getData().get(position));

            }

        };
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));//adapter.getItemDecoration()
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);

    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.mipmap.icon_spilt_comm_detail);
        }

//        @Override
//        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            int left = parent.getPaddingLeft();
//            int right = parent.getWidth() - parent.getPaddingRight();
//
//            int childCount = parent.getChildCount() - 1;
//            for (int i = 0; i < childCount; i++) {
//                View child = parent.getChildAt(i);
//
//                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//                int top = child.getBottom() + params.bottomMargin;
//                int bottom = top + mDivider.getIntrinsicHeight();
//
//                mDivider.setBounds(left, top, right, bottom);
//                mDivider.draw(c);
//            }
//        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {

            outRect.set(0, 0, 0, 0);
//            outRect.set(0,
//                    getResources().getDimensionPixelSize(R.dimen.item_divider_height_8dp), 0,
//                    0);
        }
    }

    @Override
    public void loadData() {
        getMainList(Constant.LIST_REFRESH);

    }

    private void getMainList(final int tag) {
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action", "app_findNewVoteShow");
        id1 = getActivity().getIntent().getStringExtra("id");
        JSONUtil.put(params, "id", id1);
        JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
        Log.e("params", params.toString());
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                inVisibleLoading();
                try {
                    JSONObject result = new JSONObject(jsonString);
                    id = JSONUtil.getString(result, "id");
                    type = JSONUtil.getInt(result, "type");
                    pic = JSONUtil.getString(result, "banner_pic");
                    title2 = JSONUtil.getString(result, "title");
                    all_poll = JSONUtil.getString(result, "all_poll");
                    is_more = JSONUtil.getInt(result, "is_more");
                    isopen = JSONUtil.getInt(result, "isopen");
                    is_vote = JSONUtil.getInt(result, "is_vote");
                    has_more = JSONUtil.getInt(result, "has_more");
                    is_main = JSONUtil.getInt(result, "is_main");
                    JSONArray voteArry = JSONUtil.getJSONArray(result, "voteList");
                    mVoteMoreVOList = JSONUtil.fromJson(voteArry.toString(), new TypeToken<List<VoteMoreVO>>() {
                    }.getType());
                    JSONArray voteInfo = JSONUtil.getJSONArray(result, "voteInfos");
                    mVoteInfoVOList = JSONUtil.fromJson(voteInfo.toString(), new TypeToken<List<VoteInfoVO>>() {
                    }.getType());

                    mVoteArrVO = JSONUtil.fromJson(JSONUtil.getJSONObject(result, "voteArr").toString(), VoteArrVO.class);

                    Log.e("ooo", mVoteMoreVOList.size() + "");
                    //新闻
                    JSONArray infoArray = JSONUtil.getJSONArray(result, "informationList");
                    mInfoVOs = JSONUtil.fromJson(infoArray.toString(), new TypeToken<List<InfoVO>>() {
                    }.getType());

                    JSONArray proArray = JSONUtil.getJSONArray(result, "project");
                    mProjectVOs = JSONUtil.fromJson(proArray.toString(), new TypeToken<List<ProjectVO>>() {
                    }.getType());

                    List<Integer> datas = new ArrayList<Integer>();
                    datas.add(0);
                    datas.add(1);
                    datas.add(2);
                    datas.add(3);

                    ((BaseAdapter) mRecyclerView.getAdapter()).setData(datas);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                    visibleNoData();
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });


    }

    /**
     * 我要投票
     */
    private void doVote() {
        if (TextUtils.isEmpty(id))
            return;
        showProgress(getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action", "app_findNewVoteInfo");
        JSONUtil.put(params, "id", id);
        JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
        Log.e("..........", "token:" + PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN) + "id" + id);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
//                showTost("投票成功",1);
                CommVoteDetailNewActivity.sNewVoteBeanVOList = new ArrayList<NewVoteBeanVO>();
                CommVoteDetailNewActivity.sVoteIds=null;
                NewVoteBeanVO bean = JSONUtil.fromJson(jsonString, NewVoteBeanVO.class);
                CommVoteDetailNewActivity.sNewVoteBeanVOList.add(bean);

                String mainTitle = bean.getTitle();
                JSONArray voteArry = null;
                try {
                    voteArry = JSONUtil.getJSONArray(new JSONObject(jsonString), "viceVote");
                    List<NewVoteBeanVO> hotList = JSONUtil.fromJson(voteArry.toString(), new TypeToken<List<NewVoteBeanVO>>() {
                    }.getType());
                    if (!TextUtils.isEmpty(mainTitle)) {
                        for (NewVoteBeanVO vo : hotList) {
                            vo.setTitle(mainTitle);
                        }
                    }
                    CommVoteDetailNewActivity.sNewVoteBeanVOList.addAll(hotList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CommVoteDetailNewActivity.projectId = id;
                if (CommVoteDetailNewActivity.sNewVoteBeanVOList == null || CommVoteDetailNewActivity.sNewVoteBeanVOList.size() == 0)
                    return;
                if (CommVoteDetailNewActivity.sNewVoteBeanVOList.get(0).getIsopen() == 2) {
                    showTost("投票已结束", 1);
                    return;
                }

//                CommVoteDetailNewActivity.project_title=project_title;

                Intent intent = new Intent(getContext(), CommVoteDetailNewActivity.class);
                intent.putExtra(Constant.KEY_POSITION, 0);
                CommVoteDetailNewActivity.projectId = project_id;
                CommVoteDetailNewActivity.project_title = title2;
//                intent.putExtra("project_id",project_id);
                intent.putExtra("project_title", title2);
                startActivity(intent);

            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message, 1);

            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
                visibleNoNetWork();
            }
        });
    }


}
