package com.cgi.sms.business.service;

import com.cgi.sms.business.mapper.StudentRegistrationMapper;
import com.cgi.sms.business.model.StudentRegistration;
import com.cgi.sms.business.model.response.StudentRegistrationSearchResponse;
import com.cgi.sms.dao.entity.StudentRegistrationEntity;
import com.cgi.sms.dao.service.StudentRegistrationDao;
import com.cgi.sms.domain.ErrorCatalog;
import com.cgi.sms.domain.ErrorOrigin;
import com.cgi.sms.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@Component
public class StudentRegistrationService {

    @Autowired
    private StudentRegistrationDao studentRegistrationDao;
    @Autowired
    private StudentRegistrationMapper studentRegistrationMapper;

    @Transactional
    public StudentRegistration saveStudentRegistration(final StudentRegistration studentRegistration) {
        try {
            validateArguments(studentRegistration);
            StudentRegistrationEntity studentRegistrationEntityAfterMapping = mapToEntityOrThrownException(studentRegistration);
            StudentRegistrationEntity studentRegistrationEntity = studentRegistrationDao.saveStudentRegistration(studentRegistrationEntityAfterMapping);
            return mapResponseToStudentRegisrationOrThrowExcption(studentRegistrationEntity);
        } catch (ServiceException | IllegalArgumentException e) {
            log.error("Error while registering Student Registration");
            throw e;
        } catch (Exception e) {
            handleExtractDataException(e);
        }
        return null;
    }

    @Transactional
    public boolean updateStudentRegistration(final StudentRegistration studentRegistration) {
        try {
            validateArgumentsWhileUpdate(studentRegistration);
            throwExceptionWhenRegistrationNumberEmpty(studentRegistration);
            getStudentRegistrationAndMapAndUpdate(studentRegistration);
            return Boolean.TRUE;
        } catch (ServiceException | IllegalArgumentException e) {
            log.error("Error while updating Student Registation", e);
        } catch (Exception e) {
            log.error("Error while updating Student Registration", e);
            throw new ServiceException(ErrorCatalog.DATA_BASE_ERROR, ErrorOrigin.DATA_BASE, e);
        }
        return Boolean.FALSE;
    }

    @Transactional
    public StudentRegistrationSearchResponse getAllStudentRegistration(Pageable pageable) {
        try {
            Page page = studentRegistrationDao.getAllStudentRegistration(pageable);
            return mapToStudentRegistrationSearchResponse(page);
        } catch (ServiceException | IllegalArgumentException e) {
            log.error("Error while getAllStudentRegistration");
            throw e;
        } catch (Exception e) {
            log.error("Error while getAllStudentRegistration");
            throw new ServiceException(ErrorCatalog.DATA_BASE_ERROR, ErrorOrigin.DATA_BASE, ErrorCatalog.DATA_BASE_ERROR.getMessage());
        }
    }

    private StudentRegistrationSearchResponse mapToStudentRegistrationSearchResponse(Page page) {
        List<StudentRegistrationEntity> studentRegistrationEntities = page.getContent();
        if((studentRegistrationEntities.size() == 0) && (CollectionUtils.isEmpty(studentRegistrationEntities))) {
            return new StudentRegistrationSearchResponse();
        }

        StudentRegistrationSearchResponse studentRegistrationSearchResponse = new StudentRegistrationSearchResponse();
        List<StudentRegistration> studentRegistrationList = studentRegistrationMapper.mapSearchResonseToTechnicalResources(studentRegistrationEntities);
        studentRegistrationSearchResponse.setStudentRegistrationList(studentRegistrationList);
        studentRegistrationSearchResponse.setHasNextPage(page.hasNext());
        studentRegistrationSearchResponse.setTotalRecords(page.getTotalElements());
        return studentRegistrationSearchResponse;
    }

    private StudentRegistrationEntity getStudentRegistrationAndMapAndUpdate(StudentRegistration studentRegistration) {
        StudentRegistrationEntity studentRegistrationEntity = getOldStudentRegistrationEntityFromDB(studentRegistration);

        StudentRegistrationEntity studentRegistrationEntity1 = studentRegistrationMapper.mapToStudentRegistrationEntityForUpdate(studentRegistration, studentRegistrationEntity)
                .orElseThrow(() -> new IllegalArgumentException("Error while mapping Student Registration"));
        return studentRegistrationEntity1;
    }

    private StudentRegistrationEntity getOldStudentRegistrationEntityFromDB(StudentRegistration studentRegistration) {
        StudentRegistrationEntity studentRegistrationEntity = studentRegistrationDao.getByRegistrationNumber(studentRegistration.getRegistrationNumber());
        if(isNull(studentRegistration)) {
            throw new ServiceException(ErrorCatalog.DOESNOTEXIST, ErrorOrigin.CLIENT, ErrorCatalog.DOESNOTEXIST.getMessage());
        }
        return studentRegistrationEntity;
    }

    private void validateArgumentsWhileUpdate(StudentRegistration studentRegistration) {
        if(isNull(studentRegistration)) {
            throw new IllegalArgumentException("Student Registration cannot be null");
        }
    }

    private void throwExceptionWhenRegistrationNumberEmpty(StudentRegistration studentRegistration) {
        if (isEmpty(studentRegistration.getRegistrationNumber())) {
            throw new ServiceException(ErrorCatalog.REGISTRATION_NUMBER, ErrorOrigin.CLIENT, ErrorCatalog.REGISTRATION_NUMBER.getMessage());
        }
    }

    private StudentRegistrationEntity mapToEntityOrThrownException(StudentRegistration studentRegistration) {
        return studentRegistrationMapper.mapToStudentRegistrationEntity(studentRegistration).orElseThrow(() -> new IllegalArgumentException("Entity cannot be empty"));
    }

    private StudentRegistration mapResponseToStudentRegisrationOrThrowExcption(StudentRegistrationEntity studentRegistrationEntity) {
        Optional<StudentRegistration> optionalStudentRegistration = studentRegistrationMapper.mapResponseToStudentRegistration(studentRegistrationEntity);
        return optionalStudentRegistration.orElseThrow(() -> new IllegalArgumentException("StudentRegistration Emtity Object is empty"));
    }

    private void validateArguments(StudentRegistration studentRegistration) {
        if(isNull(studentRegistration)) {
            throw new IllegalArgumentException("StudentRegistration cannot be null");
        }
        throwExceptionIfStudentRegistrationAleadyExists(studentRegistration);
    }

    private void throwExceptionIfStudentRegistrationAleadyExists(StudentRegistration studentRegistration) {
        StudentRegistrationEntity studentRegistrationEntity = null;
        try {
             studentRegistrationEntity = studentRegistrationDao.getByRegistrationNumber(studentRegistration.getRegistrationNumber());
        } catch (Exception e) {
            log.error(ErrorCatalog.DATA_BASE_ERROR.getMessage(), e);
            throw new ServiceException(ErrorCatalog.DATA_BASE_ERROR, ErrorOrigin.CLIENT, ErrorCatalog.DATA_BASE_ERROR.getMessage());
        }

        if(!isNull(studentRegistrationEntity)) {
            throw new ServiceException(ErrorCatalog.STUDENT_RECORD_ALREDAY_EXISTS, ErrorOrigin.CLIENT, ErrorCatalog.STUDENT_RECORD_ALREDAY_EXISTS.getMessage());
        }
    }

    private void handleExtractDataException(Exception e) {
        log.error("Error While Registering Student", e);
        throw new ServiceException(ErrorCatalog.DATA_BASE_ERROR, ErrorOrigin.DATA_BASE, e);
    }

}
