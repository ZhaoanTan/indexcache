package org.indexcache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ReadWriteSet<E> extends ReadWriteCollection implements Set<E> {
    private Set<E> set;

    public static <E> Set<E> wrap(Set<E> s) {
        return new ReadWriteSet<>(s);
    }

    private ReadWriteSet(Set<E> set) {
        this.set = set;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        try (ScopeLock l = read()) {
            return set.contains(o);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        try (ScopeLock l = read()) {
            return set.toArray();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        try (ScopeLock l = read()) {
            return set.toArray(a);
        }
    }

    @Override
    public boolean add(E e) {
        try (ScopeLock l = write()) {
            return set.add(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        try (ScopeLock l = write()) {
            return set.remove(o);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        try (ScopeLock l = read()) {
            return set.containsAll(c);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        try (ScopeLock l = write()) {
            return set.addAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        try (ScopeLock l = read()) {
            return set.retainAll(c);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        try (ScopeLock l = write()) {
            return set.removeAll(c);
        }
    }

    @Override
    public void clear() {
        try (ScopeLock l = write()) {
            set.clear();
        }
    }

}
