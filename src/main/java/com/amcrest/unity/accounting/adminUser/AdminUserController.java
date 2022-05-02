package com.amcrest.unity.accounting.adminUser;

import com.amcrest.unity.accounting.adminUser.domain.BlacklistedJwtDto;
import com.amcrest.unity.accounting.adminUser.domain.GetUserListDto;
import com.amcrest.unity.accounting.adminUser.domain.LockUserDto;
import com.amcrest.unity.accounting.adminUser.domain.UserUpdateDto;
import com.amcrest.unity.accounting.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path="api/v1/admin")
@Tag(name = "Admin User Module", description = "This module is intended to handle all the admin related operations.")
@PreAuthorize("hasAuthority('admin:role')")
@SecurityRequirement(name = "JwtAuth")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping(path = "/blacklistJwtToken",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "JWT Token blacklisted."),
            @ApiResponse(responseCode = "401", description = "Access is denied."),
            @ApiResponse(responseCode = "409", description = "This JWT token is already blacklisted.")
    })
    public ResponseEntity<Object> blacklistJwtToken(
            @Parameter(description="Jwt token to blacklist object.",
                    required=true, schema=@Schema(implementation = BlacklistedJwtDto.class))
            @Valid @RequestBody BlacklistedJwtDto blacklistedJwtDto){
        adminUserService.blacklistJwtToken(blacklistedJwtDto);
        return ResponseHandler
                .generateResponse(
                        "JWT Token blacklisted.",
                        HttpStatus.CREATED);
    }

    @GetMapping(path = "/userList",
            produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(
                            schema = @Schema(implementation = GetUserListDto.class))),
            @ApiResponse(responseCode = "401", description = "Access is denied.")
    })
    public ResponseEntity<Object> getUserList(){
        GetUserListDto getUserListDto =  adminUserService.getUserList();
        return ResponseHandler
                .generateResponse(
                        "Success.",
                        HttpStatus.OK,
                        getUserListDto);
    }

    @GetMapping(path = "/user",
            produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(
                            schema = @Schema(implementation = GetUserListDto.class))),
            @ApiResponse(responseCode = "401", description = "Access is denied.")
    })
    public ResponseEntity<Object> getUsers(
            @Parameter(description = "User email or firstName or lastName.")
            @Valid @NotBlank @NotNull @RequestParam("keyword") String keyword){
        GetUserListDto getUserListDto =  adminUserService.getUsers(keyword);
        return ResponseHandler
                .generateResponse(
                        "Success.",
                        HttpStatus.OK,
                        getUserListDto);
    }

    @PatchMapping(path = "/updateUser",
            produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account updated."),
            @ApiResponse(responseCode = "401", description = "Access is denied."),
            @ApiResponse(responseCode = "404", description = "Invalid email.")
    })
    public ResponseEntity<Object> updateUser(
            @Parameter(description="User object to update.",
                    required=true, schema=@Schema(implementation = UserUpdateDto.class))
            @Valid @RequestBody UserUpdateDto userUpdateDto){
        adminUserService.updateUser(userUpdateDto);
        return ResponseHandler
                .generateResponse(
                        "User account updated.",
                        HttpStatus.OK);
    }

    @PatchMapping(path = "/lockStatusUser",
            produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User lock status changed."),
            @ApiResponse(responseCode = "401", description = "Access is denied."),
            @ApiResponse(responseCode = "404", description = "Invalid email.")
    })
    public ResponseEntity<Object> lockUser(
            @Parameter(description="User email and lock status.",
                    required=true, schema=@Schema(implementation = LockUserDto.class))
            @Valid @RequestBody LockUserDto lockUserDto){
        adminUserService.lockUser(lockUserDto);
        return ResponseHandler
                .generateResponse(
                        "User lock status changed.",
                        HttpStatus.OK);
    }
}
