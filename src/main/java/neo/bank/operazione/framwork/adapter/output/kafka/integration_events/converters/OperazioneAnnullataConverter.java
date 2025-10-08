package neo.bank.operazione.framwork.adapter.output.kafka.integration_events.converters;

import jakarta.enterprise.context.ApplicationScoped;
import neo.bank.operazione.domain.models.events.OperazioneAnnullata;
import neo.bank.operazione.framwork.adapter.output.kafka.integration_events.IEOperazioneAnnullata;

@ApplicationScoped
public class OperazioneAnnullataConverter implements IntegrationEventConverter<OperazioneAnnullata, IEOperazioneAnnullata>, IntegrationEventConverterMarker{

    @Override
    public IEOperazioneAnnullata convert(OperazioneAnnullata ev) {
        return new IEOperazioneAnnullata();
    }


    @Override
    public Class<OperazioneAnnullata> supportedDomainEvent() {
        return OperazioneAnnullata.class;
    }
    
}
