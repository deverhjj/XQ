package com.biu.modulebase.binfenjiari.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.communication.uploadservice.UploadServiceBroadcastReceiver;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.fragment.InterestFragment;
import com.biu.modulebase.binfenjiari.fragment.SignatureFragment;
import com.biu.modulebase.binfenjiari.fragment.UserNameFragment;
import com.biu.modulebase.binfenjiari.model.OtherUserInfoVO;
import com.biu.modulebase.binfenjiari.model.SchoolVO;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.binfenjiari.util.ImageUtils;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.binfenjiari.widget.cameraroll.SelectImgAdapter;
import com.biu.modulebase.binfenjiari.widget.wheeltime.CityMain;
import com.biu.modulebase.binfenjiari.widget.wheeltime.EmotionMain;
import com.biu.modulebase.binfenjiari.widget.wheeltime.OnOkSelectorListener;
import com.biu.modulebase.binfenjiari.widget.wheeltime.WheelMain;
import com.biu.modulebase.common.base.BaseActivity2;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lee
 * @Title: {个人资料}
 * @Description:{描述}
 * @date 2016/4/13
 */
public class PersonalInfoActivity extends BaseActivity2 {
    private static final String TAG = "PersonalInfoActivity";
//    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    protected Toolbar toolbar;
    protected TextView title;
    protected AppBarLayout layout_app_bar;

    private static final int REQUEST_NEW_USER_NAME=11;
    private static final int REQUEST_NEW_SIGNATURE=22;
    private static final int REQUEST_NEW_HOBBY=33;
    /**
     * 拍照所得照片的 Uri
     **/
    private Uri mPhotoUri = null;
    /** 选择的头像路径 **/
    private String header_path;

    private LinearLayout ll_mine_info;

    private RelativeLayout rl_other_info;

    private FrameLayout nickname_layout;
    private FrameLayout signature_layout;
    private FrameLayout school_layout;
    private FrameLayout grade_layout;
    private FrameLayout interest_layout;

    private ImageView header;
    private TextView nicknameText;
    private TextView signatureText;
    private TextView interestText;

    private List<SchoolVO> schoolList;
    private List<String> schoolDatas =new ArrayList<>();

    private String gradeString ;

    private TextView schoolText;
    private TextView gradeText;
    private String grade;

    private String school= MyApplication.userInfo==null?"":MyApplication.userInfo.getSchoolName();
    private String school_id =MyApplication.userInfo==null?"":MyApplication.userInfo.getSchool_id();

    private String new_school_id =school_id;


    private String id;

