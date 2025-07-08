package co.com.bancolombia.usecase.updateboxname;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.BoxNameUpdatedEvent;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class UpdateboxnameUseCase {
    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;

    public UpdateboxnameUseCase(BoxRepository boxRepository, EventsGateway eventsGateway){
        this.boxRepository = boxRepository;
        this.eventsGateway = eventsGateway;
    }

    public Mono<Box> updateBoxName (String id, String nameUpdated){
        return boxRepository.findById(id)
                .flatMap(box -> {
                    String nameOld = box.getName();
                    box.setName(nameUpdated);
                    return boxRepository.save(box).flatMap(savedBox -> {
                        BoxNameUpdatedEvent event = new BoxNameUpdatedEvent(savedBox.getId(), "Pepito", Instant.now());
                        event.setNameOld(nameOld);
                        event.setNameNew(savedBox.getName());
                        return eventsGateway.emit(event).thenReturn(savedBox);
                    });
                });
    }
}
