package cn.appoa.afdemo.bean;

import java.io.Serializable;



public class GoodsTypeList implements Serializable{

    public int ID;
    public String Name;

    public GoodsTypeList() {
    }

    public GoodsTypeList(int ID, String name) {
        this.ID = ID;
        Name = name;
    }

}
