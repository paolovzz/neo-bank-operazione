package neo.bank.operazione.domain.models.events;

import neo.bank.operazione.domain.models.entities.Transazione;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.IdOperazione;

public record OperazioneAvviata(
        IdOperazione idOperazione,
        DataCreazione dataCreazione, Transazione[] transazioni) implements EventPayload {

    @Override
    public String eventType() {
        return "OperazioneAvviata";
    }
}
