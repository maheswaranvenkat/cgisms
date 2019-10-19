package com.cgi.sms.rest.service;

import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.business.model.response.StudentRegistrationSearchResponse;
import com.cgi.sms.business.service.StudentRegistrationService;
import com.cgi.sms.rest.dto.GetAllOrSearchStudentRegistrationResponseDto;
import com.cgi.sms.rest.dto.GetAllStudentRegistrationResponseDto;
import com.cgi.sms.rest.dto.StudentRegistrationRequestDto;
import com.cgi.sms.rest.dto.StudentRegistrationResponseDto;
import com.cgi.sms.rest.mapper.StudentRegistrationRestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StudentRegistrationRestService {

    @Autowired
    private StudentRegistrationService studentRegistrationService;

    @Autowired
    private StudentRegistrationRestMapper studentRegistrationRestMapper;
    @Autowired
    private Validator validator;


    public StudentRegistrationResponseDto registerStudentRecord(@Valid final StudentRegistrationRequestDto studentRegistrationRequestDto) {
        validateDto(studentRegistrationRequestDto);
        StudentRegistration studentRegistration = mapToStudentRegistrationModel(studentRegistrationRequestDto);
        StudentRegistration studentRegistration1 =  studentRegistrationService.saveStudentRegistration(studentRegistration);
        return mapToStudentRegistrationResponseDto(studentRegistration1);
    }

    public Boolean updateStudentRegistration(final StudentRegistrationRequestDto studentRegistrationRequestDto) {
        StudentRegistration studentRegistration = mapToTechnicalResourceModel(studentRegistrationRequestDto);
        return studentRegistrationService.updateStudentRegistration(studentRegistration);
    }

    public GetAllStudentRegistrationResponseDto searchForStudentRegistration(
            final Integer pageNumber,
            final Integer pageSize,
            final String  sortBy,
            final String  sortOrder) {
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        StudentRegistrationSearchResponse studentRegistrationSearchResponse =
                studentRegistrationService.getAllStudentRegistration(pageable);
        return mapToGetAllStudentRegistrationResponseDto(studentRegistrationSearchResponse);
    }

    private GetAllStudentRegistrationResponseDto mapToGetAllStudentRegistrationResponseDto(StudentRegistrationSearchResponse studentRegistrationSearchResponse) {
        GetAllStudentRegistrationResponseDto getAllStudentRegistrationResponseDto = new GetAllStudentRegistrationResponseDto();
        List<StudentRegistrationResponseDto> studentRegistrationResponseDtoList = studentRegistrationRestMapper.mapSearchResponseToStudentRegistrationResponseDtos(studentRegistrationSearchResponse.getStudentRegistrationList());
        getAllStudentRegistrationResponseDto.setRegistrationResponseDtoList(studentRegistrationResponseDtoList);
        getAllStudentRegistrationResponseDto.setHasNextPage(studentRegistrationSearchResponse.getHasNextPage());
        getAllStudentRegistrationResponseDto.setTotalRecords(studentRegistrationSearchResponse.getTotalRecords());
        return getAllStudentRegistrationResponseDto;
    }

    private Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return new PageRequest(pageNumber, pageSize, direction, sortBy);
    }
    private StudentRegistration mapToTechnicalResourceModel(StudentRegistrationRequestDto studentRegistrationRequestDto) {
        Optional<StudentRegistration> optionalStudentRegistration = studentRegistrationRestMapper.mapToStudentRegistrationModel(studentRegistrationRequestDto);
        return optionalStudentRegistration.orElseThrow(() -> new IllegalArgumentException("Student Registration Entity Object is empty"));
    }

    private void validateDto(@Valid StudentRegistrationRequestDto studentRegistrationRequestDto) {
        Set<ConstraintViolation<StudentRegistrationRequestDto>> violations = validator.validate(studentRegistrationRequestDto);
        if(!violations.isEmpty()) {
            String exception = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(","));
            throw new ConstraintViolationException(exception, violations);
        }
    }

    private StudentRegistration mapToStudentRegistrationModel(StudentRegistrationRequestDto studentRegistrationRequestDto) {
        Optional<StudentRegistration> optionalStudentRegistration = studentRegistrationRestMapper.mapToStudentRegistrationModel(studentRegistrationRequestDto);
        return optionalStudentRegistration.orElseThrow(() -> new IllegalArgumentException(" Student Registration Entity Object is empty"));
    }

    private StudentRegistrationResponseDto mapToStudentRegistrationResponseDto(StudentRegistration studentRegistration) {
        Optional<StudentRegistrationResponseDto> optionalStudentRegistrationResponseDto = studentRegistrationRestMapper.mapResponseToStudentRegistrationResponseDto(studentRegistration);
        return optionalStudentRegistrationResponseDto.orElseThrow(() -> new IllegalArgumentException("Student Regoistration response object is empty"));
    }
}
