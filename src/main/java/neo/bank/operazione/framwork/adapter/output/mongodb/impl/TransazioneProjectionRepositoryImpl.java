package neo.bank.operazione.framwork.adapter.output.mongodb.impl;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.application.ports.output.TransazioneProjectionRepositoryPort;
import neo.bank.operazione.domain.models.entities.Transazione;
import neo.bank.operazione.framwork.adapter.output.mongodb.entities.TransazioneProjectionEntity;

@ApplicationScoped
@Slf4j
public class TransazioneProjectionRepositoryImpl implements PanacheMongoRepositoryBase<TransazioneProjectionEntity, String>, TransazioneProjectionRepositoryPort {
    
    @Override
    public void salva(Transazione transazione) {
        log.info("Registro la transazione creata nella projection");
        persist(new TransazioneProjectionEntity(transazione.getIdTransazione().id(), transazione.getImporto(), transazione.getIban().codice(), transazione.getDataCreazione().dataOra(), transazione.getCausale()));
    }
    


}
