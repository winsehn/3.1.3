package web.service;

import web.model.Role;
import web.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    void addUser(User user, String password, Set<Role> roles);

    void updateUser(User user, String password, Set<Role> roles);

    void deleteUser(Long id);

    User findUser(Long id);

    List<User> getAllUsers();

    List<User> getAllUsersWithRole();

    Optional<User> findByEmail(String email);

}
