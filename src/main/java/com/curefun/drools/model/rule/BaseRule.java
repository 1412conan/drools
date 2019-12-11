package com.curefun.drools.model.rule;

public class BaseRule {

    private Integer rule_id;
    private String rule_name;
    private boolean rule_result;
    private Integer version;

    public Integer getRule_id() {
        return rule_id;
    }

    public void setRule_id(Integer rule_id) {
        this.rule_id = rule_id;
    }

    public String getRule_name() {
        return rule_name;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public boolean isRule_result() {
        return rule_result;
    }

    public void setRule_result(boolean rule_result) {
        this.rule_result = rule_result;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
