package web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.Role;
import web.model.User;
import web.model.auth.Password;
import web.repository.RoleRepository;
import web.repository.UsersRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void addUser(User user, String password, Set<Role> roles) {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
            return;
        }
        user.setPassword(new Password(password, passwordEncoder));
        user.setRoles(reSetRoles(roles));
        usersRepository.addUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user, String password, Set<Role> roles) {
        User existing = usersRepository.findUser(user.getId());

        Optional<User> findUserByMail = usersRepository.findByEmail(user.getEmail());
        if (findUserByMail.isPresent() && !findUserByMail.get().getId().equals(user.getId())) {
            return;
        }

        if (password != null && !password.isBlank()) {
            if (!passwordEncoder.matches(password, existing.getPassword())) {
                user.setPassword(new Password(password, passwordEncoder));
            } else {
                user.setPassword(new Password(existing.getPassword()));
            }
        } else {
            user.setPassword(new Password(existing.getPassword()));
        }
        user.setRoles(reSetRoles(roles));
        usersRepository.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        usersRepository.deleteUser(id);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return usersRepository.getAllUsers();
    }

    @Override
    @Transactional
    public List<User> getAllUsersWithRole() {
        return usersRepository.getAllUsersWithRole();
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User findUser(Long id) {
        return usersRepository.findUser(id);
    }

    private Set<Role> reSetRoles(Set<Role> roles) {
        Set<Role> newRoles = new HashSet<>();
        for (Role role : roles) {
            Optional<Role> existing = roleRepository.findByName(role.getName());
            newRoles.add(existing.orElseGet(() -> roleRepository.save(role)));
        }
        return newRoles;
    }
}
