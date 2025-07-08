package co.com.bancolombia.usecase.reopenbox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.BoxReopenedEvent;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class ReopenboxUseCase {
    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;

    public ReopenboxUseCase(BoxRepository boxRepository, EventsGateway eventsGateway){
        this.boxRepository = boxRepository;
        this.eventsGateway = eventsGateway;
    }

    public Mono<Box> reopenBox(String id){
        return boxRepository.findById(id)
                .flatMap(box -> {
                    BoxStatus statusOld = box.getStatus();
                    box.reopen();
                    return boxRepository.save(box).flatMap(savedBox -> {
                        BoxReopenedEvent event = new BoxReopenedEvent(savedBox.getId(), "Pepito", Instant.now());
                        event.setStatusOld(statusOld);
                        event.setStatusNew(BoxStatus.OPENED);
                        return eventsGateway.emit(event).thenReturn(savedBox);
                    });
                });
    }
}
