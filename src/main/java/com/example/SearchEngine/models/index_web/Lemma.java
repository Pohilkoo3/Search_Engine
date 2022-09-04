package com.example.SearchEngine.models.index_web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "lemma", indexes = {
        @Index(columnList = "lemma", name = "lemma_hidx")
})
public class Lemma {

    public Lemma(String lemma) {
        this.lemma = lemma;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String lemma;

    @Column(nullable = false)
    private int frequency;

    @OneToMany(mappedBy = "pk.lemma")
    private Set<IndexEntity> indexEntitySet = new HashSet<>(0);






}
