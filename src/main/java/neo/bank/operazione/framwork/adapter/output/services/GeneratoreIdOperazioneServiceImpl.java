package neo.bank.operazione.framwork.adapter.output.services;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import neo.bank.operazione.domain.models.vo.IdOperazione;
import neo.bank.operazione.domain.services.GeneratoreIdOperazioneService;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GeneratoreIdOperazioneServiceImpl  implements GeneratoreIdOperazioneService{@Override
    public IdOperazione genera() {
        return new IdOperazione(UUID.randomUUID().toString());
    }


    
}
