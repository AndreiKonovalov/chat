package ru.konovalov.chat.service;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.core.userdetails.User;
import ru.konovalov.chat.model.Role;
import ru.konovalov.chat.model.User;
import ru.konovalov.chat.repository.UserRepository;

import java.util.Collections;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailService mailService;

    @Test
    void addUser() {
        User user = new User();
        user.setEmail("some@mail.ru");

        boolean isUserCreated = service.addUser(user);

        Assertions.assertTrue(isUserCreated);
        Assertions.assertNotNull(user.getActivationCode());
        Assertions.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailService, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());

    }
    @Test
    void addUserFailTest(){
        User user = new User();
        user.setUsername("John");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("John");

        boolean isUserCreated = service.addUser(user);

        Assertions.assertFalse(isUserCreated);
        Mockito.verify(userRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailService, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());
    }

    @Test
    void activateUser() {
        User user = new User();
        user.setActivationCode("Yahoooo");

        Mockito.doReturn(user)
                .when(userRepository)
                .findByActivationCode("activate");

        boolean isUserActivate = service.activateUser("activate");

        Assertions.assertTrue(isUserActivate);
        Assertions.assertNull(user.getActivationCode());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);

    }

    @Test
    void activateUserFailTest(){
        boolean isUserActivate = service.activateUser("activate me");

        Assertions.assertFalse(isUserActivate);
        Mockito.verify(userRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(User.class));
    }
}