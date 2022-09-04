package com.example.SearchEngine.models.errors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class InterruptedCustomError extends Exception{

    private String customMessage;

    private Exception exception;



    public InterruptedCustomError(String customMessage, Exception exception) {
        this.customMessage = customMessage;
        this.exception = exception;
    }
}
