package com.cgi.sms.dao.entity;

import com.cgi.sms.dao.converter.ZonedDateTimeConverter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity(name = "student_registration")
@Table(name = "student_registration")
@Getter
@Setter
public class StudentRegistrationEntity implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "dob")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime dateOfBirth;

    @Column(name = "address")
    private String address;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        StudentRegistrationEntity that = (StudentRegistrationEntity) o;
        return Objects.equals(registrationNumber, that.registrationNumber);
    }
}
