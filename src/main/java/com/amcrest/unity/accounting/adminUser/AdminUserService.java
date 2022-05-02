package com.amcrest.unity.accounting.adminUser;

import com.amcrest.unity.accounting.adminUser.domain.BlacklistedJwtDto;
import com.amcrest.unity.accounting.adminUser.domain.GetUserListDto;
import com.amcrest.unity.accounting.adminUser.domain.LockUserDto;
import com.amcrest.unity.accounting.adminUser.domain.UserUpdateDto;

public interface AdminUserService {

    GetUserListDto getUserList();

    GetUserListDto getUsers(String keyword);

    void updateUser(UserUpdateDto userUpdateDto);

    void lockUser(LockUserDto lockUserDto);

    void blacklistJwtToken(BlacklistedJwtDto blacklistedJwtDto);

}
