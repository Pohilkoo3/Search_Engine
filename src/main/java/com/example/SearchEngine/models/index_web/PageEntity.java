package com.example.SearchEngine.models.index_web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "page", indexes = {
        @Index(columnList = "path", name = "path_hidx")
})
@Getter
@Setter
@NoArgsConstructor
public class PageEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String path;

    @Column(name = "code")
    private Integer codeResponse;

    private int site_id;

    @Column(columnDefinition = "text")
    private String content;

    @OneToMany(mappedBy = "pk.page", cascade = CascadeType.ALL)
    private Set<IndexEntity> indexEntitySet = new HashSet<>(0);




}
