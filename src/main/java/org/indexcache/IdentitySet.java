package org.indexcache;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

public class IdentitySet<E> implements Set<E> {
    public IdentityHashMap<E, Object> m = new IdentityHashMap<>();
    private static final Object mapValue = new Object();

    public IdentitySet() {
    }

    public IdentitySet(Collection<E> c) {
        this.addAll(c);
    }

    @Override
    public int size() {
        return m.size();
    }

    @Override
    public boolean isEmpty() {
        return m.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return m.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return m.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        return m.keySet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return m.keySet().toArray(a);
    }

    @Override
    public boolean add(E e) {
        return m.put(e, mapValue) == null;
    }

    @Override
    public boolean remove(Object o) {
        return m.remove(o) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return m.keySet().containsAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return m.keySet().retainAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed = add(e);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed = remove(o);
        }
        return changed;
    }

    @Override
    public void clear() {
        m.clear();
    }
}
