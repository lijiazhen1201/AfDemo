package cn.appoa.afdemo.bean;

import java.io.Serializable;


public class GoodsCategoryList implements Serializable{

    public int ID;
    public String Name;

    public GoodsCategoryList() {
    }

    public GoodsCategoryList(int ID, String name) {
        this.ID = ID;
        Name = name;
    }

}
