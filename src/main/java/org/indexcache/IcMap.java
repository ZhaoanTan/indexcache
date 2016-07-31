package org.indexcache;

import java.util.List;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public interface IcMap<Entity, Key> {
    Entity get(Key key);

    List<Entity> range(Key start, Key end);
}
