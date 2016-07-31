package org.indexcache;

import java.util.Collection;
import java.util.function.Function;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public interface Index<Entity, Field> {
    Function<Entity, Field> fieldGetter();
    void build(Collection<Entity> entities);
    boolean add(Entity entity);
    boolean remove(Entity entity);
    void setTable(IcTable table);
}
