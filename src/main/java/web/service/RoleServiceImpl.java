package web.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import web.model.Role;
import web.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Set<Role> reSetRoles(Set<Role> roles) {
        Set<Role> newRoles = new HashSet<>();
        for (Role role : roles) {
            Optional<Role> existing = roleRepository.findByName(role.getName());
            newRoles.add(existing.orElseGet(() -> roleRepository.save(role)));
        }
        return newRoles;
    }
}
