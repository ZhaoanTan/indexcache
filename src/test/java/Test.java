import org.indexcache.BaseIcMap;
import org.indexcache.BaseIcSets;
import org.indexcache.IcTable;
import org.indexcache.SetUtil;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public class Test {
    public static void main(String args[]) {
        test1();
        test2();
    }

    public static void test1() {
        UserDataSrc dataSrc = new UserDataSrc();
        IcTable<UserEntity, String, UserDataSrc> table = new IcTable<>(dataSrc, UserDataSrc::get);
        table.primary(new BaseIcMap<>(UserEntity::getId));
        Function<UserEntity, SexType> getSex = UserEntity::getSex;
        Function<UserEntity, JobType> getJob = UserEntity::getJob;
        Function<UserEntity, Integer> getAge = UserEntity::getAge;
        table.index(new BaseIcSets<>(getSex));
        table.index(new BaseIcSets<>(getJob));
        table.index(new BaseIcSets<>(getAge, (o2, o1) -> o1.compareTo(o2)));
        table.build();

        Set<UserEntity> userMale = table.getIndex(getSex).inter(SexType.MALE);

        Set<UserEntity> userFemaleProgrammer = table.getIndex(getSex).inter(SexType.FEMALE);
        userFemaleProgrammer.retainAll(table.getIndex(getJob).inter(JobType.PROGRAMMER));

        Set<UserEntity> userMaleStudent = SetUtil.inter(table.getIndex(getSex).inter(SexType.MALE),
                table.getIndex(getJob).inter(JobType.STUDENT));

        List<UserEntity> userSortByAge = table.getIndex(getAge).range(21, 19);
        List<UserEntity> userSortByAgeAndFemale = table.getIndex(getAge).range(22, 18);
        userSortByAgeAndFemale.retainAll(table.getIndex(getSex).inter(SexType.FEMALE));
    }

    public static void test2() {
        UserDataSrc dataSrc = new UserDataSrc();
        UserTable table = new UserTable(dataSrc, UserDataSrc::get);
        Set<UserEntity> userMale = table.getSexSets().inter(SexType.MALE);

        Set<UserEntity> userFemaleProgrammer = table.getSexSets().inter(SexType.FEMALE);
        userFemaleProgrammer.retainAll(table.getJobSets().inter(JobType.PROGRAMMER));

        Set<UserEntity> userMaleStudent = SetUtil.inter(table.getSexSets().inter(SexType.MALE),
                table.getJobSets().inter(JobType.STUDENT));

        List<UserEntity> userSortByAge = table.getAgeSets().range(18, 21);
        List<UserEntity> userSortByAgeAndFemale = table.getAgeSets().range(18, 22);
        userSortByAgeAndFemale.retainAll(table.getSexSets().inter(SexType.FEMALE));
    }
}
