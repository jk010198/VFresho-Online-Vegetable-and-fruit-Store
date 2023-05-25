package com.ithublive.vfresho.Models;

public class OrdersModel {

    public String id;
    public String name;
    public String mobileno;
    public String email_id;
    public String orderid;
    public String address;
    public String orderlist;
    public String grandtotal;
    public String remark;

    public OrdersModel() {
    }

    public OrdersModel(String id, String name, String mobileno, String email_id, String orderid, String address, String orderlist,
                       String grandtotal, String remark) {
        this.id = id;
        this.name = name;
        this.mobileno = mobileno;
        this.email_id = email_id;
        this.orderid = orderid;
        this.address = address;
        this.orderlist = orderlist;
        this.grandtotal = grandtotal;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderlist() {
        return orderlist;
    }

    public void setOrderlist(String orderlist) {
        this.orderlist = orderlist;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
