package userApi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import userApi.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    UserRepository userRepositoryUnderTest;

    @BeforeEach
    void setUp() {
        userRepositoryUnderTest = new UserRepository();
        userRepositoryUnderTest.getInternalUserMap().clear();
    }

    @Test
    void addUser() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userRepositoryUnderTest.addUser(user.getUserName(), user);
        assertThat(userRepositoryUnderTest.getInternalUserMap().get(user.getUserName())).isEqualTo(user);
    }

    @Test
    void save() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userRepositoryUnderTest.save(user);
        assertThat(userRepositoryUnderTest.getInternalUserMap().get(user.getUserName())).isEqualTo(user);
        assertThat(userRepositoryUnderTest.getInternalUserMap().size()).isEqualTo(1);
    }

    @Test
    void saveWhenUserNameExistThenUpdate() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userRepositoryUnderTest.save(user);
        assertThat(userRepositoryUnderTest.getInternalUserMap().get(user.getUserName())).isEqualTo(user);
        assertThat(userRepositoryUnderTest.getInternalUserMap().size()).isEqualTo(1);
        User updatedUser = new User(user.getUserId(), "jon", "111", "jon@tourGuide.com");
        userRepositoryUnderTest.save(updatedUser);
        assertThat(userRepositoryUnderTest.getInternalUserMap().get(updatedUser.getUserName())).isEqualTo(updatedUser);
        assertThat(userRepositoryUnderTest.getInternalUserMap().size()).isEqualTo(1);
    }

    @Test
    void getUserByUserName() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userRepositoryUnderTest.getInternalUserMap().put(user.getUserName(), user);
        User result = userRepositoryUnderTest.getUserByUserName(user.getUserName());
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getAllUser() {
        User user1 = new User(UUID.randomUUID(), "jon1", "000", "jon@tourGuide.com");
        userRepositoryUnderTest.getInternalUserMap().put(user1.getUserName(), user1);
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon@tourGuide.com");
        userRepositoryUnderTest.getInternalUserMap().put(user2.getUserName(), user2);
        List<User> result = userRepositoryUnderTest.getAllUser();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void deleteAll() {
        User user1 = new User(UUID.randomUUID(), "jon1", "000", "jon@tourGuide.com");
        userRepositoryUnderTest.getInternalUserMap().put(user1.getUserName(), user1);
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon@tourGuide.com");
        userRepositoryUnderTest.getInternalUserMap().put(user2.getUserName(), user2);
         userRepositoryUnderTest.deleteAll();
        assertThat(userRepositoryUnderTest.getInternalUserMap().size()).isEqualTo(0);
    }
}