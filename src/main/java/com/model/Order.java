package com.model;

public enum Order {
    ADMIN_CKEY_ASC("a_ckey ASC"), ADMIN_CKEY_DESC("a_ckey DESC"), DURATION_ASC("duration ASC"), NO_ORDER(""),
    DURATION_DESC("duration DESC"), BANTIME_ASC("bantime ASC"), BANTIME_DESC("bantime DESC"), JOB_ASC("job ASC"), JOB_DESC("job DESC");

    private String orderQueryValue;

    Order(String orderQueryValue) {
        this.orderQueryValue = orderQueryValue;
    }

    public String getOrderQueryValue() {
        return orderQueryValue;
    }
}