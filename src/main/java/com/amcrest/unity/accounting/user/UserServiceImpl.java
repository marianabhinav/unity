package com.amcrest.unity.accounting.user;

import com.amcrest.unity.accounting.email.EmailService;
import com.amcrest.unity.accounting.security.config.PasswordEncoder;
import com.amcrest.unity.accounting.user.domain.User;
import com.amcrest.unity.accounting.user.domain.UserConfirmedDto;
import com.amcrest.unity.accounting.user.domain.UserLoginDto;
import com.amcrest.unity.accounting.user.validation.UserValidator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    @Override
    public User registerNewUserAccount(User user){
        Optional<User> userPresent = userRepository.findByEmail(user.getEmail());
        boolean userExists= userPresent.isPresent();

        if(userExists){
            if(userPresent.get().isEnabled()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User already exist."
                );
            }
            else{
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "User already exist but email is not verified."
                );
            }
        }
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
        user.setJoinedDate(LocalDateTime.now());
        userRepository.save(user);
        emailService.sendEmail(user);
        return user;
    }

    @Override
    public void resendEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found."
                        )
                );
        emailService.sendEmail(user);
    }

    @Override
    public User loginUserAccount(UserLoginDto user){
        User loginUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Invalid credentials."
                        )
                );
        userValidator.validatePassword(user, loginUser);
        userValidator.validateState(loginUser);
        loginUser.setLastLoginDate(LocalDateTime.now());
        userRepository.updateLoginDate(loginUser.getEmail(), LocalDateTime.now());
        return  loginUser;
    }

    @Override
    public void forgotPassword(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found."
                        )
                );
        emailService.sendEmail(user);
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(password));
        userRepository.save(user);
    }

    @Override
    public void enableUser(String email) {
        userRepository.enableAppUser(email);
        userRepository.updateLoginDate(email, LocalDateTime.now());
    }

    @Override
    public UserConfirmedDto convertToConfirmedUserDto(User user, String jwtToken){
        UserConfirmedDto userConfirmedDto = modelMapper.map(user, UserConfirmedDto.class);
        userConfirmedDto.setJwtToken(jwtToken);
        return userConfirmedDto;
    }
}