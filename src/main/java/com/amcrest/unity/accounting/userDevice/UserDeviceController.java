package com.amcrest.unity.accounting.userDevice;

import com.amcrest.unity.accounting.response.ResponseHandler;
import com.amcrest.unity.accounting.userDevice.domain.AddUserDeviceDto;
import com.amcrest.unity.accounting.userDevice.domain.GetUserDeviceDetailsDto;
import com.amcrest.unity.accounting.userDevice.domain.GetUserDeviceDto;
import com.amcrest.unity.accounting.userDevice.domain.GetUserDeviceListDto;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path="api/v1/device")
@Tag(name = "User device Module",
        description = "This module is intended to handle all the user related devices operations.")
@SecurityRequirement(name = "JwtAuth")
public class UserDeviceController {

    private final UserDeviceService userDeviceService;

    @PostMapping(consumes = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device added."),
            @ApiResponse(responseCode = "401", description = "Invalid or no authorization token provided."),
            @ApiResponse(responseCode = "404", description = "Invalid serial number."),
            @ApiResponse(responseCode = "409", description = "This device already registered for this user.")
    })
    public ResponseEntity<Object> addDevice(
            @Parameter(description="Add device object.",
                    required=true, schema=@Schema(implementation = AddUserDeviceDto.class))
            @Valid @RequestBody AddUserDeviceDto userDevice){
        userDeviceService.addDevice(userDevice);
        return ResponseHandler
                .generateResponse(
                        "Device added.",
                        HttpStatus.CREATED);
    }

    @GetMapping(produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(
                            schema = @Schema(implementation = GetUserDeviceListDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or no authorization token provided.")
    })
    public ResponseEntity<Object> getUserDeviceList(){
        GetUserDeviceListDto getUserDeviceListDto =  userDeviceService.getUserDeviceList();
        return ResponseHandler
                .generateResponse(
                        "Success.",
                        HttpStatus.OK,
                        getUserDeviceListDto);
    }

    @GetMapping(path = "/details",
            produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(
                            schema = @Schema(implementation = GetUserDeviceDetailsDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or no authorization token provided."),
            @ApiResponse(responseCode = "404", description = "Invalid serial number.")
    })
    public ResponseEntity<Object> getUserDeviceDetails(
            @Parameter(description = "Serial Number.", required=true)
            @Valid @RequestParam("serialNumber") String serialNumber){
        GetUserDeviceDetailsDto getUserDeviceDetailsDto =  userDeviceService.getUserDeviceDetails(serialNumber);
        return ResponseHandler
                .generateResponse(
                        "Success.",
                        HttpStatus.OK,
                        getUserDeviceDetailsDto);
    }

    @DeleteMapping(produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device deleted."),
            @ApiResponse(responseCode = "401", description = "Invalid or no authorization token provided."),
            @ApiResponse(responseCode = "404", description = "Invalid serial number.")
    })
    public ResponseEntity<Object> deleteUserDevice(
            @Parameter(description = "Serial Number.", required=true)
            @Valid @RequestParam("serialNumber") String serialNumber){
        userDeviceService.deleteUserDevice(serialNumber);
        return ResponseHandler
                .generateResponse(
                        "Device deleted.",
                        HttpStatus.OK);
    }
}
