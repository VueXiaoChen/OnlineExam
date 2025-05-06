package com.example.onlineexam.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperateLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OperateLogExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andLogIdIsNull() {
            addCriterion("log_id is null");
            return (Criteria) this;
        }

        public Criteria andLogIdIsNotNull() {
            addCriterion("log_id is not null");
            return (Criteria) this;
        }

        public Criteria andLogIdEqualTo(Long value) {
            addCriterion("log_id =", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotEqualTo(Long value) {
            addCriterion("log_id <>", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdGreaterThan(Long value) {
            addCriterion("log_id >", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdGreaterThanOrEqualTo(Long value) {
            addCriterion("log_id >=", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLessThan(Long value) {
            addCriterion("log_id <", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLessThanOrEqualTo(Long value) {
            addCriterion("log_id <=", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdIn(List<Long> values) {
            addCriterion("log_id in", values, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotIn(List<Long> values) {
            addCriterion("log_id not in", values, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdBetween(Long value1, Long value2) {
            addCriterion("log_id between", value1, value2, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotBetween(Long value1, Long value2) {
            addCriterion("log_id not between", value1, value2, "logId");
            return (Criteria) this;
        }

        public Criteria andLogUrlIsNull() {
            addCriterion("log_url is null");
            return (Criteria) this;
        }

        public Criteria andLogUrlIsNotNull() {
            addCriterion("log_url is not null");
            return (Criteria) this;
        }

        public Criteria andLogUrlEqualTo(String value) {
            addCriterion("log_url =", value, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlNotEqualTo(String value) {
            addCriterion("log_url <>", value, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlGreaterThan(String value) {
            addCriterion("log_url >", value, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlGreaterThanOrEqualTo(String value) {
            addCriterion("log_url >=", value, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlLessThan(String value) {
            addCriterion("log_url <", value, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlLessThanOrEqualTo(String value) {
            addCriterion("log_url <=", value, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlLike(String value) {
            addCriterion("log_url like", value, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlNotLike(String value) {
            addCriterion("log_url not like", value, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlIn(List<String> values) {
            addCriterion("log_url in", values, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlNotIn(List<String> values) {
            addCriterion("log_url not in", values, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlBetween(String value1, String value2) {
            addCriterion("log_url between", value1, value2, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogUrlNotBetween(String value1, String value2) {
            addCriterion("log_url not between", value1, value2, "logUrl");
            return (Criteria) this;
        }

        public Criteria andLogMethodIsNull() {
            addCriterion("log_method is null");
            return (Criteria) this;
        }

        public Criteria andLogMethodIsNotNull() {
            addCriterion("log_method is not null");
            return (Criteria) this;
        }

        public Criteria andLogMethodEqualTo(String value) {
            addCriterion("log_method =", value, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodNotEqualTo(String value) {
            addCriterion("log_method <>", value, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodGreaterThan(String value) {
            addCriterion("log_method >", value, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodGreaterThanOrEqualTo(String value) {
            addCriterion("log_method >=", value, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodLessThan(String value) {
            addCriterion("log_method <", value, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodLessThanOrEqualTo(String value) {
            addCriterion("log_method <=", value, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodLike(String value) {
            addCriterion("log_method like", value, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodNotLike(String value) {
            addCriterion("log_method not like", value, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodIn(List<String> values) {
            addCriterion("log_method in", values, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodNotIn(List<String> values) {
            addCriterion("log_method not in", values, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodBetween(String value1, String value2) {
            addCriterion("log_method between", value1, value2, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogMethodNotBetween(String value1, String value2) {
            addCriterion("log_method not between", value1, value2, "logMethod");
            return (Criteria) this;
        }

        public Criteria andLogIpIsNull() {
            addCriterion("log_ip is null");
            return (Criteria) this;
        }

        public Criteria andLogIpIsNotNull() {
            addCriterion("log_ip is not null");
            return (Criteria) this;
        }

        public Criteria andLogIpEqualTo(String value) {
            addCriterion("log_ip =", value, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpNotEqualTo(String value) {
            addCriterion("log_ip <>", value, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpGreaterThan(String value) {
            addCriterion("log_ip >", value, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpGreaterThanOrEqualTo(String value) {
            addCriterion("log_ip >=", value, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpLessThan(String value) {
            addCriterion("log_ip <", value, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpLessThanOrEqualTo(String value) {
            addCriterion("log_ip <=", value, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpLike(String value) {
            addCriterion("log_ip like", value, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpNotLike(String value) {
            addCriterion("log_ip not like", value, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpIn(List<String> values) {
            addCriterion("log_ip in", values, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpNotIn(List<String> values) {
            addCriterion("log_ip not in", values, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpBetween(String value1, String value2) {
            addCriterion("log_ip between", value1, value2, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogIpNotBetween(String value1, String value2) {
            addCriterion("log_ip not between", value1, value2, "logIp");
            return (Criteria) this;
        }

        public Criteria andLogStartdateIsNull() {
            addCriterion("log_startDate is null");
            return (Criteria) this;
        }

        public Criteria andLogStartdateIsNotNull() {
            addCriterion("log_startDate is not null");
            return (Criteria) this;
        }

        public Criteria andLogStartdateEqualTo(String value) {
            addCriterion("log_startDate =", value, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateNotEqualTo(String value) {
            addCriterion("log_startDate <>", value, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateGreaterThan(String value) {
            addCriterion("log_startDate >", value, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateGreaterThanOrEqualTo(String value) {
            addCriterion("log_startDate >=", value, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateLessThan(String value) {
            addCriterion("log_startDate <", value, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateLessThanOrEqualTo(String value) {
            addCriterion("log_startDate <=", value, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateLike(String value) {
            addCriterion("log_startDate like", value, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateNotLike(String value) {
            addCriterion("log_startDate not like", value, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateIn(List<String> values) {
            addCriterion("log_startDate in", values, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateNotIn(List<String> values) {
            addCriterion("log_startDate not in", values, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateBetween(String value1, String value2) {
            addCriterion("log_startDate between", value1, value2, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogStartdateNotBetween(String value1, String value2) {
            addCriterion("log_startDate not between", value1, value2, "logStartdate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateIsNull() {
            addCriterion("log_endDate is null");
            return (Criteria) this;
        }

        public Criteria andLogEnddateIsNotNull() {
            addCriterion("log_endDate is not null");
            return (Criteria) this;
        }

        public Criteria andLogEnddateEqualTo(String value) {
            addCriterion("log_endDate =", value, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateNotEqualTo(String value) {
            addCriterion("log_endDate <>", value, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateGreaterThan(String value) {
            addCriterion("log_endDate >", value, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateGreaterThanOrEqualTo(String value) {
            addCriterion("log_endDate >=", value, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateLessThan(String value) {
            addCriterion("log_endDate <", value, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateLessThanOrEqualTo(String value) {
            addCriterion("log_endDate <=", value, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateLike(String value) {
            addCriterion("log_endDate like", value, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateNotLike(String value) {
            addCriterion("log_endDate not like", value, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateIn(List<String> values) {
            addCriterion("log_endDate in", values, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateNotIn(List<String> values) {
            addCriterion("log_endDate not in", values, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateBetween(String value1, String value2) {
            addCriterion("log_endDate between", value1, value2, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogEnddateNotBetween(String value1, String value2) {
            addCriterion("log_endDate not between", value1, value2, "logEnddate");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeIsNull() {
            addCriterion("log_responseTime is null");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeIsNotNull() {
            addCriterion("log_responseTime is not null");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeEqualTo(String value) {
            addCriterion("log_responseTime =", value, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeNotEqualTo(String value) {
            addCriterion("log_responseTime <>", value, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeGreaterThan(String value) {
            addCriterion("log_responseTime >", value, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeGreaterThanOrEqualTo(String value) {
            addCriterion("log_responseTime >=", value, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeLessThan(String value) {
            addCriterion("log_responseTime <", value, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeLessThanOrEqualTo(String value) {
            addCriterion("log_responseTime <=", value, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeLike(String value) {
            addCriterion("log_responseTime like", value, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeNotLike(String value) {
            addCriterion("log_responseTime not like", value, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeIn(List<String> values) {
            addCriterion("log_responseTime in", values, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeNotIn(List<String> values) {
            addCriterion("log_responseTime not in", values, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeBetween(String value1, String value2) {
            addCriterion("log_responseTime between", value1, value2, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogResponsetimeNotBetween(String value1, String value2) {
            addCriterion("log_responseTime not between", value1, value2, "logResponsetime");
            return (Criteria) this;
        }

        public Criteria andLogOperationIsNull() {
            addCriterion("log_operation is null");
            return (Criteria) this;
        }

        public Criteria andLogOperationIsNotNull() {
            addCriterion("log_operation is not null");
            return (Criteria) this;
        }

        public Criteria andLogOperationEqualTo(String value) {
            addCriterion("log_operation =", value, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationNotEqualTo(String value) {
            addCriterion("log_operation <>", value, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationGreaterThan(String value) {
            addCriterion("log_operation >", value, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationGreaterThanOrEqualTo(String value) {
            addCriterion("log_operation >=", value, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationLessThan(String value) {
            addCriterion("log_operation <", value, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationLessThanOrEqualTo(String value) {
            addCriterion("log_operation <=", value, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationLike(String value) {
            addCriterion("log_operation like", value, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationNotLike(String value) {
            addCriterion("log_operation not like", value, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationIn(List<String> values) {
            addCriterion("log_operation in", values, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationNotIn(List<String> values) {
            addCriterion("log_operation not in", values, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationBetween(String value1, String value2) {
            addCriterion("log_operation between", value1, value2, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationNotBetween(String value1, String value2) {
            addCriterion("log_operation not between", value1, value2, "logOperation");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeIsNull() {
            addCriterion("log_operationTime is null");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeIsNotNull() {
            addCriterion("log_operationTime is not null");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeEqualTo(Date value) {
            addCriterion("log_operationTime =", value, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeNotEqualTo(Date value) {
            addCriterion("log_operationTime <>", value, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeGreaterThan(Date value) {
            addCriterion("log_operationTime >", value, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("log_operationTime >=", value, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeLessThan(Date value) {
            addCriterion("log_operationTime <", value, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeLessThanOrEqualTo(Date value) {
            addCriterion("log_operationTime <=", value, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeIn(List<Date> values) {
            addCriterion("log_operationTime in", values, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeNotIn(List<Date> values) {
            addCriterion("log_operationTime not in", values, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeBetween(Date value1, Date value2) {
            addCriterion("log_operationTime between", value1, value2, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOperationtimeNotBetween(Date value1, Date value2) {
            addCriterion("log_operationTime not between", value1, value2, "logOperationtime");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserIsNull() {
            addCriterion("log_optionUser is null");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserIsNotNull() {
            addCriterion("log_optionUser is not null");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserEqualTo(String value) {
            addCriterion("log_optionUser =", value, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserNotEqualTo(String value) {
            addCriterion("log_optionUser <>", value, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserGreaterThan(String value) {
            addCriterion("log_optionUser >", value, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserGreaterThanOrEqualTo(String value) {
            addCriterion("log_optionUser >=", value, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserLessThan(String value) {
            addCriterion("log_optionUser <", value, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserLessThanOrEqualTo(String value) {
            addCriterion("log_optionUser <=", value, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserLike(String value) {
            addCriterion("log_optionUser like", value, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserNotLike(String value) {
            addCriterion("log_optionUser not like", value, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserIn(List<String> values) {
            addCriterion("log_optionUser in", values, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserNotIn(List<String> values) {
            addCriterion("log_optionUser not in", values, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserBetween(String value1, String value2) {
            addCriterion("log_optionUser between", value1, value2, "logOptionuser");
            return (Criteria) this;
        }

        public Criteria andLogOptionuserNotBetween(String value1, String value2) {
            addCriterion("log_optionUser not between", value1, value2, "logOptionuser");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}