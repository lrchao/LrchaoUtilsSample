package com.lrchao.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Description: Key:String Value: Set的集合
 *
 * @author lrc19860926@gmail.com
 * @date 2017/3/9 上午11:06
 */

public final class SetMap<T> {

    private HashMap<String, Set<T>> mDataSource = new HashMap<>();

    /**
     * 添加数据到集合
     *
     * @param key 唯一
     * @param t   value集合里的一个元素
     */
    public void put(String key, T t) {

        Set<T> valueSet = mDataSource.get(key);

        if (valueSet == null) {
            valueSet = new HashSet<>();
            valueSet.add(t);
            mDataSource.put(key, valueSet);
        } else {
            valueSet.add(t);
        }
    }

    /**
     * 获取Value的集合
     *
     * @param key Key
     */
    public Set<T> getValueSet(String key) {
        return mDataSource.get(key);
    }

    /**
     * 清空数据
     */
    public void clear() {

        for (Set<T> set : mDataSource.values()) {
            set.clear();
        }
        mDataSource.clear();
    }

}
