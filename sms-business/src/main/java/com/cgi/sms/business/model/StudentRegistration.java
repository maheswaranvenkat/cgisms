package com.cgi.sms.business.model;


import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class StudentRegistration {

    private String studentName;

    private String registrationNumber;

    private ZonedDateTime dateOfBirth;

    private String address;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        StudentRegistration that = (StudentRegistration) o;
        return Objects.equals(registrationNumber, that.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }
}
