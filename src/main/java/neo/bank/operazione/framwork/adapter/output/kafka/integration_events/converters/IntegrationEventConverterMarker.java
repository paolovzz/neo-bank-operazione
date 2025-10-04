package neo.bank.operazione.framwork.adapter.output.kafka.integration_events.converters;

import neo.bank.operazione.domain.models.events.EventPayload;

public interface IntegrationEventConverterMarker {
    Class<? extends EventPayload> supportedDomainEvent();
}
