package neo.bank.operazione.framwork.adapter.input.rest.response;

import java.time.LocalDateTime;

import lombok.Getter;
import neo.bank.operazione.application.ports.output.dto.TransazioneView;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;

@Getter
public class TransazioneInfoResponse {
   
    private String idTransazione;
    private String idOperazione;
    private double importo;
    private String iban;
    private LocalDateTime dataCreazione;
    private String causale;
    private TipologiaFlusso tipologiaFlusso;

    public TransazioneInfoResponse(TransazioneView transazione) {
        this.idTransazione = transazione.getIdTransazione();
        this.idOperazione = transazione.getIdOperazione();
        this.importo = transazione.getImporto();
        this.dataCreazione = transazione.getDataCreazione();
        this.causale = transazione.getCausale();
        this.tipologiaFlusso = transazione.getTipologiaFlusso();
        this.iban = transazione.getIban();
    }

    public TransazioneInfoResponse(String idTransazione, String idOperazione, double importo, String iban, LocalDateTime dataCreazione,String causale,TipologiaFlusso tipologiaFlusso) {
        this.idTransazione = idTransazione;
        this.idOperazione = idOperazione;
        this.importo = importo;
        this.dataCreazione = dataCreazione;
        this.causale = causale;
        this.tipologiaFlusso = tipologiaFlusso;
        this.iban = iban;
    }

}
