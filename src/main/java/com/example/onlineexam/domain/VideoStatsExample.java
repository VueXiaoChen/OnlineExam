package com.example.onlineexam.domain;

import java.util.ArrayList;
import java.util.List;

public class VideoStatsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VideoStatsExample() {
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

        public Criteria andDanmuIsNull() {
            addCriterion("danmu is null");
            return (Criteria) this;
        }

        public Criteria andDanmuIsNotNull() {
            addCriterion("danmu is not null");
            return (Criteria) this;
        }

        public Criteria andDanmuEqualTo(Integer value) {
            addCriterion("danmu =", value, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuNotEqualTo(Integer value) {
            addCriterion("danmu <>", value, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuGreaterThan(Integer value) {
            addCriterion("danmu >", value, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuGreaterThanOrEqualTo(Integer value) {
            addCriterion("danmu >=", value, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuLessThan(Integer value) {
            addCriterion("danmu <", value, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuLessThanOrEqualTo(Integer value) {
            addCriterion("danmu <=", value, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuIn(List<Integer> values) {
            addCriterion("danmu in", values, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuNotIn(List<Integer> values) {
            addCriterion("danmu not in", values, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuBetween(Integer value1, Integer value2) {
            addCriterion("danmu between", value1, value2, "danmu");
            return (Criteria) this;
        }

        public Criteria andDanmuNotBetween(Integer value1, Integer value2) {
            addCriterion("danmu not between", value1, value2, "danmu");
            return (Criteria) this;
        }

        public Criteria andGoodIsNull() {
            addCriterion("good is null");
            return (Criteria) this;
        }

        public Criteria andGoodIsNotNull() {
            addCriterion("good is not null");
            return (Criteria) this;
        }

        public Criteria andGoodEqualTo(Integer value) {
            addCriterion("good =", value, "good");
            return (Criteria) this;
        }

        public Criteria andGoodNotEqualTo(Integer value) {
            addCriterion("good <>", value, "good");
            return (Criteria) this;
        }

        public Criteria andGoodGreaterThan(Integer value) {
            addCriterion("good >", value, "good");
            return (Criteria) this;
        }

        public Criteria andGoodGreaterThanOrEqualTo(Integer value) {
            addCriterion("good >=", value, "good");
            return (Criteria) this;
        }

        public Criteria andGoodLessThan(Integer value) {
            addCriterion("good <", value, "good");
            return (Criteria) this;
        }

        public Criteria andGoodLessThanOrEqualTo(Integer value) {
            addCriterion("good <=", value, "good");
            return (Criteria) this;
        }

        public Criteria andGoodIn(List<Integer> values) {
            addCriterion("good in", values, "good");
            return (Criteria) this;
        }

        public Criteria andGoodNotIn(List<Integer> values) {
            addCriterion("good not in", values, "good");
            return (Criteria) this;
        }

        public Criteria andGoodBetween(Integer value1, Integer value2) {
            addCriterion("good between", value1, value2, "good");
            return (Criteria) this;
        }

        public Criteria andGoodNotBetween(Integer value1, Integer value2) {
            addCriterion("good not between", value1, value2, "good");
            return (Criteria) this;
        }

        public Criteria andBadIsNull() {
            addCriterion("bad is null");
            return (Criteria) this;
        }

        public Criteria andBadIsNotNull() {
            addCriterion("bad is not null");
            return (Criteria) this;
        }

        public Criteria andBadEqualTo(Integer value) {
            addCriterion("bad =", value, "bad");
            return (Criteria) this;
        }

        public Criteria andBadNotEqualTo(Integer value) {
            addCriterion("bad <>", value, "bad");
            return (Criteria) this;
        }

        public Criteria andBadGreaterThan(Integer value) {
            addCriterion("bad >", value, "bad");
            return (Criteria) this;
        }

        public Criteria andBadGreaterThanOrEqualTo(Integer value) {
            addCriterion("bad >=", value, "bad");
            return (Criteria) this;
        }

        public Criteria andBadLessThan(Integer value) {
            addCriterion("bad <", value, "bad");
            return (Criteria) this;
        }

        public Criteria andBadLessThanOrEqualTo(Integer value) {
            addCriterion("bad <=", value, "bad");
            return (Criteria) this;
        }

        public Criteria andBadIn(List<Integer> values) {
            addCriterion("bad in", values, "bad");
            return (Criteria) this;
        }

        public Criteria andBadNotIn(List<Integer> values) {
            addCriterion("bad not in", values, "bad");
            return (Criteria) this;
        }

        public Criteria andBadBetween(Integer value1, Integer value2) {
            addCriterion("bad between", value1, value2, "bad");
            return (Criteria) this;
        }

        public Criteria andBadNotBetween(Integer value1, Integer value2) {
            addCriterion("bad not between", value1, value2, "bad");
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

        public Criteria andShareIsNull() {
            addCriterion("`share` is null");
            return (Criteria) this;
        }

        public Criteria andShareIsNotNull() {
            addCriterion("`share` is not null");
            return (Criteria) this;
        }

        public Criteria andShareEqualTo(Integer value) {
            addCriterion("`share` =", value, "share");
            return (Criteria) this;
        }

        public Criteria andShareNotEqualTo(Integer value) {
            addCriterion("`share` <>", value, "share");
            return (Criteria) this;
        }

        public Criteria andShareGreaterThan(Integer value) {
            addCriterion("`share` >", value, "share");
            return (Criteria) this;
        }

        public Criteria andShareGreaterThanOrEqualTo(Integer value) {
            addCriterion("`share` >=", value, "share");
            return (Criteria) this;
        }

        public Criteria andShareLessThan(Integer value) {
            addCriterion("`share` <", value, "share");
            return (Criteria) this;
        }

        public Criteria andShareLessThanOrEqualTo(Integer value) {
            addCriterion("`share` <=", value, "share");
            return (Criteria) this;
        }

        public Criteria andShareIn(List<Integer> values) {
            addCriterion("`share` in", values, "share");
            return (Criteria) this;
        }

        public Criteria andShareNotIn(List<Integer> values) {
            addCriterion("`share` not in", values, "share");
            return (Criteria) this;
        }

        public Criteria andShareBetween(Integer value1, Integer value2) {
            addCriterion("`share` between", value1, value2, "share");
            return (Criteria) this;
        }

        public Criteria andShareNotBetween(Integer value1, Integer value2) {
            addCriterion("`share` not between", value1, value2, "share");
            return (Criteria) this;
        }

        public Criteria andCommentIsNull() {
            addCriterion("`comment` is null");
            return (Criteria) this;
        }

        public Criteria andCommentIsNotNull() {
            addCriterion("`comment` is not null");
            return (Criteria) this;
        }

        public Criteria andCommentEqualTo(Integer value) {
            addCriterion("`comment` =", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotEqualTo(Integer value) {
            addCriterion("`comment` <>", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThan(Integer value) {
            addCriterion("`comment` >", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThanOrEqualTo(Integer value) {
            addCriterion("`comment` >=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThan(Integer value) {
            addCriterion("`comment` <", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThanOrEqualTo(Integer value) {
            addCriterion("`comment` <=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentIn(List<Integer> values) {
            addCriterion("`comment` in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotIn(List<Integer> values) {
            addCriterion("`comment` not in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentBetween(Integer value1, Integer value2) {
            addCriterion("`comment` between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotBetween(Integer value1, Integer value2) {
            addCriterion("`comment` not between", value1, value2, "comment");
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