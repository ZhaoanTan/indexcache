package org.indexcache;

import java.util.*;
import java.util.function.Function;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public class BaseIcSets<Entity, Field> implements SetsIndex<Entity, Field>, IcSets<Entity, Field> {
    private SortedMap<Field, Set<Entity>> sets = ReadWriteSortedMap.wrap(new TreeMap<>());
    private Function<Entity, Field> fieldGetter;
    private IcTable table;
    private Comparator<Field> fieldComparator;

    public BaseIcSets(Function<Entity, Field> fieldGetter, Comparator<Field> fieldComparator) {
        this.fieldGetter = fieldGetter;
        this.fieldComparator = fieldComparator;
    }

    public BaseIcSets(Function<Entity, Field> fieldGetter) {
        this.fieldGetter = fieldGetter;
    }

    @Override
    public void build(Collection<Entity> entities) {
        SortedMap<Field, Set<Entity>> setsLocal;

        if (fieldComparator == null) {
            setsLocal = ReadWriteSortedMap.wrap(new TreeMap<>());
        } else {
            setsLocal = ReadWriteSortedMap.wrap(new TreeMap<>(fieldComparator));
        }
        if (entities != null) {
            for (Entity entity : entities) {
                if (entity == null) {
                    continue;
                }
                Field k = fieldGetter.apply(entity);
                if (k == null) {
                    continue;
                }
                setsLocal.putIfAbsent(k, ReadWriteSet.wrap(new IdentitySet<>()));
                setsLocal.get(k).add(entity);
            }
        }
        this.sets = setsLocal;
    }

    @Override
    public boolean remove(Entity entity) {
        if (entity == null) {
            return false;
        }
        Field k = fieldGetter.apply(entity);
        if (k == null) {
            return false;
        }
        Set<Entity> set = sets.get(k);
        if (set == null) {
            return false;
        }
        return set.remove(entity);
    }

    @Override
    public boolean add(Entity entity) {
        if (entity == null) {
            return false;
        }
        Field k = fieldGetter.apply(entity);
        if (k == null) {
            return false;
        }
        sets.putIfAbsent(k, Collections.synchronizedSet(new IdentitySet<>()));
        sets.get(k).add(entity);
        return true;
    }

    public Set<Entity> inter(Collection<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            return new IdentitySet<>();
        }
        Set<Set<Entity>> ss = new IdentitySet<>();
        for (Field k : fields) {
            Set<Entity> s = sets.get(k);
            if (s == null) {
                continue;
            }
            ss.add(s);
        }
        return SetUtil.inter(ss);
    }

    @Override
    public Set<Entity> inter(Field... fields) {
        return inter(Arrays.asList(fields));
    }

    public Set<Entity> union(Collection<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            return new IdentitySet<>();
        }
        Set<Set<Entity>> ss = new IdentitySet<>();
        for (Field k : fields) {
            Set<Entity> s = sets.get(k);
            if (s == null) {
                continue;
            }
            ss.add(s);
        }
        return SetUtil.union(ss);
    }

    @Override
    public Set<Entity> union(Field... fields) {
        return union(Arrays.asList(fields));
    }

    @Override
    public Set<Entity> allExclude(Collection<Field> fields) {
        Set<Entity> setAll = new IdentitySet<>(table.values());
        if (fields == null || fields.isEmpty()) {
            return setAll;
        }
        for (Field k : fields) {
            Set<Entity> s = sets.get(k);
            if (s == null || s.isEmpty()) {
                continue;
            }
            setAll.removeAll(s);
        }
        return setAll;
    }

    @Override
    public List<Entity> range(Field start, Field end) {
        SortedMap<Field, Set<Entity>> subMap = sets.subMap(start, end);
        List<Entity> l = new ArrayList<>();
        for (SortedMap.Entry<Field, Set<Entity>> e : subMap.entrySet()) {
            l.addAll(e.getValue());
        }
        return l;
    }

    @Override
    public void setTable(IcTable table) {
        this.table = table;
    }

    @Override
    public Function<Entity, Field> fieldGetter() {
        return this.fieldGetter;
    }
}
