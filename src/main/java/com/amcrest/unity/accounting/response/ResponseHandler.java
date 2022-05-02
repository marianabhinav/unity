package com.amcrest.unity.accounting.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseHandler implements Serializable {

    static final String TIMESTAMP = "timestamp";
    static final String STATUS = "status";
    static final String MESSAGE = "message";
    static final String DATA = "data";

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object... responseObj){
        Map<Object, Object> map = new LinkedHashMap<>();
        map.put(TIMESTAMP, Instant.now().getEpochSecond());
        map.put(STATUS, status.value());
        map.put(MESSAGE, message);
        if(!ObjectUtils.isEmpty(responseObj)) {
            map.put(DATA, responseObj);
        }
        return new ResponseEntity<>(map,status);
    }
}
