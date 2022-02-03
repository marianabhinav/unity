package com.amcrest.unity.accounting.email;

import com.amcrest.unity.accounting.user.domain.User;

public interface EmailService {

    String buildEmail(String name, Integer link);

    void sendEmail(User user);
}
