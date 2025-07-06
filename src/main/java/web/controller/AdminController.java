package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @GetMapping(value = "/admin")
    public String index(Model model, Principal principal) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("editingId", null);
        model.addAttribute("allRoles", roleService.findAll());

        User currentUser = userService.findByEmail(principal.getName())
                .orElseThrow();
        model.addAttribute("user", currentUser);
        return "admin";
    }

    @PostMapping(value = "/admin/create")
    public String addUser(Model model,
                          @RequestParam(name = "first_name") String first_name,
                          @RequestParam(name = "last_name") String last_name,
                          @RequestParam(name = "age") Byte age,
                          @RequestParam(name = "email") String email,
                          @RequestParam(name = "password") String password,
                          @RequestParam(name = "roles") Set<Role> roles
    ) {
        User user = new User(first_name, last_name, age, email);
        user.setRoles(roles);
        userService.addUser(user, password, roles);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/delete")
    public String deleteUser(@RequestParam(name = "id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/edit")
    public String editUser(Model model, @RequestParam(name = "id") Long id) {
        model.addAttribute("editingId", id);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user", userService.findUser(id));
        return "admin";
    }

    @PostMapping(value = "/admin/update")
    public String updateUser(Model model,
                             @RequestParam(name = "id") Long id,
                             @RequestParam(name = "first_name", required = false) String first_name,
                             @RequestParam(name = "last_name", required = false) String last_name,
                             @RequestParam(name = "age", required = false) Byte age,
                             @RequestParam(name = "email") String email,
                             @RequestParam(name = "password") String password,
                             @RequestParam(name = "roles") Set<Role> roles) {
        model.addAttribute("password", password);
        User user = new User(first_name, last_name, age, email);
        user.setId(id);
        userService.updateUser(user, password, roles);
        return "redirect:/admin";
    }
}
