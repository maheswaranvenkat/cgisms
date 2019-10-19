package com.cgi.sms.dao.repository;

import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRegistrationRepository extends PagingAndSortingRepository<StudentRegistrationEntity, Long>, JpaSpecificationExecutor<StudentRegistrationEntity> {

    List<StudentRegistrationEntity> findByStudentName(String studentName);

    StudentRegistrationEntity findByRegistrationNumber(String registrationNumber);

    StudentRegistrationEntity getByRegistrationNumber(String registrationNumber);


}
