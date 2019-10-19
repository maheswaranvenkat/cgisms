package com.cgi.sms.dao.search;

import com.cgi.sms.dao.service.PredicateType;

import java.util.List;
import java.util.function.Function;

public interface CriteriaRule<C> {
    boolean isJoin();
    PredicateType getPredicateType();
    List<String> getCriteriaLabelHierarchyList();
    Function<C, String> getCriteriaValueGetter();

}
