package com.example.SearchEngine.models.statistic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Total {
    private int sites;
    private int pages;
    private int lemmas;
    private boolean isIndexing;

}
