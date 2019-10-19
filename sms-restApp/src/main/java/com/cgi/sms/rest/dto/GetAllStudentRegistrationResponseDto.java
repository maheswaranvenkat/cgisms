package com.cgi.sms.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetAllStudentRegistrationResponseDto {
    private List<StudentRegistrationResponseDto> registrationResponseDtoList;
    private Boolean hasNextPage;
    private Long totalRecords;
}
