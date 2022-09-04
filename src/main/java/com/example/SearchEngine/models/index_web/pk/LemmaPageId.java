package com.example.SearchEngine.models.index_web.pk;


import com.example.SearchEngine.models.index_web.Lemma;
import com.example.SearchEngine.models.index_web.PageEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class LemmaPageId implements Serializable {

    @ManyToOne
    private PageEntity page;
    @ManyToOne
    private Lemma lemma;
}
