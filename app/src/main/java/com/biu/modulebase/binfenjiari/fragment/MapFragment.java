package com.biu.modulebase.binfenjiari.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.IllegalNaviArgumentException;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.widget.map.DrivingRouteOverlay;


/**
 * Created by jhj_Plus on 2016/6/22.
 */
public class MapFragment  extends Fragment implements View.OnClickListener,
        OnGetRoutePlanResultListener,BDLocationListener
{
    private static final String TAG = "MapFragment";

    private BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);

    private TextView mTargetLocation;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private String mTarget;
    private double mLatitude;
    private double mLongitude;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args=getArguments();
        if (args!=null) {
            mTarget=args.getString(Constant.KEY_MAP_TARGET);
           mLatitude=args.getDouble(Constant.KEY_LOCATION_LATITUDE);
           mLongitude= args.getDouble(Constant.KEY_LOCATION_LONGITUDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View root= layoutInflater.inflate(R.layout.fragment_map,viewGroup,false);

        mMapView= (MapView) root.findViewById(R.id.mapView);
        mBaiduMap=mMapView.getMap();

        initMap();

        root.findViewById(R.id.navigation).setOnClickListener(this);
        mTargetLocation= (TextView) root.findViewById(R.id.tv_location);
        mTargetLocation.post(new Runnable() {
            @Override
            public void run() {
                int height=mTargetLocation.getMeasuredHeight();
                mBaiduMap.setViewPadding(0,0,0,height+getResources().getDimensionPixelSize(R
                        .dimen.margin_bottom_8dp));
            }
        });

        root.findViewById(R.id.back).setOnClickListener(this);
        ((TextView)root.findViewById(R.id.tv_location)).setText(mTarget);

        return root;
    }

    private void initMap() {
        LogUtil.LogI(TAG,"latitude="+mLatitude+",longitude="+mLongitude);
        LatLng targetLocation = new LatLng(mLatitude,mLongitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(targetLocation)//设置中心点为指定点
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        initOverlay();
    }


    private void initOverlay() {
        LatLng llA = new LatLng(mLatitude, mLongitude);
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(false);
        Marker m = (Marker) (mBaiduMap.addOverlay(ooA));

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 退出时销毁定位
        if (mLocClient!=null) mLocClient.stop();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView=null;
        bdA.recycle();
        super.onDestroyView();

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.navigation) {
            test();

        } else if (i == R.id.back) {
            getActivity().finish();

        } else {
        }
    }


    void test() {

        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

    }




    // 搜索相关
    private RoutePlanSearch mSearch = null;
    // 定位相关
    LocationClient mLocClient;
    private void startNavi() {

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();


        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("常州","工程学院");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("常州","信息学院");
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            OtherUtil.showToast(getActivity(),"抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }

        DrivingRouteLine route = result.getRouteLines().get(0);
        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
        mBaiduMap.setOnMarkerClickListener(overlay);
        overlay.setData(result.getRouteLines().get(0));
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // map view 销毁后不在处理新接收的位置
        if (bdLocation == null || mMapView == null) {
            return;
        }

        Log.e(TAG,"onReceiveLocation");

        LatLng startlatLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());

        LatLng endlatLng = new LatLng(mLatitude,mLongitude);

        NaviParaOption para = new NaviParaOption();
        para.startPoint(startlatLng);
        para.startName("从这里开始");
        para.endPoint(endlatLng);
        para.endName("到这里结束");

        BaiduMapNavigation.setSupportWebNavi(true);
        try {

            BaiduMapNavigation.openBaiduMapNavi(para, getActivity());

        } catch (BaiduMapAppNotSupportNaviException e) {

            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } catch (IllegalNaviArgumentException e) {

        }
    }


    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }

}
