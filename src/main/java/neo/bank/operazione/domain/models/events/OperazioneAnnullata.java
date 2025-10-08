package neo.bank.operazione.domain.models.events;

public record OperazioneAnnullata() implements EventPayload {

    @Override
    public String eventType() {
        return "OperazioneAnnullata";
    }
}
