package neo.bank.operazione.application.ports.input.commands;

import lombok.Value;
import neo.bank.operazione.domain.models.vo.IdTransazione;

@Value
public class RecuperaDettaglioTransazioneCmd {
    
    private IdTransazione idTransazione;
}
