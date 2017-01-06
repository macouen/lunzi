package com.oakzmm.demoapp.utils.gpsutil;


// ━━━━━━━━━神兽出没━━━━━━━━━
//      ┏┓　　　      ┏┓
//     ┏┛┻━━━━━━━━━━━┛┻┓
//     ┃               ┃
//     ┃       ━       ┃
//     ┃   ┳━┛   ┗━┳   ┃
//     ┃               ┃
//     ┃       ┻       ┃
//     ┃               ┃
//     ┗━━━┓　　　  ┏━━━┛Code is far away from bug with the animal protecting
//         ┃　 　　 ┃    神兽保佑,代码无bug
//         ┃　　　  ┃
//         ┃       ┗━━━┓
//         ┃           ┣┓
//         ┃           ┏┛
//         ┗┓┓┏━━━┳┓┏━━┛
//          ┃┫┫   ┃┫┫
//          ┗┻┛   ┗┻┛
// 
// ━━━━━━━━━BUG走开━━━━━━━━━

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.oakzmm.demoapp.utils.OakLog;

import java.util.List;
import java.util.Locale;

/**
 * Created by April_Oak zhaominmin@aprbrother.com
 * on 2016/6/2 16:10
 * Description: 使用系统自带的定位进行定位的定位工具类。
 */
public class MapLocationUtil {

    private final LocationManager mLocationManager;
    private final String mProvider;
    private final MapLocationListener mLocationListener;
    private final LocationListener mListener;
    private Location mLocation;
    private String mAddress;
    private Context mContext;

    public MapLocationUtil(Context context, MapLocationListener locationListener) {
        mContext = context;
        mLocationListener = locationListener;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // todo 请根据自己情况使用 getProvider 或者直接使用 GPS_PROVIDER or NETWORK_PROVIDER
//        mProvider = getProvider(mLocationManager);
        mProvider = LocationManager.GPS_PROVIDER;
        OakLog.i("mProvider" + mProvider);
        mLocation = mLocationManager.getLastKnownLocation(mProvider);
        OakLog.i("mLocation" + mLocation);
        // 注册监听器locationListener，第2、3个参数可以控制接收gps消息的频度以节省电力。第2个参数为毫秒，
        // 表示调用listener的周期，第3个参数为米,表示位置移动指定距离后就调用listener
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                OakLog.i("onLocationChanged:" +
                        "\nLatitude:" + location.getLatitude() +
                        "\nLongitude" + location.getLongitude());
                mLocation = location;
//                Gps gps = PositionUtil.gps84_To_Gcj02(location.getLatitude(), location.getLongitude());
//                if (gps != null) {
//                    mLocation.setLatitude(gps.getWgLat());
//                    mLocation.setLongitude(gps.getWgLon());
//                }
//                OakLog.i("after:" +
//                        "\nLatitude:" + mLocation.getLatitude() +
//                        "\nLongitude" + mLocation.getLongitude());
                mLocationListener.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                OakLog.i("onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
                OakLog.i("onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                OakLog.i("onProviderDisabled");
            }
        };

    }

    public Location getLocation() {

        return mLocation;
    }

    /**
     * @param minTime     minimum time interval between location updates, in milliseconds
     * @param minDistance minimum distance between location updates, in meters
     */
    public void requestLocation(long minTime, float minDistance) {
        mLocationManager.requestLocationUpdates(mProvider, minTime, minDistance, mListener);
    }

    public void removeLocation() {
        mLocationManager.removeUpdates(mListener);
    }

    // 获取Location Provider
    private String getProvider(LocationManager locationManager) {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        // 查询精度：高
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 速度
        criteria.setSpeedRequired(false);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(false);
        // 是否查询方位角:否
        criteria.setBearingRequired(false);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 返回最合适的符合条件的provider，第2个参数为true说明,如果只有一个provider是有效的,则返回当前provider
        return locationManager.getBestProvider(criteria, true);
    }

    // 获取地址信息
    private Address getAddressByGeoPoint(Location location) {
        Address result = null;
        // 先将Location转换为GeoPoint
        // GeoPoint gp=getGeoByLocation(location);
        try {
            if (location != null) {
                // 获取Geocoder，通过Geocoder就可以拿到地址信息
                Geocoder gc = new Geocoder(mContext, Locale.CHINA);
                List<Address> lstAddress = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (lstAddress.size() > 0) {
                    result = lstAddress.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getAddress() {
        if (mLocation != null)
            mAddress = getAddressByGeoPoint(mLocation).getLocality();
        return mAddress;
    }

    public interface MapLocationListener {
        void onLocationChanged(Location location);
    }
}
