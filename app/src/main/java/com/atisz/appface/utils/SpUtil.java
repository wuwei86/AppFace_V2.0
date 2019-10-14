package com.atisz.appface.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
	private static SharedPreferences sp;
	/**
	 * 写入boolean变量至sp�?
	 * @param ctx	上下文环�?
	 * @param key	存储节点名称
	 * @param value	存储节点的�? boolean
	 */
	public static void putBoolean(Context ctx,String key,boolean value){
		//(存储节点文件名称,读写方式)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	/**
	 * 读取boolean标示从sp�?
	 * @param ctx	上下文环�?
	 * @param key	存储节点名称
	 * @param defValue	没有此节点默认�?
	 * @return		默认值或者此节点读取到的结果
	 */
	public static boolean getBoolean(Context ctx,String key,boolean defValue){
		//(存储节点文件名称,读写方式)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * 写入boolean变量至sp�?
	 * @param ctx	上下文环�?
	 * @param key	存储节点名称
	 * @param value	存储节点的�?string
	 */
	public static void putString(Context ctx,String key,String value){
		//(存储节点文件名称,读写方式)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	/**
	 * 读取boolean标示从sp�?
	 * @param ctx	上下文环�?
	 * @param key	存储节点名称
	 * @param defValue	没有此节点默认�?
	 * @return		默认值或者此节点读取到的结果
	 */
	public static String getString(Context ctx,String key,String defValue){
		//(存储节点文件名称,读写方式)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
	/**
	 * 从sp中移除指定节�?
	 * @param ctx	上下文环�?
	 * @param key	�?��移除节点的名�?
	 */
	public static void remove(Context ctx, String key) {
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().remove(key).commit();
	}
}
