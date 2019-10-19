drop table if exists student_registration;

CREATE TABLE student_registration
(
    student_name varchar(64) Not Null ,
    registration_number varchar(64) Not Null ,
    dob timestamp Not Null,
    address varchar (64) Not Null ,
    CONSTRAINT PK_student_registratio  PRIMARY KEY
    (
        registration_number
    )
);