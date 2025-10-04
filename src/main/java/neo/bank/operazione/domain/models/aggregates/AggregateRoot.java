package neo.bank.operazione.domain.models.aggregates;

import java.util.ArrayList;
import java.util.List;

import neo.bank.operazione.domain.models.events.EventPayload;

public abstract class AggregateRoot<T extends Applier> {

    private List<EventPayload> events = new ArrayList<>();

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

     public List<EventPayload> popChanges() {
        List<EventPayload> changes = List.copyOf(events);
        events.clear();
        return changes;
    }

    public void events(EventPayload... payloads) {
        for (EventPayload payload : payloads) {
            self().apply(payload);       // Applica evento allo stato
            this.events.add(payload);   // Salva evento nella lista
        }
    }
    
}
