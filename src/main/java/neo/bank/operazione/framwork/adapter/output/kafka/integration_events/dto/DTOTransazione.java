package neo.bank.operazione.framwork.adapter.output.kafka.integration_events.dto;

import lombok.Value;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;

@Value
public class DTOTransazione {
    
    private String idTransazione;
    private String iban;
    private TipologiaFlusso tipologiaFlusso;

}
