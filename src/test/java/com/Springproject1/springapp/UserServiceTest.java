package com.Springproject1.springapp;

import com.Springproject1.springapp.repository.UserRepository;
import com.Springproject1.springapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


//    @ParameterizedTest
//    @ValueSource(strings = {
//            "Ram",
//            "Sainee"
//    })
    public void findByUserNameTest(String userName) {
        assertNotNull(userRepository.findByUserName(userName));
    }
}
