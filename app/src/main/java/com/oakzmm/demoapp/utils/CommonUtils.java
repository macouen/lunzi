package com.oakzmm.demoapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 *
 */
public class CommonUtils {

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    private static long lastClickTime;

    @SuppressLint("SimpleDateFormat")
    public static String getYYMMDD(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        return sdf.format(time);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getYMDHMS(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getMDHM(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm");
        return sdf.format(new Date(time));
    }

    public static String getWeek() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWeek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWeek)) {
            mWeek = "星期天";
        } else if ("2".equals(mWeek)) {
            mWeek = "星期一";
        } else if ("3".equals(mWeek)) {
            mWeek = "星期二";
        } else if ("4".equals(mWeek)) {
            mWeek = "星期三";
        } else if ("5".equals(mWeek)) {
            mWeek = "星期四";
        } else if ("6".equals(mWeek)) {
            mWeek = "星期五";
        } else if ("7".equals(mWeek)) {
            mWeek = "星期六";
        }
        return mWeek;
    }

    public static void callPhone(Context context, String phone) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phone));
        context.startActivity(dialIntent);
    }

    public static boolean validMobile(String number) {
        return number != null && number.matches("1\\d{10}");
    }

    public static boolean validPassword(String password) {
        return password != null && password.matches("\\w{6,20}");
    }

    public static boolean isEmailAddress(String email) {
        if (email == null) {
            return false;
        }
        String regex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

        return email.matches(regex);
    }

    public static boolean isNetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @param context
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int resId) {
        toast(context, context.getResources().getString(resId));
    }

    public static void httpToast(final Activity activity, final String errMsg) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.toast(activity, errMsg);
                }
            });
        }
    }

    public static void httpToast(final Activity activity, final int errMsg) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.toast(activity, errMsg);
                }
            });
        }
    }

    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取当前程序版本号
     */
    public static int getCurrentVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取当前程序版本
     */
    public static String getCurrentVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }



    public static Bitmap readBitmapFromAssets(Context context, String path) {
        Bitmap bmp = null;
        try {
            InputStream is = context.getAssets().open(path);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static void downloadFile(File file, String url) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            byte[] buffer = new byte[8192];
            int count = 0;
            fos = new FileOutputStream(file);
            is = new URL(url).openStream();
            while ((count = is.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            fos = null;
            is.close();
            is = null;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * 当天的时间格式为：HH:mm
     * 当年的时间格式为：MM-dd
     * 其他yyyy-MM-dd
     *
     * @param
     * @return
     */
    public static String getTimeHasPassed(Context context, String dateTimeString) {

        // Date dateCreated =
        // TimeUtil.getDateFromSqlDateTimeString(sqlDateTimeString);
        Date dateCreated = new Date(Long.valueOf(dateTimeString) * 1000);
        Date date = new Date();
        if (dateCreated.before(date)) {
            String dateCreatedStr = TimeUtil.getStringTimeByDateTimeString(
                    dateCreated, TimeUtil.patternGetMonth);
            String dateStr = TimeUtil.getStringTimeByDateTimeString(date,
                    TimeUtil.patternGetMonth);
            if (TextUtils.equals(dateCreatedStr, dateStr)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int today = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);
                calendar.setTime(dateCreated);
                int dayCreated = calendar.get(Calendar.DAY_OF_MONTH);
                int yearCreated = calendar.get(Calendar.YEAR);

                if (year == yearCreated) {
                    if (today == dayCreated) {
                        return TimeUtil.getCurrentTimeString(dateCreated);
                    } else {
                        return TimeUtil.getCurrentDayTimeString(dateCreated);
                    }
                }
            }
            return dateCreatedStr = TimeUtil.getCurrentMdHmsTimeString(dateCreated);
        }
        return "";
    }

    public static boolean apkInstalled(String packageName) {
        return new File(File.separator + "data" + File.separator + "data"
                + File.separator + packageName).exists();
    }

    /**
     * 判断是否是快速度多次点击按钮。
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isGpsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;

    }


    /**
     * @param context
     * @param packagename
     */
    public static void doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /**
     * 使用系统播放器播放视频
     *
     * @param context
     * @param mediaUrl
     */
    public static void playMedia(final Context context, final String mediaUrl) {
        if (!isNetAvailable(context)) {
            Toast.makeText(context, "您的网络异常，视频暂时无法播放", Toast.LENGTH_SHORT).show();
            return;
        }
        int type = getNetworkType(context);
        if (type != 1 && type != 0) {
            new AlertDialog.Builder(context)
                    .setMessage("您当前处于非WiFi状态，观看视频可能会消耗大量网络数据")
                    .setNegativeButton("继续观看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(mediaUrl), "video/*");
                            context.startActivity(intent);
                        }
                    })
                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        } else if (type == 1) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(mediaUrl), "video/*");
            context.startActivity(intent);
        }
    }
}
