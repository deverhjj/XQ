package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommActivity;
import com.biu.modulebase.binfenjiari.activity.WebViewActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.common.adapter.BaseViewHolder;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.uploadservice.UploadServiceBroadcastReceiver;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.util.ImageUtils;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.cameraroll.SelectImgAdapter;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhj_Plus on 2016/3/18.
 */
public class CardPostFragment extends BaseFragment {
    private static final String TAG = "CardPostFragment";
    private EditText mTitle;
    private EditText mContent;
    private RecyclerView mRecyclerView;
    private Uri mPhotoUri;
    private List<String> mPaths=new ArrayList<>();
    private String mCircleId="";
    private int tag =-1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args=getArguments();
        if (args!=null) {
            mCircleId=args.getString(CircleFragment.EXTRA_CIRCLE_ID);
        }
    }

    public static CardPostFragment newInstance(String circleId) {
        Bundle args = new Bundle();
        args.putString(CircleFragment.EXTRA_CIRCLE_ID, circleId);
        CardPostFragment postFragment = new CardPostFragment();
        postFragment.setArguments(args);
        return postFragment;
    }


    @Override
    protected void getIntentData() {
        tag =getActivity().getIntent().getIntExtra("tag",-1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card_post, container, false);
        return super.onCreateView(inflater,rootView,savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        mTitle = (EditText) rootView.findViewById(R.id.title);
        mContent = (EditText) rootView.findViewById(R.id.content);
        if(tag== CommActivity.SEND_POST){
            getBaseActivity().setToolBarTitle("发布新鲜事");
            mTitle.setVisibility(View.GONE);
        }else if(tag== CommActivity.SEND_QUESTION){
            getBaseActivity().setToolBarTitle("发布问答");
            mTitle.setVisibility(View.VISIBLE);
        }else if(tag==CommActivity.SEND_CARD){
            getBaseActivity().setToolBarTitle("发布帖子");
            mTitle.setVisibility(View.VISIBLE);
            mContent.setMaxEms(1000);
            mContent.setHint("请勿发布违规的内容，否则会被删除或禁言的喔~（1000个字符以内）");
        }
        rootView.findViewById(R.id.rule).setOnClickListener(this);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        final BaseAdapter adapter = new BaseAdapter(getActivity()) {
            @Override
            public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder=new BaseViewHolder(getActivity().getLayoutInflater()
                        .inflate(R.layout.part_image_add, mRecyclerView, false),
                        new BaseViewHolder.Callbacks2() {
                            @Override
                            public void bind(BaseViewHolder holder, Object data) {
                                Bitmap bitmap= (Bitmap) data;
                                ((ImageView)holder.getView(R.id.iv_image)).setImageBitmap(bitmap);
                                holder.getView(R.id.ib_delete).setVisibility(holder
                                        .getAdapterPosition()==getItemCount()-1?View.GONE:View.VISIBLE);
                            }

                            @Override
                            public void onItemClick(BaseViewHolder holder, View view, int position)
                            {
                                int btnId = view.getId();
                                if (btnId == R.id.ib_delete) {

                                    removeData(position);
                                    mPaths.remove(position);
                                    SelectImgAdapter.mSelectedImage.remove(position);

                                } else if (btnId == R.id.iv_image) {
                                    if (holder.getAdapterPosition()==getItemCount()-1) {
                                        OtherUtil.showPhotoPop(CardPostFragment.this, false, new
                                                OtherUtil
                                                .onTakePictureFinishListener() {
                                            @Override
                                            public void onTakePictureFinished(Uri photoUri) {
                                                mPhotoUri=photoUri;
                                            }
                                        });
                                    }
                                }
                            }
                        });
                holder.setItemChildViewClickListener(R.id.iv_image, R.id.ib_delete);
                return holder;
            }

            @Override
            protected void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                    RecyclerView.State state)
            {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                outRect.set(getActivity().getResources().getDimensionPixelSize(R.dimen.item_divider_5dp), 0,
                        childAdapterPosition == parent.getAdapter().getItemCount() - 1 ? getActivity()
                                .getResources().getDimensionPixelSize(R.dimen.item_divider_5dp) : 0, 0);
            }

        };


        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecoration());
    }

    @Override
    public void loadData() {
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        adapter.addData(BaseAdapter.AddType.LASE,Collections.singletonList(BitmapFactory.decodeResource(getResources(),R
                .mipmap
                .ic_add_image)));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.CAPTURE_PHOTO:// 拍照
                    Cursor c = getActivity().getContentResolver().query(mPhotoUri, null, null,
                            null, null);
                    if (c!=null) {
                        c.moveToFirst();
                        String imgPath = c.getString(1);
                        LogUtil.LogE(TAG,"path="+imgPath);
                        SelectImgAdapter.mSelectedImage.add(imgPath);
                        addImage(SelectImgAdapter.mSelectedImage);
                        c.close();
                    }
                    break;
                case Constant.CHOICE_PHOTO:// 从相册选择照片
                    // 多选
                    ArrayList<String> list = data.getStringArrayListExtra("imgPaths");
                    addImage(list);
                    break;

            }
        }
    }

    private void addImage(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        BaseAdapter adapter = (BaseAdapter) mRecyclerView.getAdapter();
        adapter.removeData(0,adapter.getItemCount()-1);
        mPaths.clear();
        mPaths.addAll(list);
        List<Bitmap> images = new ArrayList<>();
        for(int j =0;j<mPaths.size();j++){
            Bitmap image = ImageUtils.generateThumbnail(mPaths.get(j), 8);
            images.add(image);
        }
        adapter.addData(adapter.getItemCount() - 1, images);
    }

    private class HandleImageTask extends AsyncTask<List<String>, Void, List<String>> {

        @Override
        protected List<String> doInBackground(List<String>... params) {
            try {
                return ImageUtils.compressToFiles(params[0], 480, 800);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (strings != null) {
                uploadData(strings);
            }else{
                showTost("压缩图片失败...",1);
                dismissProgress();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.post, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_post) {

            String title = mTitle.getText().toString();
            String content = mContent.getText().toString();
            if(tag== CommActivity.SEND_POST){
                if(TextUtils.isEmpty(content)){
                    showTost("内容不能为空",1);
                    return true;
                }
            }else{
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    OtherUtil.showToast(getActivity(), TextUtils.isEmpty(title) ? "标题不能为空" : "内容不能为空");
                    return true;
                }
                if(title.length()<2||title.length()>10){
                    showTost("标题在2-10个字符",0);
                    return true;
                }
                if(tag==CommActivity.SEND_CARD){
                    if(content.length()>1000){
                        showTost("内容在1000个字符以内",0);
                        return true;
                    }
                }else{
                    if(content.length()>140){
                        showTost("内容在140个字符以内",0);
                        return true;
                    }
                }

            }
            if (mPaths != null && mPaths.size() > 0) {
                showProgress(getClass().getSimpleName());
                new HandleImageTask().execute(mPaths);
            } else {
                uploadData(null);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 上传帖子
     */
    private void uploadData(List<String> paths) {
//        showProgress(TAG);
        String title = mTitle.getText().toString();
        String content = mContent.getText().toString();
        String token = PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN);
        if(Utils.isEmpty(token)){
            dismissProgress();
            showUnLoginSnackbar();
            return;
        }
        JSONObject jsonObjec =new JSONObject();
        JSONUtil.put(jsonObjec,"token",token);
        if(tag== CommActivity.SEND_QUESTION){//发布问答
            JSONUtil.put(jsonObjec,"model",Constant.MODEL_QUESTION);
            JSONUtil.put(jsonObjec,"action",Constant.ACTION_QUESTION_PUBLISH);
            JSONUtil.put(jsonObjec,"title",title);
            JSONUtil.put(jsonObjec,"content",content);
        }else if(tag==CommActivity.SEND_POST){//发布新鲜事
            JSONUtil.put(jsonObjec,"model",Constant.MODEL_NEWS);
            JSONUtil.put(jsonObjec,"action",Constant.ACTION_NEWS_PUBLISH);
            JSONUtil.put(jsonObjec,"content",content);
        }else if(tag ==CommActivity.SEND_CARD){//发布帖子
            JSONUtil.put(jsonObjec,"model",Constant.MODEL_POST);
            JSONUtil.put(jsonObjec,"action",Constant.ACTION_POST_PUBLISH);
            JSONUtil.put(jsonObjec,"title",title);
            JSONUtil.put(jsonObjec,"content",content);
            JSONUtil.put(jsonObjec,"circle_id",mCircleId);
        }
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.KEY_UPLOAD_DATA,jsonObjec.toString());
        Communications.uploadMultipart(getActivity(), Constant.UPLOAD_URL, params, "imgs", paths);

    }

    private final UploadServiceBroadcastReceiver uploadReceiver = new UploadServiceBroadcastReceiver() {

        @Override
        public void onProgress(String uploadId, int progress) {
            LogUtil.LogI(TAG,
                    "The progress of the upload with ID " + uploadId + " is: " + progress);
        }

        @Override
        public void onError(String uploadId, Exception exception) {
            dismissProgress();
            LogUtil.LogE(TAG, "Error in upload with ID: " + uploadId + ". " + exception.getLocalizedMessage());
        }

        @Override
        public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
            dismissProgress();
            LogUtil.LogI(TAG, "Upload with ID " + uploadId + " is completed: " + serverResponseCode
                    + ", "
                    + serverResponseMessage);
            try {
                JSONObject response = new JSONObject(serverResponseMessage);
                String key = JSONUtil.getString(response, "key");
                if (key.equals("1")) {// 成功
                    Activity activity=getActivity();
                    if (activity!=null) {
                        OtherUtil.showToast(activity,"发布成功");
                        Intent intent = new Intent();
                        activity.setResult(Activity.RESULT_OK,intent);
                        if(tag== CommActivity.SEND_QUESTION){
                            CommQAFragment.refresh =true;
                        }else if(tag== CommActivity.SEND_POST){//发布新鲜事
                            CommFreshThingsFragment.refresh =true;
                        }else if(tag ==CommActivity.SEND_CARD){//发帖成功
//                            CardAllFragment.refresh =true; 这里使用onActivityR处理回调
                        }
                        activity.finish();
                    }

                }else if (key.equals("1024")||key.equals("1025")){
                    showUnLoginSnackbar();
                }else if(key.equals("24")){
                    OtherUtil.showToast(getActivity(),"不是该圈子成员，不能发帖...");
                }else{
                    OtherUtil.showToast(getActivity(),"发布失败，请重试...");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.rule) {
            Intent policyIntent = new Intent(getActivity(), WebViewActivity.class);
            policyIntent.putExtra("loadType", WebViewFragment.LOAD_HTML_URL);
            policyIntent.putExtra("title", "发帖必读");
            policyIntent.putExtra(Constant.KEY_TYPE, Constant.LOAD_POST_READ_HTML);
            startActivity(policyIntent);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Communications.cancelRequest(TAG);
        SelectImgAdapter.mSelectedImage.clear();
        uploadReceiver.unregister(getActivity());
    }
}
