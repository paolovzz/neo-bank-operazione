package neo.bank.operazione.application.ports.input.commands;

import lombok.Value;
import neo.bank.operazione.domain.models.vo.DataCreazione;
import neo.bank.operazione.domain.models.vo.Iban;

@Value
public class RecuperaTotaleBonificiUscitaCmd {
    
    private Iban iban;
    private DataCreazione dataInf;
    private DataCreazione dataSup;
}
