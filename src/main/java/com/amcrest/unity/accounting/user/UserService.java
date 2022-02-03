package com.amcrest.unity.accounting.user;

import com.amcrest.unity.accounting.user.domain.User;
import com.amcrest.unity.accounting.user.domain.UserConfirmedDto;

public interface UserService {

    User registerNewUserAccount(User user);

    void resendEmail(String userEmail);

    User loginUserAccount(User user);

    void forgotPassword(String userEmail);

    void changePassword(User user, String password);

    int enableUser(String email);

    UserConfirmedDto convertToConfirmedUserDto(User user, String jwt);
}
