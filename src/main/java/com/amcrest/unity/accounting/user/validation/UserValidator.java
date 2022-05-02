package com.amcrest.unity.accounting.user.validation;

import com.amcrest.unity.accounting.security.config.PasswordEncoder;
import com.amcrest.unity.accounting.user.domain.User;
import com.amcrest.unity.accounting.user.domain.UserLoginDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@AllArgsConstructor
public class UserValidator{

    PasswordEncoder passwordEncoder;

    public void validatePassword(UserLoginDto userToVerify, User userStored){
        if(!passwordEncoder.bCryptPasswordEncoder()
                .matches(userToVerify.getPassword(), userStored.getPassword())){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials."
            );
        }
    }

    public void validateState(User user) {
        if(!user.getIsAccountNonLocked()){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "User account locked."
            );
        }
        if(!user.getIsEnabled()){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "User account disabled."
            );
        }
        if(!user.getIsAccountNonExpired()){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "User account expired."
            );
        }
        if(!user.getIsCredentialsNonExpired()){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "User credentials expired."
            );
        }
    }
}
