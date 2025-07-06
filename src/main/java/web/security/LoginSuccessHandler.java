package web.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import web.model.Role;
import web.service.RoleService;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RoleService roleService;

    @Autowired
    public LoginSuccessHandler(RoleService roleService) {
        this.roleService = roleService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Role selectedRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(roleService::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(role -> role.getRedirect() != null)
                .min(Comparator.comparing(Role::getId))
                .orElse(null);
        if (selectedRole != null) {
            response.sendRedirect(selectedRole.getRedirect());
        } else {
            response.sendRedirect("/");
        }
    }
}
