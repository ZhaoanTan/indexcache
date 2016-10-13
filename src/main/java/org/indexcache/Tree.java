package org.indexcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Tree<V> {
    private TreeNode<V> root = new TreeNode<>();
    private int deep;
    private static final Object NULL_KEY = new Object();

    public Tree(int deep) {
        this.deep = deep;
    }

    public V get(Object[] keys) {
        checkKeyLength(keys);
        TreeNode<V> ptr = root;
        for (Object key : keys) {
            ptr = ptr.getChildren().get(maskNull(key));
            if (ptr == null) {
                return null;
            }
        }
        return ptr.getValue();
    }

    public V put(Object[] keys, V value) {
        checkKeyLength(keys);
        TreeNode<V> ptr = root;
        for (int i = 0; i < deep - 1; i++) {
            ptr = ptr.putIfAbsent(maskNull(keys[i]));
        }
        return ptr.put(maskNull(keys[deep - 1]), value);
    }

    public V remove(Object[] keys) {
        checkKeyLength(keys);
        TreeNode<V> ptr = root;
        for (int i = 0; i < deep - 1; i++) {
            ptr = ptr.getChildren().get(maskNull(keys[i]));
            if (ptr == null) {
                return null;
            }
        }
        ptr = ptr.getChildren().remove(maskNull(keys[deep - 1]));
        if (ptr == null) {
            return null;
        }
        return ptr.getValue();
    }

    private void checkKeyLength(Object[] keys) {
        if (keys.length != deep) {
            throw new RuntimeException("keys length is not match!");
        }
    }

    private Object maskNull(Object key) {
        return (key == null ? NULL_KEY : key);
    }

    private static class TreeNode<V> {
        private Map<Object, TreeNode<V>> children;
        private V value;
        private boolean isValue = false;

        public TreeNode() {
            children = new ConcurrentHashMap<>();
        }

        public TreeNode(V value) {
            this.value = value;
            this.isValue = true;
        }

        public TreeNode<V> putIfAbsent(Object key) {
            if (isValue) {
                throw new RuntimeException("Not Map!");
            }
            return children.putIfAbsent(key, new TreeNode<>());
        }

        public V put(Object key, V value) {
            if (isValue) {
                throw new RuntimeException("Not Map!");
            }
            TreeNode<V> old = children.put(key, new TreeNode<>(value));
            if (old == null) {
                return null;
            }
            return old.getValue();
        }

        public Map<Object, TreeNode<V>> getChildren() {
            return children;
        }

        public V getValue() {
            if (!isValue) {
                throw new RuntimeException("Not Value!");
            }
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
