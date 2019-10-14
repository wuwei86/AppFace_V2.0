package com.atisz.appface.utils;

import com.atisz.appface.AppContext;
import com.atisz.appface.entity.CardListEntity;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 *
 * @author wuwei
 * @date 2018/10/21
 */

public class DbUtil {

    /**
     * 保存卡数据
     * 先去查找数据库中是否有相同的名称，如果有就替换，没有就直接保存
     * @param cardListEntity
     * 1:名字不相同
     * 2:名字相同，卡号不相同
     * 3:名字相同，卡号相同
     * 0:默认
     */
    public static int cardDbSave(CardListEntity cardListEntity){

        final DbManager cardDbManager = AppContext.getInstance().getCardDbManager();
        try {
            final List<CardListEntity> dbManagerAll = cardDbManager.findAll(CardListEntity.class);
            for (int i=0;i<dbManagerAll.size();i++){
                if (cardListEntity.getCardName().equals(dbManagerAll.get(i).getCardName())){
                    //名字相同，判断卡号是否相同
                    if (cardListEntity.getCardNum().equals(dbManagerAll.get(i).getCardNum())){
                        //名字和卡号都相同则不写数据库
                        return 3;
                    } else {
                        //名字相同卡号不相同，只写卡号
                        cardListEntity.setId(dbManagerAll.get(i).getId());
                        cardDbManager.update(cardListEntity, "cardName","cardNum","createTime");
                        return 2;
                    }
                }
            }
            //名字不相同写数据库
            cardDbManager.save(cardListEntity);
            return 1;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新整张表
     * @param cardList
     * @return
     */
    public static int cardDbSave(List<CardListEntity> cardList){
        final DbManager cardDbManager = AppContext.getInstance().getCardDbManager();
        try {
            final List<CardListEntity> dbManagerAll = cardDbManager.findAll(CardListEntity.class);
            for (int i=0;i<dbManagerAll.size();i++){
                if (cardList.get(i).getCardName().equals(dbManagerAll.get(i).getCardName())){
                    //名字相同，判断卡号是否相同
                    if (cardList.get(i).getCardNum().equals(dbManagerAll.get(i).getCardNum())){
                        //名字和卡号都相同则不写数据库
                        return 3;
                    } else {
                        //名字相同卡号不相同，只写卡号
                        cardList.get(i).setId(dbManagerAll.get(i).getId());
                        cardDbManager.update(cardList.get(i), "cardName","cardNum","createTime");
                        return 2;
                    }
                } else {
                    //名字不相同写数据库
                    cardDbManager.save(cardList.get(i));
                }
            }
            return 1;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取所有卡数据
     */
    public static List<CardListEntity> cardDbGetAll(){
        final DbManager cardDbManager = AppContext.getInstance().getCardDbManager();
        List<CardListEntity> dbManagerAll = null;
        try {
            dbManagerAll = cardDbManager.findAll(CardListEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return dbManagerAll;
    }

    /**
     * 通过ID来删除数据
     * @param id
     */
    public static void cardDbDel(int id){
        final DbManager cardDbManager = AppContext.getInstance().getCardDbManager();
        try {
            cardDbManager.deleteById(CardListEntity.class,id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过名字来删除
     * @param cardListEntity
     */
    public static void cardDbDel(CardListEntity cardListEntity){
        final DbManager cardDbManager = AppContext.getInstance().getCardDbManager();
        try {
            cardDbManager.delete(CardListEntity.class,WhereBuilder.b("cardName","=",cardListEntity.getCardName()));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
