package com.example.onlineexam.domain;

import java.util.ArrayList;
import java.util.List;

public class MsgUnreadExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MsgUnreadExample() {
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

        public Criteria andUidIsNull() {
            addCriterion("`uid` is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("`uid` is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(Integer value) {
            addCriterion("`uid` =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(Integer value) {
            addCriterion("`uid` <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(Integer value) {
            addCriterion("`uid` >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(Integer value) {
            addCriterion("`uid` >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(Integer value) {
            addCriterion("`uid` <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(Integer value) {
            addCriterion("`uid` <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<Integer> values) {
            addCriterion("`uid` in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<Integer> values) {
            addCriterion("`uid` not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(Integer value1, Integer value2) {
            addCriterion("`uid` between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(Integer value1, Integer value2) {
            addCriterion("`uid` not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andReplyIsNull() {
            addCriterion("reply is null");
            return (Criteria) this;
        }

        public Criteria andReplyIsNotNull() {
            addCriterion("reply is not null");
            return (Criteria) this;
        }

        public Criteria andReplyEqualTo(Integer value) {
            addCriterion("reply =", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyNotEqualTo(Integer value) {
            addCriterion("reply <>", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyGreaterThan(Integer value) {
            addCriterion("reply >", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyGreaterThanOrEqualTo(Integer value) {
            addCriterion("reply >=", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyLessThan(Integer value) {
            addCriterion("reply <", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyLessThanOrEqualTo(Integer value) {
            addCriterion("reply <=", value, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyIn(List<Integer> values) {
            addCriterion("reply in", values, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyNotIn(List<Integer> values) {
            addCriterion("reply not in", values, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyBetween(Integer value1, Integer value2) {
            addCriterion("reply between", value1, value2, "reply");
            return (Criteria) this;
        }

        public Criteria andReplyNotBetween(Integer value1, Integer value2) {
            addCriterion("reply not between", value1, value2, "reply");
            return (Criteria) this;
        }

        public Criteria andAtIsNull() {
            addCriterion("`at` is null");
            return (Criteria) this;
        }

        public Criteria andAtIsNotNull() {
            addCriterion("`at` is not null");
            return (Criteria) this;
        }

        public Criteria andAtEqualTo(Integer value) {
            addCriterion("`at` =", value, "at");
            return (Criteria) this;
        }

        public Criteria andAtNotEqualTo(Integer value) {
            addCriterion("`at` <>", value, "at");
            return (Criteria) this;
        }

        public Criteria andAtGreaterThan(Integer value) {
            addCriterion("`at` >", value, "at");
            return (Criteria) this;
        }

        public Criteria andAtGreaterThanOrEqualTo(Integer value) {
            addCriterion("`at` >=", value, "at");
            return (Criteria) this;
        }

        public Criteria andAtLessThan(Integer value) {
            addCriterion("`at` <", value, "at");
            return (Criteria) this;
        }

        public Criteria andAtLessThanOrEqualTo(Integer value) {
            addCriterion("`at` <=", value, "at");
            return (Criteria) this;
        }

        public Criteria andAtIn(List<Integer> values) {
            addCriterion("`at` in", values, "at");
            return (Criteria) this;
        }

        public Criteria andAtNotIn(List<Integer> values) {
            addCriterion("`at` not in", values, "at");
            return (Criteria) this;
        }

        public Criteria andAtBetween(Integer value1, Integer value2) {
            addCriterion("`at` between", value1, value2, "at");
            return (Criteria) this;
        }

        public Criteria andAtNotBetween(Integer value1, Integer value2) {
            addCriterion("`at` not between", value1, value2, "at");
            return (Criteria) this;
        }

        public Criteria andLoveIsNull() {
            addCriterion("love is null");
            return (Criteria) this;
        }

        public Criteria andLoveIsNotNull() {
            addCriterion("love is not null");
            return (Criteria) this;
        }

        public Criteria andLoveEqualTo(Integer value) {
            addCriterion("love =", value, "love");
            return (Criteria) this;
        }

        public Criteria andLoveNotEqualTo(Integer value) {
            addCriterion("love <>", value, "love");
            return (Criteria) this;
        }

        public Criteria andLoveGreaterThan(Integer value) {
            addCriterion("love >", value, "love");
            return (Criteria) this;
        }

        public Criteria andLoveGreaterThanOrEqualTo(Integer value) {
            addCriterion("love >=", value, "love");
            return (Criteria) this;
        }

        public Criteria andLoveLessThan(Integer value) {
            addCriterion("love <", value, "love");
            return (Criteria) this;
        }

        public Criteria andLoveLessThanOrEqualTo(Integer value) {
            addCriterion("love <=", value, "love");
            return (Criteria) this;
        }

        public Criteria andLoveIn(List<Integer> values) {
            addCriterion("love in", values, "love");
            return (Criteria) this;
        }

        public Criteria andLoveNotIn(List<Integer> values) {
            addCriterion("love not in", values, "love");
            return (Criteria) this;
        }

        public Criteria andLoveBetween(Integer value1, Integer value2) {
            addCriterion("love between", value1, value2, "love");
            return (Criteria) this;
        }

        public Criteria andLoveNotBetween(Integer value1, Integer value2) {
            addCriterion("love not between", value1, value2, "love");
            return (Criteria) this;
        }

        public Criteria andSystemIsNull() {
            addCriterion("`system` is null");
            return (Criteria) this;
        }

        public Criteria andSystemIsNotNull() {
            addCriterion("`system` is not null");
            return (Criteria) this;
        }

        public Criteria andSystemEqualTo(Integer value) {
            addCriterion("`system` =", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotEqualTo(Integer value) {
            addCriterion("`system` <>", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemGreaterThan(Integer value) {
            addCriterion("`system` >", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemGreaterThanOrEqualTo(Integer value) {
            addCriterion("`system` >=", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemLessThan(Integer value) {
            addCriterion("`system` <", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemLessThanOrEqualTo(Integer value) {
            addCriterion("`system` <=", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemIn(List<Integer> values) {
            addCriterion("`system` in", values, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotIn(List<Integer> values) {
            addCriterion("`system` not in", values, "system");
            return (Criteria) this;
        }

        public Criteria andSystemBetween(Integer value1, Integer value2) {
            addCriterion("`system` between", value1, value2, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotBetween(Integer value1, Integer value2) {
            addCriterion("`system` not between", value1, value2, "system");
            return (Criteria) this;
        }

        public Criteria andWhisperIsNull() {
            addCriterion("whisper is null");
            return (Criteria) this;
        }

        public Criteria andWhisperIsNotNull() {
            addCriterion("whisper is not null");
            return (Criteria) this;
        }

        public Criteria andWhisperEqualTo(Integer value) {
            addCriterion("whisper =", value, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperNotEqualTo(Integer value) {
            addCriterion("whisper <>", value, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperGreaterThan(Integer value) {
            addCriterion("whisper >", value, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperGreaterThanOrEqualTo(Integer value) {
            addCriterion("whisper >=", value, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperLessThan(Integer value) {
            addCriterion("whisper <", value, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperLessThanOrEqualTo(Integer value) {
            addCriterion("whisper <=", value, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperIn(List<Integer> values) {
            addCriterion("whisper in", values, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperNotIn(List<Integer> values) {
            addCriterion("whisper not in", values, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperBetween(Integer value1, Integer value2) {
            addCriterion("whisper between", value1, value2, "whisper");
            return (Criteria) this;
        }

        public Criteria andWhisperNotBetween(Integer value1, Integer value2) {
            addCriterion("whisper not between", value1, value2, "whisper");
            return (Criteria) this;
        }

        public Criteria andDynamicIsNull() {
            addCriterion("`dynamic` is null");
            return (Criteria) this;
        }

        public Criteria andDynamicIsNotNull() {
            addCriterion("`dynamic` is not null");
            return (Criteria) this;
        }

        public Criteria andDynamicEqualTo(Integer value) {
            addCriterion("`dynamic` =", value, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicNotEqualTo(Integer value) {
            addCriterion("`dynamic` <>", value, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicGreaterThan(Integer value) {
            addCriterion("`dynamic` >", value, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicGreaterThanOrEqualTo(Integer value) {
            addCriterion("`dynamic` >=", value, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicLessThan(Integer value) {
            addCriterion("`dynamic` <", value, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicLessThanOrEqualTo(Integer value) {
            addCriterion("`dynamic` <=", value, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicIn(List<Integer> values) {
            addCriterion("`dynamic` in", values, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicNotIn(List<Integer> values) {
            addCriterion("`dynamic` not in", values, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicBetween(Integer value1, Integer value2) {
            addCriterion("`dynamic` between", value1, value2, "dynamic");
            return (Criteria) this;
        }

        public Criteria andDynamicNotBetween(Integer value1, Integer value2) {
            addCriterion("`dynamic` not between", value1, value2, "dynamic");
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