package com.amcrest.unity.accounting.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Violation {

    private final String fieldName;

    private final String message;
}
