package org.project1.shopweb.controller;

import lombok.RequiredArgsConstructor;
import org.project1.shopweb.model.Role;
import org.project1.shopweb.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getAllRole(){
        List<Role> roles = roleService.getAllRole();
        return ResponseEntity.ok(roles);
    }

}
