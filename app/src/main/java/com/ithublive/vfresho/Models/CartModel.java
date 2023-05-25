package com.ithublive.vfresho.Models;

public class CartModel implements Comparable {

    public String id;
    public String name;
    public String price;
    public int quantity;
    public double total;
    public String img_url;

    public CartModel(String id, String name, String price, int quantity, double total, String img_url) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", quantity=" + quantity +
                ", total=" + total +
                ", img_url" + img_url +
                '}';
    }

    @Override
    public int compareTo( Object o) {
        return   name.compareTo(((CartModel)o).name);
        // return  2;
    }
}