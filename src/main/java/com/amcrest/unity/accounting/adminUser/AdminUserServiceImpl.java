package com.amcrest.unity.accounting.adminUser;

import com.amcrest.unity.accounting.adminUser.domain.BlacklistedJwtDto;
import com.amcrest.unity.accounting.adminUser.domain.GetUserListDto;
import com.amcrest.unity.accounting.adminUser.domain.LockUserDto;
import com.amcrest.unity.accounting.adminUser.domain.UserUpdateDto;
import com.amcrest.unity.accounting.security.jwt.BlacklistedJwtTokenRepository;
import com.amcrest.unity.accounting.security.jwt.JwtConfig;
import com.amcrest.unity.accounting.security.jwt.domain.BlacklistedJwtToken;
import com.amcrest.unity.accounting.user.UserRepository;
import com.amcrest.unity.accounting.user.domain.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdminUserService.class);
    private final UserRepository userRepository;
    private final BlacklistedJwtTokenRepository blacklistedJwtTokenRepository;
    private final JwtConfig jwtConfig;
    private final AdminUserRepository adminUserRepository;

    @Override
    public GetUserListDto getUserList() {
        List<User> userList = adminUserRepository.findAll();
        if(userList.isEmpty()){
            return GetUserListDto.builder().userList(new ArrayList<>()).build();
        }
        return GetUserListDto.builder().userList(userList).build();
    }

    @Override
    public GetUserListDto getUsers(String keyword) {
        List<User> userList = adminUserRepository.findAllByEmailOrFirstNameOrLastName(keyword);
        return GetUserListDto.builder().userList(userList).build();
    }

    @Override
    public void updateUser(UserUpdateDto userUpdateDto) {
        userRepository.findByEmail(userUpdateDto.getEmail()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invalid email."
                ));
        adminUserRepository.updateUser(userUpdateDto.getEmail(),
                userUpdateDto.getFirstName(),
                userUpdateDto.getLastName());
    }

    @Override
    public void lockUser(LockUserDto lockUserDto) {
        userRepository.findByEmail(lockUserDto.getEmail()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invalid email."
                ));
        adminUserRepository.lockUser(lockUserDto.getEmail(), lockUserDto.getIsAccountNonLocked());
    }

    @Override
    public void blacklistJwtToken(BlacklistedJwtDto blacklistedJwtDto) {
        Optional<BlacklistedJwtToken> blacklistedJwtToken = blacklistedJwtTokenRepository.
                findByToken(blacklistedJwtDto.getToken());
        if(blacklistedJwtToken.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This JWT token is already blacklisted.");
        }
        String jwtToken = blacklistedJwtDto.getToken().replace(jwtConfig.getTokenPrefix(), "").trim();
        blacklistedJwtTokenRepository.save(BlacklistedJwtToken.builder()
                .token(jwtToken).build());
    }
}
