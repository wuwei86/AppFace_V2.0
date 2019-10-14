package com.atisz.appface.entity;

import com.atisz.appface.entity.base.BaseEntity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import cn.bmob.v3.BmobObject;

/**
 *
 * @author wuwei
 * @date 2018/10/19
 */
@Table(name = "CardListEntity")
public class CardListEntity extends BaseEntity{
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;


    @Column(name = "cardName")
    private String cardName;

    @Column(name = "cardNum")
    private String cardNum;

    @Column(name = "createTime")
    private String createTime;

    public CardListEntity(){

    }

    public CardListEntity(String cardname,String cardnum,String createtime){
        this.cardName = cardname;
        this.cardNum = cardnum;
        this.createTime = createtime;
    }


    public int getId() {
        return id;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getCreateTime() {
        return createTime;
    }



    public void setId(int id) {
        this.id = id;
    }


    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
