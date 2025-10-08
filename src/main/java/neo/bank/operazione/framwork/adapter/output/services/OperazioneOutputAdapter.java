package neo.bank.operazione.framwork.adapter.output.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import neo.bank.operazione.application.exceptions.OperazioneNonTrovataException;
import neo.bank.operazione.application.ports.output.EventsPublisherPort;
import neo.bank.operazione.application.ports.output.OperazioneOutputPort;
import neo.bank.operazione.application.ports.output.OperazioneRepositoryPort;
import neo.bank.operazione.domain.models.aggregates.Operazione;
import neo.bank.operazione.domain.models.events.EventPayload;
import neo.bank.operazione.domain.models.vo.IdOperazione;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OperazioneOutputAdapter  implements OperazioneOutputPort{

    private final OperazioneRepositoryPort ccRepo;
    private final EventsPublisherPort publisherPort;

    @Override
    public void salva(Operazione cc) {

        List<EventPayload> events = cc.popChanges();
        ccRepo.save(cc.getIdOperazione(), events);
        publisherPort.publish(Operazione.AGGREGATE_NAME, cc.getIdOperazione().id(), events);
    }

    @Override
    public Operazione recuperaDaId(IdOperazione idOperazione) {
        Operazione cc = ccRepo.findById(idOperazione.id());
        if(cc == null) {
            throw new OperazioneNonTrovataException(idOperazione.id());
        }
       return cc;
    }
    
}
