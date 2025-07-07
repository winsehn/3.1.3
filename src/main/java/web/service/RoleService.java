package web.service;

import web.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Role save(Role role);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    Set<Role> reSetRoles(Set<Role> roles);
}
