package com.amcrest.unity.accounting.email.token;

import com.amcrest.unity.accounting.user.domain.User;

public interface EmailOtpService {

    User confirmOtp(Integer otp);

    Integer createOtp(User user);
}
