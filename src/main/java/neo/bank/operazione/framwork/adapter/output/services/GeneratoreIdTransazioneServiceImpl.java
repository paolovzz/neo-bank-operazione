package neo.bank.operazione.framwork.adapter.output.services;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import neo.bank.operazione.domain.models.vo.IdTransazione;
import neo.bank.operazione.domain.services.GeneratoreIdTransazioneService;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GeneratoreIdTransazioneServiceImpl  implements GeneratoreIdTransazioneService{@Override
    public IdTransazione genera() {
        return new IdTransazione(UUID.randomUUID().toString());
    }


    
}
