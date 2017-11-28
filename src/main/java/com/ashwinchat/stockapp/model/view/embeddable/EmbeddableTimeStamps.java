package com.ashwinchat.stockapp.model.view.embeddable;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EmbeddableTimeStamps {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @Column(name = "CRE_ON", nullable = false)
    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPD_ON", nullable = false)
    public LocalDateTime getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

}
