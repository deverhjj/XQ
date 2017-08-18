package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommActivity;
import com.biu.modulebase.binfenjiari.activity.CommQADetailActivity;
import com.biu.modulebase.binfenjiari.activity.PersonalInfoActivity;
import com.biu.modulebase.binfenjiari.activity.ReportActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.QuestionsVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/4/18.
 */
public class CommQAFragment extends BaseFragment {
    private static final String TAG = "CommQAFragment";

    private RecyclerView mRecyclerView;
    private LSwipeRefreshLayout mRefreshLayout;

    private long time;

    private int pageNum =1;
    private int allPageNumber;

    public static boolean refresh =true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container,false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.LogE(TAG,"onRefresh******************");
                loadData();
            }

            @Override
            public void onLoadMore() {
                LogUtil.LogE(TAG,"onLoadMore******************");
                pageNum++;
                getQuestionList( Constant.LIST_LOAD_MORE);
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        BaseAdapter adapter=new BaseAdapter(getActivity()) {

            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.item_qa, parent, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                final QuestionsVO bean = (QuestionsVO) data;
                                holder.setText(R.id.tv_nickname,bean.getUsername());
                                holder.setText(R.id.tv_date, Utils.getReleaseTime(new Date(Utils.isLong(bean.getCreate_time())*1000)));
                                ImageView header = holder.getView(R.id.iv_head_portrait);
                                header.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent =new Intent(getActivity(),PersonalInfoActivity.class);
                                        intent.putExtra(Constant.KEY_ID,bean.getUser_id());
                                        startActivity(intent);
                                    }
                                });
                                holder.setNetImage(Constant.IMG_SOURCE,R.id.iv_head_portrait,bean.getUser_pic(), ImageDisplayUtil.DISPLAY_HEADER);
                                holder.setText(R.id.tv_title,bean.getTitle());
                                holder.setText(R.id.tv_content,bean.getContent());
                                LinearLayout img_layout =holder.getView(R.id.img_layout);
                                String pic=bean.getPic();
                                if(!Utils.isEmpty(pic)){
                                    String imgs[] = pic.split(",");
                                    if(imgs!=null && imgs.length>0){
                                        img_layout.setVisibility(View.VISIBLE);
                                        ImageView img1 =holder.getView(R.id.img1);
                                        ImageView img2 =holder.getView(R.id.img2);
                                        ImageView img3 =holder.getView(R.id.img3);
                                        int width =(Utils.getScreenWidth(getActivity())-getResources().getDimensionPixelSize(R.dimen.view_margin_24dp))/3;
                                        ViewGroup.LayoutParams layoutParams =img1.getLayoutParams();
                                        layoutParams.height = width;
                                        layoutParams.width = width;
                                        img1.setLayoutParams(layoutParams);
                                        ViewGroup.LayoutParams layoutParams2 =img2.getLayoutParams();
                                        layoutParams2.height = width;
                                        layoutParams2.width = width;
                                        img2.setLayoutParams(layoutParams2);
                                        ViewGroup.LayoutParams layoutParams3 =img3.getLayoutParams();
                                        layoutParams3.height = width;
                                        layoutParams3.width = width;
                                        img3.setLayoutParams(layoutParams3);
                                        switch (imgs.length){
                                            case 1:
                                                ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL,imgs[0], img1,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                                holder.getView(R.id.img1).setVisibility(View.VISIBLE);
                                                holder.getView(R.id.img2).setVisibility(View.INVISIBLE);
                                                holder.getView(R.id.img3).setVisibility(View.INVISIBLE);
                                                break;
                                            case 2:
                                                ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL,imgs[0], img1,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                                ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL,imgs[1], img2,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                                holder.getView(R.id.img1).setVisibility(View.VISIBLE);
                                                holder.getView(R.id.img2).setVisibility(View.VISIBLE);
                                                holder.getView(R.id.img3).setVisibility(View.INVISIBLE);
                                                break;
                                            default:
                                                ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL,imgs[0], img1,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                                ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL,imgs[1], img2,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                                ImageDisplayUtil.displayImage(Constant.IMG_THUMBNAIL,imgs[2], img3,ImageDisplayUtil.DISPLAY_COMMON_IMAGE);
                                                holder.getView(R.id.img1).setVisibility(View.VISIBLE);
                                                holder.getView(R.id.img2).setVisibility(View.VISIBLE);
                                                holder.getView(R.id.img3).setVisibility(View.VISIBLE);
                                                break;
                                        }
                                    }
                                }else{
                                    img_layout.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, final int position) {
                                final QuestionsVO bean = (QuestionsVO) getData().get(position);
                                int i = view.getId();
                                if (i == R.id.ib_more) {
                                    OtherUtil.showMoreOperate(CommQAFragment.this, bean.getId(), bean.getTitle(), bean.getContent(),
                                            bean.getUser_id(), Constant.SHARE_QUESTION,
                                            Constant.REPORT_QUESTION, Constant.DELETE_QUESTION,
                                            true, (BaseAdapter) mRecyclerView.getAdapter(),
                                            position, null);

                                } else {
                                    Intent intent = new Intent(getActivity(), CommQADetailActivity.class);
                                    intent.putExtra(Constant.KEY_ID, bean.getId());
                                    startActivity(intent);

                                }

                            }
                        });
                holder.setItemChildViewClickListener(R.id.ib_more);
                return holder;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0,
                        getContext().getResources().getDimensionPixelSize(R.dimen.item_divider_height_8dp));
            }

        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        mRefreshLayout.startLoad();
                    }
                }
            }
        });
    }

    @Override
    public void loadData() {
        refresh =false;
        time = new Date().getTime()/1000;
        pageNum =1;
        getQuestionList(Constant.LIST_REFRESH);
    }

    private void getQuestionList(final int tag){
        JSONObject params = new JSONObject();
        try {
            params.put("model",Constant.MODEL_QUESTION);
            params.put("action",Constant.ACTION_QUESTION_HOME);
            params.put("time",time+"");
            params.put("pageNum",pageNum+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result =new JSONObject(jsonString);
                    time = JSONUtil.getLong(result,"time");
                    allPageNumber =JSONUtil.getInt(result,"allPageNumber");
                    JSONArray array =JSONUtil.getJSONArray(result,"questionsList");
                    ArrayList<QuestionsVO> list =JSONUtil.fromJson(array.toString(),new TypeToken<List<QuestionsVO>>(){}.getType());
                    refreshList(tag, list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                if(key==3){
                    refreshList(tag, new ArrayList<QuestionsVO>());
                }else{
                    showTost(message,1);
                }
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    private void refreshList(int tag,ArrayList<QuestionsVO> datas){
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        switch (tag){
            case Constant.LIST_REFRESH:
                if(datas.size()==0) {
                    visibleNoData();
                    return;
                }
                adapter.setData(datas);
                mRefreshLayout.setRefreshing(false);
                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(datas);
                mRefreshLayout.setLoading(false);
                break;
        }
        if(pageNum<allPageNumber){
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.BOTH);
        }else{
            mRefreshLayout.setMode(LSwipeRefreshLayout.Mode.PULL_FROM_START);
        }
    }

    private void showShare(final String id ){
        DialogFactory.showDialog(getActivity(), R.layout.dialog_share, R.style.CustomDialog,
                R.style.popwin_anim_style, Gravity.BOTTOM, 1, 0, new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        v.findViewById(R.id.btn_cancel).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.report).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(getActivity(), ReportActivity.class);
                                intent.putExtra("id",id);
                                intent.putExtra("type",Constant.REPORT_NEWS);
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case CommActivity.SEND_QUESTION:
                    loadData();
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        Communications.cancelRequest(getClass().getSimpleName());
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        if(refresh){
            loadData();
        }
        super.onResume();
    }

}
