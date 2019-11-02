package cn.appoa.aframework.bean;

import java.io.Serializable;
import java.util.List;

public class ProvinceList implements Serializable {

    public String area_code;
    public String area_name;
    public int area_type;
    public String geo;
    public List<CityList> sub;
	public boolean isSelected;

    public ProvinceList() {
    }

    public ProvinceList(String area_code, String area_name) {
        this.area_code = area_code;
        this.area_name = area_name;
    }
}
