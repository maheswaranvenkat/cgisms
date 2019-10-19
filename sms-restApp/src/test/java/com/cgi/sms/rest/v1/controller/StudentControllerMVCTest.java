package com.cgi.sms.rest.v1.controller;

import com.cgi.sms.helper.TimeService;
import com.cgi.sms.rest.dto.StudentRegistrationRequestDto;
import com.cgi.sms.rest.dto.StudentRegistrationResponseDto;
import com.cgi.sms.rest.exceptionHandler.GlobalControllerExceptionHandler;
import com.cgi.sms.rest.service.StudentRegistrationRestService;
import com.cgi.sms.rest.v1.PathConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Time;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.cgi.sms.rest.helper.JsonHelper.convertObjectToJson;
import static org.assertj.core.util.DateUtil.parse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StudentControllerMVCTest {
    @InjectMocks
    private StudentRegistrationController studentRegistrationController;

    @Mock
    private StudentRegistrationRestService studentRegistrationRestService;

    private MockMvc mockMvc;

    @Mock
    private TimeService timeService;

    @Before
    public void setUp() {
        when(timeService.getCurrentDateTime()).thenReturn(ZonedDateTime.of(2012, 10, 24, 23, 55, 0, 0, ZoneId.of("UTC")));
        mockMvc = MockMvcBuilders
                .standaloneSetup(studentRegistrationController)
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .build();
    }

    @Test
    public void should_return_200_when_studentregistertion_nominal_case() throws Exception {
        //Given
        StudentRegistrationRequestDto studentRegistrationRequestDto =new StudentRegistrationRequestDto();
        studentRegistrationRequestDto.setRegistrationNumber("2000A");
        studentRegistrationRequestDto.setStudentName("name");
        studentRegistrationRequestDto.setDateOfBirth(parse("2015-02-02"));
        studentRegistrationRequestDto.setAddress("India");

        StudentRegistrationResponseDto studentRegistrationResponseDto = new StudentRegistrationResponseDto();
        BeanUtils.copyProperties(studentRegistrationRequestDto,studentRegistrationResponseDto);

        mockMvc.perform(post(PathConstants.VERSION+PathConstants.SAVE)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJson(studentRegistrationRequestDto).get()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(convertObjectToJson(studentRegistrationResponseDto).get()));

    }
}
