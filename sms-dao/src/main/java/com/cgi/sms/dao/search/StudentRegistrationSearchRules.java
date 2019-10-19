package com.cgi.sms.dao.search;

import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.service.PredicateType;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cgi.sms.dao.service.PredicateType.*;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

public enum StudentRegistrationSearchRules implements CriteriaRule<StudentRegistrationSearchCriteria> {

    STUDENTNAME(false, LIKE, singletonList("studentName"), StudentRegistrationSearchCriteria::getStudentName),
    REGISTRATIONNUMBRT(false, LIKE, singletonList("registrationNumber"), StudentRegistrationSearchCriteria::getRegistrationNumber),
    DATEOFBIRTH_START_DATE(false, GREATER_THAN_OR_EQUAL, Collections.singletonList("dateOfBirth"), studentRegistrationSearchCriteria -> ofNullable(studentRegistrationSearchCriteria.getDateOfBirth()).map(ZonedDateTime::toString).orElse(null)),
    DATEOFBIRTH_END_DATE(false, LESS_THAN_OR_EQUAL, singletonList("dateOfBirth"), studentRegistrationSearchCriteria -> ofNullable(studentRegistrationSearchCriteria.getDateOfBirth()).map(ZonedDateTime::toString).orElse(null));


    private boolean join;
    private PredicateType predicateType;
    private List<String> criteriaLabelHierarchyList;
    private Function<StudentRegistrationSearchCriteria, String> criteriaValueGetter;

    StudentRegistrationSearchRules(boolean join, PredicateType predicateType, List<String> criteriaLabelHierarchyList, Function<StudentRegistrationSearchCriteria, String> criteriaValueGetter) {
        this.join = join;
        this.predicateType = predicateType;
        this.criteriaLabelHierarchyList = criteriaLabelHierarchyList;
        this.criteriaValueGetter = criteriaValueGetter;
    }

    @Override
    public boolean isJoin() {
        return join;
    }

    @Override
    public PredicateType getPredicateType() {
        return predicateType;
    }

    @Override
    public List<String> getCriteriaLabelHierarchyList() {
        return criteriaLabelHierarchyList;
    }

    @Override
    public Function<StudentRegistrationSearchCriteria, String> getCriteriaValueGetter() {
        return criteriaValueGetter;
    }
}
