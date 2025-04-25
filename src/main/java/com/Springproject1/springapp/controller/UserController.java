package com.Springproject1.springapp.controller;

import com.Springproject1.springapp.entity.User;
import com.Springproject1.springapp.repository.UserRepository;
import com.Springproject1.springapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        try{
            return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<User> getById(@PathVariable ObjectId id){
        try{
            Optional<User> user = userService.findById(id);
            if(user!=null){
                return new ResponseEntity<>(user.get(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteById(){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User userInDB = userService.findByUserName(auth.getName());
            userRepository.deleteByUserName(userInDB.getUserName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User tempUser){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userInDB = userService.findByUserName(auth.getName());
        if(userInDB!=null){
            userInDB.setUserName(tempUser.getUserName());
            userInDB.setPassword(tempUser.getPassword());
        }
        userService.saveNewUser(userInDB);
        return new ResponseEntity<>(userInDB, HttpStatus.NO_CONTENT);
    }
}
