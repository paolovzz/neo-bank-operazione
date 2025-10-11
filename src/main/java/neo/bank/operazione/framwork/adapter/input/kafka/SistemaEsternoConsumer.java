package neo.bank.operazione.framwork.adapter.input.kafka;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.application.OperazioneUseCase;
import neo.bank.operazione.application.ports.input.commands.AnnullaOperazioneCmd;
import neo.bank.operazione.application.ports.input.commands.ConcludiOperazioneCmd;
import neo.bank.operazione.domain.models.vo.IdOperazione;

@ApplicationScoped
@Slf4j
public class SistemaEsternoConsumer {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private OperazioneUseCase app;

    private static final String EVENT_OWNER = "SISTEMA_ESTERNO";
    private static final String CONTROLLI_SUPERATI_EVENT_NAME = "ControlliSuperati";
    private static final String CONTROLLI_NON_SUPERATI_EVENT_NAME = "ControlliNonSuperati";

    @Incoming("sistema-esterno-notifications")
    public CompletionStage<Void> consume(Message<String> msg) {
        var metadata = msg.getMetadata(IncomingKafkaRecordMetadata.class).orElseThrow();
        String eventType = new String(metadata.getHeaders().lastHeader("eventType").value(), StandardCharsets.UTF_8);
        String aggregateName = new String(metadata.getHeaders().lastHeader("aggregateName").value(),
                StandardCharsets.UTF_8);
        log.info("INCOMING:\n- EventType => {}\n- AggregateName => {}", eventType, aggregateName);
         String payload = msg.getPayload();
          JsonNode json = convertToJsonNode(payload);
        if (aggregateName.equals(EVENT_OWNER)) {
            String idOperazione = json.get("idOperazione").asText();
            switch (eventType) {
                case CONTROLLI_SUPERATI_EVENT_NAME:{
                    app.concludiOperazione(new ConcludiOperazioneCmd(new IdOperazione(idOperazione)));
                    break;
                }
                case CONTROLLI_NON_SUPERATI_EVENT_NAME:{
                    app.annullaOperazione(new AnnullaOperazioneCmd(new IdOperazione(idOperazione)));
                    break;
                }
                default:
                    log.warn("Evento [{}] non gestito...", eventType);
                    break;
            }
        }
        return msg.ack();
    }

    private JsonNode convertToJsonNode(String payload) {
        try {
            return mapper.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Errore durante la conversione json del messaggio kafka", e);
        }
    }
}
