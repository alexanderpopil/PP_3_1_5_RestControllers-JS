package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRoles(updatedUser.getRoles());
        userRepository.save(existingUser);
    }

    //Тестовые пользователи 1. admin admin (ADMIN); 2. user user (USER)
    @PostConstruct
    public void init() {
        Role adminRole = getOrCreateRole("ROLE_ADMIN");
        Role userRole = getOrCreateRole("ROLE_USER");

        addTestUser("admin", "$2a$12$NG4dwvCIbCAiXshrdvfSZ.5pgJCIJ7OYCc4CxXXAJbmQQ.Zbtj6UW", Set.of(adminRole));
        addTestUser("user", "$2a$12$GK7ws8fzQo..uHlbNT.YSOL0UBaJauundfOVipzxAUh1Mk0PxAT0W", Set.of(userRole));
    }

    private void addTestUser(String username, String password, Set<Role> roles) {
        User byUsername = userRepository.findByUsername(username);
        if (byUsername == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    private Role getOrCreateRole(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }
        return role;
    }
}
