package com.example.boostitvendor.Models;

import java.util.List;

public class allord_date {
    public String date;
    public List<orderdet> orders;
    public allord_date(String date,List<orderdet> orders){
        this.date = date;
        this.orders = orders;
    }
    public String get_date(){return date;}
    public List<orderdet> get_orders(){return orders;}

}
