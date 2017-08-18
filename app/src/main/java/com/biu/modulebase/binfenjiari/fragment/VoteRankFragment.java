package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.VoteRankMoreActivity;
import com.biu.modulebase.binfenjiari.activity.WebViewVoteDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.ProjectVO;
import com.biu.modulebase.binfenjiari.model.ShowListVO;
import com.biu.modulebase.binfenjiari.model.VoteMoreVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
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

public class VoteRankFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;
    String id;
    String Id;
    String project_id;
    int type;
    String title;
    String all_poll;
    String is_more;
    String is_main;
    List<ProjectVO> projectList;
    List<VoteMoreVO> voteList;
    String title2;

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
//        id = getActivity().getIntent().getStringExtra("id");
        project_id = id = getActivity().getIntent().getStringExtra("project_id");
        Log.e("id2", id);
        title2 = getActivity().getIntent().getStringExtra("title");
        getBaseActivity().setBackNavigationIcon();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                getMainList();
                mRefreshLayout.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                mRefreshLayout.setLoading(false);
            }
        });
        BaseAdapter adapter = new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = null;
                switch (viewType) {
                    case 0://item_vote_model
                        viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_vote_model, parent, false), new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                Log.e("type", type + "");

                                TextView m = holder.getView(R.id.more);
                                if (Utils.isInteger(is_more) == 2)
                                    m.setVisibility(View.GONE);

                                LinearLayout voteView = holder.getView(R.id.view_group);
                                voteView.removeAllViews();
//                                holder.getView(R.id.gv_imageview_projects).setVisibility(View.GONE);

                                LinearLayout l1 = holder.getView(R.id.ll);
                                TextView ti = holder.getView(R.id.title);
                                if (projectList == null || projectList.size() == 0) {
                                    m.setVisibility(View.GONE);
                                    ti.setVisibility(View.GONE);
                                    l1.setVisibility(View.GONE);
                                    return;

                                }
                                Log.e("title", title);
                                ti.setText(title);

//                                if (type == 2) {
//                                    holder.getView(R.id.gv_imageview_projects).setVisibility(View.VISIBLE);
//                                } else {
//                                    holder.getView(R.id.view_group).setVisibility(View.VISIBLE);
//                                }
//
//                                GridView gridView = holder.getView(R.id.gv_imageview_projects);
//                                if (type == 2) {
//                                    final int width = (Utils.getScreenWidth(getActivity()) - 2 * getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) - getResources().getDimensionPixelSize(R.dimen.view_margin_12dp) * 2) / 3;
//
//                                    gridView.setAdapter(new CommonAdapter<ProjectVO>(getActivity(), projectList, R.layout.item_projects_image_vote) {
//                                        @Override
//                                        public void convert(ViewHolder helper, ProjectVO item) {
//                                            FrameLayout fl = helper.getView(R.id.fl_vote_image);
//                                            LinearLayout.LayoutParams layoutParams = null;
//                                            layoutParams = (LinearLayout.LayoutParams) fl.getLayoutParams();
//                                            layoutParams.width = width;
//                                            layoutParams.height = width;
//                                            fl.setLayoutParams(layoutParams);
//                                            ImageView imageview = helper.getView(R.id.img);
//                                            TextView num = helper.getView(R.id.num);
//                                            num.setText(item.getNumber() + "");
//                                            TextView name = helper.getView(R.id.name);
//                                            name.setText(item.getTitle() + "");
//                                            ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, item.getPic(), imageview);
//                                        }
//                                    });
//                                    gridView.setOnItemClickListener(new MGridviewOnItemClickListener(projectList));
//
//                                }

