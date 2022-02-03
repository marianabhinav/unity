package com.amcrest.unity.accounting.controller;

import com.amcrest.unity.accounting.email.token.EmailOtpService;
import com.amcrest.unity.accounting.email.validation.ValidEmail;
import com.amcrest.unity.accounting.security.jwt.JwtTokenService;
import com.amcrest.unity.accounting.user.domain.UserChangePasswordDto;
import com.amcrest.unity.accounting.user.domain.UserConfirmedDto;
import com.amcrest.unity.accounting.user.domain.User;
import com.amcrest.unity.accounting.user.domain.UserEmailDto;
import com.amcrest.unity.accounting.user.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path="api/v1/accounts")
public class AccountsController {

    private final UserService userService;
    private final EmailOtpService emailOtpService;
    private final JwtTokenService jwtTokenService;

    @PostMapping(path = "/user/register",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Registered. Verification email has been sent."),
            @ApiResponse(responseCode = "400", description = "Invalid input."),
            @ApiResponse(responseCode = "400", description = "User already exist."),
            @ApiResponse(responseCode = "409", description = "User already exist but email is not verified.")
    })
    public void userRegister(
            @Parameter(description="User object.",
                    required=true, schema=@Schema(implementation = User.class))
            @Valid @RequestBody User user){
        userService.registerNewUserAccount(user);
    }

    @GetMapping(path = "/user/confirmEmail",
            produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email Verified.",
                    content = @Content(
                            schema = @Schema(implementation = UserConfirmedDto.class))),
            @ApiResponse(responseCode = "400", description = "OTP is invalid."),
            @ApiResponse(responseCode = "401", description = "OTP is expired."),
            @ApiResponse(responseCode = "409", description = "Email already verified.")
    })
    public UserConfirmedDto confirmEmail(
            @Parameter(description = "OTP sent.", required=true)
            @Valid @RequestParam("otp") Integer otp){
        User user = emailOtpService.confirmOtp(otp);
        userService.enableUser(user.getEmail());
        String jwtToken = jwtTokenService.createJwtToken(user);
        return userService.convertToConfirmedUserDto(user, jwtToken);
    }

    @PostMapping(path = "/user/resendEmail",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found. Verification email has been sent."),
            @ApiResponse(responseCode = "404", description = "User not found."),
    })
    public void resendEmail(
            @Parameter(description="User email.", required=true)
            @Valid @RequestBody UserEmailDto userEmailDto){
        userService.resendEmail(userEmailDto.getEmail());
    }

    @PostMapping(path = "/user/login",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in.",
                    content = @Content(
                    schema = @Schema(implementation = UserConfirmedDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials."),
            @ApiResponse(responseCode = "401", description = "User account locked."),
            @ApiResponse(responseCode = "401", description = "User account disabled."),
            @ApiResponse(responseCode = "401", description = "User account expired."),
            @ApiResponse(responseCode = "401", description = "User credentials expired."),
    })
    public UserConfirmedDto userLogin(
            @Parameter(description="User object.",
                    required=true, schema=@Schema(implementation = User.class))
            @Valid @RequestBody User user){
        User loginUser = userService.loginUserAccount(user);
        String jwtToken = jwtTokenService.createJwtToken(user);
        return userService.convertToConfirmedUserDto(loginUser, jwtToken);
    }

    @PostMapping(path = "/user/forgotPassword",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found. Verification email has been sent."),
            @ApiResponse(responseCode = "404", description = "User not found."),
    })
    public void forgotPassword(
            @Parameter(description="User email.", required=true)
            @Valid @RequestBody UserEmailDto userEmailDto){
        userService.forgotPassword(userEmailDto.getEmail());
    }

    @PostMapping(path = "/user/changePassword",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully.",
                    content = @Content(
                            schema = @Schema(implementation = UserConfirmedDto.class))),
            @ApiResponse(responseCode = "400", description = "OTP is invalid."),
            @ApiResponse(responseCode = "401", description = "OTP is expired."),
    })
    public UserConfirmedDto changePassword(
            @Parameter(description="User otp and password.", required=true)
            @RequestBody UserChangePasswordDto userChangePasswordDto){
        User user = emailOtpService.confirmOtp(userChangePasswordDto.getOtp());
        userService.changePassword(user, userChangePasswordDto.getPassword());
        userService.loginUserAccount(user);
        String jwtToken = jwtTokenService.createJwtToken(user);
        return userService.convertToConfirmedUserDto(user, jwtToken);
    }

}
