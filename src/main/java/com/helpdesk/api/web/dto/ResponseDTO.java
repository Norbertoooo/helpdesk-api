package com.helpdesk.api.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDTO<T> {
    private T data;
    private List<String> erros;
}
