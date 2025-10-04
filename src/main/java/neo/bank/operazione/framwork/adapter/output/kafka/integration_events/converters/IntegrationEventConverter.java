package neo.bank.operazione.framwork.adapter.output.kafka.integration_events.converters;

import neo.bank.operazione.domain.models.events.EventPayload;

public interface IntegrationEventConverter<DE extends EventPayload, IE> {
    IE convert(DE domainEvent);
}