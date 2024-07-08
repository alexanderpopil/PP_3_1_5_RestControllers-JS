package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    Role findByName(String name);

    List<Role> findAll();

    Role findById(Long id);

    Set<Role> findByIds(List<Long> ids);

    void save(Role role);
}
