package neo.bank.operazione.domain.models.aggregates;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.domain.models.entities.Transazione;
import neo.bank.operazione.domain.models.enums.StatoOperazione;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.events.EventPayload;
import neo.bank.operazione.domain.models.events.OperazioneAnnullata;
import neo.bank.operazione.domain.models.events.OperazioneAvviata;
import neo.bank.operazione.domain.models.events.OperazioneConclusa;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.Iban;
import neo.bank.operazione.domain.models.vo.IdOperazione;
import neo.bank.operazione.domain.services.GeneratoreIdOperazioneService;
import neo.bank.operazione.domain.services.GeneratoreIdTransazioneService;


@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Operazione extends AggregateRoot<Operazione> implements Applier  {

    public static final String AGGREGATE_NAME = "OPERAZIONE";
    private IdOperazione idOperazione;
    private DataCreazione dataCreazione;
    private String causale;
    private double importo;
    private Transazione transazioneIn;
    private Transazione transazioneOut;
    private StatoOperazione stato;


    public static Operazione avvia(GeneratoreIdOperazioneService generatoreIdOperazione, GeneratoreIdTransazioneService generatoreIdTransazioneService, Iban ibanMittente, Iban ibanDestinatario, String causale, double importo) {

        IdOperazione idOperazione = generatoreIdOperazione.genera();
        Operazione operazione = new Operazione();
        operazione.idOperazione = idOperazione;
        DataCreazione dataCreazione = new DataCreazione(LocalDateTime.now(ZoneOffset.UTC));
        Transazione out = new Transazione(generatoreIdTransazioneService.genera(), ibanMittente, TipologiaFlusso.ADDEBITO);
        Transazione in = new Transazione(generatoreIdTransazioneService.genera(), ibanDestinatario, TipologiaFlusso.ACCREDITO);
        operazione.events(new OperazioneAvviata(idOperazione, dataCreazione, causale, Math.abs(importo),in, out));
        return operazione;
    }

    public void concludi() {
        events(new OperazioneConclusa());
    }

    public void annulla() {
        events(new OperazioneAnnullata());
    }

    private void apply(OperazioneAvviata event) {
        this.idOperazione = event.idOperazione();
        this.dataCreazione = event.dataCreazione();
        transazioneIn = event.transazioneIn();
        transazioneOut = event.transazioneOut();
        this.stato = StatoOperazione.AVVIATA;
        this.importo = event.importo();
        this.causale = event.causale();
    }

     private void apply(OperazioneConclusa event) {
        stato = StatoOperazione.CONCLUSA;
    }

     private void apply(OperazioneAnnullata event) {
        stato = StatoOperazione.ANNULLATA;
    }

    @Override
    public void apply(EventPayload event) {
         switch (event) {
            case OperazioneAvviata ev -> apply((OperazioneAvviata) ev);
            case OperazioneConclusa ev -> apply((OperazioneConclusa) ev);
            case OperazioneAnnullata ev -> apply((OperazioneAnnullata) ev);
            default -> throw new IllegalArgumentException("Evento non supportato");
        }
    }

}