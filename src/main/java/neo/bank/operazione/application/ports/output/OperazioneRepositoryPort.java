package neo.bank.operazione.application.ports.output;

import java.util.List;

import neo.bank.operazione.domain.models.aggregates.Operazione;
import neo.bank.operazione.domain.models.events.EventPayload;
import neo.bank.operazione.domain.models.vo.IdOperazione;

public interface OperazioneRepositoryPort {
    
    public void save(IdOperazione idOperazione, List<EventPayload> events);
    public Operazione findById (String aggregateId);
}
