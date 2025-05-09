package com.example.onlineexam.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatDetailedExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ChatDetailedExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdIsNull() {
            addCriterion("another_id is null");
            return (Criteria) this;
        }

        public Criteria andAnotherIdIsNotNull() {
            addCriterion("another_id is not null");
            return (Criteria) this;
        }

        public Criteria andAnotherIdEqualTo(Integer value) {
            addCriterion("another_id =", value, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdNotEqualTo(Integer value) {
            addCriterion("another_id <>", value, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdGreaterThan(Integer value) {
            addCriterion("another_id >", value, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("another_id >=", value, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdLessThan(Integer value) {
            addCriterion("another_id <", value, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdLessThanOrEqualTo(Integer value) {
            addCriterion("another_id <=", value, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdIn(List<Integer> values) {
            addCriterion("another_id in", values, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdNotIn(List<Integer> values) {
            addCriterion("another_id not in", values, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdBetween(Integer value1, Integer value2) {
            addCriterion("another_id between", value1, value2, "anotherId");
            return (Criteria) this;
        }

        public Criteria andAnotherIdNotBetween(Integer value1, Integer value2) {
            addCriterion("another_id not between", value1, value2, "anotherId");
            return (Criteria) this;
        }

        public Criteria andContentIsNull() {
            addCriterion("content is null");
            return (Criteria) this;
        }

        public Criteria andContentIsNotNull() {
            addCriterion("content is not null");
            return (Criteria) this;
        }

        public Criteria andContentEqualTo(String value) {
            addCriterion("content =", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotEqualTo(String value) {
            addCriterion("content <>", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThan(String value) {
            addCriterion("content >", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThanOrEqualTo(String value) {
            addCriterion("content >=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThan(String value) {
            addCriterion("content <", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThanOrEqualTo(String value) {
            addCriterion("content <=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLike(String value) {
            addCriterion("content like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotLike(String value) {
            addCriterion("content not like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentIn(List<String> values) {
            addCriterion("content in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotIn(List<String> values) {
            addCriterion("content not in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentBetween(String value1, String value2) {
            addCriterion("content between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotBetween(String value1, String value2) {
            addCriterion("content not between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andUserDelIsNull() {
            addCriterion("user_del is null");
            return (Criteria) this;
        }

        public Criteria andUserDelIsNotNull() {
            addCriterion("user_del is not null");
            return (Criteria) this;
        }

        public Criteria andUserDelEqualTo(Integer value) {
            addCriterion("user_del =", value, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelNotEqualTo(Integer value) {
            addCriterion("user_del <>", value, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelGreaterThan(Integer value) {
            addCriterion("user_del >", value, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_del >=", value, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelLessThan(Integer value) {
            addCriterion("user_del <", value, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelLessThanOrEqualTo(Integer value) {
            addCriterion("user_del <=", value, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelIn(List<Integer> values) {
            addCriterion("user_del in", values, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelNotIn(List<Integer> values) {
            addCriterion("user_del not in", values, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelBetween(Integer value1, Integer value2) {
            addCriterion("user_del between", value1, value2, "userDel");
            return (Criteria) this;
        }

        public Criteria andUserDelNotBetween(Integer value1, Integer value2) {
            addCriterion("user_del not between", value1, value2, "userDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelIsNull() {
            addCriterion("another_del is null");
            return (Criteria) this;
        }

        public Criteria andAnotherDelIsNotNull() {
            addCriterion("another_del is not null");
            return (Criteria) this;
        }

        public Criteria andAnotherDelEqualTo(Integer value) {
            addCriterion("another_del =", value, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelNotEqualTo(Integer value) {
            addCriterion("another_del <>", value, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelGreaterThan(Integer value) {
            addCriterion("another_del >", value, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelGreaterThanOrEqualTo(Integer value) {
            addCriterion("another_del >=", value, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelLessThan(Integer value) {
            addCriterion("another_del <", value, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelLessThanOrEqualTo(Integer value) {
            addCriterion("another_del <=", value, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelIn(List<Integer> values) {
            addCriterion("another_del in", values, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelNotIn(List<Integer> values) {
            addCriterion("another_del not in", values, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelBetween(Integer value1, Integer value2) {
            addCriterion("another_del between", value1, value2, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andAnotherDelNotBetween(Integer value1, Integer value2) {
            addCriterion("another_del not between", value1, value2, "anotherDel");
            return (Criteria) this;
        }

        public Criteria andWithdrawIsNull() {
            addCriterion("withdraw is null");
            return (Criteria) this;
        }

        public Criteria andWithdrawIsNotNull() {
            addCriterion("withdraw is not null");
            return (Criteria) this;
        }

        public Criteria andWithdrawEqualTo(Integer value) {
            addCriterion("withdraw =", value, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawNotEqualTo(Integer value) {
            addCriterion("withdraw <>", value, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawGreaterThan(Integer value) {
            addCriterion("withdraw >", value, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawGreaterThanOrEqualTo(Integer value) {
            addCriterion("withdraw >=", value, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawLessThan(Integer value) {
            addCriterion("withdraw <", value, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawLessThanOrEqualTo(Integer value) {
            addCriterion("withdraw <=", value, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawIn(List<Integer> values) {
            addCriterion("withdraw in", values, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawNotIn(List<Integer> values) {
            addCriterion("withdraw not in", values, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawBetween(Integer value1, Integer value2) {
            addCriterion("withdraw between", value1, value2, "withdraw");
            return (Criteria) this;
        }

        public Criteria andWithdrawNotBetween(Integer value1, Integer value2) {
            addCriterion("withdraw not between", value1, value2, "withdraw");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("`time` is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("`time` is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Date value) {
            addCriterion("`time` =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Date value) {
            addCriterion("`time` <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Date value) {
            addCriterion("`time` >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`time` >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Date value) {
            addCriterion("`time` <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Date value) {
            addCriterion("`time` <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Date> values) {
            addCriterion("`time` in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Date> values) {
            addCriterion("`time` not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Date value1, Date value2) {
            addCriterion("`time` between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Date value1, Date value2) {
            addCriterion("`time` not between", value1, value2, "time");
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