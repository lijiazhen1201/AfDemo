package cn.appoa.aframework.bean;

import java.io.Serializable;

public class DistrictList implements Serializable {

    public String area_code;
    public String area_name;
    public int area_type;
    public String geo;
    public int sup_business_area;
	public boolean isSelected;

    public DistrictList() {
    }

    public DistrictList(String area_code, String area_name) {
        this.area_code = area_code;
        this.area_name = area_name;
    }
}
