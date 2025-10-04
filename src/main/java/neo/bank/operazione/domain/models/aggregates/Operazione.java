package neo.bank.operazione.domain.models.aggregates;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.bank.operazione.domain.models.entities.Transazione;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.events.EventPayload;
import neo.bank.operazione.domain.models.events.OperazioneAvviata;
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
    private Transazione[] transazioni = new Transazione[2];


    public static Operazione avvia(GeneratoreIdOperazioneService generatoreIdOperazione, GeneratoreIdTransazioneService generatoreIdTransazioneService, Iban ibanMittente, Iban ibanDestinatario, String causale, double importo) {

        IdOperazione idOperazione = generatoreIdOperazione.genera();
        Operazione operazione = new Operazione();
        operazione.idOperazione = idOperazione;
        DataCreazione dataCreazione = new DataCreazione(LocalDateTime.now(ZoneOffset.UTC));
        Transazione in = new Transazione(generatoreIdTransazioneService.genera(), dataCreazione, ibanMittente, causale, TipologiaFlusso.ADDEBITO, importo);
        Transazione out = new Transazione(generatoreIdTransazioneService.genera(), dataCreazione, ibanDestinatario, causale, TipologiaFlusso.ACCREDITO, importo);
        operazione.events(new OperazioneAvviata(idOperazione, dataCreazione, new Transazione[]{in, out}));
        return operazione;
    }

    private void apply(OperazioneAvviata event) {
        this.idOperazione = event.idOperazione();
        this.dataCreazione = event.dataCreazione();
        transazioni = event.transazioni();
    }

    @Override
    public void apply(EventPayload event) {
         switch (event) {
            case OperazioneAvviata ev -> apply((OperazioneAvviata) ev);
            default -> throw new IllegalArgumentException("Evento non supportato");
        }
    }

}