package com.example.employ;

import org.json.JSONObject;

public class Employee
{
    private int id;
    private String content;
    private long create_date;
    private String created_by;
    private long lastupdated_date;
    private String lastupdated_by;

    public Employee() {
        this.id = 0;
        this.content = null;
        this.create_date = 0;
        this.created_by = null;
        this.lastupdated_date = 0;
        this.lastupdated_by = null;
    }

    public Employee(int id, String content, String created_by) {
        this.id = id;
        this.content = content;
        this.create_date = System.currentTimeMillis();
        this.created_by = created_by;
        this.lastupdated_date = 0;
        this.lastupdated_by = null;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_by() {
        return this.created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public void update(int id, String content, String updated_by){
        this.id = id;
        this.content = content;
        this.lastupdated_by = updated_by;
        this.lastupdated_date = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("content", this.content);
        json.put("create_date", this.create_date + "");
        json.put("created_by", this.created_by);
        json.put("lastupdated_date", this.lastupdated_date + "");
        json.put("lastupdated_by", this.lastupdated_by);
        return json.toString();
    }

    public String getUpdated_by() {
        return this.lastupdated_by;
    }

    public long getCreatedDate() {
        return this.create_date;
    }
}