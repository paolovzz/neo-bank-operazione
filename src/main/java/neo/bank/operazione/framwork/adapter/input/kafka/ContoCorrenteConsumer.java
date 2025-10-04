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
import neo.bank.operazione.application.OperazioneUseCase;
import neo.bank.operazione.application.ports.input.commands.AvviaOperazioneCmd;
import neo.bank.operazione.domain.models.vo.Iban;

@ApplicationScoped
@Slf4j
public class ContoCorrenteConsumer {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private OperazioneUseCase app;

    private static final String EVENT_OWNER = "CONTO_CORRENTE";
    private static final String BONIFICO_PREDISPOSTO_EVENT_NAME = "BonificoPredisposto";

    @Incoming("cliente-notifications")
    @Blocking
    public CompletionStage<Void> consume(Message<String> msg) {
        var metadata = msg.getMetadata(IncomingKafkaRecordMetadata.class).orElseThrow();
        String eventType = new String(metadata.getHeaders().lastHeader("eventType").value(), StandardCharsets.UTF_8);
        String aggregateName = new String(metadata.getHeaders().lastHeader("aggregateName").value(),
                StandardCharsets.UTF_8);
        String payload = msg.getPayload();
        log.info("INCOMING:\n- EventType => {}\n- AggregateName => {}", eventType, aggregateName);
        if (aggregateName.equals(EVENT_OWNER)) {
            JsonNode json = convertToJsonNode(payload);
            switch (eventType) {
                case BONIFICO_PREDISPOSTO_EVENT_NAME:{
                    String ibanMittente = json.get("ibanMittente").asText();
                    String ibanDestinatario = json.get("ibanDestinatario").asText();
                    String causale = json.get("causale").asText();
                    double importo =  Math.abs(json.get("importo").asDouble());
                    app.avviaOperazione( new AvviaOperazioneCmd(new Iban(ibanMittente), new Iban(ibanDestinatario), causale, importo));
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
