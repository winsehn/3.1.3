package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView index(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("admin");
        List<User> users = userService.getAllUsers();
        modelAndView.addObject("users", users);
        modelAndView.addObject("editingId", null);
        modelAndView.addObject("allRoles", roleService.findAll());

        User currentUser = userService.findByEmail(principal.getName()).orElseThrow();

        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    @PostMapping(value = "/create")
    public ModelAndView addUser(@RequestParam(name = "first_name") String first_name,
                                @RequestParam(name = "last_name") String last_name,
                                @RequestParam(name = "age") Byte age,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "password") String password,
                                @RequestParam(name = "roles") Set<Role> roles
    ) {
        User user = new User(first_name, last_name, age, email);
        user.setRoles(roles);
        userService.addUser(user, password, roles);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin");
        return modelAndView;
    }

    @PostMapping(value = "/delete")
    public ModelAndView deleteUser(@RequestParam(name = "id") Long id) {
        userService.deleteUser(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin");
        return modelAndView;
    }

    @PostMapping(value = "/edit")
    public ModelAndView editUser(@RequestParam(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("editingId", id);
        modelAndView.addObject("users", userService.getAllUsers());
        modelAndView.addObject("allRoles", roleService.findAll());
        modelAndView.addObject("user", userService.findUser(id));
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView updateUser(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "first_name", required = false) String first_name,
                                   @RequestParam(name = "last_name", required = false) String last_name,
                                   @RequestParam(name = "age", required = false) Byte age,
                                   @RequestParam(name = "email") String email,
                                   @RequestParam(name = "password") String password,
                                   @RequestParam(name = "roles") Set<Role> roles) {
        ModelAndView modelAndView = new ModelAndView("redirect:/admin");
        User user = new User(first_name, last_name, age, email);
        user.setId(id);
        userService.updateUser(user, password, roles);
        return modelAndView;
    }
}
