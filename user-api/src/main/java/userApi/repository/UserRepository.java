package userApi.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import userApi.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    private Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final Map<String, User> internalUserMap = new HashMap<>();

    public void addUser(String userName, User user) {
        if (!internalUserMap.containsKey(userName)) {
            internalUserMap.put(userName, user);
        }
    }

    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    public User save(User user) {
        if (!user.getUserName().isEmpty()) {
            if (!internalUserMap.containsKey(user.getUserName())) {
                internalUserMap.put(user.getUserName(), user);
            } else {
                internalUserMap.replace(user.getUserName(), user);
            }
            return user;
        }
        return null;
    }

    public User getUserByUserName(String userName) {
        return internalUserMap.get(userName);
    }

    public List<User> getAllUser() {
        return new ArrayList<>(internalUserMap.values());
    }

    public void deleteAll() {
        internalUserMap.clear();
    }
}
