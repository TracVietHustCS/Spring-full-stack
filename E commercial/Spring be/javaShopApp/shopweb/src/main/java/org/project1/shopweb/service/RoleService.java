package org.project1.shopweb.service;

import lombok.RequiredArgsConstructor;
import org.project1.shopweb.model.Role;
import org.project1.shopweb.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service

public class RoleService implements  IRoleService{
    private final RoleRepository roleRepository;
    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
