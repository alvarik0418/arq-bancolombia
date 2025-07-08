package co.com.bancolombia.usecase.getboxbyid;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import reactor.core.publisher.Mono;

public class GetboxbyidUseCase {
    private final BoxRepository boxRepository;

    public GetboxbyidUseCase(BoxRepository boxRepository){
        this.boxRepository = boxRepository;
    }

    public Mono<Box> getBoxById(String id){
        return boxRepository.findById(id);
    }
}
