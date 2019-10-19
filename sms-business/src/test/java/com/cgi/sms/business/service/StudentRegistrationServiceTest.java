package com.cgi.sms.business.service;

import com.cgi.sms.business.mapper.StudentRegistrationMapper;
import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.search.SearchSpecification;
import com.cgi.sms.dao.service.StudentRegistrationDao;
import com.cgi.sms.domain.ErrorCatalog;
import com.cgi.sms.exception.ServiceException;
import com.cgi.sms.helper.TimeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentRegistrationServiceTest {

    @InjectMocks
    private StudentRegistrationService studentRegistrationService;

    @Mock
    private StudentRegistrationDao studentRegistrationDao;

    @Mock
    private StudentRegistrationMapper studentRegistrationMapper;

    @Mock
    private SearchSpecification searchSpecification;

    @Mock
    private Specification<StudentRegistrationEntity> specification;
    @Mock
    private TimeService timeService;
    private Instant instant;
    ZoneId utc = ZoneId.of("Europe/Paris");

    @Before
    public void setUp() {
        instant = Instant.parse("2016-01-23T12:34:56Z");
        Clock fixedClock = Clock.fixed(instant, utc);
        timeService.setClock(fixedClock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_return_exception_when_student_registration_is_empty() {
       //Given
        StudentRegistration studentRegistration = null;
        when(studentRegistrationMapper.mapToStudentRegistrationEntity(studentRegistration)).thenReturn(Optional.empty());
        //When
        studentRegistrationService.saveStudentRegistration(studentRegistration);
        //then
    }

    @Test(expected = ServiceException.class)
    public void should_throw_service_exception_when_repository_call_fail() {
        //Given
        StudentRegistration studentRegistration = new StudentRegistration();
        studentRegistration.setStudentName("sram");

        StudentRegistrationEntity studentRegistrationEntity = new StudentRegistrationEntity();

        when(studentRegistrationMapper.mapToStudentRegistrationEntity(studentRegistration)).thenReturn(Optional.of(studentRegistrationEntity));
        when(studentRegistrationDao.saveStudentRegistration(studentRegistrationEntity)).thenThrow(new RuntimeException());

        try {
            studentRegistrationService.saveStudentRegistration(studentRegistration);
        } catch (ServiceException e) {
            //Then
            assertThat(e.getErrorCatalog()).isEqualTo(ErrorCatalog.DATA_BASE_ERROR);

        }
    }

}
