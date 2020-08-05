package com.example.boostitvendor.Models;

public class shop {
    public String name;
    public int id;
    public Boolean servicing;
    public shop(String name,int id,Boolean servicing){
        this.name = name;
        this.id = id;
        this.servicing = servicing;
    }
    public int get_shopid(){ return id;}
    public String get_shopname(){ return name; }
    public Boolean get_shopservicing(){return servicing;}
}
