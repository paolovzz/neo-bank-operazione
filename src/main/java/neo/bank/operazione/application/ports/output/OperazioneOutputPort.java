package neo.bank.operazione.application.ports.output;

import neo.bank.operazione.domain.models.aggregates.Operazione;
import neo.bank.operazione.domain.models.vo.IdOperazione;

public interface OperazioneOutputPort {
    
    public void salva(Operazione operazione);
    public Operazione recuperaDaId(IdOperazione idOperazione);
}
