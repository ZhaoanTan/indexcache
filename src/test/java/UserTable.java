import org.indexcache.BaseIcMap;
import org.indexcache.BaseIcSets;
import org.indexcache.IcTable;

import java.util.Collection;
import java.util.function.Function;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public class UserTable extends IcTable<UserEntity, String, UserDataSrc> {
    public UserTable(UserDataSrc userDataSrc, Function<UserDataSrc, Collection<UserEntity>> dataGetter) {
        super(userDataSrc, dataGetter);
        primary(new BaseIcMap<>(UserEntity::getId));
        index(sexSets);
        index(jobSets);
        index(ageSets);
        build();
    }

    private BaseIcSets<UserEntity, SexType> sexSets = new BaseIcSets<>(UserEntity::getSex);
    private BaseIcSets<UserEntity, JobType> jobSets = new BaseIcSets<>(UserEntity::getJob);
    private BaseIcSets<UserEntity, Integer> ageSets = new BaseIcSets<>(UserEntity::getAge);

    public BaseIcSets<UserEntity, SexType> getSexSets() {
        return sexSets;
    }

    public BaseIcSets<UserEntity, JobType> getJobSets() {
        return jobSets;
    }

    public BaseIcSets<UserEntity, Integer> getAgeSets() {
        return ageSets;
    }
}
