package com.example.blog.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.TreeMap;

@NoArgsConstructor
public class ResponseHandler {

    public static ResponseEntity<Object> response(HttpStatus httpStatus, Boolean success, Object responseObject) {
        Map<String, Object> map = new TreeMap<>();
        map.put("success", success);
        map.put("responseObject", responseObject);
        return new ResponseEntity<>(map,httpStatus);
    }
}
