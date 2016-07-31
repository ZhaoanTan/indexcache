package org.indexcache;

import java.util.*;
import java.util.function.Function;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public class BaseIcMap<Entity, Field> implements Primary<Entity, Field>, IcMap<Entity, Field> {
    private SortedMap<Field, Entity> map;
    private Function<Entity, Field> fieldGetter;
    private Comparator<Field> fieldComparator;
    private IcTable table;

    public BaseIcMap(Function<Entity, Field> fieldGetter) {
        this.fieldGetter = fieldGetter;
        map = Collections.synchronizedSortedMap(new TreeMap<>());
    }

    public BaseIcMap(Function<Entity, Field> fieldGetter, Comparator<Field> fieldComparator) {
        this.fieldGetter = fieldGetter;
        this.fieldComparator = fieldComparator;
        map = Collections.synchronizedSortedMap(new TreeMap<>(fieldComparator));
    }

    @Override
    public Entity get(Field field) {
        if (field == null) {
            return null;
        }
        return map.get(field);
    }

    @Override
    public Function<Entity, Field> fieldGetter() {
        return this.fieldGetter;
    }

    @Override
    public void build(Collection<Entity> entities) {
        SortedMap<Field, Entity> mapLocal;

        if (fieldComparator == null) {
            mapLocal = Collections.synchronizedSortedMap(new TreeMap<>());
        } else {
            mapLocal = Collections.synchronizedSortedMap(new TreeMap<>(fieldComparator));
        }
        if (entities != null) {
            for (Entity entity : entities) {
                Field k = fieldGetter.apply(entity);
                if (entity == null || k == null) {
                    continue;
                }
                mapLocal.put(k, entity);
            }
        }
        this.map = mapLocal;
    }

    @Override
    public synchronized boolean remove(Entity entity) {
        if (entity == null) {
            return false;
        }
        Field k = fieldGetter.apply(entity);
        if (k == null) {
            return false;
        }
        Entity old = this.map.get(k);
        if (old == entity) {
            old = this.map.remove(k);
            return old != null;
        }
        return false;
    }

    @Override
    public Entity put(Entity entity) {
        if (entity == null || fieldGetter.apply(entity) == null) {
            return null;
        }
        return map.put(fieldGetter.apply(entity), entity);
    }

    @Override
    public List<Entity> range(Field start, Field end) {
        SortedMap<Field, Entity> subMap = map.subMap(start, end);
        List<Entity> l = new ArrayList<>();
        for (Map.Entry<Field, Entity> e : subMap.entrySet()) {
            l.add(e.getValue());
        }
        return l;
    }

    @Override
    public boolean add(Entity entity) {
        put(entity);
        return true;
    }

    @Override
    public void setTable(IcTable table) {
        this.table = table;
    }
}
