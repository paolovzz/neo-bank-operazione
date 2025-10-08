package neo.bank.operazione.application.ports.input.commands;

import lombok.Value;
import neo.bank.operazione.domain.models.vo.IdOperazione;

@Value
public class AnnullaOperazioneCmd {
    
    private IdOperazione idOperazione;
}
