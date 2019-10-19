package com.cgi.sms.business.service.cucumber.steps;

import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.business.service.StudentRegistrationService;
import com.cgi.sms.business.service.cucumber.StepsConfiguration;
import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.service.StudentRegistrationDao;
import com.cgi.sms.helper.TimeService;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@EnableJpaRepositories(basePackages = {"com.cgi.sms.dao"})
@ContextConfiguration(classes = StepsConfiguration.class)
@EntityScan("com.cgi.sms.dao.entity")
public class InitDatabaseSteps {

    @Autowired
    private StudentRegistrationDao studentRegistrationDao;
    @Autowired
    private TimeService timeService;
    @Autowired
    StudentRegistrationService studentRegistrationService;
    @Autowired
    DataSource dataSource;
    @Before
    public void setup() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("h2/h2-schema.sql"));
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("h2/h2-data.sql"));
    }

    @Given("^follow student registration exists in database with below attributes:$")
    public void add_student_registration_to_the_database(DataTable dataTable) {
        dataTable.getGherkinRows()
                .stream()
                .skip(1)
                .forEach(dataTableRow -> {
                    List<String> cells = dataTableRow.getCells();
                    StudentRegistration studentRegistration = getStudentRegistrationMapperFromDataTableRow(cells);
                    studentRegistrationService.saveStudentRegistration(studentRegistration);
                });
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
