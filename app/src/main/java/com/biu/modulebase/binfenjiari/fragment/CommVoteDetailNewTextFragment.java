package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommVoteDetailNewActivity;
import com.biu.modulebase.binfenjiari.activity.WebViewVoteDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.eventbus.VoteNextEvent;
import com.biu.modulebase.binfenjiari.model.NewVoteBeanVO;
import com.biu.modulebase.binfenjiari.model.VoteNewProjectVO;
import com.biu.modulebase.binfenjiari.model.VoteProjectVO;
import com.biu.modulebase.binfenjiari.other.pop.PopSearchVoteList;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/4/18.
 */
public class CommVoteDetailNewTextFragment<E> extends BaseFragment {
    private static final String TAG = "CommVoteDetailNewTextFragment";

    private LSwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private String id;

    private int mposition;

    private NewVoteBeanVO mNewVoteBeanVO;

//    private VoteVO voteVO;

    private long time;

    private int pageNum;

    private TextView vote_rule;
    private TextView extra_day;

    private LinearLayout layout_vote;
    private List<VoteProjectVO> projectVOList = new ArrayList<>();

    private boolean doDetail = false;

    private boolean doProject = false;
    private TextView fab_vote;

    private HashMap<String, String> voteMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            id = args.getString(Constant.KEY_ID);
            mposition = args.getInt(Constant.KEY_POSITION);

