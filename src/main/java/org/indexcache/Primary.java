package org.indexcache;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public interface Primary<Entity, Key> extends Index<Entity, Key>, IcMap<Entity, Key> {
    Entity put(Entity entity);
}
