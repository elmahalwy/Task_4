package com.example.elkholy.task;

/**
 * Created by elkholy on 16/02/2017.
 */

public class Items {
    private int id;
    private String name, url, status;

    public Items(int id, String name, String status, String url) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
