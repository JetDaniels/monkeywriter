package org.doccreator.controller;

import org.doccreator.component.entity.UserDTO;
import org.doccreator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/users",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/addUser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> addSystem(@RequestBody UserDTO user){
        userService.save(user);
        return ResponseEntity.ok(userService.findAll());
    }

}
