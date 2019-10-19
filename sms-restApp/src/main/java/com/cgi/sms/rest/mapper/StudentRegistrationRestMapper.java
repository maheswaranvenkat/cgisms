package com.cgi.sms.rest.mapper;

import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.domain.ErrorCatalog;
import com.cgi.sms.domain.ErrorOrigin;
import com.cgi.sms.exception.ServiceException;
import com.cgi.sms.rest.dto.StudentRegistrationRequestDto;
import com.cgi.sms.rest.dto.StudentRegistrationResponseDto;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class StudentRegistrationRestMapper {

    public Optional<StudentRegistration> mapToStudentRegistrationModel(StudentRegistrationRequestDto studentRegistrationRequestDto) {
        if (isNull(studentRegistrationRequestDto)) {
            return Optional.empty();
        }

        StudentRegistration studentRegistration = new StudentRegistration();
        studentRegistration.setStudentName(studentRegistrationRequestDto.getStudentName());
        studentRegistration.setRegistrationNumber(studentRegistrationRequestDto.getRegistrationNumber());
        studentRegistration.setDateOfBirth(studentRegistrationRequestDto.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()));
        studentRegistration.setAddress(studentRegistrationRequestDto.getAddress());
        return Optional.of(studentRegistration);
    }

    public Optional<StudentRegistrationResponseDto> mapResponseToStudentRegistrationResponseDto(StudentRegistration studentRegistration) {
        if (isNull(studentRegistration)) {
            return Optional.empty();
        }

        final StudentRegistrationResponseDto studentRegistrationResponseDto = new StudentRegistrationResponseDto();
        studentRegistrationResponseDto.setStudentName(studentRegistration.getStudentName());
        studentRegistrationResponseDto.setRegistrationNumber(studentRegistration.getRegistrationNumber());
        studentRegistrationResponseDto.setDateOfBirth(Date.from(studentRegistration.getDateOfBirth().toInstant()));
        studentRegistrationResponseDto.setAddress(studentRegistration.getAddress());
        return Optional.of(studentRegistrationResponseDto);
    }

    public List<StudentRegistrationResponseDto> mapSearchResponseToStudentRegistrationResponseDtos(final List<StudentRegistration> studentRegistrationList) {
        if(CollectionUtils.isEmpty(studentRegistrationList)) {
            throw new ServiceException(ErrorCatalog.NO_RECORDS, ErrorOrigin.CLIENT);
        }

        return studentRegistrationList.stream()
                .map(studentRegistration -> mapResponseToStudentRegistrationResponseDto(studentRegistration).get())
                .collect(Collectors.toList());
    }

}
