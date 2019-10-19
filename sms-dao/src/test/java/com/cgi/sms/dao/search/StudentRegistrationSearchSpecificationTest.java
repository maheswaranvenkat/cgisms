package com.cgi.sms.dao.search;

import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import gherkin.lexer.Pa;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Persistence;
import javax.persistence.criteria.*;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentRegistrationSearchSpecificationTest {
    @InjectMocks
    private SearchSpecification<StudentRegistrationSearchCriteria, StudentRegistrationEntity> resourceSpecification;

    @Mock
    private Root<StudentRegistrationEntity> registrationEntityRootMock;

    @Mock
    private CriteriaQuery<?> criteriaQueryMock;

    @Mock
    private CriteriaBuilder criteriaBuilderMock;

    @Test
    public void should_return_specification_as_we_expected() {
        StudentRegistrationEntity studentRegistrationEntity = new StudentRegistrationEntity();

        studentRegistrationEntity.setId(new Long("1"));
        studentRegistrationEntity.setStudentName("Sriram");
        studentRegistrationEntity.setRegistrationNumber("2007093A");
        studentRegistrationEntity.setAddress("India");

        StudentRegistrationSearchCriteria studentRegistrationSearchCriteria = new StudentRegistrationSearchCriteria();
        studentRegistrationSearchCriteria.setId(new Long("1"));
        studentRegistrationSearchCriteria.setStudentName("Sriram");
        studentRegistrationSearchCriteria.setRegistrationNumber("2007093A");
        studentRegistrationSearchCriteria.setAddress("India");

        Path registrationMock = mock(Path.class);
        Path studentNameMock = mock(Path.class);

        when(registrationEntityRootMock.get(StudentRegistrationSearchRules.REGISTRATIONNUMBRT.getCriteriaLabelHierarchyList().get(0).toLowerCase())).thenReturn(registrationMock);
        when(registrationMock.as(String.class)).thenReturn(registrationMock);

        when(registrationEntityRootMock.get(StudentRegistrationSearchRules.STUDENTNAME.getCriteriaLabelHierarchyList().get(0).toLowerCase())).thenReturn(studentNameMock);
        when(studentNameMock.as(String.class)).thenReturn(studentNameMock);

        Predicate registrationNumberPredicateMock = mock(Predicate.class);
        Predicate studentNamePredicateMock = mock(Predicate.class);

        when(criteriaBuilderMock.like(criteriaBuilderMock.lower(registrationMock), studentRegistrationSearchCriteria.getRegistrationNumber().toLowerCase())).thenReturn(registrationNumberPredicateMock);

        when(criteriaBuilderMock.like(criteriaBuilderMock.lower(studentNameMock), studentRegistrationSearchCriteria.getStudentName().toLowerCase())).thenReturn(studentNamePredicateMock);

        Predicate globalPredicate = mock(Predicate.class);

        when(criteriaBuilderMock.and((Predicate[]) Arrays.asList(
                registrationNumberPredicateMock,
                studentNamePredicateMock).toArray())).thenReturn(globalPredicate);

        //when
        Specification<StudentRegistrationEntity> actual = resourceSpecification.build(studentRegistrationSearchCriteria, StudentRegistrationSearchRules.values());

        //Then
        Predicate actualPredicates = actual.toPredicate(registrationEntityRootMock, criteriaQueryMock, criteriaBuilderMock);
        assertThat(globalPredicate).isEqualTo(actualPredicates);
    }


}
