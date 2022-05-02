package com.amcrest.unity.accounting.adminUser.domain;

import com.amcrest.unity.accounting.user.domain.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserListDto {

    @NotNull
    @NotEmpty
    private List<User> userList;
}
