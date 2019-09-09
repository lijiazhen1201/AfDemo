package cn.appoa.aframework.bean;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.appoa.aframework.utils.AtyUtils;

/**
 * 百度地图全国的省市区
 */
public class BMapApi implements Serializable {

    // http://map.baidu.com/?from=mapapi&qt=sub_area_list&areacode=1&level=3
    public CountryList content;
    public CurrentCityBean current_city;
    public String err_msg;
    public List<String> hot_city;
    public Object psrs;
    public ResultBean result;
    public Object suggest_query;
    public int uii_err;

    public static class CurrentCityBean implements Serializable {

        public int code;
        public String geo;
        public int level;
        public String name;
        public int sup;
        public int sup_bus;
        public int sup_business_area;
        public int sup_lukuang;
        public int sup_subway;
        public int type;
        public String up_province_name;
    }

    public static class ResultBean implements Serializable {

        public int error;
    }

    /**
     * 获取省市区
     *
     * @param context
     * @return
     */
    public static List<ProvinceList> getProvinceList(Context context) {
        List<ProvinceList> list = new ArrayList<ProvinceList>();
        String json = AtyUtils.getJsonString(context, "region.json");
        if (!TextUtils.isEmpty(json)) {
            BMapApi data = JSON.parseObject(json, BMapApi.class);
            if (data.content != null) {
                list.addAll(data.content.sub);
            }
        }
        return list;
    }

}