//                                if (type == 1) {   (projectList.size() < 5 ? projectList.size() : 5)
                                    for (int i = 0; i < (projectList.size() < 5 ? projectList.size() : projectList.size()); i++) {
                                        View view = null;
                                        ProjectVO vo = projectList.get(i);

                                        if (type == 2) {

                                            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_small_man_twostyle, null);
                                            final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                            intent2.putExtra("id", vo.getId());
                                            intent2.putExtra("project_id", project_id);
                                            intent2.putExtra("project_title", title2);
                                            intent2.putExtra("title1", title);
                                            intent2.putExtra("title", vo.getSmall_title());
                                            intent2.putExtra("content", vo.getTitle());
                                            Log.e("title1", vo.getSmall_title());
                                            intent2.putExtra("type1", 1);

                                            intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                            view.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(intent2);
                                                }
                                            });
                                            TextView name = (TextView) view.findViewById(R.id.title);
                                            name.setText(vo.getTitle());

                                            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
                                            Utils.setRankListState(tv_number,i);//vo.getCreate_number()

                                            TextView tv_small_title = (TextView) view.findViewById(R.id.tv_small_title);
                                            tv_small_title.setText(vo.getSmall_title());//vo.getCreate_number()+" "+
                                            Utils.setSexIconState(getContext(),tv_small_title,vo.getSex());
                                            TextView num = (TextView) view.findViewById(R.id.num);
                                            ImageView img1 = (ImageView) view.findViewById(R.id.img1);
                                            ImageView img3 = (ImageView) view.findViewById(R.id.img3);
//                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, vo.getPic(), img1, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, vo.getPic(), img1);
                                            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
                                            progressBar.setMax(Utils.isInteger(all_poll));
                                            progressBar.setProgress(Utils.isInteger(vo.getNumber()));
                                            num.setText(vo.getNumber());
//                                            ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, vo.getPic(), img1);
//                                            ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, vo.getPic(), img1);
                                            if (i == 0) {
                                                img3.setImageResource(R.mipmap.img111);
                                            } else if (i == 1) {
                                                img3.setImageResource(R.mipmap.vote_volunteers_number2);
                                            } else if (i == 2) {
                                                img3.setImageResource(R.mipmap.vote_volunteers_number3);
                                            } else {
                                                ImageView img2 = (ImageView) view.findViewById(R.id.img2);
                                                img3.setVisibility(View.GONE);
                                                img2.setVisibility(View.GONE);
                                            }
                                        } else {
                                            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_organiztion, null);
                                            final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                            intent2.putExtra("id", vo.getId());
                                            intent2.putExtra("project_id", project_id);
                                            intent2.putExtra("project_title", title2);

                                            intent2.putExtra("title", vo.getSmall_title());
                                            intent2.putExtra("content", vo.getTitle());
                                            intent2.putExtra("title1", title);
                                            Log.e("title1", vo.getSmall_title());
                                            intent2.putExtra("type1", 1);

                                            intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                            view.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(intent2);
                                                }
                                            });
                                            TextView name = (TextView) view.findViewById(R.id.project_name);
                                            name.setMaxLines(2);
                                            name.setText(vo.getTitle());//vo.getCreate_number() + "  " +
                                            TextView num = (TextView) view.findViewById(R.id.num);
                                            num.setText(vo.getNumber());
                                            ImageView img = (ImageView) view.findViewById(R.id.img);
                                            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
                                            progressBar.setMax(Utils.isInteger(all_poll));
                                            progressBar.setProgress(Utils.isInteger(vo.getNumber()));
                                            if (i == 0) {
                                                img.setImageResource(R.mipmap.vote_organization_number1);
                                            } else if (i == 1) {
                                                img.setImageResource(R.mipmap.vote_organization_number2);
                                            } else if (i == 2) {
                                                img.setImageResource(R.mipmap.vote_organization_number3);
                                            } else {
                                                img.setVisibility(View.INVISIBLE);
                                            }


                                        }


                                        voteView.addView(view);
                                        View f = LayoutInflater.from(getActivity()).inflate(R.layout.divider_mine, null);
                                        voteView.addView(f);
                                    }
