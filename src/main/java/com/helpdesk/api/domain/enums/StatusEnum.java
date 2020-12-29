package com.helpdesk.api.domain.enums;

public enum StatusEnum {

    NEW,
    ASSIGN,
    RESOLVED,
    APPROVED,
    DISAPPROVED,
    CLOSED;

    public static StatusEnum getStatus(String status) {
        switch (status) {
            case "ASSIGN": return ASSIGN;
            case "RESOLVED": return RESOLVED;

            case "APPROVED": return APPROVED;
            case "DISAPPROVED": return DISAPPROVED;
            case "CLOSED": return CLOSED;
            default: return NEW;
        }
    }
}
