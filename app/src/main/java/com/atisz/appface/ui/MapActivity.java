package com.atisz.appface.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.PermissionUtils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wuwei
 * @date 2019/3/4
 */

public class MapActivity extends MyBaseActivity implements View.OnClickListener, PermissionUtils.PermissionListener {
    @BindView(R.id.bmapView)
    MapView mBmapView;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.tv_traffic)
    TextView mTvTraffic;
    private BaiduMap mBaiduMap;
    private boolean mTraffic = false;
    LocationClient mLocationClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private MyLocationListener mMyLocationListener = new MyLocationListener();
    private LatLng latLng;
    private boolean isFirstLoc = true;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_map);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        checkPermission();
    }

    private void checkPermission() {
        List<String> requestPermisson = new ArrayList<>();
        requestPermisson.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        requestPermisson.add(Manifest.permission.ACCESS_FINE_LOCATION);

        PermissionUtils.requestPermission(MapActivity.this, requestPermisson.toArray(new String[requestPermisson.size()]), this);
    }

    @Override
    public void onGranted() {
        initMap();
    }

    @Override
    public void onDenied(List<String> deniedPermission) {
        for (int i = 0; i < deniedPermission.size(); i++) {
            if (deniedPermission.get(i).equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                PermissionUtils.openSettingActivity(this, "获取位置");
            }

            if (deniedPermission.get(i).equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                PermissionUtils.openSettingActivity(this, "获取Gps位置");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initMap() {
       /* MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/
        mBaiduMap = mBmapView.getMap();

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);

        initLocation();

        mLocationClient.registerLocationListener(mMyLocationListener);
        //开启地图定位图层
        mLocationClient.start();

        mLocationClient.requestLocation();
    }

    private void initLocation() {
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        //设置locationClientOption
        mLocationClient.setLocOption(option);


        /*mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, null));

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mTvLocation.setOnClickListener(this);
        mTvTraffic.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBmapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBmapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBmapView.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mBmapView = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location:
                //把定位点再次显现出来
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                break;
            case R.id.tv_traffic:
                if (mTraffic == false) {
                    mTraffic = true;
                    mBaiduMap.setTrafficEnabled(true);
                } else {
                    mTraffic = false;
                    mBaiduMap.setTrafficEnabled(false);
                }

                break;
            default:
                break;
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mBmapView == null) {
                return;
            }

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(MapActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(MapActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(MapActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
