package com.example.SearchEngine.models.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ResponseSearch {

    private boolean result;
    private int count;

    @JsonProperty(value = "data")
    private DataResponse[] dataResponse;




}
