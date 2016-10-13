package org.indexcache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public class IcTable<Entity, PrimaryKey, DataSrc> {
    private DataSrc dataSrc;
    private Function<DataSrc, Collection<Entity>> dataGetter;
    private Primary<Entity, PrimaryKey> primary = null;
    private Map<Function, Index> indexes = new HashMap<>();
    private Collection<Entity> data;

    public IcTable(DataSrc dataSrc, Function<DataSrc, Collection<Entity>> dataGetter) {
        this.dataSrc = dataSrc;
        this.dataGetter = dataGetter;
    }

    public void build() {
        Collection<Entity> data = dataGetter.apply(dataSrc);
        if (primary != null) {
            primary.build(data);
        }
        for (Index i : indexes.values()) {
            i.build(data);
        }
        this.data = data;
    }

    public void primary(Primary<Entity, PrimaryKey> primary) {
        this.primary = primary;
    }

    public void index(Index index) {
        if (index == null) {
            throw new RuntimeException("index can not be null.");
        }
        indexes.put(index.fieldGetter(), index);
    }

    public Collection<Entity> values() {
        return data;
    }

    public Entity put(Entity entity) {
        data.add(entity);
        Entity old = null;
        if (this.primary != null) {
            old = primary.put(entity);
        }
        for (Index index : indexes.values()) {
            index.remove(old);
            index.add(entity);
        }
        return old;
    }

    public void remove(Entity entity) {
        data.remove(entity);
        primary.remove(entity);
        for (Index index : indexes.values()) {
            index.remove(entity);
        }
    }

    public IcSortedMap<Entity, PrimaryKey> getPrimary() {
        return primary;
    }

    public <Key> IcSets<Entity, Key> getIndex(Function<Entity, Key> keyGetter) {
        Index index = indexes.get(keyGetter);
        if (index instanceof IcSets) {
            return (IcSets<Entity, Key>) indexes.get(keyGetter);
        }
        return null;
    }
}
