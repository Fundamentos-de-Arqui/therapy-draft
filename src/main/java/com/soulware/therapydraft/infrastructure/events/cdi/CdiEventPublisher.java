package com.soulware.therapydraft.infrastructure.events.cdi;

import com.soulware.therapydraft.shared.domain.model.events.DomainEvent;
import com.soulware.therapydraft.shared.domain.model.events.DomainEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class CdiEventPublisher implements DomainEventPublisher {
    @Inject
    private Event<Object> cdiEvent;

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent event) {
        cdiEvent.fire(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishAsync(DomainEvent event) {
        cdiEvent.fireAsync(event);
    }
}
