package com.lrchao.utils;

import java.util.LinkedList;

/**
 * Description: 有序不重复的集合
 *
 * @author lrc19860926@gmail.com
 * @date 2017/3/9 上午11:03
 */

public final class SetList<T> extends LinkedList<T> {
    private static final long serialVersionUID = 1434324234L;

    @Override
    public boolean add(T object) {
        if (size() == 0) {
            return super.add(object);
        } else {
            int count = 0;
            for (T t : this) {
                if (t.equals(object)) {
                    count++;
                    break;
                }
            }
            if (count == 0) {
                return super.add(object);
            } else {
                return false;
            }
        }
    }
}
