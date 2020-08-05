package com.example.boostitvendor.Models;

public class orderdet {
    public String amount;
    public String ord_id,staff_id,cust_name,mob,staff_name,staff_lname,status;
    public int paytype;
    public Boolean isdel,settled;
    public orderdet(String ord_id,String staff_id,String cust_name,String mob,
                    String staff_name,String staff_lname,String status,String amount,
                    int paytype,Boolean isdel,Boolean settled)
    {
        this.amount = amount;
        this.cust_name = cust_name;
        this.isdel = isdel;
        this.mob = mob;
        this.ord_id = ord_id;
        this.paytype = paytype;
        this.settled = settled;
        this.staff_lname = staff_lname;
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.status = status;

    }

    public String get_amount(){ return  amount;}
    public String get_cust_name(){ return cust_name;}
    public String get_mob(){ return mob;}
    public String get_ord_id(){ return ord_id;}
    public String get_staff_lname(){ return staff_lname;}
    public String get_staff_name(){return staff_name;}
    public String get_status(){return status;}
    public String get_staff_id(){return staff_id;}
    public int get_paytype(){ return paytype;}
    public Boolean get_isdel(){ return isdel; }
    public Boolean get_settled(){ return settled;}
}
