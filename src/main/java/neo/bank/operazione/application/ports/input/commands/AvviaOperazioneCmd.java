package neo.bank.operazione.application.ports.input.commands;

import lombok.Value;
import neo.bank.operazione.domain.models.vo.Iban;

@Value
public class AvviaOperazioneCmd {
    
    private Iban ibanMittente;
    private Iban ibanDestinatario;
    private String causale;
    private double importo;
}
