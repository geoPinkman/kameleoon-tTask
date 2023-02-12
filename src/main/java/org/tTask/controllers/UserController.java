package org.tTask.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tTask.models.JUser;
import org.tTask.services.UserService;

@RestController
@RequestMapping("api/users/")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("create")
    public JUser create(@RequestBody UserService.QUser qUser){
        return userService.createUser(qUser);
    }

    @GetMapping("user")
    public JUser getUser(@RequestParam String email, @RequestParam String password){
        return userService.getUser(email, password);
    }

    @PatchMapping("user")
    public JUser update(@RequestBody UserService.QUser qUser){
        return userService.updateUser(qUser);
    }

    @DeleteMapping("user")
    public void delete(@RequestParam int id){
        userService.deleteUserById(id);
    }
}
