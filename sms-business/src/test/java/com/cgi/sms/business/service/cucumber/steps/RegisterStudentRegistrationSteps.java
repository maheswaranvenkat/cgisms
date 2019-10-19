package com.cgi.sms.business.service.cucumber.steps;

import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.business.service.StudentRegistrationService;
import com.cgi.sms.dao.service.StudentRegistrationDao;
import com.cgi.sms.exception.ServiceException;
import com.cgi.sms.helper.TimeService;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RegisterStudentRegistrationSteps {
    @Autowired
    private StudentRegistrationDao studentRegistrationDao;
    @Autowired
    private StudentRegistrationService studentRegistrationService;
    @Autowired
    private TimeService timeService;

    ServiceException actualServiceException;
    StudentRegistration actualStudentRegistration;

    @When("^I try to register student registration with below attributes:$")
    public void register_service(DataTable dataTable) {
        dataTable.getGherkinRows()
                .stream()
                .skip(1)
                .forEach(dataTableRow -> {
                    List<String> cells = dataTableRow.getCells();
                    StudentRegistration studentRegistration = getStudentRegistrationMapperFromDataTableRow(cells);
                    try {
                        actualStudentRegistration = studentRegistrationService.saveStudentRegistration(studentRegistration);
                    } catch (ServiceException e) {
                        actualServiceException = e;
                    } catch (Exception e) {

                    }
                });
    }

    @Then("^should return error with code (.*) and the message (.*)$")
    public void verify_student_registration_results(int code, String message) {
        assertThat(actualServiceException).isNotNull();
        assertThat(actualServiceException.getErrorCatalog().getCode()).isEqualTo(code);
        assertThat(actualServiceException.getErrorCatalog().getMessage()).isEqualTo(message);
    }

    @Then("^should return the student registration below:$")
    public void verify_student_registration_inserting_into_database(DataTable dataTable) {
        StudentRegistration studentRegistration = ToStudentRegistration(dataTable);
        assertThat(actualStudentRegistration.getStudentName()).isEqualTo(studentRegistration.getStudentName());
        assertThat(actualStudentRegistration.getAddress()).isEqualTo(studentRegistration.getAddress());
        assertThat(actualStudentRegistration.getDateOfBirth()).isEqualTo(studentRegistration.getDateOfBirth());
        assertThat(actualStudentRegistration.getRegistrationNumber()).isEqualTo(studentRegistration.getRegistrationNumber());
    }

    private StudentRegistration ToStudentRegistration(DataTable dataTable) {
        DataTableRow dataTableRow = dataTable.getGherkinRows().get(1);
        List<String> cells = dataTableRow.getCells();
        return getStudentRegistrationMapperFromDataTableRow(cells);
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
