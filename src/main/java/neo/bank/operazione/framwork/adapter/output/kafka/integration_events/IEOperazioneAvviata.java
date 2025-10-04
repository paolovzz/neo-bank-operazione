package neo.bank.operazione.framwork.adapter.output.kafka.integration_events;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Value;
import neo.bank.operazione.domain.models.entities.Transazione;

@Value
public class IEOperazioneAvviata implements Serializable {
    private String idOperazione;
    private LocalDateTime dataCreazione;
    private Transazione[] transazioni;
}
