package com.example.SearchEngine.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class IllegalSearchQueryParamException extends Exception {
    private String customMessage;
    public IllegalSearchQueryParamException(String customMessage) {
        this.customMessage = customMessage;
    }
}
