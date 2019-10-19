package com.cgi.sms.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class ErrorDto {

    @JsonProperty("code")
    private int errorCode;

    @JsonProperty("message")
    private String errorMessage;

}
