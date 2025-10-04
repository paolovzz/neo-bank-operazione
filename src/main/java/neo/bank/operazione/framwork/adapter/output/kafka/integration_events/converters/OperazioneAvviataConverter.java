package neo.bank.operazione.framwork.adapter.output.kafka.integration_events.converters;

import jakarta.enterprise.context.ApplicationScoped;
import neo.bank.operazione.domain.models.events.OperazioneAvviata;
import neo.bank.operazione.framwork.adapter.output.kafka.integration_events.IEOperazioneAvviata;

@ApplicationScoped
public class OperazioneAvviataConverter implements IntegrationEventConverter<OperazioneAvviata, IEOperazioneAvviata>, IntegrationEventConverterMarker{

    @Override
    public IEOperazioneAvviata convert(OperazioneAvviata ev) {
        return new IEOperazioneAvviata(ev.idOperazione().id(), ev.dataCreazione().dataOra(), ev.transazioni());
    }

    @Override
    public Class<OperazioneAvviata> supportedDomainEvent() {
        return OperazioneAvviata.class;
    }
    
}
