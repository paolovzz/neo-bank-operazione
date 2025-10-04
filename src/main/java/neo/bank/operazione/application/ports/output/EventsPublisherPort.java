package neo.bank.operazione.application.ports.output;

import java.util.List;

import neo.bank.operazione.domain.models.events.EventPayload;

public interface EventsPublisherPort {
    void publish(String aggregateName, String aggregateId, List<EventPayload> events);
}
