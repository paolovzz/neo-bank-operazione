package neo.bank.operazione.framwork.adapter.output.kafka.integration_events.converters;

import java.util.Arrays;

import jakarta.enterprise.context.ApplicationScoped;
import neo.bank.operazione.domain.models.events.OperazioneAvviata;
import neo.bank.operazione.framwork.adapter.output.kafka.integration_events.IEOperazioneAvviata;
import neo.bank.operazione.framwork.adapter.output.kafka.integration_events.dto.DTOTransazione;

@ApplicationScoped
public class OperazioneAvviataConverter implements IntegrationEventConverter<OperazioneAvviata, IEOperazioneAvviata>, IntegrationEventConverterMarker{

    @Override
    public IEOperazioneAvviata convert(OperazioneAvviata ev) {
        DTOTransazione[] transazioni = Arrays.stream(ev.transazioni())
            .map(e -> new DTOTransazione(
                e.getIdTransazione().id(),
                e.getIban().codice(),
                e.getTipologiaFlusso()
            ))
            .toArray(DTOTransazione[]::new);

        return new IEOperazioneAvviata(
            ev.idOperazione().id(),
            ev.dataCreazione().dataOra(),
            ev.causale(),
            ev.importo(),
            transazioni
        );
    }

    @Override
    public Class<OperazioneAvviata> supportedDomainEvent() {
        return OperazioneAvviata.class;
    }
    
}
