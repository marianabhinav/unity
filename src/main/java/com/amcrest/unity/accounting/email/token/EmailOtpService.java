package com.amcrest.unity.accounting.email.token;

import com.amcrest.unity.accounting.email.token.domain.ConfirmationToken;
import com.amcrest.unity.accounting.user.domain.User;

public interface EmailOtpService {

    ConfirmationToken validateOtp(Integer otp);

    User confirmOtp(Integer otp);

    Integer createOtp(User user);
}
