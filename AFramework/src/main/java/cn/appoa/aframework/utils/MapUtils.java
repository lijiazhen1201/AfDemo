package cn.appoa.aframework.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图工具类
 *
 * @author https://blog.csdn.net/i996573526/article/details/82117862
 */
public final class MapUtils {

    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名

    /**
     * 检查地图应用是否安装
     *
     * @return
     */
    public static boolean isGdMapInstalled(Context context) {
        return isInstallPackage(context, PN_GAODE_MAP);
    }

    public static boolean isBaiduMapInstalled(Context context) {
        return isInstallPackage(context, PN_BAIDU_MAP);
    }

    public static boolean isTencentMapInstalled(Context context) {
        return isInstallPackage(context, PN_TENCENT_MAP);
    }

    public static boolean isInstallPackage(Context context, String packageName) {
        //return new File("/data/data/" + packageName).exists();
        return AtyUtils.checkApkExist(context, packageName);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return true 存在
     */
    public static boolean exist(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有true，没有false
        return packageNames.contains(packageName);
    }

    /**
     * 百度转高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    /**
     * 高德、腾讯转百度
     *
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     */
    public static double[] BD09ToGCJ02(double latitude, double longitude) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = longitude - 0.0065;
        double y = latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lat = z * Math.sin(theta);
        double gg_lng = z * Math.cos(theta);
        return new double[]{gg_lat, gg_lng};
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     */
    public static double[] GCJ02ToBD09(double latitude, double longitude) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double z = Math.sqrt(longitude * longitude + latitude * latitude)
                + 0.00002 * Math.sin(latitude * x_pi);
        double theta = Math.atan2(latitude, longitude) + 0.000003 * Math.cos(longitude * x_pi);
        double bd_lat = z * Math.sin(theta) + 0.006;
        double bd_lng = z * Math.cos(theta) + 0.0065;
        return new double[]{bd_lat, bd_lng};
    }

    /**
     * 百度经纬度转高德经纬度(从高德SDK抽取)
     *
     * @param lat
     * @param lon
     * @return
     * @from https://blog.csdn.net/z19980115/article/details/79197242
     */
    public static double[] baidu2AMap(double lat, double lon) {
        try {
            if (lat != 0 && lon != 0) {
                double var4 = 0.006401062D;
                double var6 = 0.0060424805D;
                double[] var2 = null;

                for (int var3 = 0; var3 < 2; ++var3) {
                    var2 = new double[2];
                    double var16 = lon - var4;
                    double var18 = lat - var6;
                    double[] var29 = new double[2];
                    double var24 = Math.cos(b(var16) + Math.atan2(var18, var16)) * (a(var18) + Math.sqrt(var16 * var16 + var18 * var18)) + 0.0065D;
                    double var26 = Math.sin(b(var16) + Math.atan2(var18, var16)) * (a(var18) + Math.sqrt(var16 * var16 + var18 * var18)) + 0.006D;
                    var29[1] = (c(var24));
                    var29[0] = (c(var26));
                    var2[1] = (c(lon + var16 - var29[1]));
                    var2[0] = (c(lat + var18 - var29[0]));
                    var4 = lon - var2[1];
                    var6 = lat - var2[0];
                }

                return var2;
            }
        } catch (Throwable var28) {
            // ll.a(var28, "OffsetUtil", "B2G");
        }

        return new double[]{lat, lon};
    }

    private static double a = 3.141592653589793D;

    private static double a(double var0) {
        return Math.sin(var0 * 3000.0D * (a / 180.0D)) * 2.0E-5D;
    }

    private static double b(double var0) {
        return Math.cos(var0 * 3000.0D * (a / 180.0D)) * 3.0E-6D;
    }

    private static double c(double var0) {
        return (new BigDecimal(var0)).setScale(8, 4).doubleValue();
    }

