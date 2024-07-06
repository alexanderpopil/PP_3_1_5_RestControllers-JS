package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional
    public Role findByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    @Transactional
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Transactional
    @Override
    public Role findById(Long id) {
        return roleDao.findById(id);
    }

    @Override
    @Transactional
    public void save(Role role) {
        roleDao.save(role);
    }
}
