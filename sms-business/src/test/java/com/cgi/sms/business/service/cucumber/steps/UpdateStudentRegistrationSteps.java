package com.cgi.sms.business.service.cucumber.steps;

import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.business.service.StudentRegistrationService;
import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.service.StudentRegistrationDao;
import com.cgi.sms.exception.ServiceException;
import com.cgi.sms.helper.TimeService;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UpdateStudentRegistrationSteps {
    @Autowired
    private StudentRegistrationDao studentRegistrationDao;
    @Autowired
    private StudentRegistrationService studentRegistrationService;
    @Autowired
    private TimeService timeService;

    ServiceException actualServiceException;
    Boolean actualResponse;
    StudentRegistrationEntity studentRegistrationEntity;
    StudentRegistrationEntity oldStudentRegistrationEntity;
    StudentRegistration studentRegistration;

    public void update_service(DataTable dataTable) {
        dataTable.getGherkinRows()
                .stream()
                .skip(1)
                .forEach(dataTableRow -> {
                    List<String> cells = dataTableRow.getCells();
                    studentRegistration = getStudentRegistrationMapperFromDataTableRow(cells);
                    updateStudentRegistration();
                });
    }

    private void updateStudentRegistration() {
        try {
            actualResponse = studentRegistrationService.updateStudentRegistration(studentRegistration);
        } catch (ServiceException e) {
            actualServiceException = e;
        }
    }

    @Then("^should return true:$")
    public void verify_update_student_registration_results() {
        StudentRegistrationEntity studentRegistrationEntity = studentRegistrationDao.getByRegistrationNumber(studentRegistration.getRegistrationNumber());
        assertThat(studentRegistrationEntity.getStudentName()).isEqualTo(studentRegistration.getStudentName());
        assertThat(studentRegistrationEntity.getRegistrationNumber()).isEqualTo(studentRegistration.getRegistrationNumber());
    }

    private StudentRegistration getStudentRegistrationMapperFromDataTableRow(List<String> cells) {
        StudentRegistration studentRegistration = new StudentRegistration();
        studentRegistration.setStudentName(cells.get(0));
        studentRegistration.setRegistrationNumber(cells.get(1));
        studentRegistration.setDateOfBirth(timeService.getCurrentDateTime());
        studentRegistration.setAddress(cells.get(3));
        return studentRegistration;
    }
}
