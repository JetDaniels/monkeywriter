package org.doccreator.controller;

import org.doccreator.component.entity.RoleDTO;
import org.doccreator.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/roles",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getRoles(){
        return ResponseEntity.ok(roleService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/addRole",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> addRole(@RequestBody RoleDTO roleDTO){
        roleService.save(roleDTO);
        return ResponseEntity.ok(roleService.findAll());
    }
}
