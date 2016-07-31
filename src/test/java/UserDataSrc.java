import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhaoan.Tan on 2016/7/10.
 */
public class UserDataSrc {
    List<UserEntity> get() {
        List<UserEntity> ls = new ArrayList<>();
        UserEntity user = new UserEntity();
        user.setId("1");
        user.setName("Tom");
        user.setAge(20);
        user.setSex(SexType.MALE);
        user.setJob(JobType.PROGRAMMER);
        ls.add(user);
        user = new UserEntity();
        user.setId("2");
        user.setName("Mary");
        user.setAge(21);
        user.setSex(SexType.FEMALE);
        user.setJob(JobType.STUDENT);
        ls.add(user);
        user = new UserEntity();
        user.setId("2");
        user.setName("Lily");
        user.setAge(19);
        user.setSex(SexType.FEMALE);
        user.setJob(JobType.PROGRAMMER);
        ls.add(user);
        user = new UserEntity();
        user.setId("3");
        user.setName("Jack");
        user.setAge(20);
        user.setSex(SexType.MALE);
        user.setJob(JobType.STUDENT);
        ls.add(user);
        return ls;
    }

    List<UserEntity> getNull() {
        return null;
    }
}
