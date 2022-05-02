package com.amcrest.unity.accounting.user;

import com.amcrest.unity.accounting.email.token.EmailOtpService;
import com.amcrest.unity.accounting.response.ResponseHandler;
import com.amcrest.unity.accounting.security.jwt.JwtTokenService;
import com.amcrest.unity.accounting.user.domain.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path="api/v1/user")
@Tag(name = "Accounts Module", description = "This module is intended to handle all the accounts related operations.")
public class UserController {

    private final UserService userService;
    private final EmailOtpService emailOtpService;
    private final JwtTokenService jwtTokenService;

    @PostMapping(path = "/register",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Registered. Verification email has been sent."),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input.\n\n" +
                    "User already exist."),
            @ApiResponse(responseCode = "409", description = "User already exist but email is not verified.")
    })
    public ResponseEntity<Object> userRegister(
            @Parameter(description="User object.",
                    required=true, schema=@Schema(implementation = User.class))
            @Valid @RequestBody User user){
        userService.registerNewUserAccount(user);
        return ResponseHandler
                .generateResponse(
                        "User Registered. Verification email has been sent.",
                        HttpStatus.CREATED);
    }

    @GetMapping(path = "/confirmEmail",
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
    public ResponseEntity<Object> confirmEmail(
            @Parameter(description = "OTP sent.", required=true)
            @Valid @RequestParam("otp") Integer otp){
        User user = emailOtpService.confirmOtp(otp);
        userService.enableUser(user.getEmail());
        String jwtToken = jwtTokenService.createJwtToken(user);
        return ResponseHandler
                .generateResponse(
                        "Email Verified.",
                        HttpStatus.OK,
                        userService.convertToConfirmedUserDto(user, jwtToken));
    }

    @PostMapping(path = "/resendEmail",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found. Verification email has been sent."),
            @ApiResponse(responseCode = "404", description = "User not found."),
    })
    public ResponseEntity<Object> resendEmail(
            @Parameter(description="User email.", required=true)
            @Valid @RequestBody UserEmailDto userEmailDto){
        userService.resendEmail(userEmailDto.getEmail());
        return ResponseHandler
                .generateResponse(
                        "User found. Verification email has been sent.",
                        HttpStatus.OK);
    }

    @PostMapping(path = "/login",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in.",
                    content = @Content(
                    schema = @Schema(implementation = UserConfirmedDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "Invalid credentials.\n\n" +
                    "User account locked.\n\n" +
                    "User account disabled.\n\n" +
                    "User account expired.\n\n" +
                    "User credentials expired."),
    })
    public ResponseEntity<Object> userLogin(
            @Parameter(description="User object.",
                    required=true, schema=@Schema(implementation = UserLoginDto.class))
            @Valid @RequestBody UserLoginDto user){
        User loginUser = userService.loginUserAccount(user);
        String jwtToken = jwtTokenService.createJwtToken(loginUser);
        return ResponseHandler
                .generateResponse(
                        "User logged in.",
                        HttpStatus.OK,
                        userService.convertToConfirmedUserDto(loginUser, jwtToken));
    }

    @PostMapping(path = "/forgotPassword",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found. Verification email has been sent."),
            @ApiResponse(responseCode = "404", description = "User not found."),
    })
    public ResponseEntity<Object> forgotPassword(
            @Parameter(description="User email.", required=true)
            @Valid @RequestBody UserEmailDto userEmailDto){
        userService.forgotPassword(userEmailDto.getEmail());
        return ResponseHandler
                .generateResponse(
                        "User found. Verification email has been sent.",
                        HttpStatus.OK);
    }

    @PostMapping(path = "/changePassword",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully.",
                    content = @Content(
                            schema = @Schema(implementation = UserConfirmedDto.class))),
            @ApiResponse(responseCode = "400", description = "OTP is invalid."),
            @ApiResponse(responseCode = "401",
                    description = "OTP is expired.\n\n" +
                    "User account locked. Password changed.\n\n" +
                    "User account disabled. Password changed.\n\n" +
                    "User account expired. Password changed.\n\n" +
                    "User credentials expired. Password changed."),
    })
    public ResponseEntity<Object> changePassword(
            @Parameter(description="User otp and password.", required=true)
            @RequestBody UserChangePasswordDto userChangePasswordDto){
        User user = emailOtpService.validateOtp(userChangePasswordDto.getOtp()).getUser();
        userService.changePassword(user, userChangePasswordDto.getPassword());
        try {
            userService.loginUserAccount(UserLoginDto.builder()
                    .email(user.getEmail())
                    .password(userChangePasswordDto.getPassword())
                    .build());
        }catch (ResponseStatusException responseStatusException){
            throw new ResponseStatusException(responseStatusException.getStatus(),
                    Objects.requireNonNull(responseStatusException.getReason()).concat(" Password changed."));
        }
        String jwtToken = jwtTokenService.createJwtToken(user);
        return ResponseHandler
                .generateResponse(
                        "Password changed successfully.",
                        HttpStatus.OK,
                        userService.convertToConfirmedUserDto(user, jwtToken));
    }
}
