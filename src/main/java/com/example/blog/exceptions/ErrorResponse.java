package com.example.blog.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private Date timestamp;
    private String status;
    private String message;
    private String details;

    public ErrorResponse(Date date, String status, String message, String description) {
        this.timestamp=date;
        this.status= status;
        this.message=message;
        this.details=description;
    }
}
