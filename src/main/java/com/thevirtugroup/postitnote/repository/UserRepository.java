package com.thevirtugroup.postitnote.repository;


import com.thevirtugroup.postitnote.model.User;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private User defaultUser;

    Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();

    public UserRepository() {
        defaultUser = new User();
        defaultUser.setId(999L);
        defaultUser.setName("Johnny Tango");
        String encodedPassword = passwordEncoder.encode("password");
        defaultUser.setPassword(encodedPassword);
        defaultUser.setUsername("user");
    }

    public User findUserByUsername(String username){
        if ("user".equals(username)){
            return defaultUser;
        }
        return null;
    }

    public User findById(Long id){
        if (defaultUser.getId().equals(id)){
            return defaultUser;
        }
        return null;
    }
}
