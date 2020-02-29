package com.xlu.po;

/**
 * Created by giant on 2017/7/21.
 */

public class Payment {
    private String name;
    public Payment(String name, Integer id, Integer times, Boolean isActive) {
        super();
        this.name = name;
        this.id = id;
        this.times = times;
        this.isActive = isActive;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getTimes() {
        return times;
    }
    public void setTimes(Integer times) {
        this.times = times;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    private Integer id;
    private Integer times = 3;
    private Boolean isActive;
}
