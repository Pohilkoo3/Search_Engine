package com.example.SearchEngine.models.statistic;

import com.example.SearchEngine.models.index_web.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class Detailed {

    private String url;
    private String name;
    private Status status;
    private LocalDateTime statusTime;
    private String error;
    private int pages;
    private int lemmas;


}
