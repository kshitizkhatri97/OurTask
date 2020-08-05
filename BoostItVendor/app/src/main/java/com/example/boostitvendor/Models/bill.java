package com.example.boostitvendor.Models;

public class bill {
    Double amount,trans_value;
    int cust_id,ord_id,paytype,staff_id;
    Boolean isdel;
    String cust_name,date;


    public bill(int cust_id,int ord_id,int staff_id,int paytype,String cust_name,String date,Boolean isdel,Double amount,
                Double trans_value){
        this.amount = amount;
        this.cust_id = cust_id;
        this.ord_id = ord_id;
        this.staff_id = staff_id;
        this.cust_name = cust_name;
        this.date = date;
        this.isdel = isdel;
        this.paytype = paytype;
        this.trans_value = trans_value;
    }
    public int get_cust_id(){ return cust_id;}
    public int get_ord_id(){ return ord_id;}
    public int get_staff_id(){ return staff_id;}
    public String get_cust_name(){ return cust_name;}
    public String get_date(){return date;}
    public int get_paytype(){ return paytype;}
    public Boolean get_isdel(){ return isdel;}
    public Double get_amount(){ return amount;}
    public Double get_trans_value(){return trans_value; }

}
