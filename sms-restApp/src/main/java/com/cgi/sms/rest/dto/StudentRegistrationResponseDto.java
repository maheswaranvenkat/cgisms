package com.cgi.sms.rest.dto;

import com.cgi.sms.rest.converter.JsonDateDeSerializer;
import com.cgi.sms.rest.converter.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
public class StudentRegistrationResponseDto {
    private String studentName;
    private String registrationNumber;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date dateOfBirth;
    private String address;
}
