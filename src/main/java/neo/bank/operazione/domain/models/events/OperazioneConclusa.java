package neo.bank.operazione.domain.models.events;

public record OperazioneConclusa() implements EventPayload {

    @Override
    public String eventType() {
        return "OperazioneConclusa";
    }
}
