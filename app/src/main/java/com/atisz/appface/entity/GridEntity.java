package com.atisz.appface.entity;

import com.atisz.appface.entity.base.BaseEntity;

/**
 * Created by wuwei on 2019/2/21.
 */

public class GridEntity extends BaseEntity{
    private String name;
    private int id;


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
