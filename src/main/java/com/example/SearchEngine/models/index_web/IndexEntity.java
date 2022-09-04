package com.example.SearchEngine.models.index_web;

import com.example.SearchEngine.models.index_web.pk.LemmaPageId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "index")
public class IndexEntity implements Serializable {

    @EmbeddedId
   private LemmaPageId pk = new LemmaPageId();

    @Column(nullable = false)
    private Float rank;

    public PageEntity getPage(){
        return getPk().getPage();
    }

    public void setPageEntity(PageEntity pageEntity){
        getPk().setPage(pageEntity);
    }

    public Lemma getLemma(){
        return getPk().getLemma();
    }

    public void setLemma(Lemma lemma){
        getPk().setLemma(lemma);
    }
}
