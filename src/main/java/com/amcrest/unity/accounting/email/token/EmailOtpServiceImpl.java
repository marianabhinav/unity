package com.amcrest.unity.accounting.email.token;

import com.amcrest.unity.accounting.email.token.domain.ConfirmationToken;
import com.amcrest.unity.accounting.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class EmailOtpServiceImpl implements EmailOtpService{

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken validateOtp(Integer otp){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByOtp(otp)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP is invalid.")
                );
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OTP is expired.");
        }
        return confirmationToken;
    }

    @Transactional
    public User confirmOtp(Integer otp) {
        ConfirmationToken confirmationToken = validateOtp(otp);
        if (confirmationToken.getConfirmedAt() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already verified.");
        }
        confirmationTokenRepository.updateConfirmedAt(otp, LocalDateTime.now());
        return confirmationToken.getUser();
    }

    public Integer createOtp(User user){
        Random random = new Random();
        Integer otp = 100000 + random.nextInt(900000);
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .otp(otp)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        confirmationTokenRepository.save(confirmationToken);
        return otp;
    }
}
