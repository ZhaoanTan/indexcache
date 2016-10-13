package org.indexcache;

import java.util.*;

public class ReadWriteSortedMap<K, V> extends ReadWriteCollection implements SortedMap<K, V> {
    private SortedMap<K, V> map;

    public static <K, V> SortedMap<K, V> wrap(SortedMap<K, V> map) {
        return new ReadWriteSortedMap<>(map);
    }

    private ReadWriteSortedMap(SortedMap<K, V> map) {
        this.map = map;
    }

    @Override
    public Comparator<? super K> comparator() {
        return map.comparator();
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        try (ScopeLock l = read()) {
            return map.subMap(fromKey, toKey);
        }
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        try (ScopeLock l = read()) {
            return map.headMap(toKey);
        }
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        try (ScopeLock l = read()) {
            return map.tailMap(fromKey);
        }
    }

    @Override
    public K firstKey() {
        try (ScopeLock l = read()) {
            return map.firstKey();
        }
    }

    @Override
    public K lastKey() {
        try (ScopeLock l = read()) {
            return map.lastKey();
        }
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        try (ScopeLock l = read()) {
            return map.containsKey(key);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        try (ScopeLock l = read()) {
            return map.containsValue(value);
        }
    }

    @Override
    public V get(Object key) {
        try (ScopeLock l = read()) {
            return map.get(key);
        }
    }

    @Override
    public V put(K key, V value) {
        try (ScopeLock l = write()) {
            return map.put(key, value);
        }
    }

    @Override
    public V remove(Object key) {
        try (ScopeLock l = write()) {
            return map.remove(key);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        try (ScopeLock l = write()) {
            map.putAll(m);
        }
    }

    @Override
    public void clear() {
        try (ScopeLock l = write()) {
            map.clear();
        }
    }
}
