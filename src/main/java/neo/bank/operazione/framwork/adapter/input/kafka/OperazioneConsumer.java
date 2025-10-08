package neo.bank.operazione.framwork.adapter.input.kafka;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.application.TransazioneProjUseCase;
import neo.bank.operazione.application.ports.input.commands.AggiornaProiezioneCmd;
import neo.bank.operazione.domain.models.vo.IdOperazione;

@ApplicationScoped
@Slf4j
public class OperazioneConsumer {

    @Inject
    private TransazioneProjUseCase app;

    private static final String EVENT_OWNER = "OPERAZIONE";
    private static final String OPERAZIONE_CONCLUSA_EVENT_NAME = "OperazioneConclusa";

    @Incoming("operazione-2-notifications")
    @Blocking
    public CompletionStage<Void> consume(Message<String> msg) {
        var metadata = msg.getMetadata(IncomingKafkaRecordMetadata.class).orElseThrow();
        String eventType = new String(metadata.getHeaders().lastHeader("eventType").value(), StandardCharsets.UTF_8);
        String aggregateName = new String(metadata.getHeaders().lastHeader("aggregateName").value(),
                StandardCharsets.UTF_8);
        log.info("INCOMING:\n- EventType => {}\n- AggregateName => {}", eventType, aggregateName);
        if (aggregateName.equals(EVENT_OWNER)) {
            String aggregateId = (String) metadata.getKey();
            switch (eventType) {
                case OPERAZIONE_CONCLUSA_EVENT_NAME:{
                    app.aggiornaProiezione( new AggiornaProiezioneCmd(new IdOperazione(aggregateId)));
                    break;
                }
                default:
                    log.warn("Evento [{}] non gestito...", eventType);
                    break;
            }
        }
        return msg.ack();
    }
}
