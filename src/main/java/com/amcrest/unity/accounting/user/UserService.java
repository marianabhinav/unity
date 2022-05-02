package com.amcrest.unity.accounting.user;

import com.amcrest.unity.accounting.user.domain.User;
import com.amcrest.unity.accounting.user.domain.UserConfirmedDto;
import com.amcrest.unity.accounting.user.domain.UserLoginDto;

public interface UserService {

    User registerNewUserAccount(User user);

    void resendEmail(String userEmail);

    User loginUserAccount(UserLoginDto user);

    void forgotPassword(String userEmail);

    void changePassword(User user, String password);

    void enableUser(String email);

    UserConfirmedDto convertToConfirmedUserDto(User user, String jwt);
}