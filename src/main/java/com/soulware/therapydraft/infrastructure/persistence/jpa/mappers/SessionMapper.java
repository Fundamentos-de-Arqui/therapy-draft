package com.soulware.therapydraft.infrastructure.persistence.jpa.mappers;

import com.soulware.therapydraft.domain.model.entities.Session;
import com.soulware.therapydraft.domain.model.valueobjects.TimeSlot;
import com.soulware.therapydraft.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.SessionEntity;
import jakarta.inject.Inject;

public record SessionMapper(){

    @Inject
    public SessionMapper(){}

    public Session toDomain(SessionEntity entity){
        if(entity == null){
            return null;
        }

        SessionId id = new SessionId(entity.getId());
        TimeSlot slotTime =  new TimeSlot(entity.getStartTime(), entity.getEndTime());
        TherapistId therapistId = new TherapistId(entity.getTherapistId());

        return new Session(
                id,
                slotTime,
                therapistId
        );
    }

    public SessionEntity toEntity(Session domain){
        if (domain == null){
            return null;
        }

        return new SessionEntity(
                domain.getSlot().getStart(),
                domain.getSlot().getEnd(),
                domain.getTherapist().value()
        );
    }
}
