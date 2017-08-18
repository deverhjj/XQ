package com.biu.modulebase.binfenjiari.fragment;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.uploadservice.UploadServiceBroadcastReceiver;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.CircleTypeVO;
import com.biu.modulebase.binfenjiari.util.ImageUtils;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.CircleImageView;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.binfenjiari.widget.wheeltime.CityMain;
import com.biu.modulebase.binfenjiari.widget.wheeltime.EmotionMain;
import com.biu.modulebase.binfenjiari.widget.wheeltime.OnOkSelectorListener;
import com.biu.modulebase.binfenjiari.widget.wheeltime.WheelMain;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircleCreateFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "CircleCreateFragment";
    private static final String KEY_TYPES = "types";

    private String headString;
    /**
     * 拍照所得照片的 Uri
     **/
    private Uri mPhotoUri = null;
    /** 选择的头像路径 **/
    private String header_path;

    private String typeJsonString;
    private ArrayList<CircleTypeVO> typeList;
    private String[] types;

    private CircleImageView iv_head_portrait;

    private TextView tv_typeName;

    private EditText et_name;

    private EditText et_subject;

    private TextView extra_num;

    private String type_id="";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args=getArguments();
        if (args!=null) {
            typeJsonString=args.getString(KEY_TYPES);
            typeList = JSONUtil.fromJson(typeJsonString,new TypeToken<List<CircleTypeVO>>(){}.getType());
            int tabCount= typeList.size();
            type_id =typeList.get(0).getId();
            types=new String[tabCount];
            //添加新的 Tab
            for (int i = 0; i < tabCount; i++) {
                CircleTypeVO item= typeList.get(i);
                String tabName=item.getName();
                types[i]=tabName;
            }
        }
    }

    public static CircleCreateFragment newInstance(String typesJsonString){
        Bundle args=new Bundle();
        args.putString(KEY_TYPES,typesJsonString);
        CircleCreateFragment fragment=new CircleCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_circle_create, container, false);
        return super.onCreateView(inflater,rootView,savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {
        ViewGroup layout_select_head_portrait= (ViewGroup) rootView.findViewById(R.id
                .layout_select_head_portrait);
        layout_select_head_portrait.setOnClickListener(this);

        iv_head_portrait= (CircleImageView) rootView.findViewById(R.id.iv_head_portrait);
        Drawable drawable=iv_head_portrait.getDrawable();
//        headString= ImageUtils.drawableToBase64String(drawable);

        ViewGroup layout_type_choice= (ViewGroup) rootView.findViewById(R.id.layout_type_choice);
        layout_type_choice.setOnClickListener(this);

        tv_typeName= (TextView) rootView.findViewById(R.id.type_name);
        if (types!=null&&types.length>0) {
            tv_typeName.setText(types[0]);
        }

        et_name= (EditText) rootView.findViewById(R.id.et_name);

        et_subject= (EditText) rootView.findViewById(R.id.et_subject);
        extra_num =(TextView)rootView.findViewById(R.id.extra_num);
        et_subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                extra_num.setText((140-length)+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.circle_apply, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.circle_apply) {
            uploadData();

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkEmpty(String name,String subject){
        if (TextUtils.isEmpty(name)|| TextUtils.isEmpty(subject)) {
            OtherUtil.showToast(getActivity(),TextUtils.isEmpty(name)?"名称不能为空":"简介不能为空");
            return false;
        }
        if(Utils.isEmpty(type_id)) {
            showTost("请选择栏目", 0);
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
        List<String> paths =new ArrayList<>();
        paths.add(header_path);
        String token = PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN);
        JSONObject jsonObjec =new JSONObject();
        JSONUtil.put(jsonObjec,"token",token);
        JSONUtil.put(jsonObjec,"model",Constant.MODEL_CIRCLE);
        JSONUtil.put(jsonObjec,"action",Constant.ACTION_CREATE_CIRCLE);
        JSONUtil.put(jsonObjec,"name",name);
        JSONUtil.put(jsonObjec,"intro_content",subject);
        JSONUtil.put(jsonObjec,"type_id",type_id);
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
                if (key.equals("1")) {// 成功
                    OtherUtil.showToast(getActivity(), "申请成功,请耐心等待审核");
                            getActivity().finish();
                } else {
                    String message = JSONUtil.getString(response, "message");
                    showTost(message,1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.layout_select_head_portrait) {
            OtherUtil.showPhotoPop(CircleCreateFragment.this, true, new OtherUtil
                    .onTakePictureFinishListener() {
                @Override
                public void onTakePictureFinished(Uri photoUri) {
                    mPhotoUri = photoUri;
                }
            });

        } else if (i == R.id.layout_type_choice) {
            final String[] circleTypes = types;
            if (types != null) {
                DialogFactory.showEmotionAlert(getActivity(), Arrays.asList(circleTypes), new
                        OnOkSelectorListener() {
                            @Override
                            public void onOkSelector(WheelMain wheelMain) {

                            }

                            @Override
                            public void onOkSelector(CityMain wheelMain) {

                            }

                            @Override
                            public void onOkSelector(EmotionMain wheelMain) {
                                String selectedType = circleTypes[wheelMain.getCurrentPosition()];
                                tv_typeName.setText(selectedType);
                                type_id = typeList.get(wheelMain.getCurrentPosition()).getId();
                            }
                        });
            }

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
                        ImageUtils.cropImg(this, mPhotoUri, 200,200);
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
