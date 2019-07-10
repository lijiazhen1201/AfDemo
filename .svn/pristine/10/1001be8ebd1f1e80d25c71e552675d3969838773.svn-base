package cn.appoa.aframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.appoa.aframework.R;
import cn.appoa.aframework.utils.MapUtils;


public class MapNavigationDialog extends BaseDialog {

    public MapNavigationDialog(Context context) {
        super(context);
    }

    private TextView tv_baidu_map;
    private TextView tv_gaode_map;
    private TextView tv_qq_map;
    private TextView tv_dialog_cancel;

    @Override
    public Dialog initDialog(Context context) {
        View view = View.inflate(context, R.layout.dialog_map_navigation, null);
        tv_baidu_map = (TextView) view.findViewById(R.id.tv_baidu_map);
        tv_gaode_map = (TextView) view.findViewById(R.id.tv_gaode_map);
        tv_qq_map = (TextView) view.findViewById(R.id.tv_qq_map);
        tv_dialog_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        tv_baidu_map.setOnClickListener(this);
        tv_gaode_map.setOnClickListener(this);
        tv_qq_map.setOnClickListener(this);
        tv_dialog_cancel.setOnClickListener(this);
        return initBottomInputMethodDialog(view, context);
    }

    private double slat;
    private double slon;
    private String sname;
    private double dlat;
    private double dlon;
    private String dname;

    /**
     * 显示弹窗(默认百度坐标)
     *
     * @param slat  起点纬度 可不填
     * @param slon  起点经度 可不填
     * @param sname 起点名称 可不填
     * @param dlat  终点纬度 必填
     * @param dlon  终点经度 必填
     * @param dname 终点名称 必填
     */
    public void showMapNavigationDialog(double slat, double slon, String sname,
                                        double dlat, double dlon, String dname) {
        this.slat = slat;
        this.slon = slon;
        this.sname = sname;
        this.dlat = dlat;
        this.dlon = dlon;
        this.dname = dname;
        showDialog();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_baidu_map) {
            if (listener != null) {
                listener.onMapNavigation(1);
            }
            MapUtils.openBaiDuNavi(context, slat, slon, sname, dlat, dlon, dname);
        } else if (id == R.id.tv_gaode_map) {
            if (listener != null) {
                listener.onMapNavigation(2);
            }
            double[] slatlon = MapUtils.baidu2AMap(slat, slon);
            double[] dlatlon = MapUtils.baidu2AMap(dlat, dlon);
            MapUtils.openGaoDeNavi(context, slatlon[0], slatlon[1], sname, dlatlon[0], dlatlon[1], dname);
        } else if (id == R.id.tv_qq_map) {
            if (listener != null) {
                listener.onMapNavigation(3);
            }
            double[] slatlon = MapUtils.bdToGaoDe(slat, slon);
            double[] dlatlon = MapUtils.bdToGaoDe(dlat, dlon);
            MapUtils.openTencentMap(context, slatlon[0], slatlon[1], sname, dlatlon[0], dlatlon[1], dname);
        }
        dismissDialog();
    }

    private OnMapNavigationListener listener;

    public void setOnMapNavigationListener(OnMapNavigationListener listener) {
        this.listener = listener;
    }

    public interface OnMapNavigationListener {

        void onMapNavigation(int type);
    }

}
