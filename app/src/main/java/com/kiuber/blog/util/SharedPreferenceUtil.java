package com.kiuber.blog.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Kiuber on 2016/12/29.
 */

public class SharedPreferenceUtil {

    /**
     * 存一个
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param key      键
     * @param value    值
     * @return 键或值为空返回失败
     */
    public static boolean putOne(Context context, String fileName
            , String key, String value) {
        if (key != null && value != null) {
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit()
                    .putString(key, value).apply();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取一个
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param key      键
     * @return 文件不存在返回失败
     */
    public static String getOne(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (preferences != null) {
            String string = preferences.getString(key, "");
            return string;
        } else {
            return null;
        }
    }

    /**
     * 存多个
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param keys     键集合
     * @param values   值集合
     * @return 键值集合大小不相等返回失败
     */
    public static boolean putMultiple(Context context, String fileName
            , ArrayList<String> keys, ArrayList<String> values) {
        if (keys != null && values != null) {
            int keySize = keys.size();
            int valueSize = values.size();
            if (keySize == valueSize) {
                SharedPreferences.Editor edit = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
                for (int i = 0; i < keySize; i++) {
                    edit.putString(keys.get(i), values.get(i));
                }
                edit.apply();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 取多个
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param keys     键结合
     * @return 返回值集合，文件不存在返回null
     */
    public static ArrayList<String> getMultiple(Context context, String fileName, ArrayList<String> keys) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (preferences != null) {
            ArrayList<String> values = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                values.add(preferences.getString(keys.get(i), ""));
            }
            return values;
        } else {
            return null;
        }
    }
}
