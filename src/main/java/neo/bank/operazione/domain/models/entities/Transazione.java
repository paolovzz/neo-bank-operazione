package neo.bank.operazione.domain.models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neo.bank.operazione.domain.models.enums.TipologiaFlusso;
import neo.bank.operazione.domain.models.vo.Iban;
import neo.bank.operazione.domain.models.vo.IdTransazione;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Transazione {
    
    private IdTransazione idTransazione;
    private Iban iban;
    private TipologiaFlusso tipologiaFlusso;
}
