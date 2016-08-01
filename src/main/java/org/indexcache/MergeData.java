package org.indexcache;

/**
 * Created by Zhaoan.Tan on 2016/8/1.
 */
public class MergeData<Entity> {
    public MergeData(Entity entity, MergeType mergeType) {
        this.entity = entity;
        this.mergeType = mergeType;
    }

    public Entity entity;
    public MergeType mergeType;
}