            if (CommVoteDetailNewActivity.sNewVoteBeanVOList != null) {
                mNewVoteBeanVO = CommVoteDetailNewActivity.sNewVoteBeanVOList.get(mposition);
            }
        }
    }

    public static CommVoteDetailNewTextFragment newInstance(int position, String id) {
        Bundle args = new Bundle();
        args.putInt(Constant.KEY_POSITION, position);
        args.putString(Constant.KEY_ID, id);
        CommVoteDetailNewTextFragment fragment = new CommVoteDetailNewTextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //header_vote_detail_new_text
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.header_vote_detail_new_image, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    private ViewGroup mFilterLayout;

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        mFilterLayout = (ViewGroup) rootView.findViewById(R.id.ll_pop);

        fab_vote = (TextView) rootView.findViewById(R.id.fab_vote);
        fab_vote.setOnClickListener(this);
        if (CommVoteDetailNewActivity.sNewVoteBeanVOList != null) {
            mNewVoteBeanVO = CommVoteDetailNewActivity.sNewVoteBeanVOList.get(mposition);
            if (mposition == CommVoteDetailNewActivity.sNewVoteBeanVOList.size() - 1) {
                fab_vote.setText("提交");
            }
            getBaseActivity().setToolBarTitle(mNewVoteBeanVO.getTitle());
        }
        mRefreshLayout = (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.requestFocus();
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getProjects(Constant.LIST_LOAD_MORE);
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter = new BaseAdapter(getActivity()) {
            private static final int TYPE_HEADER = 0X0001;//头部
            private static final int TYPE_BODY = 0X0002;

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder = null;
                if (viewType == TYPE_HEADER) {
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_vote_img_new_header, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                            p.setFullSpan(true);
//                            final NewVoteBeanVO bean = (NewVoteBeanVO) data;
//                            holder.setText(R.id.tv_nickname, mNewVoteBeanVO.getTitle());

                            ImageView imageView = holder.getView(R.id.img_banner);
                            int[] widthHeight = ImageDisplayUtil.getWidthHeigh(Utils.getScreenWidth(getActivity()), mNewVoteBeanVO.getBanner_pic());
                            ViewGroup.LayoutParams fl = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                            fl.width = widthHeight[0];
                            fl.height = widthHeight[1];
                            imageView.setLayoutParams(fl);
                            holder.setNetImage(Constant.IMG_COMPRESS, R.id.img_banner, mNewVoteBeanVO.getBanner_pic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
//                            ImageView header = holder.getView(R.id.iv_head_portrait);
//                            header.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
//                                    intent.putExtra(Constant.KEY_ID,bean.getUser_id());
//                                    startActivity(intent);
//                                }
//                            });
//                            int day_type = bean.getDay_type();
//                            if(day_type ==1){
//                                holder.setText(R.id.rule,String.format(getString(R.string.vote_rule1),bean.getMax_number()+""));
//                            }else{
//                                holder.setText(R.id.rule,String.format(getString(R.string.vote_rule2),bean.getMax_number()+""));
//                            }
                            holder.setText(R.id.rule, mNewVoteBeanVO.getRule());
                            long extra = Utils.isLong(mNewVoteBeanVO.getEnd_time()) * 1000 - new Date().getTime();
                            String timeStr = "";
                            try {
                                timeStr = Utils.getDistanceTimes(Utils.getCurrentDate2(), Utils.sec2Date(mNewVoteBeanVO.getEnd_time(), "yyyy-MM-dd HH:mm:ss"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (extra > 0) {
                                holder.setText(R.id.extra_day, String.format(getString(R.string.extra_day), timeStr));
                            } else {
                                holder.setText(R.id.extra_day, "投票已结束");
                            }
//                            holder.setText(R.id.tv_date, Utils.getReleaseTime(new Date(Utils.isLong(bean.getCreate_time())*1000)));
//                            holder.setText(R.id.tv_content,bean.getTitle());
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                        }
                    });
                } else if (viewType == TYPE_BODY) {
                    //part_vote_select_item_detail_new_text
                    viewHolder = new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.part_vote_detail_text_fragment, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                            p.setFullSpan(true);
//
//                            VoteNewProjectVO bean = (VoteNewProjectVO) data;
//                            CheckBox voteCheckBox = holder.getView(R.id.radioBtn);
//                            voteCheckBox.setTag(bean.getId());
//                            voteCheckBox.setText(Utils.getString(bean.getTitle()));
//
//                            if (voteCheckBox == null)
//                                return;
//                            if (bean.isChecked()) {
//                                voteCheckBox.setChecked(true);
//                            } else {
//                                voteCheckBox.setChecked(false);
//                            }
//                            voteCheckBox.setOnCheckedChangeListener(new OnVoteCheckChangeListener(bean.getId()));

                            VoteNewProjectVO bean = (VoteNewProjectVO) data;
                            View imgView = holder.getView(R.id.vote_pic);
                            if (type == 2) {
                                if (TextUtils.isEmpty(bean.getPic())) {
                                    imgView.setVisibility(View.GONE);
                                } else {
                                    imgView.setVisibility(View.VISIBLE);
                                    holder.setNetImage(Constant.IMG_COMPRESS, R.id.vote_pic, bean.getPic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                                }
                            } else {
                                imgView.setVisibility(View.GONE);
                            }
//                            if(TextUtils.isEmpty(bean.getPic())){
//                                imgView.setVisibility(View.GONE);
//                            }else{
//                                imgView.setVisibility(View.VISIBLE);
//                                holder.setNetImage(Constant.IMG_COMPRESS,R.id.vote_pic,bean.getPic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
//                            }

//                            holder.setText(R.id.name,bean.getTitle());
                            holder.setText(R.id.name, String.format("%s%s", "", bean.getTitle()));//bean.getCreate_number()
                            CheckBox checkBox = holder.getView(R.id.voteCheckBox);

                            if(bean.getIs_able()==1) {
                                if (checkBox == null)
                                    return;
                                checkBox.setClickable(true);
                                checkBox.setOnCheckedChangeListener(null);
                                if (voteMap.containsKey(bean.getId())) {
                                    checkBox.setChecked(true);
                                } else {
                                    checkBox.setChecked(false);
                                }
                                checkBox.setOnCheckedChangeListener(new OnVoteCheckChangeListener(bean.getId()));
                            }else{
                                checkBox.setClickable(false);
                            }

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {
                            VoteNewProjectVO bean = (VoteNewProjectVO) getData().get(position);
                            int i = view.getId();
                            if (i == R.id.ll_part_pop_vote || i == R.id.voteCheckBox) {
                                if (bean.getIs_able() == 0) {
                                    showTost("数据审核中", 0);
                                    return;
                                }
//                                    CheckBox checkBox = holder.getView(R.id.voteCheckBox);
//                                    if (checkBox.isChecked()) {
//                                        checkBox.setChecked(false);
//                                    } else {
//                                        checkBox.setChecked(true);
//                                    }

                            } else if (i == R.id.vote_pic || i == R.id.name) {
                                Intent intent = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                intent.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                intent.putExtra("type1", 2);
                                VoteNewProjectVO vo = (VoteNewProjectVO) getData().get(position);
                                intent.putExtra("id", vo.getId());
                                intent.putExtra("title1", mNewVoteBeanVO.getTitle());
                                startActivity(intent);

                            } else {
                            }
                        }

                    });
                }
                viewHolder.setItemChildViewClickListener(R.id.voteCheckBox,R.id.ll_part_pop_vote,R.id.vote_pic,R.id.name);
                return viewHolder;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return TYPE_HEADER;
                } else {
                    return TYPE_BODY;
                }
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                          RecyclerView.State state) {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (childAdapterPosition == 0) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0, 0,
                            getResources().getDimensionPixelSize(R.dimen.item_divider_height_2dp));
                }

            }
        };
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if(position==0){
//                    return 2;
//                }else{
//                    return 1;
//                }
//            }
//        });
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void loadData() {
        time = new Date().getTime() / 1000;
        pageNum = 1;
        getVoteDetail();
//        getProjects();
    }

    private void doVote() {
        showProgress(getClass().getSimpleName());
        StringBuilder projectIdBuilder = new StringBuilder();
        Collection values = voteMap.values();
        Iterator it = values.iterator();
        while (it.hasNext()) {
            projectIdBuilder.append(it.next()).append(",");
        }
        String projectId = projectIdBuilder.substring(0, projectIdBuilder.length() - 1);
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", Constant.MODEL_VOTE);
        JSONUtil.put(params, "action", Constant.ACTION_DO_VOTE);
        JSONUtil.put(params, "projectId", projectId);
        JSONUtil.put(params, "id", getArguments().getString(Constant.KEY_ID));
        JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                int size = voteMap.size();
//                voteVO.setAll_poll(voteVO.getAll_poll() + size);
//                if (voteVO.getDay_type() == 1) {
//                    voteVO.setSurplus_number(0);
//                } else {
//                    voteVO.setSurplus_number(voteVO.getSurplus_number() - size);
//                }
//                fab_vote.setText(String.format(getString(R.string.voted_num),voteVO.getAll_poll()+""));
                voteMap.clear();
                for (int i = 0; i < layout_vote.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) layout_vote.getChildAt(i);
                    checkBox.setChecked(false);
                }

            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                if (key == 10) {//投票次数已完，不能进行投票
                    showTost(message, 1);
                } else if (key == 11) {//当天投票次数已完，不能进行投票
                    showTost(message, 1);
                } else {
                    showTost(message, 1);
                }

            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
                visibleNoNetWork();
            }
        });
    }

    private void getVoteDetail() {
        if (TextUtils.isEmpty(id))
            return;
        if (mNewVoteBeanVO != null) {
            inVisibleLoading();
            setHasOptionsMenu(true);
            ArrayList<E> datas = new ArrayList<>();
            datas.add((E) mNewVoteBeanVO);
            BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
            adapter.setData(datas);
            getProjects(Constant.LIST_REFRESH);
        }

//        JSONObject params = new JSONObject();
//        JSONUtil.put(params, "model", Constant.MODEL_VOTE);
//        JSONUtil.put(params, "action", Constant.ACTION_VOTE_DETAIL);
//        JSONUtil.put(params, "id", getArguments().getString(Constant.KEY_ID));
//        JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
//        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
//            @Override
//            public void onSuccess(String jsonString) {
//                doDetail = true;
//                voteVO = JSONUtil.fromJson(jsonString, VoteVO.class);
//                setViewData();
//                if (doDetail && doProject) {
//                    inVisibleLoading();
//                    mRefreshLayout.stopRefresh();
//                }
//
//            }
//
//            @Override
//            public void onCodeError(int key, String message) {
//                if (key == 3) {
//                    visibleNoData();
//                } else {
//                    showTost(message, 1);
//                }
//            }
//
//            @Override
//            public void onConnectError(String message) {
//                visibleNoNetWork();
//            }
//        });

    }

    private int type = 1;
    private int allPageNumber = 1;

    private void getProjects(final int tag) {
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action", "app_findVoteProjectList");
        JSONUtil.put(params, "id", id);
        JSONUtil.put(params, "time", time);
        Log.e("time", time + "");
        JSONUtil.put(params, "pageNum", pageNum);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result = new JSONObject(jsonString);
                    time = JSONUtil.getLong(result, "time");
                    allPageNumber = JSONUtil.getInt(result, "allPageNumber");
                    type = JSONUtil.getInt(result, "type");
                    JSONObject object = new JSONObject(jsonString);
                    JSONArray array = JSONUtil.getJSONArray(object, "projectList");
                    List<VoteNewProjectVO> projectVOList = JSONUtil.fromJson(array.toString(), new TypeToken<List<VoteNewProjectVO>>() {
                    }.getType());
                    List<E> datas = new ArrayList<>();
                    datas.addAll((Collection<? extends E>) projectVOList);
                    refreshList(tag, datas);

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

    private <T> void refreshList(int tag, List<T> datas) {
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag) {
            case Constant.LIST_REFRESH:
                if (datas.size() == 0) {
                    showTost("没有最新数据...", 1);
                    return;
                }
                adapter.addData(1, datas);
//                adapter.setData(datas);
                mRefreshLayout.setRefreshing(false);
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

    private void showProjects() {
        layout_vote.removeAllViews();
        for (int i = 0; i < projectVOList.size(); i++) {
            VoteProjectVO projectVO = projectVOList.get(i);
            CheckBox radio = (CheckBox) LayoutInflater.from(getContext()).inflate(R.layout.part_vote_select_item_detail_new_text, null);
            radio.setTag(projectVO.getId());
            radio.setText(Utils.getString(projectVO.getTitle()));
            radio.setOnCheckedChangeListener(new OnVoteCheckChangeListener(projectVO.getId()));

            layout_vote.addView(radio, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showVotesFilter(String title) {

        PopSearchVoteList pop = new PopSearchVoteList(this, mFilterLayout, title, id, mNewVoteBeanVO, voteMap, getClass().getSimpleName());
        pop.setOnPopSelectListener(new PopSearchVoteList.OnPopOperatorListener() {

            @Override
            public void popSelect(boolean isCheck, String str) {
                updateVoteState(isCheck, str);
            }

            @Override
            public void popDismiss() {
                hideSoftKeyboard();
                if (mSearchView != null)
                    mSearchView.onActionViewCollapsed();
                getBaseActivity().setToolBarTitle(mNewVoteBeanVO == null ? "投票详情" : mNewVoteBeanVO.getTitle());

            }

            @Override
            public void popSelectFull(String id) {
                selectUpdateCancleFirst(id);
            }
        });
        pop.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate( R.menu.share , menu);
//        inflater.inflate(R.menu.more, menu);
        inflater.inflate(R.menu.more_search, menu);

//        loadSearchMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    SearchView mSearchView;

    /**
     * 加载搜索菜单
     */
    public void loadSearchMenu(Menu menu) {
        final MenuItem item = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        SearchView.SearchAutoComplete mEdit = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        final float density = getResources().getDisplayMetrics().density;
//        mSearchView.setIconified(false);
//        mSearchView.setIconifiedByDefault(false);
        final int closeImgId = getResources().getIdentifier("search_close_btn", "id", getActivity().getPackageName());
        ImageView closeImg = (ImageView) mSearchView.findViewById(closeImgId);
        if (closeImg != null) {
            LinearLayout.LayoutParams paramsImg = (LinearLayout.LayoutParams) closeImg.getLayoutParams();
            paramsImg.topMargin = (int) (-2 * density);
//            paramsImg.rightMargin =(int)(16* density);
            closeImg.setImageResource(R.mipmap.close1);
            closeImg.setLayoutParams(paramsImg);
        }
        final int editViewId = getResources().getIdentifier("search_src_text", "id", getActivity().getPackageName());
        mEdit = (SearchView.SearchAutoComplete) mSearchView.findViewById(editViewId);
        if (mEdit != null) {
            mEdit.setPadding(4, 4, 4, 4);
            mEdit.setHintTextColor(getResources().getColor(R.color.colorTextGray));
            mEdit.setTextColor(getResources().getColor(R.color.colorTextBlack));
            mEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mEdit.setHint("搜索...");
        }
        LinearLayout rootView = (LinearLayout) mSearchView.findViewById(R.id.search_bar);
        rootView.setClickable(true);
        LinearLayout editLayout = (LinearLayout) mSearchView.findViewById(R.id.search_plate);
//        editLayout.setBackgroundResource(R.mipmap.edit_bg);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) editLayout.getLayoutParams();
        LinearLayout tipLayout = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        LinearLayout.LayoutParams tipParams = (LinearLayout.LayoutParams) tipLayout.getLayoutParams();
        tipParams.leftMargin = 0;
        tipParams.rightMargin = 0;
        tipLayout.setLayoutParams(tipParams);
        ImageView icTip = (ImageView) mSearchView.findViewById(R.id.search_mag_icon);
        icTip.setImageResource(R.mipmap.search);
        params.topMargin = (int) (4 * density);
        params.bottomMargin = (int) (4 * density);
        params.rightMargin = (int) (16 * density);
        editLayout.setLayoutParams(params);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().setToolBarTitle("");
            }

        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getBaseActivity().setToolBarTitle(mNewVoteBeanVO == null ? "投票详情" : mNewVoteBeanVO.getTitle());
                return false;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                showTypePop(getBaseActivity().getToolbar());
                showVotesFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_more) {
            OtherUtil.showMoreOperate(CommVoteDetailNewTextFragment.this, id, mNewVoteBeanVO.getTitle(), CommVoteDetailNewActivity.project_title, null,
                    Constant.SHARE_VOTE_PROJECT_INFO, -1, "-1",
                    false, null, -1, null);

        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 更改id
     *
     * @param isCheck
     * @param id
     */
    public void updateVoteState(boolean isCheck, String id) {
        BaseAdapter baseAdapter = (BaseAdapter) (mRecyclerView.getAdapter());
        ArrayList<E> datas = (ArrayList<E>) baseAdapter.getData();
        for (int i = 0; i < datas.size(); i++) {
            E data = datas.get(i);
            if (data instanceof VoteNewProjectVO) {
                if (((VoteNewProjectVO) data).getId().equals(id)) {
                    ((VoteNewProjectVO) data).setChecked(isCheck);
                    baseAdapter.changeData(i, data);
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.fab_vote) {
            if (voteMap.size() < mNewVoteBeanVO.getMin_number()) {
                showTost("未达最小投票数" + mNewVoteBeanVO.getMin_number() + "票", 0);
                return;
            }
            if (voteMap.size() > mNewVoteBeanVO.getMax_number()) {
                showTost("已达最大投票数" + mNewVoteBeanVO.getMax_number() + "票", 0);
                return;
            }
            if (voteMap.size() == 0) {
                showTost("请选择投票...", 0);
                return;
            }
            StringBuffer sb = new StringBuffer();
            sb.append(mNewVoteBeanVO.getId()).append("@");
            for (String id : voteMap.values()) {
                sb.append(id).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            ((CommVoteDetailNewActivity) getActivity()).onVoteNextEvent(new VoteNextEvent(sb.toString(), mposition));
//                if(Utils.isEmpty(PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(getActivity())==null) {
//                    showUnLoginSnackbar();
//                    return;
//                }
//                if(voteVO.getIsopen().equals("1")){//未结束
//                    if(voteVO.getSurplus_number()>0){
//                        int voteSize =voteMap.size();
//                        if(voteSize==0){
//                            showTost("请先选择投票项...",0);
//                        }else if(voteSize>voteVO.getSurplus_number()){
//                            showTost("已超过最大投票额...",0);
//                        }else{
//                            doVote();
//                        }
//                    }else{
//                        showTost("投票次数已用完...",0);
//                    }
//                }else{
//                    showTost("投票已结束...",0);
//                }

        }
    }

    class OnVoteCheckChangeListener implements CompoundButton.OnCheckedChangeListener {
        /**
         * 投票项id
         **/
        private String id;

        public OnVoteCheckChangeListener(String id) {
            this.id = id;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (Utils.isEmpty(PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN)) || MyApplication.getUserInfo(getActivity()) == null) {
                showUnLoginSnackbar();
                buttonView.setChecked(false);
                return;
            }
            if (isChecked) {
                if (voteMap.size() < mNewVoteBeanVO.getMax_number()) {
                    voteMap.put(id, id);
                } else {
//
                    if (mNewVoteBeanVO.getMax_number() > 1) {
                        buttonView.setChecked(false);
                        showTost("已达最大投票数" + mNewVoteBeanVO.getMax_number() + "票", 0);
                    } else {
                        selectUpdateCancleFirst(id);
                    }

                }
            } else {
                voteMap.remove(id);
            }
        }
    }

    @Override
    public void onDestroyView() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroyView();
    }

    /**
     * 已达最大投票数  移除第一个  选择
     *
     * @param id
     */
    public void selectUpdateCancleFirst(String id) {
//        if (mNewVoteBeanVO.getMax_number()>1) {
//            showTost("已达最大投票数"+mNewVoteBeanVO.getMax_number()+"票", 0);
//            return;
//        }

        BaseAdapter baseAdapter = (BaseAdapter) (mRecyclerView.getAdapter());
        ArrayList<E> datas = (ArrayList<E>) baseAdapter.getData();
        for (int i = 0; i < datas.size(); i++) {
            E data = datas.get(i);
            if (data instanceof VoteNewProjectVO) {
                String voteid = ((VoteNewProjectVO) data).getId();
                if (voteMap.containsKey(voteid)) {
                    voteMap.remove(voteid);
                    voteMap.put(id, id);
                    baseAdapter.changeData(i, data);
                    break;
                }
            }
        }

        for (int i = 0; i < datas.size(); i++) {
            E data = datas.get(i);
            if (data instanceof VoteNewProjectVO) {
                String voteid = ((VoteNewProjectVO) data).getId();
                if (voteid.equalsIgnoreCase(id)) {
                    baseAdapter.changeData(i, data);
                    break;
                }
            }
        }

    }
}