    /**
     * 打开高德地图导航功能
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openGaoDeNavi(Context context, double slat, double slon, String sname,
                                     double dlat, double dlon, String dname) {
        if (context == null || dname == null || dlat == 0 || dlon == 0) {
            return;
        }
        if (isGdMapInstalled(context)) {
            String uriString = null;
            StringBuilder builder = new StringBuilder("amapuri://route/plan?sourceApplication=maxuslife");
            if (sname != null && slat != 0 && slon != 0) {
                builder.append("&sname=").append(sname)
                        .append("&slat=").append(slat)
                        .append("&slon=").append(slon);
            }
            builder.append("&dlat=").append(dlat)
                    .append("&dlon=").append(dlon)
                    .append("&dname=").append(dname)
                    .append("&dev=0")
                    .append("&t=0");
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_GAODE_MAP);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } else {
            //https://lbs.amap.com/api/uri-api/guide/travel/route
            String uriString = null;
            StringBuilder builder = new StringBuilder("https://uri.amap.com/navigation?mode=car");//驾车
            builder.append("&policy=0");//0:推荐策略,1:避免拥堵,2:避免收费,3:不走高速（仅限移动端）
            builder.append("&coordinate=gaode");//坐标系参数coordinate=gaode,表示高德坐标（gcj02坐标）
            builder.append("&callnative=0");//是否尝试调起高德地图APP并在APP中查看，0表示不调起，1表示调起, 默认值为0
            builder.append("&src=").append(context.getPackageName());//使用方来源信息
            if (sname != null && slat != 0 && slon != 0) {
                builder.append("&from=").append(slon).append(",").append(slat).append(",").append(sname);
            }
            builder.append("&to=").append(dlon).append(",").append(dlat).append(",").append(dname);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        }
    }

    /**
     * 打开腾讯地图
     * params 参考http://lbs.qq.com/uri_v1/guide-route.html
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     *                驾车：type=drive，policy有以下取值
     *                0：较快捷
     *                1：无高速
     *                2：距离
     *                policy的取值缺省为0
     *                &from=" + dqAddress + "&fromcoord=" + dqLatitude + "," + dqLongitude + "
     */
    public static void openTencentMap(Context context, double slat, double slon, String sname,
                                      double dlat, double dlon, String dname) {
        if (context == null || dname == null || dlat == 0 || dlon == 0) {
            return;
        }
        if (isTencentMapInstalled(context)) {
            String uriString = null;
            StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=drive&policy=0&referer=zhongshuo");
            if (sname != null && slat != 0 && slon != 0) {
                builder.append("&from=").append(sname)
                        .append("&fromcoord=").append(slat)
                        .append(",")
                        .append(slon);
            }
            builder.append("&to=").append(dname)
                    .append("&tocoord=").append(dlat)
                    .append(",")
                    .append(dlon);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_TENCENT_MAP);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } else {
            String uriString = null;
            StringBuilder builder = new StringBuilder("https://apis.map.qq.com/uri/v1/routeplan?type=drive");
            builder.append("&policy=0");
            builder.append("&coord_type=2");//坐标类型，取值如下：1 GPS;2 腾讯坐标（默认）
            builder.append("&referer=").append(context.getPackageName());
            if (sname != null && slat != 0 && slon != 0) {
                builder.append("&from=").append(sname)
                        .append("&fromcoord=").append(slat)
                        .append(",")
                        .append(slon);
            }
            builder.append("&to=").append(dname)
                    .append("&tocoord=").append(dlat)
                    .append(",")
                    .append(dlon);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        }
    }

    /**
     * 打开百度地图导航功能(默认坐标点是高德地图，需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuNavi(Context context, double slat, double slon, String sname,
                                     double dlat, double dlon, String dname) {
        if (context == null || dname == null || dlat == 0 || dlon == 0) {
            return;
        }
        if (isBaiduMapInstalled(context)) {
            String uriString = null;
            StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=driving");
            if (sname != null && slat != 0 && slon != 0) {
                builder.append("&origin=latlng:")
                        .append(slat)
                        .append(",")
                        .append(slon)
                        .append("|name:")
                        .append(sname);
            }
            builder.append("&destination=latlng:")
                    .append(dlat)
                    .append(",")
                    .append(dlon)
                    .append("|name:")
                    .append(dname);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_BAIDU_MAP);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } else {
            String uriString = null;
            StringBuilder builder = new StringBuilder("http://api.map.baidu.com/direction?mode=driving");
            builder.append("&output=html");
            builder.append("&src=").append(context.getPackageName());
            if (sname != null && slat != 0 && slon != 0) {
//                builder.append("&origin=latlng")
//                        .append(slat)
//                        .append(",")
//                        .append(slon)
//                        .append("|name:")
//                        .append(sname);
            }
            builder.append("&destination=")
                    .append(dlat)
                    .append(",")
                    .append(dlon)
                    .append("|name:")
                    .append(dname);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        }
    }

}
