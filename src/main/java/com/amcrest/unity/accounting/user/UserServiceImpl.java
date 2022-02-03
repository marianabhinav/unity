package com.amcrest.unity.accounting.user;

import com.amcrest.unity.accounting.email.EmailService;
import com.amcrest.unity.accounting.security.config.PasswordEncoder;
import com.amcrest.unity.accounting.user.domain.User;
import com.amcrest.unity.accounting.user.domain.UserConfirmedDto;
import com.amcrest.unity.accounting.user.validation.UserValidator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
                emailService.sendEmail(user);
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "User already exist but email is not verified."
                );
            }
        }
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
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
    public User loginUserAccount(User user){
        User loginUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Invalid credentials."
                        )
                );
        userValidator.validatePassword(user, loginUser);
        userValidator.validateState(loginUser);
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
    public int enableUser(String email) {
        int usersUpdated = userRepository.enableAppUser(email);
        if(usersUpdated != 1){
            LOGGER.error("Multiple entries detected for user. Email:" + email);
//            throw new IllegalStateException("Multiple entries detected for user. Email:" + email);
        }
        return usersUpdated;
    }

    @Override
    public UserConfirmedDto convertToConfirmedUserDto(User user, String jwtToken){
        UserConfirmedDto userConfirmedDto = modelMapper.map(user, UserConfirmedDto.class);
        userConfirmedDto.setJwtToken(jwtToken);
        return userConfirmedDto;
    }
}
