package com.cgi.sms.rest.v1.controller;

import com.cgi.sms.rest.dto.ErrorDto;
import com.cgi.sms.rest.dto.StudentRegistrationRequestDto;
import com.cgi.sms.rest.exceptionHandler.GlobalControllerExceptionHandler;
import com.cgi.sms.rest.service.StudentRegistrationRestService;
import com.cgi.sms.rest.v1.PathConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.cgi.sms.rest.helper.JsonHelper.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static com.cgi.sms.rest.helper.JsonHelper.convertObjectToJson;
import static org.assertj.core.util.DateUtil.parse;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(Parameterized.class)
public class StudentRegistrationController_FocusOn_DtoValidationTest {

    private String studentName;
    private String registrationNumber;
    private String dateOfBirth;
    private String address;
    private String errorMessage;

    private MockMvc mockMvc;
    private StudentRegistrationController studentRegistrationController;
    private StudentRegistrationRestService studentRegistrationRestService;

    @Before
    public void setUp() {
        studentRegistrationController = new StudentRegistrationController();
        mockMvc = MockMvcBuilders
                .standaloneSetup(studentRegistrationController)
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .build();

        studentRegistrationRestService = mock(StudentRegistrationRestService.class);
        ReflectionTestUtils.setField(studentRegistrationController, "studentRegistrationRestService", studentRegistrationRestService);
    }

    public StudentRegistrationController_FocusOn_DtoValidationTest(
            String studentName,
            String registrationNumber,
            String dateOfBirth,
            String address,
            String errorMessage
    ){
        this.studentName = studentName;
        this.registrationNumber = registrationNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.errorMessage = errorMessage;
    }
    @Parameterized.Parameters
    public static Collection inputs() {
        return Arrays.asList(
                new Object[][]{
                        {null, "2009093A", "2010-01-01", "India", "Error in Student Registration Student Name should not be empty"},
                        {"ram", null, "2010-01-01", "India", "Error in Student Registration Name should be empty"},
                        {"ram", "2009093A", null, "India", "Error in Student Registration Data of Birth Should not be Empty"},
                        {"ram", "2009093A", "2010-01-01", null, "Error in Student Registration Address should not be empty"},
                }
        );
    }

    @Test
    public void validateDto() throws Exception {
        StudentRegistrationRequestDto studentRegistrationRequestDto = new StudentRegistrationRequestDto();
        studentRegistrationRequestDto.setStudentName(studentName);
        studentRegistrationRequestDto.setRegistrationNumber(registrationNumber);
        studentRegistrationRequestDto.setDateOfBirth(parse(dateOfBirth));
        studentRegistrationRequestDto.setAddress(address);

        String message = errorMessage;

        mockMvc.perform(MockMvcRequestBuilders
        .post(PathConstants.VERSION + PathConstants.SAVE)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJson(studentRegistrationRequestDto).orElse("{}")))
                .andDo(print())
                .andExpect(
                        content()
                        .json(convertObjectToJson(new ErrorDto(2, message))
                        .orElseThrow(() -> new IllegalArgumentException("Error while parsing the ErrorDto")))

                );

    }

}
