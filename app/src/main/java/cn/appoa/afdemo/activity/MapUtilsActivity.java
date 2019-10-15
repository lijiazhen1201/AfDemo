package cn.appoa.afdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.dialog.MapNavigationDialog;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;

/**
 * 地图导航工具类
 */
public class MapUtilsActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("MapUtils")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_map_utils);
    }

    private EditText et_lat;
    private EditText et_lng;
    private EditText et_address;
    private Button btn_map_navigation;

    @Override
    public void initView() {
        super.initView();
        et_lat = (EditText) findViewById(R.id.et_lat);
        et_lng = (EditText) findViewById(R.id.et_lng);
        et_address = (EditText) findViewById(R.id.et_address);
        btn_map_navigation = (Button) findViewById(R.id.btn_map_navigation);
        btn_map_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });
    }

    private MapNavigationDialog dialogNavigation;

    /**
     * 显示地图选择
     */
    private void showMap() {
        if (AtyUtils.isTextEmpty(et_lat)) {
            AtyUtils.showShort(mActivity, et_lat.getHint(), false);
            return;
        }
        if (AtyUtils.isTextEmpty(et_lng)) {
            AtyUtils.showShort(mActivity, et_lng.getHint(), false);
            return;
        }
        if (AtyUtils.isTextEmpty(et_address)) {
            AtyUtils.showShort(mActivity, et_address.getHint(), false);
            return;
        }
        double dlat = Double.parseDouble(AtyUtils.getText(et_lat));
        double dlon = Double.parseDouble(AtyUtils.getText(et_lng));
        String dname = AtyUtils.getText(et_address);
        if (dialogNavigation == null) {
            dialogNavigation = new MapNavigationDialog(mActivity);
        }
        dialogNavigation.showMapNavigationDialog(
                0, 0, null,//选填
                dlat, dlon, dname//必填
        );
    }

    @Override
    public void initData() {

    }
}
