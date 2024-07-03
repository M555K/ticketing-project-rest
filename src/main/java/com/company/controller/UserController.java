package com.company.controller;

import com.company.dto.ResponseWrapper;
import com.company.dto.UserDTO;
import com.company.service.RoleService;
import com.company.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @RolesAllowed({"Manager","Admin"})
    @GetMapping
    public ResponseEntity<ResponseWrapper> getUsers(){
        List<UserDTO> userDTOList = userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved",userDTOList, HttpStatus.OK));
    }
    @RolesAllowed("Admin")
    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String userName){
        UserDTO user = userService.findByUserName(userName);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully retrieved",user, HttpStatus.OK));
    }
    @RolesAllowed("Admin")
    @PostMapping
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO user){
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper("User are successfully created", HttpStatus.CREATED));
    }
    @RolesAllowed("Admin")
    @PutMapping
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user){
        userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("User are successfully updated",user, HttpStatus.OK));
    }
    @RolesAllowed("Admin")
    @DeleteMapping("/{username}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String userName){
        userService.delete(userName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseWrapper("User are successfully deleted", HttpStatus.NO_CONTENT));
    }
}
