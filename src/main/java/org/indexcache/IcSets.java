package org.indexcache;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public interface IcSets<Entity, Key> {
    Set<Entity> inter(Key... keys);
    Set<Entity> inter(Collection<Key> keys);
    Set<Entity> union(Key... keys);
    Set<Entity> union(Collection<Key> keys);

    Set<Entity> allExclude(Collection<Key> keys);

    List<Entity> range(Key start, Key end);
}
