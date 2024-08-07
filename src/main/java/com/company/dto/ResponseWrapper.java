package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper {
    private boolean success;
    private String message;
    private Integer code;
    private Object data;

    public ResponseWrapper(String message, Object data,HttpStatus httpStatus) {
        this.success = true;
        this.message = message;
        this.code = httpStatus.value();
        this.data = data;
    }

    public ResponseWrapper(String message, HttpStatus httpStatus) {
        this.message = message;
        this.code = httpStatus.value();
        this.success = true;
    }
}
