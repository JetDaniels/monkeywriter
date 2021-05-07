package org.doccreator.controller;

import org.doccreator.component.entity.UserDTO;
import org.doccreator.service.RoleService;
import org.doccreator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AuthController(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(value = "/api/auth", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> checkAuth(@RequestParam(defaultValue = " ") String username){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Total-Count", String.valueOf(1));
        httpHeaders.add("Access-Control-Expose-Headers", "X-Total-Count");
        httpHeaders.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(userService.findByLogin(username), httpHeaders, HttpStatus.OK);
    }

}
