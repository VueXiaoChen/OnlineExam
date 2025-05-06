package com.example.onlineexam.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserVideoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserVideoExample() {
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

        public Criteria andVidIsNull() {
            addCriterion("vid is null");
            return (Criteria) this;
        }

        public Criteria andVidIsNotNull() {
            addCriterion("vid is not null");
            return (Criteria) this;
        }

        public Criteria andVidEqualTo(Integer value) {
            addCriterion("vid =", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidNotEqualTo(Integer value) {
            addCriterion("vid <>", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidGreaterThan(Integer value) {
            addCriterion("vid >", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidGreaterThanOrEqualTo(Integer value) {
            addCriterion("vid >=", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidLessThan(Integer value) {
            addCriterion("vid <", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidLessThanOrEqualTo(Integer value) {
            addCriterion("vid <=", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidIn(List<Integer> values) {
            addCriterion("vid in", values, "vid");
            return (Criteria) this;
        }

        public Criteria andVidNotIn(List<Integer> values) {
            addCriterion("vid not in", values, "vid");
            return (Criteria) this;
        }

        public Criteria andVidBetween(Integer value1, Integer value2) {
            addCriterion("vid between", value1, value2, "vid");
            return (Criteria) this;
        }

        public Criteria andVidNotBetween(Integer value1, Integer value2) {
            addCriterion("vid not between", value1, value2, "vid");
            return (Criteria) this;
        }

        public Criteria andPlayIsNull() {
            addCriterion("play is null");
            return (Criteria) this;
        }

        public Criteria andPlayIsNotNull() {
            addCriterion("play is not null");
            return (Criteria) this;
        }

        public Criteria andPlayEqualTo(Integer value) {
            addCriterion("play =", value, "play");
            return (Criteria) this;
        }

        public Criteria andPlayNotEqualTo(Integer value) {
            addCriterion("play <>", value, "play");
            return (Criteria) this;
        }

        public Criteria andPlayGreaterThan(Integer value) {
            addCriterion("play >", value, "play");
            return (Criteria) this;
        }

        public Criteria andPlayGreaterThanOrEqualTo(Integer value) {
            addCriterion("play >=", value, "play");
            return (Criteria) this;
        }

        public Criteria andPlayLessThan(Integer value) {
            addCriterion("play <", value, "play");
            return (Criteria) this;
        }

        public Criteria andPlayLessThanOrEqualTo(Integer value) {
            addCriterion("play <=", value, "play");
            return (Criteria) this;
        }

        public Criteria andPlayIn(List<Integer> values) {
            addCriterion("play in", values, "play");
            return (Criteria) this;
        }

        public Criteria andPlayNotIn(List<Integer> values) {
            addCriterion("play not in", values, "play");
            return (Criteria) this;
        }

        public Criteria andPlayBetween(Integer value1, Integer value2) {
            addCriterion("play between", value1, value2, "play");
            return (Criteria) this;
        }

        public Criteria andPlayNotBetween(Integer value1, Integer value2) {
            addCriterion("play not between", value1, value2, "play");
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

        public Criteria andUnloveIsNull() {
            addCriterion("unlove is null");
            return (Criteria) this;
        }

        public Criteria andUnloveIsNotNull() {
            addCriterion("unlove is not null");
            return (Criteria) this;
        }

        public Criteria andUnloveEqualTo(Integer value) {
            addCriterion("unlove =", value, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveNotEqualTo(Integer value) {
            addCriterion("unlove <>", value, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveGreaterThan(Integer value) {
            addCriterion("unlove >", value, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveGreaterThanOrEqualTo(Integer value) {
            addCriterion("unlove >=", value, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveLessThan(Integer value) {
            addCriterion("unlove <", value, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveLessThanOrEqualTo(Integer value) {
            addCriterion("unlove <=", value, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveIn(List<Integer> values) {
            addCriterion("unlove in", values, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveNotIn(List<Integer> values) {
            addCriterion("unlove not in", values, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveBetween(Integer value1, Integer value2) {
            addCriterion("unlove between", value1, value2, "unlove");
            return (Criteria) this;
        }

        public Criteria andUnloveNotBetween(Integer value1, Integer value2) {
            addCriterion("unlove not between", value1, value2, "unlove");
            return (Criteria) this;
        }

        public Criteria andCoinIsNull() {
            addCriterion("coin is null");
            return (Criteria) this;
        }

        public Criteria andCoinIsNotNull() {
            addCriterion("coin is not null");
            return (Criteria) this;
        }

        public Criteria andCoinEqualTo(Integer value) {
            addCriterion("coin =", value, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinNotEqualTo(Integer value) {
            addCriterion("coin <>", value, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinGreaterThan(Integer value) {
            addCriterion("coin >", value, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinGreaterThanOrEqualTo(Integer value) {
            addCriterion("coin >=", value, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinLessThan(Integer value) {
            addCriterion("coin <", value, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinLessThanOrEqualTo(Integer value) {
            addCriterion("coin <=", value, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinIn(List<Integer> values) {
            addCriterion("coin in", values, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinNotIn(List<Integer> values) {
            addCriterion("coin not in", values, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinBetween(Integer value1, Integer value2) {
            addCriterion("coin between", value1, value2, "coin");
            return (Criteria) this;
        }

        public Criteria andCoinNotBetween(Integer value1, Integer value2) {
            addCriterion("coin not between", value1, value2, "coin");
            return (Criteria) this;
        }

        public Criteria andCollectIsNull() {
            addCriterion("`collect` is null");
            return (Criteria) this;
        }

        public Criteria andCollectIsNotNull() {
            addCriterion("`collect` is not null");
            return (Criteria) this;
        }

        public Criteria andCollectEqualTo(Integer value) {
            addCriterion("`collect` =", value, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectNotEqualTo(Integer value) {
            addCriterion("`collect` <>", value, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectGreaterThan(Integer value) {
            addCriterion("`collect` >", value, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectGreaterThanOrEqualTo(Integer value) {
            addCriterion("`collect` >=", value, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectLessThan(Integer value) {
            addCriterion("`collect` <", value, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectLessThanOrEqualTo(Integer value) {
            addCriterion("`collect` <=", value, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectIn(List<Integer> values) {
            addCriterion("`collect` in", values, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectNotIn(List<Integer> values) {
            addCriterion("`collect` not in", values, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectBetween(Integer value1, Integer value2) {
            addCriterion("`collect` between", value1, value2, "collect");
            return (Criteria) this;
        }

        public Criteria andCollectNotBetween(Integer value1, Integer value2) {
            addCriterion("`collect` not between", value1, value2, "collect");
            return (Criteria) this;
        }

        public Criteria andPlayTimeIsNull() {
            addCriterion("play_time is null");
            return (Criteria) this;
        }

        public Criteria andPlayTimeIsNotNull() {
            addCriterion("play_time is not null");
            return (Criteria) this;
        }

        public Criteria andPlayTimeEqualTo(Date value) {
            addCriterion("play_time =", value, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeNotEqualTo(Date value) {
            addCriterion("play_time <>", value, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeGreaterThan(Date value) {
            addCriterion("play_time >", value, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("play_time >=", value, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeLessThan(Date value) {
            addCriterion("play_time <", value, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeLessThanOrEqualTo(Date value) {
            addCriterion("play_time <=", value, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeIn(List<Date> values) {
            addCriterion("play_time in", values, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeNotIn(List<Date> values) {
            addCriterion("play_time not in", values, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeBetween(Date value1, Date value2) {
            addCriterion("play_time between", value1, value2, "playTime");
            return (Criteria) this;
        }

        public Criteria andPlayTimeNotBetween(Date value1, Date value2) {
            addCriterion("play_time not between", value1, value2, "playTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeIsNull() {
            addCriterion("love_time is null");
            return (Criteria) this;
        }

        public Criteria andLoveTimeIsNotNull() {
            addCriterion("love_time is not null");
            return (Criteria) this;
        }

        public Criteria andLoveTimeEqualTo(Date value) {
            addCriterion("love_time =", value, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeNotEqualTo(Date value) {
            addCriterion("love_time <>", value, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeGreaterThan(Date value) {
            addCriterion("love_time >", value, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("love_time >=", value, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeLessThan(Date value) {
            addCriterion("love_time <", value, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeLessThanOrEqualTo(Date value) {
            addCriterion("love_time <=", value, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeIn(List<Date> values) {
            addCriterion("love_time in", values, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeNotIn(List<Date> values) {
            addCriterion("love_time not in", values, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeBetween(Date value1, Date value2) {
            addCriterion("love_time between", value1, value2, "loveTime");
            return (Criteria) this;
        }

        public Criteria andLoveTimeNotBetween(Date value1, Date value2) {
            addCriterion("love_time not between", value1, value2, "loveTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeIsNull() {
            addCriterion("coin_time is null");
            return (Criteria) this;
        }

        public Criteria andCoinTimeIsNotNull() {
            addCriterion("coin_time is not null");
            return (Criteria) this;
        }

        public Criteria andCoinTimeEqualTo(Date value) {
            addCriterion("coin_time =", value, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeNotEqualTo(Date value) {
            addCriterion("coin_time <>", value, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeGreaterThan(Date value) {
            addCriterion("coin_time >", value, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("coin_time >=", value, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeLessThan(Date value) {
            addCriterion("coin_time <", value, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeLessThanOrEqualTo(Date value) {
            addCriterion("coin_time <=", value, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeIn(List<Date> values) {
            addCriterion("coin_time in", values, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeNotIn(List<Date> values) {
            addCriterion("coin_time not in", values, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeBetween(Date value1, Date value2) {
            addCriterion("coin_time between", value1, value2, "coinTime");
            return (Criteria) this;
        }

        public Criteria andCoinTimeNotBetween(Date value1, Date value2) {
            addCriterion("coin_time not between", value1, value2, "coinTime");
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