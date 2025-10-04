package neo.bank.operazione.application.ports.output;

import neo.bank.operazione.domain.models.entities.Transazione;

public interface TransazioneProjectionRepositoryPort {
    
    public void salva(Transazione transazione);
}
