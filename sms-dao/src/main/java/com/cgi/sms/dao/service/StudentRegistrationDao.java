package com.cgi.sms.dao.service;

import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.repository.StudentRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import static java.util.Objects.isNull;

@Service
public class StudentRegistrationDao {

    @Autowired
    private StudentRegistrationRepository studentRegistrationRepository;

    public StudentRegistrationEntity saveStudentRegistration(final StudentRegistrationEntity studentRegistrationEntity) {
        checkArgument(studentRegistrationEntity);
        return studentRegistrationRepository.save(studentRegistrationEntity);
    }

    public List<StudentRegistrationEntity> getStudentRegistrationByStudentName(String studentName) {
        if(isNull(studentName)) {
            return Collections.emptyList();
        }
        return studentRegistrationRepository.findByStudentName(studentName);
    }

    public StudentRegistrationEntity getByRegistrationNumber(String registrationNumber) {
        if(isNull(registrationNumber)) {
            throw new IllegalArgumentException(" Registration Number can't be null");
        }
        return studentRegistrationRepository.findByRegistrationNumber(registrationNumber);
    }

    public Page<StudentRegistrationEntity> getAllStudentRegistration(Pageable pageable) {
        if(isNull(pageable)) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        return studentRegistrationRepository.findAll(pageable);
    }


    private void checkArgument(StudentRegistrationEntity studentRegistrationEntity) {
        if(isNull(studentRegistrationEntity)) {
            throw new IllegalArgumentException("Student Registration Can't be null");
        }
    }
}
