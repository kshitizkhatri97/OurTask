package com.example.boostitvendor.Models;

public class item {
    public String itemid,itemname,shopid;
    public Double qty,price;
    public Boolean enable;
    public item(String itemid,String itemname, Double qty,Double price,Boolean enable,String shopid){
        this.itemid = itemid;
        this.itemname = itemname;
        this.qty = qty;
        this.price = price;
        this.enable = enable;
        this.shopid = shopid;
    }

    public String get_id(){ return itemid;}
    public String get_name(){ return itemname;}
    public Double get_qty(){return qty;}
    public Double get_price(){return price;}
    public Boolean get_enable(){ return enable;}
    public String get_shopid(){ return shopid;}

}
