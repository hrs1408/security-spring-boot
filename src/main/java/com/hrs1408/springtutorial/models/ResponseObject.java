package com.hrs1408.springtutorial.models;

import lombok.Data;

import java.util.List;

@Data
public class ResponseObject {
    private String status;
    private String message;
    private Object data;

    private List<Object> listData;

    public ResponseObject() {
    }

    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseObject(String status, String message, List<Object> listData) {
        this.status = status;
        this.message = message;
        this.listData = listData;
    }
}
