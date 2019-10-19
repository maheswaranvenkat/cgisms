package com.cgi.sms.rest.v1.controller;

import com.cgi.sms.SmsApplication;
import com.cgi.sms.helper.TimeService;
import com.cgi.sms.rest.dto.StudentRegistrationRequestDto;
import com.cgi.sms.rest.dto.StudentRegistrationResponseDto;
import com.cgi.sms.rest.v1.PathConstants;
import cucumber.api.java.cs.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static com.cgi.sms.rest.helper.JsonHelper.convertObjectToJson;
import static org.assertj.core.util.DateUtil.parse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SmsApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-endtoendtest.properties")
public class StudentRegistrationControllerE2ETest {
    @Autowired
    private MockMvc mockMvc;
    private static final String PATHCONSTANTVERSION = PathConstants.VERSION;

    @Autowired
    private TimeService timeService;
    private Instant instant;
    ZoneId utc = ZoneId.of("UTC");

    @Before
    public void setUp() {
        instant = Instant.parse("2018-08-14T12:05:05.258Z");
        Clock fixedClock = Clock.fixed(instant,utc);
        timeService.setClock(fixedClock);
    }

    @Test
    public void should_return_200_when_registertion_nominal_case() throws Exception{
        //Given
        StudentRegistrationRequestDto studentRegistrationRequestDto= new StudentRegistrationRequestDto();
        studentRegistrationRequestDto.setStudentName("ram");
        studentRegistrationRequestDto.setRegistrationNumber("200910A");
        studentRegistrationRequestDto.setDateOfBirth(parse("2015-02-12"));
        studentRegistrationRequestDto.setAddress("Incia");

        StudentRegistrationResponseDto registrationResponseDto = new StudentRegistrationResponseDto();
        BeanUtils.copyProperties(studentRegistrationRequestDto, registrationResponseDto);

        //When
        mockMvc.perform(post(PathConstants.VERSION + PathConstants.SAVE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(studentRegistrationRequestDto).get()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void should_return_200_when_update_student_registration_nomincal_case() throws Exception{
        //Given
        StudentRegistrationRequestDto studentRegistrationRequestDto = new StudentRegistrationRequestDto();
        studentRegistrationRequestDto.setAddress("India");
        studentRegistrationRequestDto.setStudentName("ram");
        studentRegistrationRequestDto.setDateOfBirth(parse("2015-02-12"));
        studentRegistrationRequestDto.setRegistrationNumber("200910A");

        mockMvc.perform(put(PathConstants.VERSION + PathConstants.UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(studentRegistrationRequestDto).get()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void should_return_206_when_get_all_student_registration_nominal_case() throws Exception{


        mockMvc.perform(
                get(PathConstants.VERSION + PathConstants.SEARCH)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isPartialContent());


    }
}
