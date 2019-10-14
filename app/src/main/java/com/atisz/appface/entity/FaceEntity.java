package com.atisz.appface.entity;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 *
 * @author wuwei
 * @date 2018/9/5
 */

public class FaceEntity extends BmobObject{
    private String name;
    private String phone;
    private String localdataUrl;
    private String localimgUrl;
    private String bmobdataUrl;
    private String bmobimgUrl;
    private String createTime;
    private BmobFile dataFile;
    private BmobFile imgFile;


    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocaldataUrl() {
        return localdataUrl;
    }

    public String getLocalimgUrl() {
        return localimgUrl;
    }

    public String getBmobdataUrl() {
        return bmobdataUrl;
    }

    public String getBmobimgUrl() {
        return bmobimgUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public BmobFile getDataFile() {
        return dataFile;
    }

    public BmobFile getImgFile() {
        return imgFile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocaldataUrl(String localdataUrl) {
        this.localdataUrl = localdataUrl;
    }

    public void setLocalimgUrl(String localimgUrl) {
        this.localimgUrl = localimgUrl;
    }

    public void setBmobdataUrl(String bmobdataUrl) {
        this.bmobdataUrl = bmobdataUrl;
    }

    public void setBmobimgUrl(String bmobimgUrl) {
        this.bmobimgUrl = bmobimgUrl;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDataFile(BmobFile dataFile) {
        this.dataFile = dataFile;
    }

    public void setImgFile(BmobFile imgFile) {
        this.imgFile = imgFile;
    }
}
