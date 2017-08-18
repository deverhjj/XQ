package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.WebViewVoteDetailActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.ProjectVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.swiperefreshlayout.LSwipeRefreshLayout;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{描述}
 * @date ${2016/9/13}
 */
public class VoteRankMoreFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    Long phoneTime;
    Long severTime;
    int pageNum=1;
    String id;
    int allPageNum;
    int type;
    int all_Num;
    String project_id;
    String title2;
    private LSwipeRefreshLayout mRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recyclerview_swiperefresh,
                container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        title2=getActivity().getIntent().getStringExtra("project_title");
        visibleLoading();
        id=getActivity().getIntent().getStringExtra("id");
        project_id=getActivity().getIntent().getStringExtra("project_id");

        getBaseActivity().setBackNavigationIcon();
        mRecyclerView= (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRefreshLayout= (LSwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        mRefreshLayout.setSwipeRefreshListener(new LSwipeRefreshLayout.SwipeRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum=1;
                getMainList(Constant.LIST_REFRESH);
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                if(pageNum>allPageNum){
                    showTost("没有更多数据了...",1);
                    mRefreshLayout.setLoading(false);
                    return;
                }else {
                    getMainList(Constant.LIST_LOAD_MORE);
                }

            }
        });
        BaseAdapter adapter=new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder viewHolder;
                if(type==1){
                    viewHolder=new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_organiztion, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {
                            final ProjectVO vo= (ProjectVO) data;
                            ImageView img=holder.getView(R.id.img);
                            if(holder.getAdapterPosition()==1){
                                img.setVisibility(View.VISIBLE);
                                img.setImageResource(R.mipmap.vote_organization_number2);

                            }else if(holder.getAdapterPosition()==2){
                                img.setVisibility(View.VISIBLE);

                                img.setImageResource(R.mipmap.vote_organization_number3);
                            }else if(holder.getAdapterPosition()==0){
                                img.setVisibility(View.VISIBLE);
                                img.setImageResource(R.mipmap.vote_organization_number1);
                            }else {
                                img.setVisibility(View.INVISIBLE);
                            }

                            TextView projectname=holder.getView(R.id.project_name);
                            projectname.setText(vo.getTitle());//vo.getCreate_number()+"  "+
                            projectname.setMaxLines(2);
                            ProgressBar progressBar=holder.getView(R.id.progress);
                            progressBar.setMax(all_Num);
                            progressBar.setProgress(Utils.isInteger(vo.getNumber()));
                            holder.setText(R.id.num,vo.getNumber());
                            LinearLayout rl=holder.getView(R.id.rl);
                            rl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                    intent2.putExtra("id",vo.getId());
                                    intent2.putExtra("project_id",project_id);
                                    intent2.putExtra("title1",getActivity().getIntent().getStringExtra("title"));
                                    intent2.putExtra("project_title",title2);
                                    intent2.putExtra("content",vo.getTitle());

                                    intent2.putExtra("title",vo.getSmall_title());
                                    Log.e("title1",vo.getSmall_title());
                                    intent2.putExtra("type1", 1);
                                    intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                    startActivity(intent2);
                                }
                            });

                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {


                        }
                    });


                }else {
                    viewHolder=new BaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_small_man_twostyle, parent, false), new BaseViewHolder.Callbacks2() {
                        @Override
                        public void bind(BaseViewHolder holder, Object data) {

                            final ProjectVO vo= (ProjectVO) data;
                            RelativeLayout rl=holder.getView(R.id.rl);
                            rl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Intent intent2 = new Intent(getActivity(), WebViewVoteDetailActivity.class);
                                    intent2.putExtra("id",vo.getId());
                                    intent2.putExtra("project_id",project_id);
                                    intent2.putExtra("project_title",title2);
                                    intent2.putExtra("title1",getActivity().getIntent().getStringExtra("title"));
                                    intent2.putExtra("title",vo.getSmall_title());
                                    intent2.putExtra("content",vo.getTitle());
                                    Log.e("title1",vo.getSmall_title());
                                    intent2.putExtra("type1", 1);
                                    intent2.putExtra("type", WebViewVoteDetailFragment.LOAD_TYPE_APP_FINDVOTEPROJECTINFO);
                                    startActivity(intent2);
                                }
                            });

                            TextView tv_small_title = holder.getView(R.id.tv_small_title);
                            tv_small_title.setText(vo.getSmall_title());//vo.getCreate_number()+" "+
                            Utils.setSexIconState(getContext(),tv_small_title,vo.getSex());

                            ImageView img2=holder.getView(R.id.img2);
                            ImageView img3=holder.getView(R.id.img3);
                            img2.setVisibility(View.GONE);
                            img3.setVisibility(View.GONE);
                            if(holder.getAdapterPosition()==0){
//                                img2.setVisibility(View.VISIBLE);
//                                img3.setVisibility(View.VISIBLE);
//                                img3.setImageResource(R.mipmap.img111);
                            }else if(holder.getAdapterPosition()==1){
//                                img3.setImageResource(R.mipmap.vote_volunteers_number2);
//                                img2.setVisibility(View.VISIBLE);
//                                img3.setVisibility(View.VISIBLE);
                            }else if(holder.getAdapterPosition()==2){
//                                img2.setVisibility(View.VISIBLE);
//                                img3.setVisibility(View.VISIBLE);
//                                img3.setImageResource(R.mipmap.vote_volunteers_number3);
                            }else {
//                                img2.setVisibility(View.GONE);
//                                img3.setVisibility(View.GONE);
                            }

                            TextView tv_number = holder.getView(R.id.tv_number);
                            Utils.setRankListState(tv_number,holder.getAdapterPosition());//vo.getCreate_number()

