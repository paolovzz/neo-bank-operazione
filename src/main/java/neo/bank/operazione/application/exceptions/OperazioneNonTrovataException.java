package neo.bank.operazione.application.exceptions;

public class OperazioneNonTrovataException extends RuntimeException {
    
    public OperazioneNonTrovataException(String idOperazione) {
        super(String.format("Operazione con id [%s] non trovata...", idOperazione));
    }
}
