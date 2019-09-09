package cn.appoa.aframework.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 国家
 */
public class CountryList implements Serializable {

    public String area_code;
    public String area_name;
    public int area_type;
    public String geo;
    public List<ProvinceList> sub;

    public CountryList() {
    }

    public CountryList(String area_code, String area_name) {
        this.area_code = area_code;
        this.area_name = area_name;
    }
}
