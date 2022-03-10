package com.growfxtrade.model;

import java.io.Serializable;

public class ViewServiceModel implements Serializable {

    String id="";
    String user_id="";
    String date="";
    String type="";
    String amount="";
    String name="";
    String updated_at="";
    String created_at="";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
