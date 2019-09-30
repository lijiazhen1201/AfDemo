package cn.appoa.afdemo.bean;

import java.io.Serializable;
import java.util.List;


public class GoodsSpecItemList implements Serializable{

    public int ID;
    public String Name;
    public List<GoodsSpecItemChildList> ChildList;
}
