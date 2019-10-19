package com.cgi.sms.rest.dto;

import com.cgi.sms.rest.converter.JsonDateDeSerializer;
import com.cgi.sms.rest.converter.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class GetAllOrSearchStudentRegistrationResponseDto {

    @ApiModelProperty(value="Student Name", position = 1, required = true)
    private String studentName;

    @ApiModelProperty(value = "Student Registration Number", position = 2, required = true)
    private String registrationNumber;

    @ApiModelProperty(value = "Student Date Of Birth", position = 3, required = true)
    @JsonProperty("dateOfBirth")
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date dateOfBirth;

    @ApiModelProperty(value="Address", position = 4, required = true)
    @JsonProperty("address")
    private String address;

}