//                                }

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
                                    Intent intent = new Intent(getActivity(), VoteRankMoreActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("project_id", project_id);
                                    intent.putExtra("project_title", title2);
                                    intent.putExtra("title", title);
                                    startActivity(intent);

                                }

                            }
                        });
                        viewHolder.setItemChildViewClickListener(R.id.more);
                        break;
                    case 1:
                        viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.lay_out, parent, false), new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                LinearLayout ll = holder.getView(R.id.ll1);
                                ll.removeAllViews();
                                Log.e("voteList", voteList.size() + "");
                                for (int i = 0; i < voteList.size(); i++) {
                                    final VoteMoreVO vo = voteList.get(i);
                                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_vote_model, null);

                                    TextView t = (TextView) v.findViewById(R.id.title);
                                    t.setText(vo.getTitle());
                                    TextView m = (TextView) v.findViewById(R.id.more);
                                    if (Utils.isInteger(vo.getIs_more()) == 2)
                                        m.setVisibility(View.GONE);
                                    m.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), VoteRankMoreActivity.class);
                                            intent.putExtra("id", vo.getId());
                                            intent.putExtra("project_id", project_id);
                                            intent.putExtra("project_title", title2);
                                            intent.putExtra("title", vo.getTitle());
                                            startActivity(intent);
                                        }
                                    });


                                    LinearLayout ll2 = (LinearLayout) v.findViewById(R.id.view_group);
                                    ll2.removeAllViews();
                                    List<ShowListVO> l = vo.getList();
                                    LinearLayout l1 = (LinearLayout) v.findViewById(R.id.ll);

                                    if (l.size() == 0 || l == null) {
                                        l1.setVisibility(View.GONE);
                                        break;
                                    }
                                    for (int j = 0; j < (l.size() < 5 ? l.size() : 5); j++) {
                                        View v1;
                                        ShowListVO sh = l.get(j);
                                        if (Utils.isInteger(vo.getType()) == 1) {
                                            v1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_organiztion, null);
                                            ProgressBar progressBar = (ProgressBar) v1.findViewById(R.id.progress);
                                            progressBar.setMax(Utils.isInteger(vo.getAll_poll()));
                                            progressBar.setProgress(Utils.isInteger(sh.getNumber()));
                                            final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                            intent2.putExtra("id", sh.getId());
                                            intent2.putExtra("project_id", project_id);
                                            intent2.putExtra("title", sh.getSmall_title());
                                            intent2.putExtra("title1", vo.getTitle());
                                            intent2.putExtra("content", sh.getTitle());
                                            Log.e("title1", sh.getSmall_title());
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
                                            if (j == 0) {
                                                img3.setImageResource(R.mipmap.vote_1);
                                            } else if (j == 1) {
                                                img3.setImageResource(R.mipmap.vote_2);

                                            } else if (j == 2) {
                                                img3.setImageResource(R.mipmap.vote_3);
                                            } else {
                                                img3.setVisibility(View.INVISIBLE);
                                            }


                                        } else {
                                            v1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_small_man_twostyle, null);
                                            ProgressBar progressBar = (ProgressBar) v1.findViewById(R.id.progress);
                                            progressBar.setMax(Utils.isInteger(vo.getAll_poll()));
                                            progressBar.setProgress(Utils.isInteger(sh.getNumber()));
                                            final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                            intent2.putExtra("id", sh.getId());
                                            intent2.putExtra("project_id", project_id);
                                            intent2.putExtra("project_title", title2);
                                            intent2.putExtra("title1", vo.getTitle());
                                            intent2.putExtra("content", sh.getTitle());

                                            intent2.putExtra("title", sh.getSmall_title());
                                            Log.e("title1", sh.getSmall_title());
                                            intent2.putExtra("type1", 1);

                                            intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                            v1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(intent2);
                                                }
                                            });
                                            TextView title = (TextView) v1.findViewById(R.id.title);
                                            //sh.getCreate_number() + "  " +
                                            title.setText(sh.getTitle());
                                            ImageView img1 = (ImageView) v1.findViewById(R.id.img1);

                                            TextView tv_number = (TextView) v1.findViewById(R.id.tv_number);
                                            Utils.setRankListState(tv_number,j);//sh.getCreate_number()

                                            TextView tv_small_title = (TextView) v1.findViewById(R.id.tv_small_title);
                                            tv_small_title.setText(sh.getSmall_title());//sh.getCreate_number()+" "+
                                            Utils.setSexIconState(getContext(),tv_small_title,sh.getSex());

