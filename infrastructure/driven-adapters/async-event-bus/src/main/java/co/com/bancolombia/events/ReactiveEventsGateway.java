package co.com.bancolombia.events;

import co.com.bancolombia.model.events.BoxCreatedEvent;
import co.com.bancolombia.model.events.BoxDeletedEvent;
import co.com.bancolombia.model.events.BoxNameUpdatedEvent;
import co.com.bancolombia.model.events.BoxReopenedEvent;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static reactor.core.publisher.Mono.from;

@EnableDomainEventBus
public class ReactiveEventsGateway implements EventsGateway {
    public static final String BOX_CREATED = "box.event.created";
    public static final String BOX_NAME_UPDATED = "box.event.nameupdated";
    public static final String BOX_DELETED = "box.event.deleted";
    public static final String BOX_REOPENED = "box.event.reopened";
    private final DomainEventBus domainEventBus;

    public ReactiveEventsGateway(DomainEventBus domainEventBus) {
        this.domainEventBus = domainEventBus;
    }

    private String resolveEventType(Object event) {

        if (event instanceof BoxCreatedEvent) {
            return BOX_CREATED;
        } if (event instanceof BoxNameUpdatedEvent) {
            return BOX_NAME_UPDATED;
        } if (event instanceof BoxDeletedEvent) {
            return BOX_DELETED;
        }if (event instanceof BoxReopenedEvent) {
            return BOX_REOPENED;
        }else {
            return "box.event";
        }
    }
    @Override
    public Mono<Void> emit(Object event) {
        String eventType = resolveEventType(event);
        return Mono.from(domainEventBus.emit(new DomainEvent<>(eventType, UUID.randomUUID().toString(), event)));
    }
}
