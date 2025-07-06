package web.repository;

import web.model.User;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    void addUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User findUser(Long id);

    List<User> getAllUsers();

    List<User> getAllUsersWithRole();

    Optional<User> findByEmail(String email);
}