//                                            ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, sh.getPic(), img1, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                            ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, sh.getPic(), img1);
                                            TextView num = (TextView) v1.findViewById(R.id.num);
                                            num.setText(sh.getNumber());
                                            ImageView img2 = (ImageView) v1.findViewById(R.id.img2);
                                            ImageView img3 = (ImageView) v1.findViewById(R.id.img3);
                                            if (j == 0) {
                                                img3.setImageResource(R.mipmap.img111);
                                            } else if (j == 1) {
                                                img3.setImageResource(R.mipmap.vote_volunteers_number2);

                                            } else if (j == 2) {
                                                img3.setImageResource(R.mipmap.vote_volunteers_number3);
                                            } else {
                                                img2.setVisibility(View.GONE);
                                                img3.setVisibility(View.GONE);
                                            }


                                        }
                                        View f = LayoutInflater.from(getActivity()).inflate(R.layout.divider_mine, null);
                                        ll2.addView(v1);
                                        ll2.addView(f);

                                    }
//                                    ShowListVO sh=l.get(2);
//
//                                    if (vo.getType() == "1") {
//                                        View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.part_vote_select_item, null);
//                                        TextView title= (TextView) v1.findViewById(R.id.project_name);
//                                        title.setText(sh.getTitle());
//                                        TextView num= (TextView) v1.findViewById(R.id.num);
//                                        num.setText(sh.getNumber());
//                                        ImageView img3= (ImageView) v1.findViewById(R.id.img);
//                                        img3.setImageResource(R.mipmap.vote_3);
//                                    }else {
//                                        View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_small_man, null);
//                                        TextView title= (TextView) v1.findViewById(R.id.title);
//                                        title.setText(sh.getTitle());
//                                        ImageView img1 = (ImageView) v1.findViewById(R.id.img1);
//                                        ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL, sh.getPic(), img1, ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
//                                        TextView num= (TextView) v1.findViewById(R.id.num);
//                                        num.setText(sh.getNumber());
//                                        ImageView img3= (ImageView) v1.findViewById(R.id.img3);
//                                        img3.setImageResource(R.mipmap.vote_volunteers_number3);
//
//                                    }
                                    View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_diveder, null);
                                    ll.addView(v);
                                    ll.addView(v1);
                                }
                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position) {
                                int i = view.getId();
                                if (i == R.id.more) {
                                    Intent intent = new Intent(getActivity(), VoteRankMoreActivity.class);
                                    intent.putExtra("title", title);
                                    startActivity(intent);

                                }
                            }
                        });
                        break;
                }
                return viewHolder;
            }

            @Override
            public int getItemViewType(int position) {
                return (Integer) (getData().get(position));
            }

//            @Override
//            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.set(0, 0, 0, 0);
//            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    public void loadData() {

        getMainList();
    }

    public void getMainList() {
        inVisibleLoading();
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action", "app_voteRankingShow");
        JSONUtil.put(params, "id", id);
        Log.e("params+:", params.toString());
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    id = JSONUtil.getString(result, "id");
                    type = JSONUtil.getInt(result, "type");
                    title = JSONUtil.getString(result, "title");
                    all_poll = JSONUtil.getString(result, "all_poll");
                    is_main = JSONUtil.getString(result, "is_main");

                    is_more = JSONUtil.getString(result, "is_more");
                    JSONArray array1 = JSONUtil.getJSONArray(result, "projectList");
                    JSONArray array2 = JSONUtil.getJSONArray(result, "voteList");
                    voteList = JSONUtil.fromJson(array2.toString(), new TypeToken<List<VoteMoreVO>>() {
                    }.getType());
                    projectList = JSONUtil.fromJson(array1.toString(), new TypeToken<List<ProjectVO>>() {
                    }.getType());
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    List<Integer> datas = new ArrayList<Integer>();
                    if (Utils.isInteger(is_main) == 1) {
                        datas.add(0);
                        datas.add(1);
                    } else {
                        datas.add(0);

                    }


                    ((BaseAdapter) mRecyclerView.getAdapter()).setData(datas);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCodeError(int key, String message) {
                if (key == 3) {
                    visibleNoData();
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
}
