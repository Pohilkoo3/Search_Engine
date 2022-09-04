package com.example.SearchEngine.models.statistic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
public class Statistics {

    private Total total;

    @JsonProperty(value = "detailed")
    private HashSet<Detailed> detailedList = new HashSet<>(0);

   public void addDetailed(Detailed detailed){
        detailedList.add(detailed);
    }

}
