package com.cgi.sms.dao.service;

import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.repository.StudentRegistrationRepository;
import com.cgi.sms.dao.search.SearchSpecification;
import com.cgi.sms.dao.search.StudentRegistrationSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentRegistrationDaoTest {
    @InjectMocks
    private StudentRegistrationDao studentRegistrationDao;

    @Mock
    private StudentRegistrationRepository studentRegistrationRepository;

    @Mock
    private SearchSpecification<StudentRegistrationSearchCriteria, StudentRegistrationEntity> searchSpecification;

    @Mock
    private Specification<StudentRegistrationEntity> studentRegistrationEntitySpecification;

    @Test
    public void should_return_illegalArgumentException_when_studentregistration_is_null() {
        //Given
        StudentRegistrationEntity studentRegistrationEntity = null;
        //When
        studentRegistrationDao.saveStudentRegistration(studentRegistrationEntity);
        //Then - IllegalArgumentExcption is Thrown
    }

    @Test
    public void should_save_Student_Registration_nominal_case() {
        //Given
        StudentRegistrationEntity expected = new StudentRegistrationEntity();
        StudentRegistrationEntity studentRegistrationEntity = new StudentRegistrationEntity();
        when(studentRegistrationRepository.save(studentRegistrationEntity)).thenReturn(expected);
        //When
        StudentRegistrationEntity actual = studentRegistrationDao.saveStudentRegistration(studentRegistrationEntity);
        //Then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
        verify(studentRegistrationRepository).save(studentRegistrationEntity);
    }

    @Test
    public void should_return_matching_conditions_of_search_criteria_met(){
        //Given
        StudentRegistrationEntity studentRegistrationEntity = new StudentRegistrationEntity();
        studentRegistrationEntity.setStudentName("Sriram");

        Pageable pageable = getPageable();
        List<StudentRegistrationEntity> studentRegistrationEntities = new ArrayList<>();
        studentRegistrationEntities.add(studentRegistrationEntity);

        Page<StudentRegistrationEntity> page = new PageImpl<>(studentRegistrationEntities, pageable, studentRegistrationEntities.size());

        when(studentRegistrationRepository.findAll(pageable)).thenReturn(page);
        //when
        List<StudentRegistrationEntity> studentRegistrationEntityList = studentRegistrationDao.getAllStudentRegistration(pageable).getContent();

        //Then
        assertThat(studentRegistrationEntities.size()).isEqualTo(1);
        assertThat(studentRegistrationEntities.get(0).getStudentName()).isEqualTo(studentRegistrationEntity.getStudentName());
    }

    private Pageable getPageable() {
        return new PageRequest(0, 2, Sort.Direction.ASC, "studentName");
    }
}
