package org.indexcache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private boolean isBuilding = false;
    private boolean isAmounting = false;

    public IcTable(DataSrc dataSrc, Function<DataSrc, Collection<Entity>> dataGetter) {
        this.dataSrc = dataSrc;
        this.dataGetter = dataGetter;
    }

    public synchronized void build() {
        isBuilding = true;
        try {
            Collection<Entity> data = dataGetter.apply(dataSrc);
            if (primary != null) {
                primary.build(data);
            }
            for (Index i : indexes.values()) {
                i.build(data);
            }
            while (!queue.isEmpty()) {
                MergeData<Entity> md = queue.poll();
                if (md.mergeType == MergeType.add) {
                    putInner(md.entity, data);
                } else {
                    removeInner(md.entity, data);
                }
            }
            isAmounting = true;
            while (!queue.isEmpty()) {
                MergeData<Entity> md = queue.poll();
                if (md.mergeType == MergeType.add) {
                    putInner(md.entity, data);
                } else {
                    removeInner(md.entity, data);
                }
            }
            this.data = data;
        } finally {
            isAmounting = false;
            isBuilding = false;
        }
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

    private ConcurrentLinkedQueue<MergeData<Entity>> queue = new ConcurrentLinkedQueue<>();

    public Entity put(Entity entity) {
        if (isBuilding && !isAmounting) {
            queue.add(new MergeData<>(entity, MergeType.add));
        }
        while (isAmounting) {
            try {
                Thread.sleep(0, 100);
            } catch (InterruptedException e) {
                throw new RuntimeException("sleep in IcTable.put.");
            }
        }
        return putInner(entity, this.data);
    }

    private Entity putInner(Entity entity, Collection<Entity> data) {
        data.add(entity);
        Entity old = null;
        if (this.primary != null) {
            old = primary.put(entity);
        }
        if (old != null) {
            data.remove(old);
        }
        for (Index index : indexes.values()) {
            index.remove(old);
            index.add(entity);
        }
        return old;
    }

    public void remove(Entity entity) {
        if (isBuilding && !isAmounting) {
            queue.add(new MergeData<>(entity, MergeType.rm));
        }
        while (isAmounting) {
            try {
                Thread.sleep(0, 100);
            } catch (InterruptedException e) {
                throw new RuntimeException("sleep in IcTable.put.");
            }
        }
        removeInner(entity, this.data);
    }

    private void removeInner(Entity entity, Collection<Entity> data) {
        data.remove(entity);
        primary.remove(entity);
        for (Index index : indexes.values()) {
            index.remove(entity);
        }
    }

    public IcMap<Entity, PrimaryKey> getPrimary() {
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
