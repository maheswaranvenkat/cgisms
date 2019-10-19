package com.cgi.sms.business.mapper;

import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.service.StudentRegistrationDao;
import com.cgi.sms.domain.ErrorCatalog;
import com.cgi.sms.domain.ErrorOrigin;
import com.cgi.sms.exception.ServiceException;
import com.cgi.sms.helper.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class StudentRegistrationMapper {

    @Autowired
    private TimeService timeService;
    @Autowired
    private StudentRegistrationDao studentRegistrationDao;

    public Optional<StudentRegistration> mapResponseToStudentRegistration(final StudentRegistrationEntity studentRegistrationEntity) {
        if(isNull(studentRegistrationEntity)) {
            return Optional.empty();
        }
        final StudentRegistration studentRegistration = new StudentRegistration();
        studentRegistration.setStudentName(studentRegistrationEntity.getStudentName());
        studentRegistration.setRegistrationNumber(studentRegistrationEntity.getRegistrationNumber());
        studentRegistration.setDateOfBirth(studentRegistrationEntity.getDateOfBirth());
        studentRegistration.setAddress(studentRegistrationEntity.getAddress());
        return Optional.of(studentRegistration);
    }

    public Optional<StudentRegistrationEntity> mapToStudentRegistrationEntity(final StudentRegistration studentRegistration) {
        if(isNull(studentRegistration)) {
            return Optional.empty();
        }
        StudentRegistrationEntity studentRegistrationEntity = new StudentRegistrationEntity();
        studentRegistrationEntity.setStudentName(studentRegistration.getStudentName());
        studentRegistrationEntity.setRegistrationNumber(studentRegistration.getRegistrationNumber());
        studentRegistrationEntity.setDateOfBirth(studentRegistration.getDateOfBirth());
        studentRegistrationEntity.setAddress(studentRegistration.getAddress());
        return Optional.of(studentRegistrationEntity);
    }

    public Optional<StudentRegistrationEntity> mapToStudentRegistrationEntityForUpdate(final StudentRegistration studentRegistration, final StudentRegistrationEntity studentRegistrationEntity) {
        if(isNull(studentRegistration) || isNull(studentRegistrationEntity)) {
            return Optional.empty();
        }

        setStudentRegistrationEntityForSaveUpdate(studentRegistration, studentRegistrationEntity);
        return Optional.of(studentRegistrationEntity);
    }

    private void setStudentRegistrationEntityForSaveUpdate(StudentRegistration studentRegistration, StudentRegistrationEntity studentRegistrationEntity){
        if(nonNull(studentRegistration.getAddress())) {
            studentRegistrationEntity.setAddress(studentRegistration.getAddress());
        }
        if(nonNull(studentRegistration.getDateOfBirth())) {
            studentRegistrationEntity.setDateOfBirth(studentRegistration.getDateOfBirth());
        }
        if(nonNull(studentRegistration.getStudentName())) {
            studentRegistrationEntity.setStudentName(studentRegistration.getStudentName());
        }
    }

    public List<StudentRegistration> mapSearchResonseToTechnicalResources(final List<StudentRegistrationEntity> studentRegistrationEntities) {
        checkArguments(studentRegistrationEntities);
        return studentRegistrationEntities.stream()
                .map(studentRegistrationEntity -> mapResponseToStudentRegistration(studentRegistrationEntity).get())
                .collect(Collectors.toList());
    }

    private void checkArguments(List<StudentRegistrationEntity> studentRegistrationEntities) {
        if(CollectionUtils.isEmpty(studentRegistrationEntities)) {
            throw new ServiceException(ErrorCatalog.NO_RECORDS, ErrorOrigin.CLIENT);
        }
    }
}
