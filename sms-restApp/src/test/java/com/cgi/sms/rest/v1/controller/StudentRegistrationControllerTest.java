package com.cgi.sms.rest.v1.controller;

import com.cgi.sms.helper.TimeService;
import com.cgi.sms.rest.dto.StudentRegistrationRequestDto;
import com.cgi.sms.rest.dto.StudentRegistrationResponseDto;
import com.cgi.sms.rest.service.StudentRegistrationRestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.util.DateUtil.parse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentRegistrationControllerTest {

    @InjectMocks
    private StudentRegistrationController studentRegistrationController;

    @Mock
    private StudentRegistrationRestService studentRegistrationRestService;

    @Mock
    private TimeService timeService;

    private Clock clock;
    private Instant instant;

    ZoneId utc = ZoneId.of("UTC");

    @Before
    public void setUp() {
        instant = Instant.parse("2016-01-23T12:32:56Z");
        Clock fixedClock = Clock.fixed(instant, utc);
        timeService.setClock(fixedClock);
    }

    @Test
    public void should_return_student_registration_response_dto() {
        //Given
        StudentRegistrationRequestDto studentRegistrationRequestDto = new StudentRegistrationRequestDto();
        studentRegistrationRequestDto.setAddress("India");
        studentRegistrationRequestDto.setDateOfBirth(parse("2015-02-12"));
        studentRegistrationRequestDto.setStudentName("ram");
        studentRegistrationRequestDto.setRegistrationNumber("200A");


        StudentRegistrationResponseDto studentRegistrationResponseDto = new StudentRegistrationResponseDto();
        studentRegistrationResponseDto.setAddress("India");
        studentRegistrationResponseDto.setRegistrationNumber("200A");
        studentRegistrationResponseDto.setStudentName("ram");
        studentRegistrationResponseDto.setDateOfBirth(parse("2015-02-12"));

        when(studentRegistrationRestService.registerStudentRecord(studentRegistrationRequestDto)).thenReturn(studentRegistrationResponseDto);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        //when
        studentRegistrationResponseDto = studentRegistrationController.registerStudentDetails(studentRegistrationRequestDto);
        //then
        assertThat(studentRegistrationResponseDto).isEqualTo(studentRegistrationResponseDto);
    }

}
