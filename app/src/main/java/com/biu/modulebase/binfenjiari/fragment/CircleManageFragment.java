package com.biu.modulebase.binfenjiari.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CircleMemeberActivity;
import com.biu.modulebase.binfenjiari.activity.NoticeListActivity;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.communication.uploadservice.UploadServiceBroadcastReceiver;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.CircleMemeberVO;
import com.biu.modulebase.binfenjiari.model.CircleVO;
import com.biu.modulebase.binfenjiari.util.ImageUtils;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: {圈子管理}
 * @Description:{}
 * @date 2016/4/13
 */
public class CircleManageFragment extends BaseFragment {

    private static final String TAG = "CircleManageFragment";
    public static final String KEY_CIRCLE_ID="circle_id";

    private static boolean upload_head_success;

    private static boolean upload_info_success;

    private TextView hint;
    private ImageView iv_head_portrait;

    private EditText et_name;
    private EditText et_subject;
    private LinearLayout layout_people_manage;
    private LinearLayout memeber_layout;

    private String header_path="";

    private String mCircleId ;
    private CircleVO circleVO;
    private Uri mPhotoUri;

    private String originalSubject;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args=getArguments();
        mCircleId= args!=null?args.getString(KEY_CIRCLE_ID) :null;
    }

    public static CircleManageFragment newInstance(String circleId) {
        Bundle args = new Bundle();
        args.putString(KEY_CIRCLE_ID, circleId);
        CircleManageFragment manageFragment = new CircleManageFragment();
        manageFragment.setArguments(args);
        return manageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_circle_manage, container, false);
        return super.onCreateView(inflater,rootView,savedInstanceState);
    }

    protected void initView(View rootView) {
        visibleLoading();
        ViewGroup layout_manage = (ViewGroup) rootView.findViewById(R.id.layout_notice_manage);
        layout_manage.setOnClickListener(this);
        hint =(TextView)rootView.findViewById(R.id.hint);
        et_name = (EditText) rootView.findViewById(R.id.et_name);
        et_subject = (EditText) rootView.findViewById(R.id.et_subject);
        iv_head_portrait = (ImageView) rootView.findViewById(R.id.iv_head_portrait);
        iv_head_portrait.setOnClickListener(this);
        layout_people_manage =(LinearLayout) rootView.findViewById(R.id.layout_people_manage);
        layout_people_manage.setOnClickListener(this);
        memeber_layout =(LinearLayout)rootView.findViewById(R.id.memeber_layout);

    }

    @Override
    public void loadData() {
            getCircleManageInfo();
    }

    private void getCircleManageInfo(){
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"circle_id", mCircleId);
        JSONUtil.put(params,"model", Constant.MODEL_CIRCLE_MANAGE);
        JSONUtil.put(params,"action",Constant.ACTION_GET_MY_CIRCLE_DETAIL);
        jsonRequest(false, params, Constant.SERVERURL, TAG, new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                circleVO =JSONUtil.fromJson(jsonString,CircleVO.class);
                setViewData();
                inVisibleLoading();

            }

            @Override
            public void onCodeError(int key, String message) {
                visibleNoData();
            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();
            }
        });
    }

    private void setViewData(){
        String isEdit = circleVO.getIsEdit();
        getActivity().invalidateOptionsMenu();
        switch (Utils.isInteger(isEdit)){
            case 1:
                setEnable(true);
                hint.setText("创建圈子审核失败，请重新提交审核");
                break;
            case 2:
                setEnable(true);
                hint.setText("信息一经修改，三天内进行审核");
                break;
            case 3:
                setEnable(false);
                hint.setText("创建圈子审核中,自提交日起三天内完成...");
                break;
            case 4:
                setEnable(false);
                hint.setText("信息修改审核中，自提交日起三天内完成...");
                break;
        }

        et_name.setText(Utils.getString(circleVO.getName()));
        et_subject.setText(Utils.getString(circleVO.getIntro_content()));
        originalSubject=et_subject.getText().toString();
        ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS,circleVO.getIntro_img(),iv_head_portrait,ImageDisplayUtil.DISPLAY_ROUND_IMAGE);

        //成员
        List <CircleMemeberVO>list =circleVO.getUser_list();
        int length =list.size();
        if(length==0){
            memeber_layout.setVisibility(View.GONE);
        }else{
            memeber_layout.setVisibility(View.VISIBLE);
            memeber_layout.removeAllViews();
            for(int i=0;i<length;i++){
                CircleMemeberVO bean =list.get(i);
                View view = View.inflate(getActivity(),R.layout.item_circle_memeber,null);
                ImageView imgView = (ImageView) view.findViewById(R.id.img);
                ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS,bean.getUser_pic(),imgView,ImageDisplayUtil.DISPLAY_HEADER);
                memeber_layout.addView(view);
            }
        }

    }

    private boolean checkEmpty(String name,String subject){
        if (TextUtils.isEmpty(name)|| TextUtils.isEmpty(subject)) {
            OtherUtil.showToast(getActivity(),TextUtils.isEmpty(name)?"名称不能为空":"简介不能为空");
            return false;
        }
        return true;
    }

    private void uploadData(){
        String name=et_name.getText().toString();
        String subject=et_subject.getText().toString();
        if(!checkEmpty(name,subject)){
            return;
        }
        showProgress(TAG);
        List<String> paths =new ArrayList<>();
        paths.add(header_path);
        String token = PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN);
        JSONObject jsonObjec =new JSONObject();
        JSONUtil.put(jsonObjec,"id",mCircleId);
        JSONUtil.put(jsonObjec,"token",token);
        JSONUtil.put(jsonObjec,"model",Constant.MODEL_CIRCLE_MANAGE);
        JSONUtil.put(jsonObjec,"action",Constant.ACTION_UPDATE_CIRCLE);
        if(!name.equals(circleVO.getName())){
            JSONUtil.put(jsonObjec,"name",name);
        }else{
            JSONUtil.put(jsonObjec,"name",circleVO.getName());
        }
        if(!subject.equals(circleVO.getIntro_content())){
            JSONUtil.put(jsonObjec,"intro_content",subject);
        }else{
            JSONUtil.put(jsonObjec,"intro_content",circleVO.getIntro_content());
        }
        if(Utils.isEmpty(header_path)){
            JSONUtil.put(jsonObjec,"intro_img",circleVO.getIntro_img());
        }
        JSONUtil.put(jsonObjec,"type_id",circleVO.getType_id());
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.KEY_UPLOAD_DATA,jsonObjec.toString());
        LogUtil.LogE(TAG, "params: " + jsonObjec.toString());
        Communications.uploadMultipart(getActivity(), Constant.UPLOAD_URL, params, "imgs", paths);

    }

    private final UploadServiceBroadcastReceiver uploadReceiver = new UploadServiceBroadcastReceiver() {

        @Override
        public void onProgress(String uploadId, int progress) {
            LogUtil.LogI(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);
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
                String message = JSONUtil.getString(response, "message");
                if (key.equals("1")) {// 成功
                    showTost("修改成功,请等待审核...",1);
                    hint.setText("信息修改审核中，自提交日起三天内完成...");
                    setEnable(false);
                    circleVO.setIsEdit("4");
                    getActivity().invalidateOptionsMenu();
                } else {
                    showTost(message,1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private void setEnable(boolean enable){
        et_name.setEnabled(enable);
        et_subject.setEnabled(enable);
        iv_head_portrait.setEnabled(enable);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(circleVO!=null){
            if(circleVO.getIsEdit().equals("3")|| circleVO.getIsEdit().equals("4")){
                menu.findItem(R.id.action_save).setVisible(false);
            }else{

            }

        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_save) {
            uploadData();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_head_portrait) {
            OtherUtil.showPhotoPop(CircleManageFragment.this, true, new OtherUtil
                    .onTakePictureFinishListener() {
                @Override
                public void onTakePictureFinished(Uri photoUri) {
                    mPhotoUri = photoUri;
                }
            });

        } else if (i == R.id.layout_people_manage) {
            Intent intentCircleMemeber = new Intent(getActivity(), CircleMemeberActivity.class);
            intentCircleMemeber.putExtra("circle_id", circleVO.getId());
            startActivity(intentCircleMemeber);

        } else if (i == R.id.layout_notice_manage) {
            Intent intent = new Intent(getActivity(), NoticeListActivity.class);
            String circleId = circleVO.getId();
            intent.putExtra(CircleFragment.EXTRA_CIRCLE_ID, circleId);
            startActivity(intent);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Cursor cursor0;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.CAPTURE_PHOTO:
                    cursor0 = getActivity().getContentResolver().query(mPhotoUri, null, null, null,
                            null);
                    if (cursor0 != null) {
                        cursor0.moveToFirst();
                        header_path = cursor0.getString(1);
                        LogUtil.LogE(TAG, "photoUri2------>" + mPhotoUri.toString());
                        LogUtil.LogE(TAG, "header_path------>" + header_path);
                        cursor0.close();
                        ImageUtils.cropImg(this, mPhotoUri, 200, 200);
                    } else {
                        Toast.makeText(getActivity(), "请选择本地资源!", Toast.LENGTH_LONG).show();
                        try {
                            throw new FileNotFoundException("该图片不是本地图片");
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return;
                    }
                    break;
                case Constant.CHOICE_PHOTO:
                    ArrayList<String> list = data.getStringArrayListExtra("imgPaths");
                    header_path = list.get(0);
                    mPhotoUri = Uri.parse("file://" + header_path);
                    LogUtil.LogE(TAG, "photoUri------>" + mPhotoUri.toString());
                    ImageUtils.cropImg(this, mPhotoUri, 200, 200);
                    break;
                case Constant.CROP_IMG:
                    // 拿到剪切数据
//                    Bitmap bm = data.getParcelableExtra("data");
//                    LogUtil.LogE(TAG,"************bm*********----------------->");
//                    iv_head_portrait.setImageBitmap(bm);
//
//                    headString = ImageUtils.bitmapToBase64String2(bm);
//                    mHeadChanged=true;
                    Bitmap bitmap =null;
                    if (data.getExtras()!=null) {
                        bitmap = data.getParcelableExtra("data");
                    }else {//nexus 6.0手机裁剪后intnet里的是图片路径Uri
                        Uri uri = data.getData();
                        try {
                            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    iv_head_portrait.setImageBitmap(bitmap);
                    try {
                        header_path =ImageUtils.bitmapToFile(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getActivity());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uploadReceiver.unregister(getActivity());
        Communications.cancelRequest(TAG);
    }

}
