package org.indexcache;

import java.util.List;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public interface IcFuzzy<Entity, Field> extends Index<Entity, Field> {
    List<Entity> search(String keyWord, int start, int count);
}
