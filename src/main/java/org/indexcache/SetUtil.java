package org.indexcache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public class SetUtil {
    public static <T> Set<T> inter(Collection<Set<T>> sets) {
        if (sets == null || sets.isEmpty() || sets.contains(null)) {
            return new HashSet<T>();
        }
        //get the min set
        Set<T> setMin = null;
        for (Set<T> s : sets) {
            if (setMin == null) {
                setMin = s;
            } else {
                if (setMin.size() > s.size()) {
                    setMin = s;
                }
            }
        }
        if (setMin == null) {
            throw new RuntimeException("setMin can not be null.");
        }
        Set<T> set = new HashSet<>(setMin);
        boolean b = true;
        for (Set s : sets) {
            if (b) {
                b = false;
                continue;
            }
            set.retainAll(s);
        }
        return set;
    }

    @SafeVarargs
    public static <T> Set<T> inter(Set<T>... sets) {
        if (sets == null) {
            return new HashSet<>();
        }
        return inter(Arrays.asList(sets));
    }

    public static <T> Set<T> union(Collection<Set<T>> sets) {
        if (sets == null || sets.isEmpty()) {
            return new HashSet<>();
        }
        Set<T> setAll = new HashSet<>();
        for (Set<T> s : sets) {
            if (s == null || s.isEmpty()) {
                continue;
            }
            setAll.addAll(s);
        }
        return setAll;
    }

    @SafeVarargs
    public static <T> Set<T> union(Set<T>... sets) {
        if (sets == null) {
            return new HashSet<>();
        }
        return union(sets);
    }
}
