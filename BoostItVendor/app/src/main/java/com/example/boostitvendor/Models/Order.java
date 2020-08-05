package com.example.boostitvendor.Models;

public class Order {

    private String ord_id, status, name, mob, add,slot;
    private Boolean delivery;

    public Order(String ord_id, String status, String name, String mob, String add, Boolean delivery,String slot) {
        this.ord_id = ord_id;
        this.status = status;
        this.name = name;
        this.mob = mob;
        this.add = add;
        this.delivery = delivery;
        this.slot = slot;
    }

    public String getOrd_id() {
        return ord_id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getMob() {
        return mob;
    }

    public String getAdd() {
        return add;
    }

    public Boolean getDel() {
        return delivery;
    }

    public String getSlot(){ return slot; }
}

