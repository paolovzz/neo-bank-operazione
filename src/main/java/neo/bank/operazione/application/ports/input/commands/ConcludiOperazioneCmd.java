package neo.bank.operazione.application.ports.input.commands;

import lombok.Value;
import neo.bank.operazione.domain.models.vo.IdOperazione;

@Value
public class ConcludiOperazioneCmd {
    
    private IdOperazione idOperazione;
}
