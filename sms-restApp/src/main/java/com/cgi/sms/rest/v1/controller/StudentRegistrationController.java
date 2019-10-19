package com.cgi.sms.rest.v1.controller;

import com.cgi.sms.rest.dto.*;
import com.cgi.sms.rest.service.StudentRegistrationRestService;
import com.cgi.sms.rest.v1.PathConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
public class StudentRegistrationController {
    @Autowired
    private StudentRegistrationRestService studentRegistrationRestService;

    @PostMapping(value= PathConstants.SAVE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Registering of Student",
            notes="Registering of studentRegistrationRequestDTO", response = StudentRegistrationResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message ="Success, Registration"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable", response = ErrorDto.class)
    })
    public StudentRegistrationResponseDto registerStudentDetails(
            @Valid @RequestBody StudentRegistrationRequestDto studentRegistrationRequestDto) {
        log.info("Rest request to register Student Record: {}, ", studentRegistrationRequestDto);
        return studentRegistrationRestService.registerStudentRecord(studentRegistrationRequestDto);
    }



    @PutMapping(value = PathConstants.UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Student Registration",
            notes="Update Student Registration studentRegistrationRequestDto", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success, Requested Student Registration is updated"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable", response = ErrorDto.class)
    })
    public ResponseEntity<?> updateStudentRegistration(@RequestBody StudentRegistrationRequestDto studentRegistrationRequestDto) {
        studentRegistrationRestService.updateStudentRegistration(studentRegistrationRequestDto);
        log.info("Rest request to update student registration : {}", studentRegistrationRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = PathConstants.SEARCH)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get All or Search Student", response = GetAllStudentRegistrationResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 206, message = "Success, Returns incomplete/partial list of Student Registration Details", response = GetAllStudentRegistrationResponseDto.class),
            @ApiResponse(code = 204, message = "No Record found for the specified criteria"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable", response = ErrorDto.class)
    })
    public ResponseEntity<GetAllStudentRegistrationResponseDto> getAllAndSearchStudent(
            @ApiParam(value = "The page number", defaultValue = "0") @RequestParam(value = "pageNumber", required = false, defaultValue = "0") final Integer pageNumber,
            @ApiParam(value = "The number of records per page", defaultValue = "100") @RequestParam(value = "pageSize", required = false, defaultValue = "100") final Integer pageSize,
            @ApiParam(value = "The Sorting Order (eg. ASC /DESC)", defaultValue = "ASC") @RequestParam(value = "sortingOrder", required = false, defaultValue = "ASC") final String sortingOrder,
            @ApiParam(value = "The Sorting Order (eg. ASC /DESC)", defaultValue = "registrationNumber") @RequestParam(value = "sortingBy", required = false, defaultValue = "registrationNumber") final String sortingBy
    ) {

        GetAllStudentRegistrationResponseDto getAllStudentRegistrationResponseDto = studentRegistrationRestService.searchForStudentRegistration(pageNumber,pageSize,sortingBy,sortingOrder);
        if(Objects.nonNull(getAllStudentRegistrationResponseDto)) {
            if(getAllStudentRegistrationResponseDto.getTotalRecords() < pageSize) {
                return new ResponseEntity<>(getAllStudentRegistrationResponseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(getAllStudentRegistrationResponseDto, HttpStatus.PARTIAL_CONTENT);
            }
        }
        return null;
    }

}
