package neo.bank.operazione.domain.models.entities;

import lombok.Value;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.Iban;
import neo.bank.operazione.domain.models.vo.IdTransazione;

@Value
public class Transazione {
    
    private IdTransazione idTransazione;
    private DataCreazione dataCreazione;
    private Iban iban;
    private String causale;
    private TipologiaFlusso tipologiaFlusso;
    private double importo;
}
