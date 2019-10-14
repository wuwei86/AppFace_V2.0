package com.atisz.appface.entity;

/**
 *
 * @author wuwei
 * @date 2018/11/9
 */

public class FaceLibEntity {
    private String name;
    private String imgUrl;
    private String dataUrl;

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }
}