//                            holder.setNetImage(Constant.IMG_SOURCE,R.id.img1,vo.getPic(), ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                            ImageDisplayUtil.LoadCircleImg(getContext(), Constant.IMG_COMPRESS, vo.getPic(), (ImageView) holder.getView(R.id.img1));
                            //vo.getCreate_number()+"  "+

                            holder.setText(R.id.title,vo.getTitle());
                            holder.setText(R.id.num,vo.getNumber());
                            ProgressBar progressBar=holder.getView(R.id.progress);
                            progressBar.setMax(all_Num);
                            progressBar.setProgress(Utils.isInteger(vo.getNumber()));
                        }

                        @Override
                        public void onItemClick(BaseViewHolder holder, View view, int position) {

                        }
                    });
                }
                return viewHolder;
            }

//            @Override
//            public int getItemViewType(int position) {
//                return position;
//            }

        };
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,1,0,0);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void loadData() {
        getMainList(Constant.LIST_REFRESH);
    }

    private void getMainList(final int tag) {
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action","app_voteRankingList");
        phoneTime=new Date().getTime()/1000;
        if(tag==Constant.LIST_REFRESH){
            JSONUtil.put(params, "time", phoneTime);
        }else {
            JSONUtil.put(params, "time", severTime);
        }
        JSONUtil.put(params,"pageNum",pageNum);
        JSONUtil.put(params, "id", id);
        Log.e("id---->:",id);
        Log.e("time",phoneTime+"");
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                try {
                    JSONObject result=new JSONObject(jsonString);
                    type=JSONUtil.getInt(result,"type");
                    all_Num=JSONUtil.getInt(result,"all_number");
                    severTime=JSONUtil.getLong(result,"time");
                    allPageNum=JSONUtil.getInt(result,"allPageNumber");
                    JSONArray array=JSONUtil.getJSONArray(result,"projectList");
                    List<ProjectVO> list=JSONUtil.fromJson(array.toString(), new TypeToken<List<ProjectVO>>(){}.getType());
                    reFreshList(tag,list);

//                    adapter.removeAllData();
//                    adapter.setData(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onCodeError(int key, String message) {
                if(key==3){
                    visibleNoData();
                }else {
                    showTost(message,1);
                }

            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();

            }
        });

    }
    private void reFreshList(int tag, List<ProjectVO> list) {
        BaseAdapter adapter= (BaseAdapter) mRecyclerView.getAdapter();

        switch (tag){
            case Constant.LIST_REFRESH:
                adapter.setData(list);
                mRefreshLayout.stopRefresh();

                break;
            case Constant.LIST_LOAD_MORE:
                adapter.addItems(list);
                mRefreshLayout.setLoading(false);

                break;

        }

    }
}
