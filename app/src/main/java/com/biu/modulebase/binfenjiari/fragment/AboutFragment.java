package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/4/14
 */
public class AboutFragment extends BaseFragment {


    private TextView phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_about, container, false);
        return layout;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        getBaseActivity().setBackNavigationIcon();
        TextView version = (TextView) rootView.findViewById(R.id.version);
        version.setText("享去 v" + Utils.getAppVersion(getActivity()));
        phone = (TextView) rootView.findViewById(R.id.phone);
        phone.setOnClickListener(this);
    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.phone) {//                requestPremission(v,Manifest.permission.CALL_PHONE);
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone.getText().toString()));
            startActivity(intent);

        }

    }

//    /**
//     *
//     * @param view
//     * @param premission   Manifest.permission
//     */
//    public void requestPremission(View view,String premission) {
//        LogUtil.LogI("Show camera button pressed. Checking permission.");
//        // BEGIN_INCLUDE(camera_permission)
//        // Check if the Camera permission is already available.
//        if ( ActivityCompat.checkSelfPermission(getActivity(), premission)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Camera permission has not been granted.
//            LogUtil.LogI( premission+" permission has NOT been granted. Requesting permission.");
//
//            // BEGIN_INCLUDE(camera_permission_request)
//            if ( ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                // Provide an additional rationale to the user if the permission was not granted
//                // and the user would benefit from additional context for the use of the permission.
//                // For example if the user has previously denied the permission.
//                LogUtil.LogI("Displaying "+ premission +" rationale to provide additional context.");
//                Snackbar.make(view, "拍照功能需要相机权限",
//                        Snackbar.LENGTH_INDEFINITE)
//                        .setAction("设置", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                        Constant.REQUEST_CAMERA);
//                            }
//                        })
//                        .show();
//            } else {
//
//                // Camera permission has not been granted yet. Request it directly.
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        Constant.REQUEST_CAMERA);
//            }
//            // END_INCLUDE(camera_permission_request)
//
//        } else {
//
//            // Camera permissions is already available, show the camera preview.
//            LogUtil.LogI(premission+" has already been granted. Displaying camera preview.");
//            takePhoto();
//        }
//        // END_INCLUDE(camera_permission)
//
//    }
//
//
//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//
//        if (requestCode == Constant.REQUEST_CAMERA) {
//            // BEGIN_INCLUDE(permission_result)
//            // Received permission result for camera permission.
//            Log.i(TAG, "Received response for Camera permission request.");
//
//            // Check if the only required permission has been granted
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Camera permission has been granted, preview can be displayed
//                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
//                Snackbar.make(getView(), "permision_available_camera",
//                        Snackbar.LENGTH_SHORT).show();
//                takePhoto();
//            } else {
//                Log.i(TAG, "CAMERA permission was NOT granted.");
//                Snackbar.make(getView(), "permissions_not_granted",
//                        Snackbar.LENGTH_SHORT).show();
//
//            }
//            // END_INCLUDE(permission_result)
//
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

}
