/*
 * Copyright (C) 2010 mAPPn.Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lrchao.utils;

/**
 * Description: Container to ease passing around a tuple of two objects. This object provides a sensible
 * implementation of equals(), returning true if equals() is true on each of the contained
 * objects.
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/17 上午9:36
 */
public class Pair<F, S> {
    public final F mFirst;
    public final S mSecond;

    /**
     * Constructor for a Pair. If either are null then equals() and hashCode() will throw
     * a NullPointerException.
     *
     * @param first  the mFirst object in the Pair
     * @param second the mSecond object in the pair
     */
    public Pair(F first, S second) {
        this.mFirst = first;
        this.mSecond = second;
    }

    /**
     * Convenience method for creating an appropriately typed pair.
     *
     * @param a the mFirst object in the Pair
     * @param b the mSecond object in the pair
     * @return a Pair that is templatized with the types of a and b
     */
    public static <A, B> Pair<A, B> create(A a, B b) {
        return new Pair<>(a, b);
    }

    @Override
    public int hashCode() {
        int result = mFirst.hashCode();
        result = 31 * result + mSecond.hashCode();
        return result;
    }

    /**
     * Checks the two objects for equality by delegating to their respective equals() methods.
     *
     * @param o the Pair to which this one is to be checked for equality
     * @return true if the underlying objects of the Pair are both considered equals()
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        final Pair<F, S> other;
        try {
            other = (Pair<F, S>) o;
        } catch (ClassCastException e) {
            return false;
        }
        return mFirst.equals(other.mFirst) && mSecond.equals(other.mSecond);
    }


}