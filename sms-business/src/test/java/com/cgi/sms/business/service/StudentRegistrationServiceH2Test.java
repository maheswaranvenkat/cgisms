package com.cgi.sms.business.service;

import com.cgi.sms.business.mapper.StudentRegistrationMapper;
import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.repository.StudentRegistrationRepository;
import com.cgi.sms.helper.TimeService;
import cucumber.api.java.cs.A;
import cucumber.api.java.gl.E;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import sun.management.resources.agent;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@EntityScan("com.cgi.sms.dao.entity")
@EnableJpaRepositories(basePackages = {"com.cgi.sms.business", "com.cgi.sms.dao", "com.cgi.sms.helper"})
@SpringBootTest(classes = StudentRegistrationServiceH2Test.ApplicationTest.class)
@EnableTransactionManagement
public class StudentRegistrationServiceH2Test {

    @Autowired
    private StudentRegistrationService studentRegistrationService;
    @Autowired
    private StudentRegistrationMapper studentRegistrationMapper;
    @Autowired
    private StudentRegistrationRepository studentRegistrationRepository;
    @Autowired
    private TimeService timeService;
    private Instant instant;
    ZoneId utc = ZoneId.of("Europe/Paris");
    @Before
    public void setUp() {
        instant = Instant.parse("2016-01-23T12:34:56Z");
        Clock fixedClock = Clock.fixed(instant, utc);
        timeService.setClock(fixedClock);
    }

    @Transactional
    @Test
    public void should_save_Student_Registration_into_h2_table() {
        //Given
        StudentRegistration studentRegistration = new StudentRegistration();
        studentRegistration.setStudentName("Sriram");
        studentRegistration.setRegistrationNumber("2007093A");
        studentRegistration.setDateOfBirth(timeService.getCurrentDateTime());
        studentRegistration.setAddress("india");
        //when
        StudentRegistration response = studentRegistrationService.saveStudentRegistration(studentRegistration);
        //Then
        assertThat(response).isNotNull();

        StudentRegistrationEntity actual = studentRegistrationRepository.getByRegistrationNumber(studentRegistration.getRegistrationNumber());
        assertThat(actual.getStudentName()).isEqualTo(response.getStudentName());
        assertThat(actual.getDateOfBirth()).isEqualTo(response.getDateOfBirth());
        assertThat(actual.getRegistrationNumber()).isEqualTo(response.getRegistrationNumber());
        assertThat(actual.getAddress()).isEqualTo(response.getAddress());
    }

    @Transactional
    @Test
    public void should_update_Student_Registration_into_h2_table() {
        //Given
        StudentRegistration studentRegistrationForSave = new StudentRegistration();
        studentRegistrationForSave.setStudentName("Ram");
        studentRegistrationForSave.setRegistrationNumber("2007093A");
        studentRegistrationForSave.setDateOfBirth(timeService.getCurrentDateTime());
        studentRegistrationForSave.setAddress("INDIA");

        StudentRegistration studentRegistrationforUpdate = new StudentRegistration();
        studentRegistrationforUpdate.setStudentName("ram");
        studentRegistrationforUpdate.setRegistrationNumber("2007093A");
        studentRegistrationforUpdate.setDateOfBirth(timeService.getCurrentDateTime());
        studentRegistrationforUpdate.setAddress("Europe");

        StudentRegistration saveResponse = studentRegistrationService.saveStudentRegistration(studentRegistrationForSave);

        //when
        try {
            boolean upateResponse = studentRegistrationService.updateStudentRegistration(studentRegistrationforUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Then
        StudentRegistrationEntity actual = studentRegistrationRepository.findByRegistrationNumber(studentRegistrationForSave.getRegistrationNumber());
        assertThat(actual.getAddress()).isEqualTo(studentRegistrationforUpdate.getAddress());
        assertThat(actual.getRegistrationNumber()).isEqualTo(studentRegistrationforUpdate.getRegistrationNumber());
        assertThat(actual.getDateOfBirth()).isEqualTo(studentRegistrationforUpdate.getDateOfBirth());
        assertThat(actual.getStudentName()).isEqualTo(studentRegistrationforUpdate.getStudentName());
    }


    @SpringBootApplication(scanBasePackages = {"com.cgi.sms.dao", "com.cgi.sms.business", "com.cgi.sms.helper"})
    public static class ApplicationTest {

    }



}