    private ImageView img_info;
    private TextView tv_qianming,tv_small_title,tv_schoolname,tv_classname,tv_hobby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        id = getIntent().getStringExtra(Constant.KEY_ID);
        initView();

    }

    protected void initView() {
        initToolBar();

        ll_mine_info= (LinearLayout) findViewById(R.id.ll_mine_info);
        ll_mine_info.setVisibility(View.GONE);
        rl_other_info= (RelativeLayout) findViewById(R.id.rl_other_info);
        rl_other_info.setVisibility(View.GONE);

        header = (ImageView) findViewById(R.id.header);
        nicknameText =(TextView)findViewById(R.id.nickname);
        signatureText =(TextView)findViewById(R.id.signature);
        interestText=(TextView)findViewById(R.id.interest);
        schoolText= (TextView)findViewById(R.id.school);
        gradeText =(TextView)findViewById(R.id.grade);

        nickname_layout =(FrameLayout)findViewById(R.id.nickname_layout);
        signature_layout = (FrameLayout) findViewById(R.id.signature_layout);
        school_layout = (FrameLayout) findViewById(R.id.school_layout);
        grade_layout = (FrameLayout) findViewById(R.id.grade_layout);
        interest_layout = (FrameLayout) findViewById(R.id.interest_layout);
        if(MyApplication.userInfo!=null &&MyApplication.userInfo.getId().equals(id)){
            setViewData(MyApplication.userInfo);
            ll_mine_info.setVisibility(View.VISIBLE);
        }else{
            getOtherInfo();
            rl_other_info.setVisibility(View.VISIBLE);

            img_info  = (ImageView) findViewById(R.id.img_info);
            tv_qianming  = (TextView)findViewById(R.id.tv_qianming);
            tv_small_title  = (TextView)findViewById(R.id.tv_small_title);
            tv_schoolname = (TextView)findViewById(R.id.tv_schoolname);
            tv_classname = (TextView)findViewById(R.id.tv_classname);
            tv_hobby = (TextView)findViewById(R.id.tv_hobby);
            tv_qianming = (TextView)findViewById(R.id.tv_qianming);

        }

    }

    private void initToolBar(){

        setTitle(null);
        layout_app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("个人资料");

        toolbar.setNavigationIcon(R.mipmap.ico_fanhui_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
//        toolbar.setNavigationIcon(R.mipmap.back);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
//        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        //通过CollapsingToolbarLayout修改字体颜色
//        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);//设置还没收缩时状态下字体颜色
//        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);//设置收缩后Toolbar上字体的颜色
    }

    private<T> void setViewData(T bean){
        if(bean!=null){
            if(bean instanceof UserInfoBean){
                UserInfoBean userInfoVO = (UserInfoBean) bean;
//                mCollapsingToolbarLayout.setTitle(userInfoVO.getUsername());
                ImageDisplayUtil.displayImage(Constant.IMG_SOURCE,userInfoVO.getUser_pic(),header,ImageDisplayUtil.DISPLAY_PERSONAL_HEADER);
                nicknameText.setText(userInfoVO.getUsername());
                signatureText.setText(userInfoVO.getSignature());
                schoolText.setText(userInfoVO.getSchoolName());
                gradeText.setText(userInfoVO.getClassName());
                interestText.setText(userInfoVO.getHobby());

                nickname_layout.setOnClickListener(this);
                signature_layout.setOnClickListener(this);
                school_layout.setOnClickListener(this);
                grade_layout.setOnClickListener(this);
                interest_layout.setOnClickListener(this);
                header.setOnClickListener(this);
            }else{
                OtherUserInfoVO userInfoVO = (OtherUserInfoVO) bean;
//                mCollapsingToolbarLayout.setTitle(userInfoVO.getUsername());
                ImageDisplayUtil.displayImage(Constant.IMG_SOURCE,userInfoVO.getUser_pic(),header,ImageDisplayUtil.DISPLAY_PERSONAL_HEADER);
                nicknameText.setText(userInfoVO.getUsername());
                signatureText.setText(userInfoVO.getSignature());
                schoolText.setText(userInfoVO.getSchoolName());
                gradeText.setText(userInfoVO.getClassName());
                interestText.setText(userInfoVO.getHobby());
                is_friend =userInfoVO.getIs_friend();
//                invalidateOptionsMenu();

                title.setText(userInfoVO.getUsername());

                ImageDisplayUtil.displayImage(Constant.IMG_SOURCE, userInfoVO.getUser_pic(), img_info, ImageDisplayUtil.DISPLAY_PERSONAL_HEADER);
                tv_small_title.setText(userInfoVO.getUsername());

                Drawable drawable = null;
                if (userInfoVO.getGender() == 1) {
                    drawable = getResources().getDrawable(R.mipmap.icon_sex_man);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                } else if (userInfoVO.getGender() == 2) {
                    drawable = getResources().getDrawable(R.mipmap.icon_sex_woman);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                }
                tv_small_title.setCompoundDrawables(null, null, drawable, null);

                tv_schoolname.setText(TextUtils.isEmpty(userInfoVO.getSchoolName()) ? "未知" : userInfoVO.getSchoolName());
                tv_classname.setText(TextUtils.isEmpty(userInfoVO.getClassName()) ? "未知" : userInfoVO.getClassName());
                tv_hobby.setText(TextUtils.isEmpty(userInfoVO.getHobby()) ? "未知" : userInfoVO.getHobby());
                tv_qianming.setText(userInfoVO.getSignature());

            }

        }


    }

    public void loadData() {

    }

    //1是  2不是
    private String is_friend="1";

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem addItem=menu.findItem(R.id.action_add);
//        MenuItem chatItem=menu.findItem(R.id.action_chat);
//        if(id.equals(MyApplication.userInfo==null?"":MyApplication.userInfo.getId())){
//            addItem.setVisible(false);
//            chatItem.setVisible(false);
//        }
//        if(is_friend.equals("1")){
//            addItem.setVisible(false);
//        }else{
//            addItem.setVisible(true);
//        }
//
//        return super.onPrepareOptionsMenu(menu);
//
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.person_info, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_add) {
//            return true;
//        }else if(id==R.id.action_chat){
//            Intent intent =new Intent(this,ChatActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void showSchoolChoiceDialog(){
        if(schoolDatas.size()==0){
            showTost("没有学校数据",1);
            return;
        }
        DialogFactory.showEmotionAlert(this, schoolDatas, new OnOkSelectorListener() {
            @Override
            public void onOkSelector(WheelMain wheelMain) {

            }

            @Override
            public void onOkSelector(CityMain wheelMain) {

            }

            @Override
            public void onOkSelector(EmotionMain wheelMain) {
                school =wheelMain.getSelectedData();
                schoolText.setText(school);
                new_school_id= schoolList.get(wheelMain.getCurrentPosition()).getId();
            }
        });
    }

    private void showGradeChoiceDialog(String jsonString){
        DialogFactory.getInstance(this).showCityDialog(jsonString, CityMain.CITY_TYPE.LEVEL_TWO, new OnOkSelectorListener() {
            @Override
            public void onOkSelector(WheelMain wheelMain) {

            }

            @Override
            public void onOkSelector(CityMain wheelMain) {
                grade= wheelMain.getOneName()+ wheelMain.getTwoName();
                String id =wheelMain.getCurentCityId();
                gradeText.setText(grade);
                updateProfile(3,id);
            }

            @Override
            public void onOkSelector(EmotionMain wheelMain) {

            }
        });
    }

    private void getOtherInfo(){
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_OTHER_INFO);
        JSONUtil.put(params,"action",Constant.ACTION_GET_OTHER_INFO);
        JSONUtil.put(params,"token", PreferencesUtils.getString(getApplicationContext(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"userId",id);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
               OtherUserInfoVO userInfoVO= JSONUtil.fromJson(jsonString, OtherUserInfoVO.class);
               setViewData(userInfoVO);

            }

            @Override
            public void onCodeError(int key, String message) {
                showTost("获取信息失败，请稍后再试",0);
                finish();

            }

            @Override
            public void onConnectError(String message) {

            }
        });
    }

    private void getSchoolList(){
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_SCHOOL);
        JSONUtil.put(params,"action",Constant.ACTION_GET_SCHOOL_LIST);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                schoolList =JSONUtil.fromJson(jsonString,new TypeToken<List<SchoolVO>>(){}.getType());
                for(int i=0;i<schoolList.size();i++){
                    schoolDatas.add(schoolList.get(i).getName());
                }
                showSchoolChoiceDialog();

            }

            @Override
            public void onCodeError(int key, String message) {

            }

            @Override
            public void onConnectError(String message) {

            }
        });
    }

    private void getGradeList(){
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_SCHOOL);
        JSONUtil.put(params,"action",Constant.ACTION_GET_GRADE_LIST);
        JSONUtil.put(params,"school_id",new_school_id);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                gradeString =jsonString;
                showGradeChoiceDialog(gradeString);

            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message,1);

            }

            @Override
            public void onConnectError(String message) {
            }
        });
    }

    private void changeHead(String headPath){
//        showProgress(getClass().getSimpleName());
        List<String> paths =new ArrayList<>();
        paths.add(headPath);
        String token = PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_TOKEN);
        JSONObject jsonObjec =new JSONObject();
        JSONUtil.put(jsonObjec,"token",token);
        JSONUtil.put(jsonObjec,"model",Constant.MODEL_PERSONAL);
        JSONUtil.put(jsonObjec,"action",Constant.ACTION_UPDATE_HEAD);
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.KEY_UPLOAD_DATA,jsonObjec.toString());
        Communications.uploadMultipart(this, Constant.UPLOAD_URL, params, "img", paths);
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
//            dismissProgress();
            LogUtil.LogI(TAG, "Upload with ID " + uploadId + " is completed: " + serverResponseCode
                    + ", "
                    + serverResponseMessage);
            try {
                JSONObject response = new JSONObject(serverResponseMessage);
                String key = JSONUtil.getString(response, "key");
                if (key.equals("1")) {// 成功
                    String user_pic =JSONUtil.getString(response,"result");
                    MyApplication.getUserInfo(getApplicationContext()).setUser_pic(user_pic);
                    String json = JSONUtil.getGson().toJson(MyApplication.userInfo);
                    PreferencesUtils.putString(PersonalInfoActivity.this, PreferencesUtils.KEY_USER_INFO, json);
                    ImageDisplayUtil.displayImage(Constant.IMG_SOURCE,user_pic,header,ImageDisplayUtil.DISPLAY_HEADER);
                } else {
                    showTost("修改失败,请稍后再试...",1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     *
     * @param type  1：昵称 2：个性签名 3：学校（班级id ）4：爱好 5：性别（1男 2女）
     * @param newParam 新的信息
     */
    private void updateProfile(final int type,final String newParam){
//        showProgress(getClass().getSimpleName());
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"action",Constant.ACTION_UPDATE_USER_INFO);
        JSONUtil.put(params,"model",Constant.MODEL_REG);
        JSONUtil.put(params,"token",PreferencesUtils.getString(getApplicationContext(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"type",type);
        switch (type){
            case 1://昵称
                JSONUtil.put(params,"username",newParam);
                break;
            case 2://签名
                JSONUtil.put(params,"signature",newParam);
                break;
            case 3://班级
                JSONUtil.put(params,"class_id",newParam);
                break;
            case 4://爱好
                JSONUtil.put(params,"hobby",newParam);
                break;
            case 5://性别
                JSONUtil.put(params,"gender",newParam);
                break;
        }
        jsonRequest(true, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                switch (type){
                    case 1:
                        nicknameText.setText(newParam);
//                        mCollapsingToolbarLayout.setTitle(newParam);
                        MyApplication.userInfo.setUsername(newParam);
                        break;
                    case 2:
                        signatureText.setText(newParam);
                        MyApplication.userInfo.setSignature(newParam);
                        break;
                    case 3:
                        gradeText.setText(grade);
                        MyApplication.userInfo.setClassName(grade);
                        MyApplication.userInfo.setSchoolName(school);
                        break;
                    case 4:
                        interestText.setText(newParam);
                        MyApplication.userInfo.setHobby(newParam);
                        break;
                    case 5:

                        break;

                }
                String json = JSONUtil.getGson().toJson(MyApplication.userInfo);
                PreferencesUtils.putString(getApplicationContext(), PreferencesUtils.KEY_USER_INFO, json);
            }

            @Override
            public void onCodeError(int key, String message) {
//                dismissProgress();
                showTost(message,1);

            }

            @Override
            public void onConnectError(String message) {
//                dismissProgress();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Cursor cursor0;
        if(resultCode== Activity.RESULT_OK) {
            switch (requestCode){
                case Constant.CAPTURE_PHOTO:
                    cursor0 = getContentResolver().query(mPhotoUri, null, null, null, null);
                    if (cursor0 != null) {
                        cursor0.moveToFirst();
                        header_path = cursor0.getString(1);
                        cursor0.close();
                        cropImg( mPhotoUri, 200, 200);
                    } else {
                        Toast.makeText(this,"请选择本地资源!",Toast.LENGTH_LONG).show();
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
//                    if(Build.VERSION.SDK_INT>=24){
//                        final File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
//                        final File newFile = new File(imagePath,imagePath.getAbsolutePath());
//                        mPhotoUri  = FileProvider.getUriForFile(PersonalInfoActivity.this, "com.biu.provider", newFile);
//                    }else{
                        mPhotoUri = Uri.parse("file://" + header_path);
//                    }
                    cropImg( mPhotoUri, 200, 200);
                    break;
                case Constant.CROP_IMG:
                    // 拿到剪切数据
                    Bitmap bitmap =null;
                    if (data.getExtras()!=null) {
                        bitmap = data.getParcelableExtra("data");
                    }else {//nexus 6.0手机裁剪后intnet里的是图片路径Uri
                        Uri uri = data.getData();
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        String path = ImageUtils.bitmapToFile(bitmap);
                        changeHead(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//
                    break;
                case REQUEST_NEW_USER_NAME:
                    String newUserName=data.getStringExtra(UserNameFragment.EXTRA_NEW_USER_NAME);
                    updateProfile(1, newUserName);
                    break;
                case REQUEST_NEW_SIGNATURE:
                    String signature=data.getStringExtra(SignatureFragment.NEW_SIGNATURE);
                    updateProfile(2, signature);
                    break;
                case REQUEST_NEW_HOBBY:
                    String hobby=data.getStringExtra(InterestFragment.NEW_HOBBY);
                    updateProfile(4, hobby);
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cropImg(Uri uri, int outputX, int outputY) {
        if (null == uri)
            return;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
//        intent.putExtra("crop", "true"); // 可裁剪
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);// 输出图片大小
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);// 若为false则表示不返回数据
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        startActivityForResult(intent, Constant.CROP_IMG);

    }


    private  void showTakePhoto() {
        DialogFactory.showDialog(this, R.layout.pop_take_photo,
                R.style.WheelDialog, R.style.popwin_anim_style, Gravity.BOTTOM, 0.9f, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        v.findViewById(R.id.take_photo).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       mPhotoUri = ImageUtils.takePhoto(PersonalInfoActivity.this, dialog);

                                    }
                                });
                        // 从相册选取照片
                        v.findViewById(R.id.choice_photo).setOnClickListener(
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        ImageUtils.selectPicture(PersonalInfoActivity.this, dialog, true);
                                    }
                                });
                        v.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.header) {
            showTakePhoto();

        } else if (i == R.id.nickname_layout) {
            Intent intent = new Intent(PersonalInfoActivity.this, UserNameActivity.class);
            intent.putExtra(UserNameFragment.EXTRA_OLD_USER_NAME, nicknameText.getText().toString());
            startActivityForResult(intent, REQUEST_NEW_USER_NAME);

        } else if (i == R.id.signature_layout) {
            Intent signatureIntent = new Intent(this, SignatureActivity.class);
            signatureIntent.putExtra(UserNameFragment.EXTRA_OLD_USER_NAME, signatureText.getText().toString());
            startActivityForResult(signatureIntent, REQUEST_NEW_SIGNATURE);

        } else if (i == R.id.school_layout) {
            if (schoolList == null) {
                getSchoolList();
            } else {
                showSchoolChoiceDialog();
            }

        } else if (i == R.id.grade_layout) {
            if (gradeString == null || !new_school_id.equals(school_id)) {
                getGradeList();
            } else {
                showGradeChoiceDialog(gradeString);
            }

        } else if (i == R.id.interest_layout) {
            Intent interestIntet = new Intent(this, InterestActivity.class);
            interestIntet.putExtra(UserNameFragment.EXTRA_OLD_USER_NAME, interestText.getText().toString());
            startActivityForResult(interestIntet, REQUEST_NEW_HOBBY);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onDestroy() {
        Communications.cancelRequest(getClass().getSimpleName());
        SelectImgAdapter.mSelectedImage.clear();
        uploadReceiver.unregister(this);
        super.onDestroy();
    }
}
