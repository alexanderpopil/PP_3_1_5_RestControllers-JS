package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
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

        addTestUser("admin", "$2a$12$t4Z7rFKRGyn0yZzejObpN.bA6.yjz/FTP4FBb0hxAiu1yqiaqal5.", Set.of(adminRole));
        addTestUser("user", "$2a$12$XdzRmQFvOUz.67iN4CHAV.Sx5AXSN0JozLQzroq2z5sTyKBMl1MJu", Set.of(userRole));
    }

    private void addTestUser(String username, String password, Set<Role> roles) {
        User byUsername = userService.findByUsername(username);
        if (byUsername == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
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
