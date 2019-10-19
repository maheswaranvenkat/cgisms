package com.cgi.sms.business.model.response;

import com.cgi.sms.business.model.StudentRegistration;
import lombok.Data;

import java.util.List;

@Data
public class StudentRegistrationSearchResponse {
    private List<StudentRegistration> studentRegistrationList;
    private Boolean hasNextPage;
    private Long totalRecords;
}
