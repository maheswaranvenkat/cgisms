package com.cgi.sms.dao.service;

import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.repository.StudentRegistrationRepository;
import com.cgi.sms.helper.TimeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@EntityScan("com.cgi.sms.dao.entity")
@EnableJpaRepositories("com.cgi.sms.dao")
@SpringBootTest(classes = StudentRegistrationDaoH2Test.ApplicationTest.class)
@TransactionConfiguration(defaultRollback = false)
public class StudentRegistrationDaoH2Test {

    @Autowired
    private StudentRegistrationDao studentRegistrationDao;

    @Autowired
    private StudentRegistrationRepository studentRegistrationRepository;

    @Autowired
    private TimeService timeService;

    private Instant instant;
    ZoneId utc = ZoneId.of("Europe/Paris");

    @Before
    public void setUp() {
        instant = Instant.parse("2016-01-23T12:34:56z");
        Clock fixedClock = Clock.fixed(instant, utc);
        timeService.setClock(fixedClock);
    }

    @Test
    @Transactional
    public void should_save_student_registration_into_h2_table() {
        //Given
        StudentRegistrationEntity studentRegistrationEntity = saveStudentRegistrationEntity(
                "Sriram",
                "2007093A",
                "India"
        );

        saveStudentRegistrationEntity(
                "Sriram",
                "2007093B",
                "India"
        );

        saveStudentRegistrationEntity(
                "Sriram",
                "2007093C",
                "India"
        );

        //WHEN
        String registrationNumber = studentRegistrationEntity.getRegistrationNumber();
        StudentRegistrationEntity studentRegistrationEntity1 = studentRegistrationRepository.getByRegistrationNumber(registrationNumber);

        //Then
        assertThat(studentRegistrationEntity1).isNotNull();
        assertThat(registrationNumber).isNotNull();
        assertThat(this.studentRegistrationRepository.getByRegistrationNumber(registrationNumber)).isNotNull();
        assertThat(studentRegistrationEntity1.getStudentName()).isEqualTo("Sriram");
    }

    @Test
    @Transactional
    public void should_update_Student_Registration_into_h2_table() {
        //Given

        //WHEN
        StudentRegistrationEntity studentRegistrationEntity = saveStudentRegistrationEntity(
                "Sriram",
                "2007093C",
                "India"
        );

        //Then
        String registrationNumber = studentRegistrationEntity.getRegistrationNumber();
        StudentRegistrationEntity studentRegistrationEntity1 = studentRegistrationRepository.getByRegistrationNumber(registrationNumber);
        studentRegistrationEntity1.setStudentName("Ram");
        studentRegistrationEntity1.setAddress("Europe");

        StudentRegistrationEntity afterUpdateEntity = studentRegistrationRepository.getByRegistrationNumber(studentRegistrationEntity1.getRegistrationNumber());

        assertThat(afterUpdateEntity.getStudentName()).isEqualTo("Ram");
        assertThat(afterUpdateEntity.getRegistrationNumber()).isEqualTo("2007093C");
        assertThat(afterUpdateEntity.getAddress()).isEqualTo("India");
    }

    @Transactional
    @Test
    public void should_retrieve_Student_Registration_from_h2_table() {
        //Given
        StudentRegistrationEntity studentRegistrationEntity = saveStudentRegistrationEntity(
                "Sriram",
                "2007093C",
                "India"
        );
        //when
        StudentRegistrationEntity studentRegistrationEntity1 = studentRegistrationRepository.findByRegistrationNumber(studentRegistrationEntity.getRegistrationNumber());

        //then
        assertThat(studentRegistrationEntity1.getAddress()).isEqualTo(studentRegistrationEntity.getAddress());
        assertThat(studentRegistrationEntity1.getRegistrationNumber()).isEqualTo(studentRegistrationEntity.getRegistrationNumber());
        assertThat(studentRegistrationEntity1.getStudentName()).isEqualTo(studentRegistrationEntity.getStudentName());
        assertThat(studentRegistrationEntity1.getDateOfBirth()).isEqualTo(studentRegistrationEntity.getDateOfBirth());

    }

    private StudentRegistrationEntity saveStudentRegistrationEntity(String studentName, String registrationNumber, String address) {
        StudentRegistrationEntity studentRegistrationEntity = new StudentRegistrationEntity();
        studentRegistrationEntity.setStudentName(studentName);
        studentRegistrationEntity.setRegistrationNumber(registrationNumber);
        studentRegistrationEntity.setDateOfBirth(timeService.getCurrentDateTime());
        studentRegistrationEntity.setAddress(address);
        StudentRegistrationEntity studentRegistrationEntity1 = studentRegistrationDao.saveStudentRegistration(studentRegistrationEntity);
        return studentRegistrationEntity1;
    }

    @SpringBootApplication(scanBasePackages = {"com.cgi.sms.dao", "com.cgi.sms.helper"})
    public static class ApplicationTest {

    }
}
