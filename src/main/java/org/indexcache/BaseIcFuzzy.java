package org.indexcache;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public class BaseIcFuzzy<Entity, Field> implements Index<Entity, Field>, IcFuzzy<Entity, Field> {
    @Override
    public List<Entity> search(String keyWord, int start, int count) {
        return null;
    }

    @Override
    public Function<Entity, Field> fieldGetter() {
        return null;
    }

    @Override
    public void build(Collection<Entity> entities) {

    }

    @Override
    public boolean add(Entity entity) {
        return false;
    }

    @Override
    public boolean remove(Entity entity) {
        return false;
    }

    @Override
    public void setTable(IcTable table) {

    }
}
