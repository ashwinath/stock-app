package com.ashwinchat.stockapp.model.view;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import com.ashwinchat.stockapp.model.view.embeddable.EmbeddableTimeStamps;

@MappedSuperclass
public class DatabaseView {

    private EmbeddableTimeStamps timeStamps;

    @Embedded
    public EmbeddableTimeStamps getTimeStamps() {
        return this.timeStamps;
    }

    public void setTimeStamps(EmbeddableTimeStamps timeStamps) {
        this.timeStamps = timeStamps;
    }

}
