package neo.bank.operazione.framwork.adapter.input.kafka;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

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
        if (aggregateName.equals(EVENT_OWNER)) {
            String aggregateId = (String) metadata.getKey();
            switch (eventType) {
                case CONTROLLI_SUPERATI_EVENT_NAME:{
                    app.concludiOperazione(new ConcludiOperazioneCmd(new IdOperazione(aggregateId)));
                    break;
                }
                case CONTROLLI_NON_SUPERATI_EVENT_NAME:{
                    app.annullaOperazione(new AnnullaOperazioneCmd(new IdOperazione(aggregateId)));
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
