package com.example.SearchEngine.models.index_web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Site2Lemma {

    @Id
    private String id;
    private int lemmaId;

    private int site_id;
}
