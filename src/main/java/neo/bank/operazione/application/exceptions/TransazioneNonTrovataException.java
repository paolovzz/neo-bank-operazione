package neo.bank.operazione.application.exceptions;

public class TransazioneNonTrovataException extends RuntimeException {
    
    public TransazioneNonTrovataException(String idTransazione) {
        super(String.format("Transazione con id [%s] non trovata...", idTransazione));
    }
}
