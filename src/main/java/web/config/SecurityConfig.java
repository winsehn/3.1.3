package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import web.model.Role;
import web.security.LoginSuccessHandler;
import web.security.UserDetailsServiceImpl;
import web.service.RoleService;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final RoleService roleService;

    @Autowired
    public SecurityConfig(RoleService roleService) {
        this.roleService = roleService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsServiceImpl userDetailsServiceImpl,
                                                   AuthenticationSuccessHandler successHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                            auth
                                    .requestMatchers("/css/**").permitAll()
                                    .requestMatchers("/login", "/error").permitAll();
                            List<Role> roles = roleService.findAll();
                            for (Role role : roles) {
                                var roleName = role.getName();
                                var redirect = role.getRedirect();
                                if (redirect != null) {
                                    auth.requestMatchers(redirect + "/**").hasAuthority(roleName);
                                }
                            }
                            auth.anyRequest().authenticated();
                        }
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .userDetailsService(userDetailsServiceImpl);
        ;
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler(RoleService roleService) {
        return new LoginSuccessHandler(roleService);
    }
}
