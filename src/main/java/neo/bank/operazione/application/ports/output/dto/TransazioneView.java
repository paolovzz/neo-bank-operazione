package neo.bank.operazione.application.ports.output.dto;

import java.time.LocalDateTime;

import lombok.Value;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;

@Value
public class TransazioneView {
    
    private String idTransazione;
    private String idOperazione;
    private double importo;
    private String iban;
    private LocalDateTime dataCreazione;
    private String causale;
    private TipologiaFlusso tipologiaFlusso;
}
