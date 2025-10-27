package neo.bank.operazione.framwork.adapter.output.kafka.integration_events.converters;

import jakarta.enterprise.context.ApplicationScoped;
import neo.bank.operazione.domain.models.events.OperazioneConclusa;
import neo.bank.operazione.framwork.adapter.output.kafka.integration_events.IEOperazioneConclusa;

@ApplicationScoped
public class OperazioneConclusaConverter implements IntegrationEventConverter<OperazioneConclusa, IEOperazioneConclusa>, IntegrationEventConverterMarker{

    @Override
    public IEOperazioneConclusa convert(OperazioneConclusa ev) {
        return new IEOperazioneConclusa();
    }


    @Override
    public Class<OperazioneConclusa> supportedDomainEvent() {
        return OperazioneConclusa.class;
    }
    
}
