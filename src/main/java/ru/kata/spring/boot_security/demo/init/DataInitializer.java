package ru.kata.spring.boot_security.demo.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //Тестовые пользователи 1. admin admin (ADMIN); 2. user user (USER)
    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }

    public void init() {
        Role adminRole = getOrCreateRole("ROLE_ADMIN");
        Role userRole = getOrCreateRole("ROLE_USER");

        addTestUser("admin", "admin", 32, "admin.kata@gmail.com", Set.of(adminRole));
        addTestUser("user", "user", 18, "user.kata@gmail.com", Set.of(userRole));
    }

    private void addTestUser(String username, String password, Integer age, String email, Set<Role> roles) {
        User byUsername = userService.findByUsername(username);
        if (byUsername == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setAge(age);
            user.setEmail(email);
            user.setRoles(roles);
            userService.save(user);
        }
    }

    private Role getOrCreateRole(String roleName) {
        Role role = roleService.findByName(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleService.save(role);
        }
        return role;
    }
}
