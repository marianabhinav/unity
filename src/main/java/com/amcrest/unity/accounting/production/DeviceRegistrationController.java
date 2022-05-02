package com.amcrest.unity.accounting.production;

import com.amcrest.unity.accounting.production.domain.DeviceRegisteredDto;
import com.amcrest.unity.accounting.production.domain.DeviceRegistrationDto;
import com.amcrest.unity.accounting.response.ResponseHandler;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path="api/v1/production")
@Tag(name = "Production Module", description = "This module is intended to handle all the device registration related operations.")
public class DeviceRegistrationController {

    private final DeviceRegistrationService deviceRegistrationService;

    @PostMapping(path = "/device/register",
            consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device Registered.",
                    content = @Content(
                            schema = @Schema(implementation = DeviceRegisteredDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid variant ID.")
    })
    public ResponseEntity<Object> deviceRegister(
            @Parameter(description="User object.",
                    required=true, schema=@Schema(implementation = DeviceRegistrationDto.class))
            @Valid @RequestBody DeviceRegistrationDto deviceRegistrationDto){

        DeviceRegisteredDto deviceRegisteredDto = deviceRegistrationService.registerDevice(deviceRegistrationDto);
        return ResponseHandler
                .generateResponse(
                        "Device Registered.",
                        HttpStatus.CREATED,
                        deviceRegisteredDto);
    }

}
