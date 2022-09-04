package com.example.SearchEngine.models.index_web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "site")
public class SiteEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "status_time")
    private LocalDateTime statusTime; //дата и время статуса (в случае статуса INDEXING дата и время должны обновляться регулярно при добавлении каждой новой страницы в индекс);

    @Column(name = "last_error")
    private String lastError;  //текст ошибки индексации или NULL, если её не было;

    @Column(nullable = false)
    private String uri; //адрес главной страницы сайта;

    @Column(nullable = true)
    private String name;//имя сайта







}
