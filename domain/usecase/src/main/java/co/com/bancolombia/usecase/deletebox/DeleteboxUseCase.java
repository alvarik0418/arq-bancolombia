package co.com.bancolombia.usecase.deletebox;

import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.BoxDeletedEvent;
import co.com.bancolombia.model.events.BoxNameUpdatedEvent;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class DeleteboxUseCase {
    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;

    public DeleteboxUseCase(BoxRepository boxRepository, EventsGateway eventsGateway){
        this.boxRepository = boxRepository;
        this.eventsGateway = eventsGateway;
    }

    public Mono<Void> deleteBox(String id){
        return boxRepository.findById(id)
                .flatMap(box -> {
                    BoxStatus statusOld = box.getStatus();
                    box.delete();
                    return boxRepository.save(box).flatMap(savedBox -> {
                        BoxDeletedEvent event = new BoxDeletedEvent(savedBox.getId(), "Pepito", Instant.now());
                        event.setStatusOld(statusOld);
                        event.setStatusNew(savedBox.getStatus());
                        return eventsGateway.emit(event);
                });
        });
    }
}
