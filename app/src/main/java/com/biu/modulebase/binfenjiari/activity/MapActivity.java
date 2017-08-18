package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.MapFragment;
import com.biu.modulebase.common.base.BaseActivity2;

/**
 * Created by jhj_Plus on 2016/6/22.
 */
public class MapActivity extends BaseActivity2 {

    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pure);
        init();
    }

    private void init() {

        Intent intent = getIntent();
        String target=intent!=null?intent.getStringExtra(Constant.KEY_MAP_TARGET):"";
        String latitudeStr = intent != null && !TextUtils.isEmpty(
                intent.getStringExtra(Constant.KEY_LOCATION_LATITUDE)) ? intent.getStringExtra(
                Constant.KEY_LOCATION_LATITUDE) : null;
        String longitudeStr= intent != null && !TextUtils.isEmpty(
                intent.getStringExtra(Constant.KEY_LOCATION_LONGITUDE)) ? intent.getStringExtra(
                Constant.KEY_LOCATION_LONGITUDE) : null;
        double latitude = latitudeStr != null ? Double.valueOf(latitudeStr) : 0;
        double longitude = longitudeStr != null ? Double.valueOf(
                longitudeStr) : 0;

        MapFragment mapFragment = new MapFragment();

        Bundle args = new Bundle();
        args.putDouble(Constant.KEY_LOCATION_LATITUDE, latitude);
        args.putDouble(Constant.KEY_LOCATION_LONGITUDE, longitude);
        args.putString(Constant.KEY_MAP_TARGET,target);
        mapFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, mapFragment)
                .commit();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
