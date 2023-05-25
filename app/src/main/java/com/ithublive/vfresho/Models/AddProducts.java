package com.ithublive.vfresho.Models;

public class AddProducts {

    public String id;
    public String name;
    public String price;
    public String old_price;
    public String percent_discount;
    public String order_in_list;
    public String food_category;
    public String img_url;

    public AddProducts() {}

    public AddProducts(String id, String name, String price, String old_price, String percent_discount, String food_category, String img_url){
        this.id = id;
        this.name = name;
        this.price = price;
        this.old_price = old_price;
        this.percent_discount = percent_discount;
        this.food_category = food_category;
        this.img_url = img_url;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getPercent_discount() {
        return percent_discount;
    }

    public void setPercent_discount(String percent_discount) {
        this.percent_discount = percent_discount;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getFood_category() {
        return food_category;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setFood_category(String food_category) {
        this.food_category = food_category;
    }

    public String getOrder_in_list() {
        return order_in_list;
    }

    public void setOrder_in_list(String order_in_list) {
        this.order_in_list = order_in_list;
    }
}
