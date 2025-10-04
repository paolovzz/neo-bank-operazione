package neo.bank.operazione.domain.models.aggregates;

import neo.bank.operazione.domain.models.events.EventPayload;

public interface Applier {
    void apply(EventPayload event);
}