package web.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class BdInit implements InitializingBean {
    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public BdInit(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Role adminRole = new Role("admin");
        adminRole.setRedirect("/admin");
        roleService.save(adminRole);

        Role userRole = new Role("user");
        userRole.setRedirect("/user");
        roleService.save(userRole);

        Role guestRole = new Role("guest");
        userRole.setRedirect("/loggin");
        roleService.save(guestRole);


        User user = new User("user_name", "user_second_name", (byte) 25, "user@mail.com");
        User admin = new User("admin", "admin", (byte) 1, "admin@mail.com");

        Set<Role> rolesAdmin = new HashSet<>();
        rolesAdmin.add(adminRole);
        rolesAdmin.add(userRole);
        Set<Role> rolesUser = new HashSet<>();
        rolesUser.add(userRole);

        userService.addUser(admin, "admin", rolesAdmin);
        userService.addUser(user, "user", rolesUser);
    }
}