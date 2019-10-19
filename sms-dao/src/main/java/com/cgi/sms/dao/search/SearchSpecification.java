package com.cgi.sms.dao.search;

import com.cgi.sms.dao.service.PredicateType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class SearchSpecification<C extends E, E> {

    public Specification<E> build(C entitySearchCriteria, CriteriaRule<C>[] criteriaRules) {
        return (root, query, cb) -> cb.and(
                stream(criteriaRules)
                .map(criteria -> mapCriteriaToPredicate(
                        root,
                        cb,
                        criteria.isJoin(),
                        criteria.getPredicateType(),
                        criteria.getCriteriaLabelHierarchyList(),
                        criteria.getCriteriaValueGetter().apply(entitySearchCriteria)
                ))
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new));
    }

    private Predicate mapCriteriaToPredicate(Root<E> root,
                                             CriteriaBuilder cb,
                                             boolean join,
                                             PredicateType predicateType,
                                             List<String> criteriaLabelList,
                                             String criteriaValue
                                             ) {
        if(isNotBlank(criteriaValue) && (!isEmpty(criteriaLabelList))) {
            Expression<String> expression = getExpression(root, join, criteriaLabelList);
            return buildPredicate(cb, predicateType, criteriaValue, expression);
        }
        return null;
    }

    private Expression<String> getExpression(Root<E> root, boolean join, List<String> criteriaLabelList) {
        String firstCriteria = criteriaLabelList.get(0);
        if(join) {
            return getJoinExpression(root, criteriaLabelList);

        }
        return getExpression(root, criteriaLabelList, firstCriteria);
    }

    private Predicate buildPredicate(CriteriaBuilder cb, PredicateType predicateType,
                                     String criteriaValue,
                                     Expression<String> expression) {
        switch (predicateType) {
            case LIKE:
                return cb.like(cb.lower(expression), "%" + criteriaValue.toLowerCase() + "%");
            case LESS_THAN_OR_EQUAL:
                return cb.lessThanOrEqualTo(expression, criteriaValue);
            case GREATER_THAN_OR_EQUAL:
                return cb.greaterThanOrEqualTo(expression, criteriaValue);
            default:
                return null;
        }
    }
    private Expression<String> getJoinExpression(Root<E> root, List<String> criteriaLabelList) {
        checkCriteriaLabelListSize(criteriaLabelList);
        Join<Object, Object> join = root.join(criteriaLabelList.get(0));
        return join.get(criteriaLabelList.get(1)).as(String.class);
    }

    private void checkCriteriaLabelListSize(List<String> criteriaLabelList) {
        if(criteriaLabelList.size() != 2) {
            throw new IllegalArgumentException("for join clauses we expect only 2 parameters");
        }
    }

    private Expression<String> getExpression(Root<E> root, List<String> criteriaLabelList, String firstCriteria) {
        AtomicReference<Path<Object>> objectPath = new AtomicReference<>(root.get(firstCriteria));
        criteriaLabelList.stream()
                .skip(1)
                .forEach(criteria -> objectPath.set(objectPath.get().get(criteria)));

        return objectPath.get().as(String.class);
    }
}
