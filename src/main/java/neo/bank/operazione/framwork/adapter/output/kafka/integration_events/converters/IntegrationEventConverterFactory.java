package neo.bank.operazione.framwork.adapter.output.kafka.integration_events.converters;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.domain.models.events.EventPayload;

@ApplicationScoped
@Slf4j
public class IntegrationEventConverterFactory {

    @Inject
    Instance<IntegrationEventConverterMarker> converters;

    private final Map<Class<? extends EventPayload>, IntegrationEventConverter<?, ?>> converterMap = new HashMap<>();

    void init(@Observes StartupEvent event) {
        log.info("Initializing IntegrationEventConverterFactory...");

        for (IntegrationEventConverterMarker marker : converters) {
            IntegrationEventConverter<?, ?> converter = (IntegrationEventConverter<?, ?>) marker;
            Class<? extends EventPayload> eventType = marker.supportedDomainEvent();
            converterMap.put(eventType, converter);
            log.info("Registered converter for: {}", eventType.getSimpleName());
        }
        log.info("Total converters registered: {}", converterMap.size());
    }

    @SuppressWarnings("unchecked")
    public <DE extends EventPayload, IE> IntegrationEventConverter<DE, IE> getConverter(DE event) {
        IntegrationEventConverter<?, ?> converter = converterMap.get(event.getClass());

        if (converter == null) {
            throw new IllegalArgumentException("No converter found for event class: " + event.getClass().getName());
        }

        return (IntegrationEventConverter<DE, IE>) converter;
    }
}
