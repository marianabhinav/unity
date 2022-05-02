package com.amcrest.unity.camera.controller;

import com.amcrest.unity.accounting.email.validation.ValidEmail;
import com.amcrest.unity.accounting.response.ResponseHandler;
import com.amcrest.unity.camera.config.CameraConfigService;
import com.amcrest.unity.camera.config.domain.HeartbeatResultDto;
import com.amcrest.unity.camera.config.domain.SyncDataDto;
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

@Slf4j
@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path="api/v1/camera")
@Tag(name = "Camera Module",
        description = "This module is intended to handle all camera related operations.")
public class CameraController {

    private final CameraConfigService cameraConfigService;

    @GetMapping(path = "/config/sync",
            produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Config data changed.\n\n" +
                    "Config data not changed.",
                    content = @Content(
                            schema = @Schema(implementation = SyncDataDto.class))),
            @ApiResponse(responseCode = "404", description = "Device ID is invalid."),
            @ApiResponse(responseCode = "503", description = "Cannot create config.")
    })
    public ResponseEntity<Object> configSync(
            @Parameter(description = "Device Id.", required=true)
            @RequestParam("deviceId") String deviceId,
            @Parameter(description = "Config timestamp.")
            @RequestParam(value = "timestamp", required = false) Long timestamp){

        SyncDataDto syncDataDto = cameraConfigService.syncConfiguration(deviceId, timestamp);
        if(syncDataDto != null) {
            return ResponseHandler
                    .generateResponse(
                            "Config data changed.",
                            HttpStatus.OK,
                            syncDataDto);
        }
        else {
            return ResponseHandler
                    .generateResponse(
                            "Config data not changed.",
                            HttpStatus.OK);
        }
    }

    @GetMapping(path = "/heartbeat",
            produces = { "application/json", "application/xml" })
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(
                            schema = @Schema(implementation = HeartbeatResultDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    public ResponseEntity<Object> heartbeat(
            @Parameter(description = "User Email.")
            @ValidEmail @RequestParam("email") String email){
        HeartbeatResultDto heartbeatResultDto = cameraConfigService.heartbeat(email);
        return ResponseHandler
                .generateResponse(
                        "Success.",
                        HttpStatus.OK,
                        heartbeatResultDto);
    }
}
