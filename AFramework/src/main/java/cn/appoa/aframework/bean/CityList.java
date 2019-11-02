package cn.appoa.aframework.bean;

import java.io.Serializable;
import java.util.List;

public class CityList implements Serializable {

    public String area_code;
    public String area_name;
    public int area_type;
    public String geo;
    public List<DistrictList> sub;
	public boolean isSelected;

    public CityList() {
        super();
    }

    public CityList(String area_code, String area_name) {
        this.area_code = area_code;
        this.area_name = area_name;
    }
}
