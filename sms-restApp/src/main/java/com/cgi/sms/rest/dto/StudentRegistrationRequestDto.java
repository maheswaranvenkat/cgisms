package com.cgi.sms.rest.dto;

import com.cgi.sms.rest.converter.JsonDateDeSerializer;
import com.cgi.sms.rest.converter.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class StudentRegistrationRequestDto {

    @ApiModelProperty(value="Student Name", position = 1, required = true)
    @JsonProperty("studentName")
    private String studentName;

    @ApiModelProperty(value = "Student Registration Number", position = 2, required = true)
    @JsonProperty("registrationNumber")
    private String registrationNumber;

    @ApiModelProperty(value = "Student Date Of Birth", position = 3, required = true)
    @JsonProperty("dateOfBirth")
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date dateOfBirth;

    @ApiModelProperty(value="Address", position = 4, required = true)
    @JsonProperty("address")
    private String address;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        StudentRegistrationRequestDto that = (StudentRegistrationRequestDto) o;
        return Objects.equals(registrationNumber, that.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }
}
